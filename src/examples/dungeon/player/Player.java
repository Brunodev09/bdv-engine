package examples.dungeon.player;

import engine.texture.SpriteSheet;
import examples.dungeon.generation.Location;
import examples.dungeon.objects.InstalledObject;
import examples.dungeon.tiles.Tile;

import java.awt.*;
import java.io.File;

public class Player extends InstalledObject implements Cloneable {

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic2").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 1, 1);

    public Player(Tile tile) {
        super(tile);
        tile.setInstalledObject(this);
    }

    public Player(Location location, Tile tile, int width, int height) {
        super(location, tile, width, height);
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
