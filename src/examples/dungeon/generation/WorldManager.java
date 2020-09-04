package examples.dungeon.generation;

import examples.dungeon.player.Player;
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
            map.get(tile.getPositionX()).set(tile.getPositionY(), new Wall(tile.getPositionX(), tile.getPositionY()));
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

    public static Tile tryGetTile(int xGlobal, int yGlobal, int zGlobal, int xLocal, int yLocal) {
        List<List<Tile>> map = tryToAcessMap(xGlobal, yGlobal, zGlobal);
        if (map == null) return null;
        if (!tryToAcessTile(xGlobal, yGlobal, zGlobal, xLocal, yLocal)) return null;
        Tile tile = map.get(xLocal).get(yLocal);
        LOG.info("Getting tile of id " + tile.getType() + " in position [" + xLocal + ", " + yLocal + "]");
        return map.get(xLocal).get(yLocal);
    }

    public static void trySetTile(int xGlobal, int yGlobal, int zGlobal, int xLocal, int yLocal, Tile tile) {
        if (!tryToAcessTile(xGlobal, yGlobal, zGlobal, xLocal, yLocal)) return;
        List<List<Tile>> map = getMapFromLocation(xGlobal, yGlobal, zGlobal);
        map.get(xLocal).set(yLocal, tile);
        LOG.info("Setting tile of id " + tile.getType() + " in position [" + xLocal + ", " + yLocal + "]");
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
}
