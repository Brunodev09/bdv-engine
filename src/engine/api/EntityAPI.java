package engine.api;


import engine.math.Dimension;
import engine.math.RGBAf;
import engine.texture.SpriteSheet;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class EntityAPI {
    public static int globalId = 0;
    private int id;
    private String file;
    private String model;
    private Vector3f position;
    private Vector2f speed;
    private float rotX;
    private float rotY;
    private float rotZ;
    private float scaleX;
    private float scaleY;
    private float scaleZ;
    private int[] indexes;
    private float[] vertexes;
    private float[] textureCoords;
    private boolean switchModel = false;
    private SpriteSheet spriteSheet;
    private RGBAf rgb;
    private Vector3f rgbVector;
    private Vector3f ambientLight;
    private float width;
    private float height;
    private boolean isGlowing = false;
    private boolean ambientLightToggle = false;
    private Vector3f colorGlow;
    private boolean player;
    private boolean shouldRender = false;

    private int _API_LINK;

    public EntityAPI(String file) {
        globalId++;
        this.id = globalId;
        this.file = file;
        position = new Vector3f(0, 0, 0);
        speed = new Vector2f(0, 0);
        rotX = 0; rotY = 0; rotZ = 0;
        scaleX = 1;
        scaleY = 1;
        scaleZ = 1;
        model = null;
    }

    public EntityAPI(String file, Vector3f position, Vector2f speed) {
        globalId++;
        this.id = globalId;
        this.file = file;
        this.position = position;
        this.speed = speed;
        rotX = 0; rotY = 0; rotZ = 0;
        scaleX = 1;
        scaleY = 1;
        scaleZ = 1;
        model = null;
    }

    public EntityAPI(String file, Vector3f position, Dimension dimension, Vector2f speed) {
        globalId++;
        this.id = globalId;
        this.file = file;
        this.position = position;
        this.speed = speed;
        this.width = dimension.width;
        this.height = dimension.height;
        rotX = 0; rotY = 0; rotZ = 0;
        scaleX = 1;
        scaleY = 1;
        scaleZ = 1;
        model = null;
    }

    public EntityAPI(String file, int[] indexes, float[] vertexes, float[] textureCoords) {
        globalId++;
        this.id = globalId;
        this.file = file;
        this.indexes = indexes;
        this.vertexes = vertexes;
        this.textureCoords = textureCoords;
        position = new Vector3f(0, 0, 0);
        speed = new Vector2f(0, 0);
        rotX = 0; rotY = 0; rotZ = 0;
        scaleX = 1;
        scaleY = 1;
        scaleZ = 1;
        model = null;
    }

    public Vector2f getSpeed() {
        return speed;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void translate(Vector3f position) {
        this.position = position;
    }

    public void accelerate(Vector2f speed) {
        this.speed = speed;
    }

    public float getRotationX() {
        return rotX;
    }
    public float getRotationY() {
        return rotY;
    }
    public float getRotationZ() {
        return rotZ;
    }

    public void rotate(float x, float y, float z) {
        rotX += x;
        rotY += y;
        rotZ += z;
    }

    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
        this.scaleZ = scale;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public void setLink(int _API_LINK) {
        this._API_LINK = _API_LINK;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
        this.switchModel = true;
    }

    public int getLink() {
        return _API_LINK;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean getEditModel() {
        return switchModel;
    }

    public void setEditModel(boolean edit) {
        this.switchModel = edit;
    }

    public void setSpriteSheet(SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
        this.switchModel = true;
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public int getId() {
        return id;
    }

    public RGBAf getRgb() {
        return rgb;
    }

    public void setRgb(RGBAf rgb) {
        this.rgb = rgb;
        this.switchModel = true;
    }

    public Vector3f getRgbVector() {
        return rgbVector;
    }

    public void setRgbVector(Vector3f rgbVector) {
        this.rgbVector = rgbVector;
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Vector3f getColorGlow() {
        return colorGlow;
    }

    public void setColorGlow(Vector3f colorGlow) {
        this.colorGlow = colorGlow;
    }

    public boolean isGlowing() {
        return isGlowing;
    }

    public void setGlowing(boolean glowing) {
        isGlowing = glowing;
    }

    public boolean isAmbientLightOn() {
        return ambientLightToggle;
    }

    public void setAmbientLightOn(boolean ambientLightOn) {
        ambientLightToggle = ambientLightOn;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public boolean isPlayer() {
        return player;
    }

    public boolean shouldRender() {
        return shouldRender;
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }
}
