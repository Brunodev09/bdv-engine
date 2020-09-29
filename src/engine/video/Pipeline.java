package engine.video;

import engine.math.BufferOperations;
import engine.models.Model;
import engine.texture.SpriteSheet;
import engine.texture.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Pipeline {

    // Tracking VAO's and VBO's to collect them from memory afterwards
    private List<Integer> VAOs = new ArrayList<>();
    private List<Integer> VBOs = new ArrayList<>();

    private List<Integer> TEXTURES = new ArrayList<>();

    private List<VAOManager> managers = new ArrayList<>();

    public Model loadDataToVAO(float[] positions, int[] indexes) {
        int VID = _createVAO();
        _bindIndexBufferVBO(indexes);
        _storeVBODataInVAOList(0, 3, positions);
        _unbindVAO();

        return new Model(VID, indexes.length);
    }

    public Model loadDataToVAO(float[] positions, float[] textureCoords, int[] indexes) {
        int VID = _createVAO();
        VAOManager vaoManager = new VAOManager(VID);
        managers.add(vaoManager);
        _bindIndexBufferVBO(indexes);
        _storeVBODataInVAOList(0, 3, positions);
        _storeVBODataInVAOList(1, 2, textureCoords);
        _unbindVAO();

        return new Model(VID, indexes.length);
    }

    public Model loadDataToVAO(float[] positions, float[] textureCoords, int[] indexes, float[] colorPointer) {
        int VID = _createVAO();
        VAOManager vaoManager = new VAOManager(VID);
        managers.add(vaoManager);
        _bindIndexBufferVBO(indexes);
        _storeVBODataInVAOList(0, 3, positions);
        _storeVBODataInVAOList(1, 2, textureCoords);
        _storeVBODataInVAOList(2, 3, colorPointer);
        _unbindVAO();

        return new Model(VID, indexes.length);
    }

    public void updateTextureDataInVAO(int managerID, float[] textureCoords, float[] colorPointer) {
        VAOManager vaoManager = null;
        for (VAOManager findManager : managers) {
            if (findManager.getId() == managerID) {
                vaoManager = findManager;
                break;
            }
        }
        if (vaoManager == null) return;
        _usePreviouslyCreatedVAO(vaoManager.getVid());
        _updateDataInExistingVBO(vaoManager.getVboPointers().get(1), 1, 2, textureCoords);
        _updateDataInExistingVBO(vaoManager.getVboPointers().get(2), 2, 3, colorPointer);
        _unbindVAO();
    }

    public Model loadDataToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indexes) {
        int VID = _createVAO();
        _bindIndexBufferVBO(indexes);
        _storeVBODataInVAOList(0, 3, positions);
        _storeVBODataInVAOList(1, 2, textureCoords);
        _storeVBODataInVAOList(2, 3, normals);
        _unbindVAO();

        return new Model(VID, indexes.length);
    }

    public int loadTexture(String name) {
        Texture texture = new Texture(name + ".png");
        int id = texture.getTextureID();
        TEXTURES.add(id);

        return id;
    }

    public int loadTextureFromSpritesheet(SpriteSheet spriteSheet) {
        Texture texture = new Texture( spriteSheet.getFile() + ".png", spriteSheet);
        int id = texture.getTextureID();
        TEXTURES.add(id);

        return id;
    }

    public int generateTexture(BufferedImage image) {
        Texture texture = new Texture();
        int id = texture.loadTextureWithImage(image);
        TEXTURES.add(id);

        return id;
    }

    public int generateTexture(BufferedImage image, SpriteSheet spriteSheet) {
        Texture texture = new Texture();
        int id = texture.loadTextureWithImage(image, spriteSheet);
        TEXTURES.add(id);

        return id;
    }

    public void runCollector() {
        for (int VAO : VAOs) {
            GL30.glDeleteVertexArrays(VAO);
        }
        for (int VBO : VBOs) {
            GL15.glDeleteBuffers(VBO);
        }
        for (int TEX : TEXTURES) {
            GL11.glDeleteTextures(TEX);
        }
    }

    private void _bindIndexBufferVBO(int[] indexes) {
        int vboID = GL15.glGenBuffers();
        VBOs.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = BufferOperations.convertIntToIntBuffer(indexes);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private int _createVAO() {
        int VID = GL30.glGenVertexArrays();
        VAOs.add(VID);
        GL30.glBindVertexArray(VID);

        return VID;
    }

    private void _storeVBODataInVAOList(int attrNum, int coordSize, float[] data) {
        int vboId = GL15.glGenBuffers();

        managers.get(managers.size() - 1).addVbo(vboId);

        VBOs.add(vboId);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = BufferOperations.convertFloatToFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        // coordSize = 3 for 3-D vectors
        GL20.glVertexAttribPointer(attrNum, coordSize, GL11.GL_FLOAT, false, 0, 0);
        // unbinding VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void _unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void _updateDataInExistingVBO(int vboID, int attrNum, int coordSize, float[] data) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferOperations.convertFloatToFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        // coordSize = 3 for 3-D vectors
        GL20.glVertexAttribPointer(attrNum, coordSize, GL11.GL_FLOAT, false, 0, 0);
        // unbinding VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void _usePreviouslyCreatedVAO(int vaoID) {
        GL30.glBindVertexArray(vaoID);
    }

    public List<VAOManager> getManagers() {
        return managers;
    }
}
