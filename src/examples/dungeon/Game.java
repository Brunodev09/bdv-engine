package examples.dungeon;

import engine.Bdv;
import engine.api.BdvScriptGL;
import engine.api.EntityAPI;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;
import engine.texture.SpriteSheet;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BdvScriptGL {

    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/res/basic").getAbsolutePath();
    SpriteSheet wall = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 5, 3);
    SpriteSheet dirt = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 3);

    private static final int ROWS = 100;
    private static final int COLS = 100;
    private final Dimension tileSize;
    private final Random random = new Random();

    private int[][] world;

    // this will turn into a enum
    final int FREE = 0;
    final int WALL = 1;
    final int DOOR = 2;
    final int PICKUP = 3;
    final int MOB = 4;
    final int ENTRANCE = 9;
    final int EXIT = 10;

    public Game() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.FPS = 10;
        this.tileSize = new Dimension(this.resolution.width / ROWS, this.resolution.height / COLS);

        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        // formula to calculate how many rooms to generate based on the player level
        final int numberOfRooms = 10;
        final int roomMaxWidth = 11;
        final int roomMaxHeight = 11;

        world = generateWorld(ROWS, COLS);
        generateDungeon(world, 1, 10, 12, 12);
        render();
    }

    @Override
    public void update() {

    }

    public void render() {
        int xIterator = 0;
        for (int i = -ROWS / 2; i < ROWS / 2; i++) {
            int yIterator = 0;
            for (int j = -COLS / 2; j < COLS / 2; j++) {
                EntityAPI entityAPI = null;
                if (world[yIterator][xIterator] == FREE) {
                    entityAPI = new EntityAPI(null,
                            new Vector3f(
                                    (float) tileSize.width * i,
                                    (float) tileSize.height * j, 0),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(dirt);
                }
                else if (world[yIterator][xIterator] == WALL) {
                    entityAPI = new EntityAPI(null,
                            new Vector3f(
                                    (float) tileSize.width * i,
                                    (float) tileSize.height * j, 0),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(wall);
                }
                if (entityAPI == null) continue;
                this.entities.add(entityAPI);
                yIterator++;
            }
            xIterator++;
        }
    }

    public static void main(String[] args) {
        try {
            new Bdv(Game.class);
        } catch (Exception exception) {
            LOG.log(Level.SEVERE, exception.toString(), exception);
        }
    }

    public int[][] generateWorld(int width, int height) {
        return new int[width][height];
    }

    public void generateDungeon(int[][] world, int playerLevel, int numberOfRooms, int roomMaxWidth, int roomMaxHeight) {

        int numberOfRoomsToBeGenerated = numberOfRooms;
        // list storing each room's tiles
        List<List<int[]>> rooms = new ArrayList<>();

        while (numberOfRoomsToBeGenerated > 0) {
            final int roomWidth = random.nextInt(roomMaxWidth - 1) + 1;
            final int roomHeight = random.nextInt(roomMaxHeight - 1) + 1;

            int[] randomFreePoint;

            // each list contain a list of free tiles in that specific line matching the index
            List<List<Integer>> freeTilesByLine = new ArrayList<>();

            // navigates the world line by line checking for free tiles
            for (int i = 0; i < world.length; i++) {
                List<Integer> freeTilesOnThisLine = new ArrayList<>();
                for (int j = 0; j < world[i].length; j++) {
                    if (world[j][i] == FREE) {
                        freeTilesOnThisLine.add(j);
                    }
                }
                freeTilesByLine.add(freeTilesOnThisLine);
            }

            boolean obstructed = false;
            boolean roomCreated = false;

            while (!roomCreated) {
                // attempting to carve the room

                randomFreePoint = findRandomFreeTileOnWorld(world, freeTilesByLine.size());
                int[][] searchFactors = new int[][]{
                        {
                                1, 1
                        },
                        {
                                -1, 1
                        },
                        {
                                1, -1
                        },
                        {
                                -1, -1
                        },
                };
                int carvingAttempts = 4;

                // checking for the edges of the dungeon
                if ((randomFreePoint[0] + roomWidth > ROWS || randomFreePoint[1] + roomHeight > COLS) &&
                        (randomFreePoint[0] - roomWidth < 0 || randomFreePoint[1] - roomHeight < 0)) {
                    obstructed = true;
                }

                if (obstructed) continue;

                int iterator = 0;
                while (carvingAttempts > 0) {
                    List<int[]> room = createRoom(roomWidth, roomHeight, randomFreePoint,
                            searchFactors[iterator][iterator], searchFactors[iterator][iterator]);
                    if (!room.isEmpty()) {
                        roomCreated = true;
                        rooms.add(room);
                        numberOfRoomsToBeGenerated--;
                        break;
                    }
                    carvingAttempts--;
                    iterator++;
                }
            }
        }
        populateWorldWithRooms(world, rooms);
    }

    public int[] findRandomFreeTileOnWorld(int[][] world, int numberOfFreeTiles) {
        int randomPointOnWorldX = random.nextInt(numberOfFreeTiles);
        int randomPointOnWorldY = random.nextInt(numberOfFreeTiles);
        while (world[randomPointOnWorldX][randomPointOnWorldY] != FREE) {
            randomPointOnWorldX = random.nextInt(numberOfFreeTiles);
            randomPointOnWorldY = random.nextInt(numberOfFreeTiles);
        }
        return new int[]{
                randomPointOnWorldX,
                randomPointOnWorldY
        };
    }

    public List<int[]> createRoom(int cellsToSearchX, int cellsToSearchY, int[] start, int xSearchFactor, int ySearchFactor) {
        boolean canCreateRoom = true;
        List<int[]> room = new ArrayList<>();
        int xSearch = 0;
        for (int i = 0; i < cellsToSearchX; i++) {
            int ySearch = 0;
            for (int j = 0; j < cellsToSearchY; j++) {
                if (world[start[0] + (xSearch * xSearchFactor)][start[1] + (ySearch * ySearchFactor)] != FREE) {
                    canCreateRoom = false;
                    break;
                } else {
                    room.add(new int[]{start[0] + (xSearch * xSearchFactor), start[1] + (ySearch * ySearchFactor)});
                }
                ySearch++;
            }
            if (!canCreateRoom) break;
            xSearch++;
        }

        if (!canCreateRoom) room.clear();

        return room;
    }

    public void populateWorldWithRooms(int[][] world, List<List<int[]>> rooms) {
        for (List<int[]> room : rooms) {
            for (int[] coordinates : room) {
                world[coordinates[0]][coordinates[1]] = WALL;
            }
        }
    }
}
