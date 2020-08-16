package app.API;


import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class EntityAPI {
    private String file;
    private String model;
    private Vector3f position;
    private Vector2f speed;
    private float rotX;
    private float rotY;
    private float rotZ;
    private float scale;
    private int[] indexes;
    private float[] vertexes;
    private float[] textureCoords;
    private boolean switchModel = false;

    private int _API_LINK;

    public EntityAPI(String file) {
        this.file = file;
        position = new Vector3f(0, 0, 0);
        speed = new Vector2f(0, 0);
        rotX = 0; rotY = 0; rotZ = 0;
        scale = 1;
        model = null;
    }

    public EntityAPI(String file, Vector3f position, Vector2f speed) {
        this.file = file;
        this.position = position;
        this.speed = speed;
        rotX = 0; rotY = 0; rotZ = 0;
        scale = 1;
        model = null;
    }

    public EntityAPI(String file, int[] indexes, float[] vertexes, float[] textureCoords) {
        this.file = file;
        this.indexes = indexes;
        this.vertexes = vertexes;
        this.textureCoords = textureCoords;
        position = new Vector3f(0, 0, 0);
        speed = new Vector2f(0, 0);
        rotX = 0; rotY = 0; rotZ = 0;
        scale = 1;
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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
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

}
