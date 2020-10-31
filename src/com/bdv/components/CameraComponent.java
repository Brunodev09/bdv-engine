package com.bdv.components;

import org.lwjgl.util.vector.Vector3f;

public interface CameraComponent {
    Vector3f position = new Vector3f(0, 4, 0);
    float pitch = 0;
    float yaw = 0;
    float roll = 0;
    boolean threeDimensions = false;

    float speed = 0.2f;

    void move();

    Vector3f getPosition();

    float getPitch();

    float getYaw();

    float getRoll();

    boolean getDimensions();
}
