package examples.dungeon.objects;

import engine.texture.SpriteSheet;
import examples.dungeon.generation.Location;
import examples.dungeon.mob.Brain;
import examples.dungeon.mob.FactionTypes;
import examples.dungeon.tiles.Tile;

import java.awt.*;
import java.io.File;

public class Knight extends Actor {
    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/assetsComplete").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 21, new Rectangle(783, 1176));

    private final Brain brain;

    public Knight(Location location, Tile tile, int width, int height) {
        super(location, tile, width, height, "knight");
        fov = 10;
        mob = true;
        faction = FactionTypes.HUMANS;
        brain = new Brain(this);
        setType("knight");
    }

    @Override
    public int getFov() {
        return fov;
    }

    @Override
    public boolean action() {
        return move();
    }

    private void moveKnight(Tile newTile) {
        Tile currTile = this.getCurrentTile();
        if (!newTile.isSolid()) {
            this.setPreviousTile(currTile);
            this.setCurrentTile(newTile);
            this.setNextTile(null);
        }
    }

    @Override
    public boolean move() {
        Tile nTile = brain.getNextTile();
        if (currentTile != null && (Math.abs(nTile.getPositionX() - currentTile.getPositionX()) > 2 ||  Math.abs(nTile.getPositionY() - currentTile.getPositionY()) > 2)) {
            nTile = currentTile;
        }
        moveKnight(nTile);
        return true;
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }
}
