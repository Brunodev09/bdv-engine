package engine.api;

import engine.core.interfaces.Entity;
import engine.math.Dimension;
import engine.math.RGBA;

import java.util.ArrayList;
import java.util.List;

public abstract class BdvScript {
    public List<Entity> entities = new ArrayList<Entity>();
    public Dimension resolution;
    public RGBA background;
    public String windowTitle;

    public abstract void init(List<Entity> entities, Dimension resolution, RGBA background);
    public abstract void update();
}
