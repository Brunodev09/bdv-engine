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
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        // In OpenGL, the default viewport sets the origin (0,0,0) at the center of the screen
        int rows = 12;
        int cols = 12;
        Dimension tileSize = new Dimension(this.resolution.width / rows, this.resolution.height / cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                EntityAPI entityAPI = new EntityAPI(null,
                        new Vector3f(
                                (tileSize.width / 2) * i,
                                (tileSize.height / 2) * j, 0),
                        new Dimension(tileSize.width, tileSize.height),
                        new Vector2f(0, 0));
                entityAPI.setSpriteSheet(tile1);
//                entityAPI.setAmbientLightOn(true);
//                entityAPI.setAmbientLight(new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()));
                this.entities.add(entityAPI);
            }
        }
//        this.entities.get(0).setGlowing(true);
//        this.entities.get(0).setColorGlow(new Vector3f(0.0f, 0.0f, 1.0f));
    }

    @Override
    public void update() {
    }

    public static void main(String[] args) {
        try {
            new Bdv(GL_TEST_GRID.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
