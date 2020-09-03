package examples.dungeon.system;

public enum TileMapping {
    FREE(0),
    WALL(1),
    DOOR(2),
    PICKUP(3),
    MOB(4),
    ROOM(5),
    STONE(6),
    ENTRANCE(9),
    EXIT(10),
    PLAYER(100);

    private final int tile;

    TileMapping(int tileMap) {
        tile = tileMap;
    }

    public int getTile() {
        return tile;
    }

}
