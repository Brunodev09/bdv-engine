package com.bdv.components;

import com.bdv.ECS.Component;
import org.lwjgl.util.vector.Vector3f;

public class OpenGLightsourceComponent extends Component<OpenGLightsourceComponent> {
    private Vector3f position;
    private Vector3f color;

    public OpenGLightsourceComponent() {}

    private OpenGLightsourceComponent(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    public static OpenGLightsourceComponent invoke(Vector3f position, Vector3f color) {
        return new OpenGLightsourceComponent(position, color);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

}
