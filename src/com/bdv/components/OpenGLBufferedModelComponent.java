package com.bdv.components;

import com.bdv.ECS.Component;

public class OpenGLBufferedModelComponent extends Component<OpenGLBufferedModelComponent> {
    private final float[] VERTICES;
    private final float[] TEXTURES;
    private final float[] NORMALS;
    private final int[] INDEXES;

    public OpenGLBufferedModelComponent(float[] vertices, float[] textures, float[] normals, int[] indexes) {
        this.VERTICES = vertices;
        this.TEXTURES = textures;
        this.NORMALS = normals;
        this.INDEXES = indexes;
    }

    public OpenGLBufferedModelComponent(float[] vertices, float[] textures, int[] indexes) {
        this.VERTICES = vertices;
        this.INDEXES = indexes;
        this.TEXTURES = textures;
        this.NORMALS = null;
    }

    public OpenGLBufferedModelComponent(float[] vertices, int[] indexes) {
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
