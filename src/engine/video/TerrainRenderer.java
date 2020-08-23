package engine.video;

import engine.math.MatrixUtils;
import engine.models.Model;
import engine.models.Terrain;
import engine.shaders.Terrain3DShader;
import engine.texture.ModelTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class TerrainRenderer {
    private Terrain3DShader _shader;
    private Matrix4f _projection;

    public TerrainRenderer(Terrain3DShader shader, Matrix4f projectionMatrix) {
        this._shader = shader;
        shader.init();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrainList) {
        for (Terrain terrain : terrainList) {
            _modelSetup(terrain);
            _applyTransformationAndLoadIntoShader(terrain);
            GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    terrain.getProcessedModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT,
                    0);

            _unbindTexture();
        }
    }

    private void _modelSetup(Terrain tmdl) {
        Model mdl = tmdl.getProcessedModel();
        GL30.glBindVertexArray(mdl.getVAOID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture md = tmdl.getTexture();
        _shader.loadSpecularLights(md.getShineDamper(), md.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, md.getId());
    }

    private void _unbindTexture() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void _applyTransformationAndLoadIntoShader(Terrain terrain) {
        // Applying transformations and loading them to the VAO
        Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(
                new Vector3f(
                        terrain.getX(),
                        0,
                        terrain.getZ()),
                0,
                0,
                0,
                1);
        _shader.loadTransformationMatrix(transformationMatrix);
    }

}
