package engine.entities;

import engine.video.RenderManager;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class Camera {
    private Vector3f _position = new Vector3f(0, 4, 0);
    private float _pitch;
    private float _yaw;
    private float _roll;

    private float _speed = 0.2f;

    public Camera() {

    }

    public void move(double delta) {
        long windowContext = RenderManager.getWindow();

        if (glfwGetKey(windowContext, GLFW_KEY_W) == GLFW_PRESS) {
            _position.z -= _speed;

        }
        if (glfwGetKey(windowContext, GLFW_KEY_S) == GLFW_PRESS) {
            _position.z += _speed;

        }
        if (glfwGetKey(windowContext, GLFW_KEY_D) == GLFW_PRESS) {
            _position.x += _speed;

        }
        if (glfwGetKey(windowContext, GLFW_KEY_A) == GLFW_PRESS) {
            _position.x -= _speed;
        }

        if (glfwGetKey(windowContext, GLFW_KEY_UP) == GLFW_PRESS) {
            _position.y -= _speed;
        }
        if (glfwGetKey(windowContext, GLFW_KEY_DOWN) == GLFW_PRESS) {
            _position.y += _speed;
        }

        if (glfwGetKey(windowContext, GLFW_KEY_LEFT) == GLFW_PRESS) {
            _yaw -= _speed;
        }
        if (glfwGetKey(windowContext, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            _yaw += _speed;
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

