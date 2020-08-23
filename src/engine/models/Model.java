package engine.models;

public class Model {
    private final int _vaoID;
    private final int _vertexCount;

    public Model(int vaoID, int vertexCount) {
        this._vaoID = vaoID;
        this._vertexCount = vertexCount;
    }

    public int getVAOID() {
        return this._vaoID;
    }

    public int getVertexCount() {
        return this._vertexCount;
    }

}
