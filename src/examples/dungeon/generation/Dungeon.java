package examples.dungeon.generation;

import examples.dungeon.system.TileMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dungeon extends Location {

    private Random random = new Random();
    private List<List<int[]>> rooms = new ArrayList<>();


    public Dungeon() {
    }

    public List<List<int[]>> getRooms() {
        return rooms;
    }

    public void generateDungeon(int playerLevel,
                                int numberOfRooms,
                                int roomMaxWidth,
                                int roomMaxHeight,
                                int roomMinWidth,
                                int roomMinHeight) {

        int[][] world = WorldManager.getWorld();
        int numberOfRoomsToBeGenerated = numberOfRooms;
        // list storing each room's tiles

        while (numberOfRoomsToBeGenerated > 0) {
            int roomWidth = random.nextInt(roomMaxWidth - 1) + 1;
            int roomHeight = random.nextInt(roomMaxHeight - 1) + 1;

            if (roomWidth < roomMinWidth) roomWidth = roomMinWidth;
            if (roomHeight < roomMinHeight) roomHeight = roomMinHeight;

            int[] randomFreePoint;

            // each list contain a list of free tiles in that specific line matching the index
            List<List<Integer>> freeTilesByLine = new ArrayList<>();

            // navigates the world line by line checking for free tiles
            for (int i = 0; i < world.length; i++) {
                List<Integer> freeTilesOnThisLine = new ArrayList<>();
                for (int j = 0; j < world[i].length; j++) {
                    if (world[j][i] == TileMapping.FREE.getTile()) {
                        freeTilesOnThisLine.add(j);
                    }
                }
                freeTilesByLine.add(freeTilesOnThisLine);
            }

            boolean obstructed;
            boolean roomCreated = false;

            while (!roomCreated) {
                // attempting to carve the room

                obstructed = false;
                randomFreePoint = WorldManager.findRandomFreeTileOnWorld(freeTilesByLine.size());
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
                if ((randomFreePoint[0] + roomWidth > WorldManager.getROWS() ||
                        randomFreePoint[1] + roomHeight > WorldManager.getCOLS()) &&
                        (randomFreePoint[0] - roomWidth < 0 || randomFreePoint[1] - roomHeight < 0)) {
                    obstructed = true;
                }

                if (obstructed) continue;

                int iterator = 0;
                while (carvingAttempts > 0) {
                    List<int[]> room = createRoom(roomWidth, roomHeight, randomFreePoint,
                            searchFactors[iterator][1], searchFactors[iterator][1]);
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
        WorldManager.populateWorldWithRooms(rooms);
    }

    public List<int[]> createRoom(int cellsToSearchX, int cellsToSearchY, int[] start, int xSearchFactor, int ySearchFactor) {
        int[][] world = WorldManager.getWorld();
        boolean canCreateRoom = true;
        List<int[]> room = new ArrayList<>();
        int xSearch = 0;
        for (int i = 0; i < cellsToSearchX; i++) {
            int ySearch = 0;
            for (int j = 0; j < cellsToSearchY; j++) {
                if (!WorldManager.tryToAcessTile(start[0] + (xSearch * xSearchFactor),
                        start[1] + (ySearch * ySearchFactor))) {
                    canCreateRoom = false;
                    break;
                }
                if (world[start[0] + (xSearch * xSearchFactor)][start[1] + (ySearch * ySearchFactor)] != TileMapping.FREE.getTile()) {
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
}
