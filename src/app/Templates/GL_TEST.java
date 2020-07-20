package app.Templates;

import app.API.EntityAPI;
import app.API.ScriptGL;
import app.Entities.Camera2D;
import app.Math.Dimension;
import app.Math.RGBAf;

import java.util.ArrayList;
import java.util.List;

public class GL_TEST extends ScriptGL {

    public GL_TEST() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0,0,0,255);
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        entities.add(new EntityAPI("tex1"));
    }

    @Override
    public void update() {
        entities.get(0).rotate(0, 0, 0.5f);
    }
}
