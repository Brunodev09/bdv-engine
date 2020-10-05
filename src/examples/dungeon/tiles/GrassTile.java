package examples.dungeon.tiles;

import engine.texture.SpriteSheet;
import examples.dungeon.system.TileMapping;

import java.awt.*;
import java.io.File;

public class GrassTile extends Tile {
    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/assetsComplete").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 11, 4, new Rectangle(783, 1176));

    public GrassTile() {
        super();
        this.type = TileMapping.FREE.getTile();
    }

    public GrassTile(int x, int y) {
        super(x, y);
        this.type = TileMapping.FREE.getTile();
    }

    public GrassTile(int x, int y, boolean solid) {
        super(x, y, solid);
        this.type = TileMapping.FREE.getTile();
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }
}
