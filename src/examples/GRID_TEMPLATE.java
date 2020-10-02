package examples;

import engine.api.BdvScript;
import engine.Bdv;
import engine.core.interfaces.Entity;
import engine.core.interfaces.Model;
import engine.math.Dimension;
import engine.math.RGBA;
import engine.math.Vector2f;
import engine.math.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GRID_TEMPLATE extends BdvScript {

    private static final Logger LOGGER = Logger.getLogger(GRID_TEMPLATE.class.getName());
    private static final String GRASS_TEXTURE = new File("src/examples/res/grass").getAbsolutePath();

    public GRID_TEMPLATE() {
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(800, 600);
        this.background = new RGBA(255, 20, 147, 255);
        this.windowTitle = "GRID";
        this.init(entities, resolution, background);
    }

    @Override
    public void init(List<Entity> entities, Dimension resolution, RGBA background) {
        int rows = 200;
        int cols = 200;
        Dimension tileSize = new Dimension(this.resolution.width / rows, this.resolution.height / cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Entity entity = new Entity(new Vector3f(tileSize.width * i + i, tileSize.height * j + j),
                        new Vector2f(0f, 0f), tileSize, Model.TEXTURE);
                entity.loadImage(GRASS_TEXTURE);
                this.entities.add(entity);
            }
        }
    }

    @Override
    public void update(double deltaTime) {

    }

    public static void main(String[] args) {
        try {
            new Bdv(GRID_TEMPLATE.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
