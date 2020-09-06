package examples.dungeon.tiles;

import engine.texture.SpriteSheet;
import examples.dungeon.system.TileMapping;

import java.awt.*;
import java.io.File;

public class PlayerTile extends Tile {
    private static final String SPRITESHEET_FILE_PATH2 = new File("src/examples/dungeon/assets/basic2").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH2, new Rectangle(39, 39), 1, 1);

    public PlayerTile() {
        super();
        this.type = TileMapping.PLAYER.getTile();
    }

    public PlayerTile(int x, int y) {
        super(x, y);
        this.type = TileMapping.PLAYER.getTile();
    }

    public PlayerTile(int x, int y, boolean solid) {
        super(x, y, solid);
        this.type = TileMapping.PLAYER.getTile();
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }

}
