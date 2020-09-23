package engine.api;

import engine.entities.Camera;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;

import java.util.ArrayList;
import java.util.List;

public abstract class BdvScriptGL {
    public List<EntityAPI> entities = new ArrayList<>();
    public Dimension resolution;
    public RGBAf background;
    public String windowTitle;
    public Camera camera;
    public Camera2D camera2d;
    public int FPS = 0;
    public boolean debugShader = false;
    public boolean logFps;
    public static InputAPI input;
    public InputAPI inputAPI;

    public abstract void init(List<EntityAPI> entities, Dimension resolution, RGBAf background);
    public abstract void update();
}
