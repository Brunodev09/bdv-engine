package com.bdv.components;

import com.bdv.helpers.MatrixUtils;
import com.bdv.renders.opengl.Terrain3DShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class OpenGLTerrainRendererComponent {
    private Terrain3DShader shader;

    public OpenGLTerrainRendererComponent(Terrain3DShader shader, Matrix4f projectionMatrix) {
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
        OpenGLModelComponent mdl = tmdl.getProcessedModel();
        GL30.glBindVertexArray(mdl.vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        OpenGLTextureCustomComponent md = tmdl.getTexture();
        shader.loadSpecularLights(md.getShineDamper(), md.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, md.getId());
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
}
