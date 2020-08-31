package examples.dungeon.tiles;

import examples.dungeon.system.TileMapping;

public class RoomTile extends Tile {
    public RoomTile() {
        super();
        this.type = TileMapping.ROOM.getTile();
    }

    public RoomTile(int x, int y) {
        super(x, y);
        this.type = TileMapping.ROOM.getTile();
    }

    public RoomTile(int x, int y, boolean solid) {
        super(x, y, solid);
        this.type = TileMapping.ROOM.getTile();
    }
}
