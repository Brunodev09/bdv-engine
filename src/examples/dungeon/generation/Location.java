package examples.dungeon.generation;

import examples.dungeon.tiles.Stone;
import examples.dungeon.tiles.Tile;
import examples.dungeon.tiles.VoidTile;

import java.util.ArrayList;
import java.util.List;

public abstract class Location {
    private final int xGlobal;
    private final int yGlobal;
    private final int zGlobal;
    private List<List<Tile>> map = new ArrayList<>();
    private final int mapWidth;
    private final int mapHeight;

    public Location(int x, int y, int z, int width, int height) {
        this.xGlobal = x;
        this.yGlobal = y;
        this.zGlobal = z;
        this.mapWidth = width;
        this.mapHeight = height;
        this.fillWorld(width, height);
    }

    public int getXGlobal() {
        return xGlobal;
    }

    public int getYGlobal() {
        return yGlobal;
    }

    public int getZGlobal() {
        return zGlobal;
    }

    public List<List<Tile>> getMap() {
        return map;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void fillWorld(int width, int height) {
        for (int i = 0; i < width; i++) {
            List<Tile> tiles = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                tiles.add(new Stone(i, j));
            }
            map.add(tiles);
        }
    }

    public void updateMap(List<List<Tile>> map) {
        this.map = map;
    }
}
