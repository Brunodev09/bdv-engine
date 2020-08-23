package engine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera2D {
    private Vector3f _position = new Vector3f(0, 0, 0);
    private float _pitch;
    private float _yaw;
    private float _roll;

    private float _speed = 10.0f;

    public Camera2D() {

    }

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            _position.y -= _speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            _position.x += _speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            _position.x -= _speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            _position.y += _speed;
        }
    }

    public Vector3f getPosition() {
        return _position;
    }

    public float getPitch() {
        return _pitch;
    }

    public float getYaw() {
        return _yaw;
    }

    public float getRoll() {
        return _roll;
    }
}

