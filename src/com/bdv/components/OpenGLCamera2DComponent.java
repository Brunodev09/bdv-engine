package com.bdv.components;

import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class OpenGLCamera2DComponent {
    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    private float speed = 10.0f;

    public OpenGLCamera2DComponent() {

    }

    public void move() {
        long windowContext = OpenGLRenderManagerComponent.getWindow();

        if (glfwGetKey(windowContext, GLFW_KEY_W) == GLFW_PRESS) {
            position.y -= speed;

        }
        if (glfwGetKey(windowContext, GLFW_KEY_S) == GLFW_PRESS) {
            position.y += speed;

        }
        if (glfwGetKey(windowContext, GLFW_KEY_D) == GLFW_PRESS) {
            position.x += speed;

        }
        if (glfwGetKey(windowContext, GLFW_KEY_A) == GLFW_PRESS) {
            position.x -= speed;

        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
