package com.bdv.components;

import com.bdv.ECS.Component;
import org.lwjgl.util.vector.Vector3f;

public class TransformComponent extends Component<TransformComponent> {
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public TransformComponent() {}

    public static TransformComponent invoke(Vector3f position, Vector3f rotation, Vector3f scale) {
        TransformComponent component = new TransformComponent();
        component.position = position;
        component.rotation = rotation;
        component.scale = scale;
        return component;
    }
}
