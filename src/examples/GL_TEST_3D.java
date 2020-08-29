package examples;

import engine.api.BdvScriptGL;
import engine.api.EntityAPI;
import engine.Bdv;
import engine.entities.Camera;
import engine.math.Dimension;
import engine.math.RGBAf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GL_TEST_3D extends BdvScriptGL {

    private static final Logger LOGGER = Logger.getLogger(GL_TEST_3D.class.getName());
    private static final String TEXTURE_FILE_PATH = new File("src/examples/res/testTexture").getAbsolutePath();
    private static final String SPHERE_OBJ_FILE_PATH = new File("src/examples/res/f16").getAbsolutePath();

    public GL_TEST_3D() {
        this.camera = new Camera();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0,0,255,255);
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        EntityAPI entity = new EntityAPI(TEXTURE_FILE_PATH);
        entity.setModel(SPHERE_OBJ_FILE_PATH);
        entities.add(entity);
    }

    @Override
    public void update() {
        this.entities.get(0).rotate(0, 0.5f, 0.5f);
    }

    public static void main(String[] args) {
        try {
            new Bdv(GL_TEST_3D.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}


