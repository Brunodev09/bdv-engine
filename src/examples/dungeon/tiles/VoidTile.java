package examples.dungeon.tiles;

import engine.texture.SpriteSheet;
import examples.dungeon.system.TileMapping;

import java.awt.*;
import java.io.File;

public class VoidTile extends Tile {
    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/assetsComplete").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 3, 2, new Rectangle(783, 1176));

    public VoidTile() {
        super();
        this.type = TileMapping.FREE.getTile();
    }

    public VoidTile(int x, int y) {
        super(x, y);
        this.type = TileMapping.FREE.getTile();
    }

    public VoidTile(int x, int y, boolean solid) {
        super(x, y, solid);
        this.type = TileMapping.FREE.getTile();
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }

}
