package engine.texture;

import org.lwjgl.util.vector.Vector3f;

public class ModelTexture {

    private final int _textureId;
    private float shineDamper = 1;
    private float reflectivity = 0;
    private Vector3f colorOffset = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f ambientLight;
    private boolean toggleGlow = false;
    private boolean ambientLightToggle = false;
    private Vector3f glowColor;
    private boolean player;

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

    public Vector3f getGlowColor() {
        return glowColor;
    }

    public boolean isToggleGlow() {
        return toggleGlow;
    }

    public void setGlowColor(Vector3f glowColor) {
        this.glowColor = glowColor;
    }

    public void setToggleGlow(boolean toggleGlow) {
        this.toggleGlow = toggleGlow;
    }

    public boolean isAmbientLightToggle() {
        return ambientLightToggle;
    }

    public void setAmbientLightToggle(boolean ambientLightToggle) {
        this.ambientLightToggle = ambientLightToggle;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }
}

