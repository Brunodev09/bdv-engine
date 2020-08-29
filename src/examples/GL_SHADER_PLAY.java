package examples;

import engine.Bdv;
import engine.api.BdvScriptGL;
import engine.api.EntityAPI;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;
import engine.texture.SpriteSheet;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// @TODO - Remove the need to set null a spite that will be set to a spritesheet

public class GL_SHADER_PLAY extends BdvScriptGL {

    private static final Logger LOGGER = Logger.getLogger(GL_SHADER_PLAY.class.getName());
    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/res/black").getAbsolutePath();
    public GL_SHADER_PLAY() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        EntityAPI entity = new EntityAPI(SPRITESHEET_FILE_PATH, new Vector3f(0, 0, 0), this.resolution, new Vector2f(0,0));
        this.entities.add(entity);
    }

    @Override
    public void update() {

    }

    public static void main(String[] args) {
        try {
            new Bdv(GL_SHADER_PLAY.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
