package app.Models;

public class BufferedModel {
    private final float[] _VERTICES;
    private final float[] _TEXTURES;
    private final float[] _NORMALS;
    private final int[] _INDEXES;

    public BufferedModel(float[] vertices, float[] textures, float[] normals, int[] indexes) {
        this._VERTICES = vertices;
        this._TEXTURES = textures;
        this._NORMALS = normals;
        this._INDEXES = indexes;
    }

    public BufferedModel(float[] vertices, float[] textures, int[] indexes) {
        this._VERTICES = vertices;
        this._INDEXES = indexes;
        this._TEXTURES = textures;
        this._NORMALS = null;
    }

    public BufferedModel(float[] vertices, int[] indexes) {
        this._VERTICES = vertices;
        this._INDEXES = indexes;
        this._TEXTURES = null;
        this._NORMALS = null;
    }

    public float[] getVertices() {
        return _VERTICES;
    }

    public float[] getNormals() {
        return _NORMALS;
    }

    public float[] getTextures() {
        return _TEXTURES;
    }

    public int[] getIndexes() {
        return _INDEXES;
    }
}

