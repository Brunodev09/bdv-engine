package examples.dungeon.generation;

import examples.dungeon.system.TileMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class WorldManager {
    private static final Logger LOG = Logger.getLogger(WorldManager.class.getName());

    private static int[][] world;
    private static int ROWS;
    private static int COLS;
    private static final List<Location> locations = new ArrayList<>();
    private static final Random random = new Random();


    private WorldManager() {}

    public static void newInstance(int width, int height) {
        ROWS = width;
        COLS = height;
        world = generateWorld(width, height);
    }

    public static void addNewDungeon(int playerLevel,
                                     int numberOfRooms,
                                     int roomMaxWidth,
                                     int roomMaxHeight,
                                     int roomMinWidth,
                                     int roomMinHeight) {
        Dungeon dungeon = new Dungeon();
        locations.add(dungeon);
        dungeon.generateDungeon(playerLevel, numberOfRooms, roomMaxWidth, roomMaxHeight, roomMinWidth, roomMinHeight);
    }

    public static Location getLocationAtIndex(int index) {
        if (index < 0 || index > locations.size()) return null;
        return locations.get(index);
    }

    public static int getCOLS() {
        return COLS;
    }

    public static int getROWS() {
        return ROWS;
    }

    public static int[][] getWorld() {
        return world;
    }

    private static int[][] generateWorld(int width, int height) {
        return new int[width][height];
    }

    // @TODO - Adjust the seed according to number of free leftover tiles not only the leftover lines of tiles
    public static int[] findRandomFreeTileOnWorld(int numberOfFreeLines) {
        int randomPointOnWorldX = random.nextInt(numberOfFreeLines);
        int randomPointOnWorldY = random.nextInt(numberOfFreeLines);
        while (world[randomPointOnWorldX][randomPointOnWorldY] != TileMapping.FREE.getTile()) {
            randomPointOnWorldX = random.nextInt(numberOfFreeLines);
            randomPointOnWorldY = random.nextInt(numberOfFreeLines);
        }
        return new int[]{
                randomPointOnWorldX,
                randomPointOnWorldY
        };
    }

    public static void populateWorldWithRooms(List<List<int[]>> rooms) {
        for (List<int[]> room : rooms) {
            for (int[] coordinates : room) {
                world[coordinates[0]][coordinates[1]] = TileMapping.WALL.getTile();
            }
        }
    }

    public static boolean tryToAcessTile(int x, int y) {
        try {
            int access = world[x][y];
        } catch (ArrayIndexOutOfBoundsException exception) {
            LOG.info("Out of bounds at [" + x + ", " + y + "]");
            return false;
        }
        return true;
    }

}
