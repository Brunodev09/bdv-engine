package examples.dungeon.tiles;

import examples.dungeon.system.TileMapping;

public class PlayerTile extends Tile {
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
}
