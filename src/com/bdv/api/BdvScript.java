package com.bdv.api;

import com.bdv.ECS.Entity;

import java.util.ArrayList;
import java.util.List;

public abstract class BdvScript {
    private int width;
    private int height;
    private String windowTitle;
    private int fps = 0;
    private boolean debugShader = false;
    private boolean logFps;
    private RendererAPI rendererAPI;
    private List<Entity> entities = new ArrayList<>();

    public abstract void init();
    public abstract void update();
    public abstract void render();

    public int getFPS() {
        return fps;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public boolean isDebugShader() {
        return debugShader;
    }

    public boolean isLogFps() {
        return logFps;
    }

    public void setDebugShader(boolean debugShader) {
        this.debugShader = debugShader;
    }

    public void setFPS(int fps) {
        this.fps = fps;
    }

    public void setLogFps(boolean logFps) {
        this.logFps = logFps;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public RendererAPI getRendererAPI() {
        return rendererAPI;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
