package examples.dungeon;

import engine.Bdv;
import engine.api.BdvScriptGL;
import engine.api.EntityAPI;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BdvScriptGL {

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private static final int ROWS = 100;
    private static final int COLS = 100;
    private final Dimension tileSize;
    private final Random random = new Random();

    private int[][] world;

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

    }

    @Override
    public void update() {

    }

    public static void main(String[] args) {
        try {
            new Bdv(Game.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }

    public int[][] generateWorld(int width, int height) {
        return new int[width][height];
    }

    public void generateDungeon(int[][] world, int playerLevel, int numberOfRooms, int roomMaxWidth, int roomMaxHeight) {

        int numberOfRoomsGenerated = numberOfRooms;
        // list storing each room's tiles
        List<List<int[]>> rooms = new ArrayList<>();

        while (numberOfRoomsGenerated > 0) {
            final int roomWidth = random.nextInt(roomMaxWidth - 1) + 1;
            final int roomHeight = random.nextInt(roomMaxHeight - 1) + 1;

            // this will turn into a enum
            final int FREE = 0;
            final int OCCUPIED = 1;
            final int DOOR = 2;
            final int PICKUP = 3;
            final int MOB = 4;

            final int ENTRANCE = 9;
            final int EXIT = 10;

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

            int sequenceToSearchHorizontally = roomWidth;
            int sequenceToSearchVertically = roomHeight;
            boolean obstructed = false;
            boolean roomCreated = false;

            while (!roomCreated) {
                // attempting to carve the room

                randomFreePoint = findRandomFreeTileOnWorld(world, freeTilesByLine.size());

                // checking for the edges of the dungeon
                if (randomFreePoint[0] + sequenceToSearchHorizontally > ROWS) obstructed = true;
                if (randomFreePoint[1] + sequenceToSearchVertically > COLS) obstructed = true;
                if (randomFreePoint[0] - sequenceToSearchHorizontally < 0) obstructed = true;
                if (randomFreePoint[1] - sequenceToSearchVertically < 0) obstructed = true;

                if (obstructed) continue;

                List<int[]> room = createRoom(sequenceToSearchHorizontally, sequenceToSearchVertically, randomFreePoint);

                if (!room.isEmpty()) {
                    roomCreated = true;
                    rooms.add(room);
                }
            }

            numberOfRoomsGenerated--;
        }


    }

    public int[] findRandomFreeTileOnWorld(int[][] world, int numberOfFreeTiles) {
        final int FREE = 0;
        int randomPointOnWorldX = 0;
        int randomPointOnWorldY = 0;
        while (world[randomPointOnWorldX][randomPointOnWorldY] != FREE) {
            randomPointOnWorldX = random.nextInt(numberOfFreeTiles);
            randomPointOnWorldY = random.nextInt(numberOfFreeTiles);
        }
        return new int[]{
                randomPointOnWorldX,
                randomPointOnWorldY
        };
    }

    public List<int[]> createRoom(int cellsToSearchX, int cellsToSearchY, int[] start) {
        int FREE = 0;
        boolean canCreateRoom = true;
        List<int[]> room = new ArrayList<>();
        int xSearch = 0;
        for (int i = 0; i < cellsToSearchX; i++) {
            int ySearch = 0;
            for (int j = 0; j < cellsToSearchY; j++) {
                if (world[start[0] + xSearch][start[1] + ySearch] != FREE) {
                    canCreateRoom = false;
                    break;
                }
                else room.add(new int[] {start[0] + xSearch, start[1] + ySearch});
                ySearch++;
            }
            if (!canCreateRoom) break;
            xSearch++;
        }
        return room;
    }
}
