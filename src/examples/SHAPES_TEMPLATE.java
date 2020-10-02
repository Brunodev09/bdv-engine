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

public class SHAPES_TEMPLATE extends BdvScript {

    private static final Logger LOGGER = Logger.getLogger(SHAPES_TEMPLATE.class.getName());
    private static final String WHITE_TEXTURE = new File("src/examples/res/white").getAbsolutePath();
    private static final String BLACK_TEXTURE = new File("src/examples/res/black").getAbsolutePath();

    public SHAPES_TEMPLATE() {
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(800, 600);
        this.background = new RGBA(255, 20, 147, 255);
        this.windowTitle = "GRID";
        this.init(entities, resolution, background);
    }

    @Override
    public void init(List<Entity> entities, Dimension resolution, RGBA background) {
        Entity entity1 = new Entity(new Vector3f(0, 0), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), Model.TEXTURE);
        Entity entity2 = new Entity(new Vector3f(350, 150), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), Model.TEXTURE);

        entity1.loadImage(WHITE_TEXTURE);
        entity2.loadImage(BLACK_TEXTURE);

        this.entities.add(entity1);
        this.entities.add(entity2);
    }

    @Override
    public void update(double deltaTime) {
        for (Entity entity : this.entities) {
            Vector3f prevPosition = entity.getPosition();
            entity.setPosition(new Vector3f((float) (prevPosition.x + (entity.getSpeed().getX() * deltaTime)),
                    (float) (prevPosition.y + (entity.getSpeed().getY() * deltaTime)), 0f));

            if (entity.getPosition().x > this.resolution.width || entity.getPosition().x < 0) {
                entity.setSpeedX(entity.getSpeed().getX() * -1.0f);
            }
            if (entity.getPosition().y > this.resolution.height || entity.getPosition().y < 0) {
                entity.setSpeedY(entity.getSpeed().getY() * -1.0f);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Bdv(SHAPES_TEMPLATE.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
