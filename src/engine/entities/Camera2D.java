package engine.entities;

import engine.video.RenderManager;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera2D {
    private Vector3f _position = new Vector3f(0, 0, 0);
    private float _pitch;
    private float _yaw;
    private float _roll;

    private float _speed = 10.0f;

    public Camera2D() {

    }

    public void move(double delta) {
        long windowContext = RenderManager.getWindow();

        if (glfwGetKey(windowContext, GLFW_KEY_W) == GLFW_PRESS) {
            _position.y -= _speed;

        }
        if (glfwGetKey(windowContext, GLFW_KEY_S) == GLFW_PRESS) {
            _position.y += _speed;

        }
        if (glfwGetKey(windowContext, GLFW_KEY_D) == GLFW_PRESS) {
            _position.x += _speed;

        }
        if (glfwGetKey(windowContext, GLFW_KEY_A) == GLFW_PRESS) {
            _position.x -= _speed;

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

