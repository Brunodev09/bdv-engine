package com.bdv.api;

public enum RendererAPI {
    SWING_RENDERER("SWING_RENDERER"),
    OPENGL_RENDERER("OPENGL_RENDERER");

    private final String description;

    RendererAPI(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
