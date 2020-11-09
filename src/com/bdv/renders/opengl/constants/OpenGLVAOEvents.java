package com.bdv.renders.opengl.constants;

public enum OpenGLVAOEvents {
    INIT("init"),
    UPDATE("update");

    private final String description;

    OpenGLVAOEvents(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
