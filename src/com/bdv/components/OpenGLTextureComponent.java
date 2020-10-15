package com.bdv.components;

public class OpenGLTextureComponent {
    private final int id;
    private String path;

    public OpenGLTextureComponent(int id) {
        this.id = id;
    }

    public OpenGLTextureComponent(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
