package app.Templates;

import app.API.EntityAPI;
import app.API.ScriptGL;
import app.Entities.Camera;
import app.Math.Dimension;
import app.Math.RGBAf;

import java.util.ArrayList;
import java.util.List;

public class GL_TEST_3D extends ScriptGL {

    public GL_TEST_3D() {
        this.camera = new Camera();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(255,255,255,255);
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        EntityAPI entity = new EntityAPI("tex1");
        entity.setModel("testModel");
        entities.add(entity);
    }

    @Override
    public void update() {
        this.entities.get(0).rotate(0, 0.5f, 0);
    }
}
