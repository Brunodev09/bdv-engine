package examples.dungeon.tiles;

import examples.dungeon.system.TileMapping;

public class Wall extends Tile {
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
}
