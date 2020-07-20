package app.API;

import app.Core.Interfaces.Entity;
import app.Math.Dimension;
import app.Math.RGBA;

import java.util.ArrayList;
import java.util.List;

public abstract class Script {
    public List<Entity> entities = new ArrayList<Entity>();
    public Dimension resolution;
    public RGBA background;
    public String windowTitle;

    public abstract void init(List<Entity> entities, Dimension resolution, RGBA background);
    public abstract void update();
}
