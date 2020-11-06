package com.bdv.api;

import com.bdv.ECS.SystemManager;
import com.bdv.assets.AssetPool;
import com.bdv.exceptions.InvalidInstance;

import java.lang.reflect.InvocationTargetException;

public abstract class BdvScript {
    public int width = 1024;
    public int height = 768;
    public String windowTitle = "Unnamed window";
    public int fps = 60;
    public boolean logFps = false;
    public RendererAPI rendererAPI = RendererAPI.SWING_RENDERER;
    public ProjectDimensionNumber projectDimensionNumber = ProjectDimensionNumber.twoDimensions;
    public final SystemManager manager = new SystemManager();
    public final AssetPool assetPool = new AssetPool();
    public InputAPI inputAPI;
    // ---------------- OpenGL mode only ----------------
    // [x][y][rgba] per tile for OpenGL effects mapping.
    public float[][][] effects;
    public float tileSizeX = 16;
    public float tileSizeY = 16;
    public boolean debugShader = false;
    // ---------------- OpenGL mode only ----------------

    protected BdvScript() {
    }

    public abstract void init()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, InvalidInstance;
    public abstract void update(double deltaTime);

    public int getFPS() {
        return fps;
    }
}
