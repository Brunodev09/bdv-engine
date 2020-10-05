package examples.dungeon.generation;

import examples.dungeon.tiles.Stone;
import examples.dungeon.tiles.Tile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Location {
    private final int xGlobal;
    private final int yGlobal;
    private final int zGlobal;
    private List<List<Tile>> map = new ArrayList<>();
    private final int mapWidth;
    private final int mapHeight;
    private Class<?> filler;

    public Location(int x, int y, int z, int width, int height) {
        this.xGlobal = x;
        this.yGlobal = y;
        this.zGlobal = z;
        this.mapWidth = width;
        this.mapHeight = height;
        try {
            this.fillWorld(width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Location(int x, int y, int z, int width, int height, Class<?> initialTileFiller) {
        this.xGlobal = x;
        this.yGlobal = y;
        this.zGlobal = z;
        this.mapWidth = width;
        this.mapHeight = height;
        this.filler = initialTileFiller;
        try {
            this.fillWorld(width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void fillWorld(int width, int height) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (int i = 0; i < width; i++) {
            List<Tile> tiles = new ArrayList<>();
            for (int j = 0; j < height; j++) {
                if (filler != null) {
                    Constructor<?> tileConstructor = filler.getConstructor(int.class, int.class);
                    Tile tile = (Tile) tileConstructor.newInstance(i, j);
                    tiles.add(tile);
                }
                else tiles.add(new Stone(i, j));
            }
            map.add(tiles);
        }
    }

    public void updateMap(List<List<Tile>> map) {
        this.map = map;
    }
}
