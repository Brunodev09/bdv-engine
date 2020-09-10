package examples.dungeon.tiles;

import engine.texture.SpriteSheet;
import examples.dungeon.system.TileMapping;

import java.awt.*;
import java.io.File;

public class DoorTile extends Tile {
    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 4, 1);

    public DoorTile() {
        super();
        this.type = TileMapping.DOOR.getTile();
    }

    public DoorTile(int x, int y) {
        super(x, y);
        this.type = TileMapping.DOOR.getTile();
    }

    public DoorTile(int x, int y, boolean solid) {
        super(x, y, solid);
        this.type = TileMapping.DOOR.getTile();
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }
}
