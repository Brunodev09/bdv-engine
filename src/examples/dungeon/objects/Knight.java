package examples.dungeon.objects;

import engine.texture.SpriteSheet;
import examples.dungeon.algorithms.astar.AStar;
import examples.dungeon.generation.Location;
import examples.dungeon.mob.Brain;
import examples.dungeon.tiles.Tile;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Actor {
    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic2").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 1, new Rectangle(783, 393));

    private final Brain brain = new Brain();

    public Knight(Location location, Tile tile, int width, int height) {
        super(location, tile, width, height, "knight");
        fov = 10;
        mob = true;
        setType("knight");
    }

    @Override
    public int getFov() {
        return fov;
    }

    @Override
    public boolean action() {
        boolean triggerRender = false;
        Location location = this.getCurrentLocation();

        int xGlobal = location.getXGlobal();
        int yGlobal = location.getYGlobal();
        int zGlobal = location.getZGlobal();

        return true;
    }

    @Override
    public boolean move() {
        return true;
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }
}
