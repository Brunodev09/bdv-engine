package app.Templates;

import app.API.Script;
import app.Core.Interfaces.Entity;
import app.Math.Dimension;
import app.Math.Point;
import app.Math.RGBA;

import java.util.ArrayList;
import java.util.List;

public class GRID_TEMPLATE extends Script {

    public GRID_TEMPLATE() {
        this.entities = new ArrayList<Entity>();
        this.resolution = new Dimension(800, 600);
        this.background = new RGBA(255, 20, 147, 255);
        this.init(entities, resolution, background);
    }

    @Override
    public void init(List<Entity> entities, Dimension resolution, RGBA background) {
        int rows = 10;
        int cols = 10;
        var tileSize = new Dimension(this.resolution.width / rows, this.resolution.height / cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.entities.add(new Entity(new Point<>(tileSize.width * i + i, tileSize.height * j + j),
                        new Point<>(0f, 0f), tileSize, new RGBA(133, 133, 133, 255)));
            }
        }
    }

    @Override
    public void update() {

    }
}
