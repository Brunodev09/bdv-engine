package com.bdv.renders.opengl;

public class OpenGLBufferedModel {
    private final float[] VERTICES;
    private final float[] TEXTURES;
    private final float[] NORMALS;
    private final int[] INDEXES;

    public OpenGLBufferedModel(float[] vertices, float[] textures, float[] normals, int[] indexes) {
        this.VERTICES = vertices;
        this.TEXTURES = textures;
        this.NORMALS = normals;
        this.INDEXES = indexes;
    }

    public OpenGLBufferedModel(float[] vertices, float[] textures, int[] indexes) {
        this.VERTICES = vertices;
        this.INDEXES = indexes;
        this.TEXTURES = textures;
        this.NORMALS = null;
    }

    public OpenGLBufferedModel(float[] vertices, int[] indexes) {
        this.VERTICES = vertices;
        this.INDEXES = indexes;
        this.TEXTURES = null;
        this.NORMALS = null;
    }

    public float[] getVertices() {
        return VERTICES;
    }

    public float[] getNormals() {
        return NORMALS;
    }

    public float[] getTextures() {
        return TEXTURES;
    }

    public int[] getIndexes() {
        return INDEXES;
    }
}
