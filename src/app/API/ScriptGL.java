package app.API;

import app.Entities.Camera;
import app.Entities.Camera2D;
import app.Math.Dimension;
import app.Math.RGBAf;

import java.util.ArrayList;
import java.util.List;

public abstract class ScriptGL {
    public List<EntityAPI> entities = new ArrayList<>();
    public Dimension resolution;
    public RGBAf background;
    public String windowTitle;
    public Camera camera;
    public Camera2D camera2d;
    public int FPS = 0;

    public abstract void init(List<EntityAPI> entities, Dimension resolution, RGBAf background);
    public abstract void update();
}
