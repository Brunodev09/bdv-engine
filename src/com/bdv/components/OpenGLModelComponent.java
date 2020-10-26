package com.bdv.components;

import com.bdv.ECS.Component;

public class OpenGLModelComponent extends Component<OpenGLModelComponent> {
    public final int vaoId;
    public final int vertexCount;

    public OpenGLModelComponent(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }
}
