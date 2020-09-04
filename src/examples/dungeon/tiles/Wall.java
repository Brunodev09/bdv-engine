package examples.dungeon.tiles;

import engine.texture.SpriteSheet;
import examples.dungeon.system.TileMapping;

import java.awt.*;
import java.io.File;

public class Wall extends Tile {
    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 5, 3);

    public Wall() {
        super();
        this.type = TileMapping.WALL.getTile();
    }

    public Wall(int x, int y) {
        super(x, y);
        this.type = TileMapping.WALL.getTile();
    }

    public Wall(int x, int y, boolean solid) {
        super(x, y, solid);
        this.type = TileMapping.WALL.getTile();
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }
}
