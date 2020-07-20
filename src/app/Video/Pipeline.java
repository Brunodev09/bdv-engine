package app.Video;

import app.Math.BufferOperations;
import app.Models.Model;
import app.Texture.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Pipeline {

    // Tracking VAO's and VBO's to collect them from memory afterwards
    private List<Integer> VAOs = new ArrayList<>();
    private List<Integer> VBOs = new ArrayList<>();

    private List<Integer> TEXTURES = new ArrayList<>();

    public Model loadDataToVAO(float[] positions, int[] indexes) {
        int VID = _createVAO();
        _bindIndexBufferVBO(indexes);
        _storeVBODataInVAOList(0, 3, positions);
        _unbindVAO();

        return new Model(VID, indexes.length);
    }

    public Model loadDataToVAO(float[] positions, float[] textureCoords, int[] indexes) {
        int VID = _createVAO();
        _bindIndexBufferVBO(indexes);
        _storeVBODataInVAOList(0, 3, positions);
        _storeVBODataInVAOList(1, 2, textureCoords);
        _unbindVAO();

        return new Model(VID, indexes.length);
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
        Texture texture = new Texture("src/res/" + name + ".png");
        int id = texture.getTextureID();
        TEXTURES.add(id);

        return id;
    }

    public int loadTextureOnLWJGL3(String name) {
//        int textureID;
//        int width, height;
//        ByteBuffer image;
//
//        try (MemoryStack stack = MemoryStack.stackPush()) {
//            IntBuffer w = stack.mallocInt(1);
//            IntBuffer h = stack.mallocInt(1);
//            IntBuffer comp = stack.mallocInt(1);
//
//            image = stbi_load("res/"+ name +".png", w, h, comp, 4);
//            if (image == null) {
//                System.out.println("Failed to load texture file: "+path+"\n" +
//                        stbi_failure_reason()
//                );
//            }
//            width = w.get();
//            height = h.get();
//        }
//
//        textureID = glGenTextures();
//        glBindTexture(GL_TEXTURE_2D, textureID);
//        TEXTURES.add(textureID);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); //sets MINIFICATION filtering to nearest
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); //sets MAGNIFICATION filtering to nearest
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
//
//        return textureID;
        return 0;
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


}
