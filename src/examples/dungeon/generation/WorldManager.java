package examples.dungeon.generation;

import examples.dungeon.system.TileMapping;
import examples.dungeon.tiles.Tile;
import examples.dungeon.tiles.Wall;

import java.util.*;
import java.util.logging.Logger;

public class WorldManager {
    private static final Logger LOG = Logger.getLogger(WorldManager.class.getName());

    private static final Random random = new Random();
    private static final Map<String, Location> world = new HashMap<>();


    private WorldManager() {
    }

    public static void newDungeonLocation(int x, int y, int z, int width, int height) {
        Dungeon location = new Dungeon(x, y, z, width, height);
        String key = generateHashKey(x, y, z);
        world.put(key, location);
    }

    public static void generateDungeonLocationLayout(
            int x,
            int y,
            int z,
            int playerLevel,
            int numberOfRooms,
            int roomMaxWidth,
            int roomMaxHeight,
            int roomMinWidth,
            int roomMinHeight) {
        Dungeon dungeon = (Dungeon) world.get(generateHashKey(x, y, z));
        dungeon.generateDungeon(dungeon, playerLevel, numberOfRooms, roomMaxWidth, roomMaxHeight, roomMinWidth, roomMinHeight);
    }

    public static Location getLocationAtIndex(int x, int y, int z) {
        return world.get(generateHashKey(x, y, z));
    }

    private static String generateHashKey(int x, int y, int z) {
        return x + "," + y + "," + z;
    }

    public static List<List<Tile>> getMapFromLocation(int x, int y, int z) {
        Location location = world.get(generateHashKey(x, y, z));
        return location.getMap();
    }

    // @TODO - Adjust the seed according to number of free leftover tiles not only the leftover lines of tiles
    public static Tile findRandomFreeTileOnMap(int x, int y, int z, int numberOfFreeLines) {
        Location location = world.get(generateHashKey(x, y, z));
        List<List<Tile>> map = location.getMap();
        int randomPointOnWorldX = random.nextInt(numberOfFreeLines);
        int randomPointOnWorldY = random.nextInt(numberOfFreeLines);

        while (map.get(randomPointOnWorldX).get(randomPointOnWorldY).getType() != TileMapping.FREE.getTile()) {
            randomPointOnWorldX = random.nextInt(numberOfFreeLines);
            randomPointOnWorldY = random.nextInt(numberOfFreeLines);
        }
        return map.get(randomPointOnWorldX).get(randomPointOnWorldY);
    }

    public static void populateMapWithRoom(int x, int y, int z, List<Tile> room) {
        List<List<Tile>> map = tryToAcessMap(x, y, z);
        for (Tile tile : room) {
            map.get(tile.getPositionX()).set(tile.getPositionY(), tile);
        }
    }

    public static List<List<Tile>> tryToAcessMap(int xGlobal, int yGlobal, int zGlobal) {
        Location location = world.get(generateHashKey(xGlobal, yGlobal, zGlobal));
        if (location == null) {
            String msg = new StringBuilder()
                    .append("NO SUCH GLOBAL LOCATION AT ")
                    .append("[").append(xGlobal)
                    .append(", ")
                    .append(yGlobal)
                    .append(", ")
                    .append(zGlobal)
                    .append("]")
                    .toString();
            LOG.info(msg);
            return new ArrayList<>();
        }
        return location.getMap();
    }

    public static List<Tile> tryToGetNeighbor(int xGlobal, int yGlobal, int zGlobal, int xLocal, int yLocal) {

        List<Tile> result = new ArrayList<>();
        Tile target = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal, yLocal);

        if (target == null) return result;

        Tile tile1 = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal + 1, yLocal);
        Tile tile2 = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal - 1, yLocal);
        Tile tile3 = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal + 1, yLocal + 1);
        Tile tile4 = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal + 1, yLocal - 1);
        Tile tile5 = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal - 1, yLocal + 1);
        Tile tile6 = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal - 1, yLocal - 1);
        Tile tile7 = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal, yLocal - 1);
        Tile tile8 = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal, yLocal + 1);

        if (tile1 != null) result.add(tile1);
        if (tile2 != null) result.add(tile2);
        if (tile3 != null) result.add(tile3);
        if (tile4 != null) result.add(tile4);
        if (tile5 != null) result.add(tile5);
        if (tile6 != null) result.add(tile6);
        if (tile7 != null) result.add(tile7);
        if (tile8 != null) result.add(tile8);

        return result;
    }

    public static Tile tryGetTile(int xGlobal, int yGlobal, int zGlobal, int xLocal, int yLocal) {
        List<List<Tile>> map = tryToAcessMap(xGlobal, yGlobal, zGlobal);
        if (map == null) return null;
        if (!tryToAcessTile(xGlobal, yGlobal, zGlobal, xLocal, yLocal)) return null;
        Tile tile = map.get(xLocal).get(yLocal);
        return map.get(xLocal).get(yLocal);
    }

    public static void trySetTile(int xGlobal, int yGlobal, int zGlobal, int xLocal, int yLocal, Tile tile) {
        if (!tryToAcessTile(xGlobal, yGlobal, zGlobal, xLocal, yLocal)) return;
        List<List<Tile>> map = getMapFromLocation(xGlobal, yGlobal, zGlobal);
        map.get(xLocal).set(yLocal, tile);
        tile.setPosition(xLocal, yLocal);
    }

    public static boolean tryToAcessTile(int xGlobal, int yGlobal, int zGlobal, int xLocal, int yLocal) {
        try {
            List<List<Tile>> map = tryToAcessMap(xGlobal, yGlobal, zGlobal);
            if (map == null) return false;
            map.get(xLocal).get(yLocal);
        } catch (IndexOutOfBoundsException exception) {
            LOG.info("Out of bounds at [" + xLocal + ", " + yLocal + "]");
            return false;
        }
        return true;
    }

    public static List<Tile> tryGetChunk(int xGlobal, int yGlobal, int zGlobal, int xLocal, int yLocal, int radius) {
        List<Tile> chunk = new ArrayList<>();
        List<List<Tile>> map = tryToAcessMap(xGlobal, yGlobal, zGlobal);
        if (map == null) return chunk;
        Tile tileFrom = tryGetTile(xGlobal, yGlobal, zGlobal, xLocal, yLocal);
        if (tileFrom == null) return chunk;

        for (int i = 0; i < radius; i++) {
            for (int j = 0; j < radius; j++) {
                Tile target = tryGetTile(xGlobal, yGlobal, zGlobal, tileFrom.getPositionX() + i, tileFrom.getPositionY() + j);
                if (target != null) chunk.add(target);
            }
        }

        return chunk;
    }
}
