package app.Templates;

import app.API.Script;
import app.Core.Interfaces.Entity;
import app.Math.Dimension;
import app.Math.RGBA;
import app.Math.Vector2f;
import app.Math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SHAPES_TEMPLATE extends Script {

    public SHAPES_TEMPLATE() {
        this.entities = new ArrayList<Entity>();
        this.resolution = new Dimension(800, 600);
        this.background = new RGBA(255, 20, 147, 255);
        this.windowTitle = "GRID";
        this.init(entities, resolution, background);
    }

    @Override
    public void init(List<Entity> entities, Dimension resolution, RGBA background) {
        this.entities.add(new Entity(new Vector3f(0, 0), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(255, 0, 0, 255)));
        this.entities.add(new Entity(new Vector3f(350, 150), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(0, 0, 255, 255)));
    }

    @Override
    public void update() {
        for (Entity entity : this.entities) {
            entity.setPosition(new Vector3f(entity.getPosition().x + ((Number) entity.getSpeed().getX()).intValue(), entity.getPosition().y + ((Number) entity.getSpeed().getY()).intValue()));

            if (entity.getPosition().x > this.resolution.width || entity.getPosition().x < 0) {
                entity.setSpeedX(entity.getSpeed().getX() * -1.0f);
            }
            if (entity.getPosition().y > this.resolution.height || entity.getPosition().y < 0) {
                entity.setSpeedY(entity.getSpeed().getY() * -1.0f);
            }
        }
    }
}
