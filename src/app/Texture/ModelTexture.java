package app.Texture;

public class ModelTexture {

    private final int _textureId;
    private float shineDamper = 1;
    private float reflectivity = 0;

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
}

