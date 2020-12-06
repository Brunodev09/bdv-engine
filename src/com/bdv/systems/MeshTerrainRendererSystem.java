package com.bdv.systems;

import com.bdv.ECS.System;
import com.bdv.components.OpenGLTerrainComponent;
import com.bdv.renders.opengl.OpenGLModel;
import com.bdv.renders.opengl.OpenGLTextureCustom;
import com.bdv.renders.opengl.helpers.MatrixUtils;
import com.bdv.renders.opengl.shaders.Terrain3DShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class MeshTerrainRendererSystem extends System {
    private Terrain3DShader shader;

    public MeshTerrainRendererSystem invoke() {
        return new MeshTerrainRendererSystem();
    }

    public MeshTerrainRendererSystem() {
    }

    public void setShader(Terrain3DShader shader, Matrix4f projection) {
        this.shader = shader;
        shader.init();
        shader.loadProjectionMatrix(projection);
        shader.stop();
    }

    public MeshTerrainRendererSystem(Terrain3DShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.init();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<OpenGLTerrainComponent> terrainList) {
        for (OpenGLTerrainComponent terrain : terrainList) {
            modelSetup(terrain);
            applyTransformationAndLoadIntoShader(terrain);
            GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    terrain.getProcessedModel().vertexCount,
                    GL11.GL_UNSIGNED_INT,
                    0);

            unbindTexture();
        }
    }

    private void modelSetup(OpenGLTerrainComponent tmdl) {
        OpenGLModel mdl = tmdl.getProcessedModel();
        GL30.glBindVertexArray(mdl.vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        OpenGLTextureCustom md = tmdl.getTexture();
        shader.loadSpecularLights(md.getShineDamper(), md.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, md.getTextureId());
    }

    private void unbindTexture() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void applyTransformationAndLoadIntoShader(OpenGLTerrainComponent terrain) {
        // Applying transformations and loading them to the VAO
        Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(
                new Vector3f(
                        terrain.getX(),
                        0,
                        terrain.getZ()),
                0,
                0,
                0,
                1,1, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    @Override
    public void update(Object... objects) {

    }
}
