package com.bdv.systems;

import com.bdv.ECS.System;
import com.bdv.ECS.Entity;
import com.bdv.components.OpenGLTexturedModelComponent;
import com.bdv.components.TransformComponent;
import com.bdv.renders.opengl.*;
import com.bdv.renders.opengl.helpers.MatrixUtils;
import com.bdv.renders.opengl.shaders.MeshShader;
import com.bdv.renders.opengl.shaders.RectangleShader;
import com.bdv.renders.opengl.shaders.Shader;
import com.bdv.renders.opengl.shaders.Terrain3DShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

public class MeshRendererSystem extends System {
    private Matrix4f projection;
    private MeshShader meshShader;
    private RectangleShader rectangleShader;
    private Terrain3DShader terrainShader;
    private int previousBoundTexture = 0;

    private boolean debugShader;

    public MeshRendererSystem invoke() {
        return new MeshRendererSystem();
    }

    public MeshRendererSystem() {
    }

    public void setShader(Shader shader, Matrix4f projection) {
        glCullFaces();
        if (shader instanceof MeshShader) {
            this.meshShader = (MeshShader) shader;
            this.meshShader.init();
            this.meshShader.loadProjectionMatrix(projection);
            this.meshShader.stop();
        } else if (shader instanceof RectangleShader) {
            this.rectangleShader = (RectangleShader) shader;
            this.rectangleShader.init();
            this.rectangleShader.loadProjectionMatrix(projection);
            this.rectangleShader.stop();
        } else if (shader instanceof Terrain3DShader) {
            this.terrainShader = (Terrain3DShader) shader;
            this.terrainShader.init();
            this.terrainShader.loadProjectionMatrix(projection);
            this.terrainShader.stop();
        }
    }

    public void glCullFaces() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void renderEntities(Map<OpenGLTexturedModelComponent, List<Entity>> entities) {
        for (OpenGLTexturedModelComponent key : entities.keySet()) {
            this.modelSetup(key);
            List<Entity> entitiesToLoadFromModel = entities.get(key);
            for (Entity entity : entitiesToLoadFromModel) {
                this.applyTransformationAndLoadIntoShader(entity);
                // Used only if we are not indexing the vertexes
                // GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mdl.getVertexCount());
                GL11.glDrawElements(GL11.GL_TRIANGLES, key.getModel().vertexCount, GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexture(3);
        }
    }

    public void renderEntities2D(Map<OpenGLTexturedModelComponent, List<Entity>> entities) {
        for (OpenGLTexturedModelComponent key : entities.keySet()) {
            this.modelSetup2D(key);
            List<Entity> entitiesToLoadFromModel = entities.get(key);
            for (Entity entity : entitiesToLoadFromModel) {
                this.applyTransformationAndLoadIntoShader(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, key.getModel().vertexCount, GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexture(2);
        }
    }


    private void modelSetup(OpenGLTexturedModelComponent tmdl) {
        OpenGLModel mdl = tmdl.getModel();
        bindVertexes(3, mdl.vaoId);
        OpenGLTextureCustom md = tmdl.getModelTexture();
        meshShader.loadSpecularLights(md.getShineDamper(), md.getReflectivity());
        bindTexture(tmdl.getModelTexture().getTextureId());
    }

    private void modelSetup2D(OpenGLTexturedModelComponent tmdl) {
        OpenGLModel mdl = tmdl.getModel();

        if (tmdl.getModelTexture().getColorPointer() != null) {
            bindVertexes(3, mdl.vaoId);
        } else bindVertexes(2, mdl.vaoId);

        rectangleShader.loadIsPlayer(tmdl.getModelTexture().isPlayer());

        // Entities independent uniform variables
        rectangleShader.loadCurrentTimeFlow();
        rectangleShader.loadDebugToggle(debugShader);

        bindTexture(tmdl.getModelTexture().getTextureId());


        if (previousBoundTexture != tmdl.getModelTexture().getTextureId()) {
        }

        previousBoundTexture = tmdl.getModelTexture().getTextureId();
    }

    private void bindVertexes(int n, int vaoId) {
        GL30.glBindVertexArray(vaoId);
        for (int i = 0; i < n; i++) {
            GL20.glEnableVertexAttribArray(i);
        }
    }

    private void bindTexture(int textureId) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }

    private void unbindTexture(int n) {
        for (int i = 0; i < n; i++) {
            GL20.glDisableVertexAttribArray(i);
        }
        GL30.glBindVertexArray(0);
    }

    private void applyTransformationAndLoadIntoShader(Entity entity) {
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        // Applying transformations and loading them to the VAO
        Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(
                transformComponent.position,
                transformComponent.rotation.x,
                transformComponent.rotation.y,
                transformComponent.rotation.z,
                transformComponent.scale.x,
                transformComponent.scale.y,
                transformComponent.scale.z);
        if (rectangleShader == null && meshShader != null)
            meshShader.loadTransformationMatrix(transformationMatrix);
        else if (rectangleShader != null && meshShader == null)
            rectangleShader.loadTransformationMatrix(transformationMatrix);
    }

    public void setDebugShaderMode(boolean debugShader) {
        this.debugShader = debugShader;
    }

    @Override
    public void update(Object... objects) {

    }
}
