package examples;

import engine.api.EntityAPI;
import engine.api.BdvScriptGL;
import engine.Bdv;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GL_TEST extends BdvScriptGL {

    private static final Logger LOGGER = Logger.getLogger(GL_TEST.class.getName());
    private static final String TEXTURE_FILE_PATH = new File("src/examples/res/tex1").getAbsolutePath();

    public GL_TEST() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0,0,0,255);
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        entities.add(new EntityAPI(TEXTURE_FILE_PATH));
    }

    @Override
    public void update() {
        entities.get(0).rotate(0, 0, 0.5f);
        camera2d.move();
    }

    public static void main(String[] args) {
        try {
            new Bdv(GL_TEST.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
