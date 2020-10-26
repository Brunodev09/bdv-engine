package com.bdv.components;

import com.bdv.ECS.Component;
import org.lwjgl.util.vector.Vector3f;

public class TransformComponent extends Component<TransformComponent> {
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public TransformComponent(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }
}
