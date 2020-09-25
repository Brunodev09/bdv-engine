package examples.dungeon.objects;

import engine.texture.SpriteSheet;
import examples.dungeon.generation.Location;
import examples.dungeon.tiles.Tile;

import java.awt.*;
import java.io.File;

public class Knight extends Actor {
    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic2").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 1, new Rectangle(783, 393));

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
    public SpriteSheet getSprite() {
        return sprite;
    }
}
