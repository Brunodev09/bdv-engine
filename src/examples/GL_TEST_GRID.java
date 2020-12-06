package examples;

import engine.api.BdvScriptGL;
import engine.api.EntityAPI;
import engine.Bdv;
import engine.entities.Camera2D;
import engine.math.*;
import engine.math.Dimension;
import engine.texture.SpriteSheet;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GL_TEST_GRID extends BdvScriptGL {

    private static final Logger LOGGER = Logger.getLogger(GL_TEST_GRID.class.getName());
    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/res/basic").getAbsolutePath();
    SpriteSheet tile1 = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 4);
    Random random = new Random();

    public GL_TEST_GRID() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(800, 600);
        this.background = new RGBAf(0, 0, 0, 255);
        this.logFps = true;
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        // In OpenGL, the default viewport sets the origin (0,0,0) at the center of the screen
        int rows = 100;
        int cols = 100;
        Dimension tileSize = new Dimension(this.resolution.width / rows, this.resolution.height / cols);
        for (int i = -rows / 2; i < rows / 2; i++) {
            for (int j = -cols / 2; j < cols / 2; j++) {
                EntityAPI entityAPI = new EntityAPI(tile1,
                        new Vector3f(
                                (float) tileSize.width * i,
                                (float) tileSize.height * j, 0),
                        new Dimension(tileSize.width, tileSize.height),
                        new Vector2f(0, 0));
                entityAPI.setShouldRender(true);
//                entityAPI.setAmbientLightOn(true);w
//                entityAPI.setAmbientLight(new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()));
                this.entities.add(entityAPI);
            }
        }
//        this.entities.get(0).setGlowing(true);
//        this.entities.get(0).setColorGlow(new Vector3f(0.0f, 0.0f, 1.0f));
    }

    @Override
    public void update() {
        camera2d.move();

    }

    public static void main(String[] args) {
        try {
            new Bdv(GL_TEST_GRID.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
