package examples.dungeon.tiles;

import examples.dungeon.system.TileMapping;

public class Void extends Tile {
    public Void() {
        super();
        this.type = TileMapping.FREE.getTile();
    }

    public Void(int x, int y) {
        super(x, y);
        this.type = TileMapping.FREE.getTile();
    }

    public Void(int x, int y, boolean solid) {
        super(x, y, solid);
        this.type = TileMapping.FREE.getTile();
    }
}
