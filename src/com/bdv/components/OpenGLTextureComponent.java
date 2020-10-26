package com.bdv.components;

import com.bdv.ECS.Component;

public class OpenGLTextureComponent extends Component<OpenGLTextureComponent> {
    private final int textureId;
    private String path;

    public OpenGLTextureComponent(int id) {
        this.textureId = id;
    }

    public OpenGLTextureComponent(int id, String path) {
        this.textureId = id;
        this.path = path;
    }

    public int getTextureId() {
        return textureId;
    }

    public String getPath() {
        return path;
    }
}
