package com.bdv.components;

import com.bdv.ECS.Entity;
import com.bdv.helpers.MatrixUtils;
import com.bdv.renders.opengl.MeshShader;
import com.bdv.renders.opengl.RectangleShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

public class OpenGLRendererComponent {
    private Matrix4f projection;
    private MeshShader shader;
    private RectangleShader rectangleShader;
    private int previousBoundTexture = 0;

    private boolean debugShader;

    public OpenGLRendererComponent() {
    }

    public OpenGLRendererComponent(MeshShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        // Culling back faces
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        shader.init();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public OpenGLRendererComponent(RectangleShader shader) {
        // Culling back faces
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        shader.init();
        shader.loadProjectionMatrix(projection);
        shader.stop();
    }

    public OpenGLRendererComponent(RectangleShader shader, Matrix4f orthographicMatrix) {
        this.projection = orthographicMatrix;
        rectangleShader = shader;
        shader.init();
        shader.loadProjectionMatrix(this.projection);
        shader.stop();
    }

    public void render(OpenGLModelComponent mdl) {
        GL30.glBindVertexArray(mdl.vaoId);
        GL20.glEnableVertexAttribArray(0);
//        Used only if we are not indexing the vertexes
//        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mdl.getVertexCount());
        GL11.glDrawElements(GL11.GL_TRIANGLES, mdl.vertexCount, GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public void render(OpenGLTexturedModelComponent tmdl) {
        OpenGLModelComponent mdl = tmdl.getModel();
        GL30.glBindVertexArray(mdl.vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmdl.getModelTexture().getId());
        GL11.glDrawElements(GL11.GL_TRIANGLES, mdl.vaoId, GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void renderEntities(Map<OpenGLTexturedModelComponent, List<Entity>> entities) {
        for (OpenGLTexturedModelComponent key : entities.keySet()) {
            this.modelSetup(key);
            List<Entity> entitiesToLoadFromModel = entities.get(key);
            for (Entity entity : entitiesToLoadFromModel) {
                this.applyTransformationAndLoadIntoShader(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, key.getModel().vertexCount, GL11.GL_UNSIGNED_INT, 0);
            }
            this.unbindTexture();
        }
    }

    public void renderEntities2D(Map<OpenGLTexturedModelComponent, List<Entity>> entities) {
        // @TODO - Reduce the number of draw calls into one big VAO - one big mesh
        for (OpenGLTexturedModelComponent key : entities.keySet()) {
            this.modelSetup2D(key);
            List<Entity> entitiesToLoadFromModel = entities.get(key);
            for (Entity entity : entitiesToLoadFromModel) {
                this.applyTransformationAndLoadIntoShader2D(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, key.getModel().vertexCount, GL11.GL_UNSIGNED_INT, 0);
            }
            this.unbindTexture2D();
        }
    }


    private void modelSetup(OpenGLTexturedModelComponent tmdl) {
        OpenGLModelComponent mdl = tmdl.getModel();
        GL30.glBindVertexArray(mdl.vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        OpenGLTextureCustomComponent md = tmdl.getModelTexture();
        shader.loadSpecularLights(md.getShineDamper(), md.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmdl.getModelTexture().getId());
    }

    private void modelSetup2D(OpenGLTexturedModelComponent tmdl) {
        OpenGLModelComponent mdl = tmdl.getModel();
        GL30.glBindVertexArray(mdl.vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        if (tmdl.getModelTexture().getColorPointer() != null) {
            GL20.glEnableVertexAttribArray(2);
        }
        if (tmdl.getModelTexture().getRgbTilesetEffects() != null) {
            rectangleShader.loadTileColors(tmdl.getModelTexture().getRgbTilesetEffects());
        }
        if (tmdl.getModelTexture().isChunkRendering()) {
            rectangleShader.loadIsChunkRendering(tmdl.getModelTexture().isChunkRendering());
        }
        if (tmdl.getModelTexture().getChunkTileSize() != null) {
            rectangleShader.loadChunkTileSize(tmdl.getModelTexture().getChunkTileSize());
        }

        rectangleShader.loadIsPlayer(tmdl.getModelTexture().isPlayer());

        // Entities independent uniform variables
        rectangleShader.loadCurrentTimeFlow();
        rectangleShader.loadDebugToggle(debugShader);

        if (previousBoundTexture != tmdl.getModelTexture().getId()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmdl.getModelTexture().getId());
        }

        previousBoundTexture = tmdl.getModelTexture().getId();
    }

    private void unbindTexture() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void unbindTexture2D() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void applyTransformationAndLoadIntoShader(Entity entity) {
        TransformComponent transformComponent = entity.<TransformComponent>getComponent();
        // Applying transformations and loading them to the VAO
        Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(
                transformComponent.position,
                transformComponent.rotation.x,
                transformComponent.rotation.y,
                transformComponent.rotation.z,
                transformComponent.scale.x,
                transformComponent.scale.y,
                transformComponent.scale.z);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void applyTransformationAndLoadIntoShader2D(Entity entity) {
        TransformComponent transformComponent = entity.<TransformComponent>getComponent();
        Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(
                transformComponent.position,
                transformComponent.rotation.x,
                transformComponent.rotation.y,
                transformComponent.rotation.z,
                transformComponent.scale.x,
                transformComponent.scale.y,
                transformComponent.scale.z);
        rectangleShader.loadTransformationMatrix(transformationMatrix);
    }

    public void setDebugShaderMode(boolean debugShader) {
        this.debugShader = debugShader;
    }
}
