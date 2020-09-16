package examples.dungeon.tiles;

import engine.texture.SpriteSheet;
import examples.dungeon.system.TileMapping;

import java.awt.*;
import java.io.File;

public class Stone extends Tile {

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 3);

    public Stone() {
        super();
        this.type = TileMapping.STONE.getTile();
        this.solid = true;
    }

    public Stone(int x, int y) {
        super(x, y);
        this.type = TileMapping.STONE.getTile();
        this.solid = true;
    }

    public Stone(int x, int y, boolean solid) {
        super(x, y, solid);
        this.type = TileMapping.STONE.getTile();
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }
}
