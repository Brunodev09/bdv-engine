package com.bdv.api;

import com.bdv.ECS.SystemManager;
import com.bdv.assets.AssetPool;

import java.lang.reflect.InvocationTargetException;

public abstract class BdvScript {
    private int width = 800;
    private int height = 600;
    private String windowTitle = "Unnamed window";
    private int fps = 60;
    private boolean debugShader = false;
    private boolean logFps = false;
    private RendererAPI rendererAPI = RendererAPI.SWING_RENDERER;
    private ProjectDimensionNumber projectDimensionNumber = ProjectDimensionNumber.twoDimensions;
    public final SystemManager manager = new SystemManager();
    public final AssetPool assetPool = new AssetPool();


    protected BdvScript() {
    }

    public abstract void init()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
    public abstract void update(double deltaTime);

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

    public ProjectDimensionNumber getProjectDimensionNumber() {
        return projectDimensionNumber;
    }
}
