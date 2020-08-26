package engine.texture;

import org.lwjgl.util.vector.Vector3f;

public class ModelTexture {

    private final int _textureId;
    private float shineDamper = 1;
    private float reflectivity = 0;
    private Vector3f colorOffset = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f ambientLight = new Vector3f(1.0f, 1.0f, 1.0f);

    public ModelTexture(int id) {
        this._textureId = id;
    }

    public int getId() {
        return this._textureId;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public Vector3f getColorOffset() {
        return colorOffset;
    }

    public void setColorOffset(Vector3f colorOffset) {
        this.colorOffset = colorOffset;
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }
}

