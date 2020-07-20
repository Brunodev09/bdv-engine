package app.Entities;

import app.Models.Model;
import app.Models.TexturedModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
    private TexturedModel _model;
    private Model _model_primitive;
    private Vector3f _position;
    private Vector2f _position2d;
    private float _rotX, _rotY, _rotZ;
    private float _scale;

    public static int _GLOBAL_ID_ = 0;
    private final int id;

    private Lightsource _light;

    public Entity(TexturedModel texturedModel, Vector3f positionVector, float rotX, float rotY, float rotZ, float scale) {
        this._model = texturedModel;
        this._position = positionVector;
        this._rotX = rotX;
        this._rotY = rotY;
        this._rotZ = rotZ;
        this._scale = scale;
        _GLOBAL_ID_++;
        this.id = _GLOBAL_ID_;
    }

    public Entity(Model mdl, Vector3f positionVector, float rotX, float rotY, float rotZ, float scale) {
        this._model_primitive = mdl;
        this._position = positionVector;
        this._rotX = rotX;
        this._rotY = rotY;
        this._rotZ = rotZ;
        this._scale = scale;
        _GLOBAL_ID_++;
        this.id = _GLOBAL_ID_;
    }

    public void offsetPosition(float dx, float dy, float dz) {
        this._position.x += dx;
        this._position.y += dy;
        this._position.z += dz;
    }

    public void rotate(float dx, float dy, float dz) {
        this._rotX += dx;
        this._rotY += dy;
        this._rotZ += dz;
    }

    public TexturedModel getModel() {
        return _model;
    }

    public void setModel(TexturedModel _model) {
        this._model = _model;
    }

    public Model getModelPrimitive() {
        return _model_primitive;
    }

    public void setModelPrimitive(Model _model) {
        this._model_primitive = _model;
    }

    public Vector3f getPosition() {
        return _position;
    }

    public Vector2f getPosition2D() { return _position2d; }

    public void setPosition(Vector3f _position) {
        this._position = _position;
    }

    public float getRotX() {
        return _rotX;
    }

    public void setRotX(float _rotX) {
        this._rotX = _rotX;
    }

    public float getRotY() {
        return _rotY;
    }

    public void setRotY(float _rotY) {
        this._rotY = _rotY;
    }

    public float getRotZ() {
        return _rotZ;
    }

    public void setRotZ(float _rotZ) {
        this._rotZ = _rotZ;
    }

    public float getScale() {
        return _scale;
    }

    public void setScale(float _scale) {
        this._scale = _scale;
    }

    public void setLight(Lightsource light) {
        this._light = light;
    }

    public Lightsource getLight() {
        return _light;
    }

    public int getId() {
        return id;
    }
}

