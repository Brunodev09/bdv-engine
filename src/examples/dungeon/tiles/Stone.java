package examples.dungeon.tiles;

import examples.dungeon.system.TileMapping;

public class Stone extends Tile {
    public Stone() {
        super();
        this.type = TileMapping.STONE.getTile();
    }

    public Stone(int x, int y) {
        super(x, y);
        this.type = TileMapping.STONE.getTile();
    }

    public Stone(int x, int y, boolean solid) {
        super(x, y, solid);
        this.type = TileMapping.STONE.getTile();
    }
}
