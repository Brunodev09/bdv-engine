package examples.dungeon.generation;

import examples.dungeon.algorithms.astar.AStar;
import examples.dungeon.algorithms.astar.AStarNode;
import examples.dungeon.system.TileMapping;
import examples.dungeon.tiles.*;
import examples.dungeon.tiles.Void;

import java.util.*;
import java.util.logging.Logger;

public class Dungeon extends Location {

    private static final Logger LOG = Logger.getLogger(Dungeon.class.getName());

    private Random random = new Random();
    private List<List<Tile>> rooms = new ArrayList<>();
    private Dungeon dungeon;

    public Dungeon(int x, int y, int z, int width, int height) {
        super(x, y, z, width, height);
    }


    public List<List<Tile>> getRooms() {
        return rooms;
    }

    public void generateDungeon(Dungeon dungeon,
                                int playerLevel,
                                int numberOfRooms,
                                int roomMaxWidth,
                                int roomMaxHeight,
                                int roomMinWidth,
                                int roomMinHeight) {
        this.dungeon = dungeon;

        List<List<Tile>> world = dungeon.getMap();
        int numberOfRoomsToBeGenerated = numberOfRooms;
        // list storing each room's tiles

        while (numberOfRoomsToBeGenerated > 0) {
            int roomWidth = random.nextInt(roomMaxWidth - 1) + 1;
            int roomHeight = random.nextInt(roomMaxHeight - 1) + 1;

            if (roomWidth < roomMinWidth) roomWidth = roomMinWidth;
            if (roomHeight < roomMinHeight) roomHeight = roomMinHeight;

            Tile randomFreePoint;

            // each list contain a list of free tiles in that specific line matching the index
            List<List<Integer>> freeTilesByLine = new ArrayList<>();

            // navigates the world line by line checking for free tiles
            for (int i = 0; i < world.size(); i++) {
                List<Integer> freeTilesOnThisLine = new ArrayList<>();
                for (int j = 0; j < world.get(i).size(); j++) {
                    if (world.get(j).get(i).getType() == TileMapping.FREE.getTile() && world.get(j).get(i).getInstalledObject() == null) {
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
                randomFreePoint = WorldManager.findRandomFreeTileOnMap(
                        dungeon.getXGlobal(),
                        dungeon.getYGlobal(),
                        dungeon.getZGlobal(),
                        freeTilesByLine.size());

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
                if ((randomFreePoint.getPositionX() + roomWidth > dungeon.getMapWidth() ||
                        randomFreePoint.getPositionY() + roomHeight > dungeon.getMapHeight()) &&
                        (randomFreePoint.getPositionX() - roomWidth < 0 || randomFreePoint.getPositionY() - roomHeight < 0)) {
                    obstructed = true;
                }

                if (obstructed) continue;

                int iterator = 0;
                while (carvingAttempts > 0) {
                    List<Tile> room = createRoom(roomWidth, roomHeight, randomFreePoint,
                            searchFactors[iterator][0], searchFactors[iterator][1]);
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

        List<Tile> toUpdate = generateMaze();
        for (Tile tile : toUpdate) {
            if (WorldManager.tryGetTile(dungeon.getXGlobal(), dungeon.getYGlobal(), dungeon.getZGlobal(), tile.getPositionX(), tile.getPositionY()).getInstalledObject() != null) continue;
            WorldManager.trySetTile(dungeon.getXGlobal(), dungeon.getYGlobal(), dungeon.getZGlobal(), tile.getPositionX(), tile.getPositionY(), new Stone(tile.getPositionX(), tile.getPositionY()));
        }
        List<Tile> toUpdateDoorPathTiles = connectDoors();
        for (Tile tile : toUpdateDoorPathTiles) {
            if (WorldManager.tryGetTile(dungeon.getXGlobal(), dungeon.getYGlobal(), dungeon.getZGlobal(), tile.getPositionX(), tile.getPositionY()).getInstalledObject() != null) continue;
            WorldManager.trySetTile(dungeon.getXGlobal(), dungeon.getYGlobal(), dungeon.getZGlobal(), tile.getPositionX(), tile.getPositionY(), new Void(tile.getPositionX(), tile.getPositionY()));
        }
    }

    public List<Tile> createRoom(int cellsToSearchX, int cellsToSearchY, Tile start, int xSearchFactor, int ySearchFactor) {
        List<List<Tile>> world = this.dungeon.getMap();
        boolean canCreateRoom = true;
        boolean hasDoorBeenCreated = false;
        List<Tile> room = new ArrayList<>();
        int xSearch = 0;
        for (int i = 0; i < cellsToSearchX; i++) {
            int ySearch = 0;
            for (int j = 0; j < cellsToSearchY; j++) {
                if (!WorldManager.tryToAcessTile(dungeon.getXGlobal(),
                        dungeon.getYGlobal(),
                        dungeon.getZGlobal(),
                        start.getPositionX() + (xSearch * xSearchFactor),
                        start.getPositionY() + (ySearch * ySearchFactor))) {
                    canCreateRoom = false;
                    break;
                }
                if (WorldManager.tryGetTile(dungeon.getXGlobal(),
                        dungeon.getYGlobal(),
                        dungeon.getZGlobal(),
                        start.getPositionX() + (xSearch * xSearchFactor),
                        start.getPositionY() + (ySearch * ySearchFactor)).getInstalledObject() != null) {
                    canCreateRoom = false;
                    break;
                }
                if (world.get(start.getPositionX() + (xSearch * xSearchFactor)).get(start.getPositionY() + (ySearch * ySearchFactor)).getType() != TileMapping.FREE.getTile()) {
                    canCreateRoom = false;
                    break;
                } else {
                    if (room.size() > cellsToSearchY && room.size() < (cellsToSearchY * (cellsToSearchX - 1)) && ySearch != 0 && ySearch != cellsToSearchY - 1) {
                        room.add(new RoomTile(start.getPositionX() + (xSearch * xSearchFactor), start.getPositionY() + (ySearch * ySearchFactor)));

                    } else {
                        if (!hasDoorBeenCreated && random.nextInt(3) == 2 && checkNeighboursForPath(start.getPositionX() + (xSearch * xSearchFactor), start.getPositionY() + (ySearch * ySearchFactor))) {
                            hasDoorBeenCreated = true;
                            room.add(new DoorTile(start.getPositionX() + (xSearch * xSearchFactor), start.getPositionY() + (ySearch * ySearchFactor)));
                        }
                        else room.add(new Wall(start.getPositionX() + (xSearch * xSearchFactor), start.getPositionY() + (ySearch * ySearchFactor)));
                    }
                }
                ySearch++;
            }
            if (!canCreateRoom) break;
            xSearch++;
        }

        if (!canCreateRoom) room.clear();
        else {
            WorldManager.populateMapWithRoom(dungeon.getXGlobal(), dungeon.getYGlobal(), dungeon.getZGlobal(), room);
        }

        return room;
    }

    public boolean checkNeighboursForPath(int x, int y) {
        int xGlobal = dungeon.getXGlobal();
        int yGlobal = dungeon.getYGlobal();
        int zGlobal = dungeon.getZGlobal();

        Tile tile = WorldManager.tryGetTile(xGlobal, yGlobal, zGlobal, x + 1, y);
        Tile tile2 = WorldManager.tryGetTile(xGlobal, yGlobal, zGlobal, x - 1, y);
//        Tile tile3 = WorldManager.tryGetTile(xGlobal, yGlobal, zGlobal, x + 1, y + 1);
//        Tile tile4 = WorldManager.tryGetTile(xGlobal, yGlobal, zGlobal, x + 1, y - 1);
//        Tile tile5 = WorldManager.tryGetTile(xGlobal, yGlobal, zGlobal, x - 1, y + 1);
//        Tile tile6 = WorldManager.tryGetTile(xGlobal, yGlobal, zGlobal, x - 1, y - 1);
        Tile tile7 = WorldManager.tryGetTile(xGlobal, yGlobal, zGlobal, x, y + 1);
        Tile tile8 = WorldManager.tryGetTile(xGlobal, yGlobal, zGlobal, x, y - 1);

        if (tile == null) tile = new Wall(0, 0);
        if (tile2 == null) tile2 = new Wall(0, 0);
//        if (tile3 == null) tile3 = new Wall(0, 0);
//        if (tile4 == null) tile4 = new Wall(0, 0);
//        if (tile5 == null) tile5 = new Wall(0, 0);
//        if (tile6 == null) tile6 = new Wall(0, 0);
        if (tile7 == null) tile7 = new Wall(0, 0);
        if (tile8 == null) tile8 = new Wall(0, 0);

        if (tile.getType() == TileMapping.FREE.getTile() ||
                tile2.getType() == TileMapping.FREE.getTile() ||
//                tile3.getType() == TileMapping.FREE.getTile() ||
//                tile4.getType() == TileMapping.FREE.getTile() ||
//                tile5.getType() == TileMapping.FREE.getTile() ||
//                tile6.getType() == TileMapping.FREE.getTile() ||
                tile7.getType() == TileMapping.FREE.getTile() ||
                tile8.getType() == TileMapping.FREE.getTile()) {
            return true;
        }
        return false;
    }

    public List<Tile> generateMaze() {
//                          Depth first search
//        Choose the initial cell, mark it as visited and push it to the stack
//        While the stack is not empty
//        Pop a cell from the stack and make it a current cell
//        If the current cell has any neighbours which have not been visited
//        Push the current cell to the stack
//        Choose one of the unvisited neighbours
//        Remove the wall between the current cell and the chosen cell
//        Mark the chosen cell as visited and push it to the stack

//        Adapting algorithm to suit tiled map: will only search C (cell) tiles and not B (blocks)
//        by making every odd pair of coordinates a "block" and searching through even cells.
//          C C C
//          C B C
//          C C C
        List<List<Tile>> map = WorldManager.getMapFromLocation(this.getXGlobal(), this.getYGlobal(), this.getZGlobal());
        List<Tile> tilesToUpdate = new ArrayList<>();
        List<Tile> visitedTiles = new ArrayList<>();
        Stack<Tile> stack = new Stack<>();

        Tile initialTile = WorldManager.findRandomFreeTileOnMap(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), 100);
        Tile evenTile = map.get((initialTile.getPositionX() / 2) * 2).get((initialTile.getPositionY() / 2) * 2);
        visitedTiles.add(evenTile);
        stack.push(evenTile);

        while (!stack.isEmpty()) {
            List<Tile> notVisitedThisIteration = new ArrayList<>();
            Tile current = stack.pop();
            boolean hasAny = false;

            Tile n1 = null;
            Tile n2 = null;
            Tile n3 = null;
            Tile n4 = null;
            Tile n5 = null;
            Tile n6 = null;
            Tile n7 = null;
            Tile n8 = null;

            if (WorldManager.tryToAcessTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), current.getPositionX() + 2, current.getPositionY())) {
                n1 = map.get(current.getPositionX() + 2).get(current.getPositionY());
            }
            if (WorldManager.tryToAcessTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), current.getPositionX() - 2, current.getPositionY())) {
                n2 = map.get(current.getPositionX() - 2).get(current.getPositionY());
            }
            if (WorldManager.tryToAcessTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), current.getPositionX() + 2, current.getPositionY() + 2)) {
                n3 = map.get(current.getPositionX() + 2).get(current.getPositionY() + 2);
            }
            if (WorldManager.tryToAcessTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), current.getPositionX() - 2, current.getPositionY() - 2)) {
                n4 = map.get(current.getPositionX() - 2).get(current.getPositionY() - 2);
            }
            if (WorldManager.tryToAcessTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), current.getPositionX() - 2, current.getPositionY() + 2)) {
                n5 = map.get(current.getPositionX() - 2).get(current.getPositionY() + 2);
            }
            if (WorldManager.tryToAcessTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), current.getPositionX() + 2, current.getPositionY() - 2)) {
                n6 = map.get(current.getPositionX() + 2).get(current.getPositionY() - 2);
            }
            if (WorldManager.tryToAcessTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), current.getPositionX(), current.getPositionY() + 2)) {
                n7 = map.get(current.getPositionX()).get(current.getPositionY() + 2);
            }
            if (WorldManager.tryToAcessTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), current.getPositionX(), current.getPositionY() - 2)) {
                n8 = map.get(current.getPositionX()).get(current.getPositionY() - 2);
            }

            if (n1 != null && !visitedTiles.contains(n1) && n1.getType() == TileMapping.FREE.getTile()) {
                hasAny = true;
                notVisitedThisIteration.add(n1);
            }
            if (n2 != null && !visitedTiles.contains(n2) && n2.getType() == TileMapping.FREE.getTile()) {
                hasAny = true;
                notVisitedThisIteration.add(n2);
            }
            if (n3 != null && !visitedTiles.contains(n3) && n3.getType() == TileMapping.FREE.getTile()) {
                hasAny = true;
                notVisitedThisIteration.add(n3);
            }
            if (n4 != null && !visitedTiles.contains(n4) && n4.getType() == TileMapping.FREE.getTile()) {
                hasAny = true;
                notVisitedThisIteration.add(n4);
            }
            if (n5 != null && !visitedTiles.contains(n5) && n5.getType() == TileMapping.FREE.getTile()) {
                hasAny = true;
                notVisitedThisIteration.add(n5);
            }
            if (n6 != null && !visitedTiles.contains(n6) && n6.getType() == TileMapping.FREE.getTile()) {
                hasAny = true;
                notVisitedThisIteration.add(n6);
            }
            if (n7 != null && !visitedTiles.contains(n7) && n7.getType() == TileMapping.FREE.getTile()) {
                hasAny = true;
                notVisitedThisIteration.add(n7);
            }
            if (n8 != null && !visitedTiles.contains(n8) && n8.getType() == TileMapping.FREE.getTile()) {
                hasAny = true;
                notVisitedThisIteration.add(n8);
            }

            if (hasAny) stack.push(current);
            else continue;

            int randInt = random.nextInt(notVisitedThisIteration.size());
            Tile randomN = notVisitedThisIteration.get(randInt);
            visitedTiles.add(randomN);
            stack.push(randomN);

            // updating only the "block" tiles
            Tile tileToUpdate = null;
            if (randomN == n1) {
                tileToUpdate = map.get(current.getPositionX() + 1).get(current.getPositionY());
            }
            if (randomN == n2) {
                tileToUpdate = map.get(current.getPositionX() - 1).get(current.getPositionY());
            }
            if (randomN == n3) {
                tileToUpdate = map.get(current.getPositionX() + 1).get(current.getPositionY() + 1);
            }
            if (randomN == n4) {
                tileToUpdate = map.get(current.getPositionX() - 1).get(current.getPositionY() - 1);
            }
            if (randomN == n5) {
                tileToUpdate = map.get(current.getPositionX() - 1).get(current.getPositionY() + 1);
            }
            if (randomN == n6) {
                tileToUpdate = map.get(current.getPositionX() + 1).get(current.getPositionY() - 1);
            }
            if (randomN == n7) {
                tileToUpdate = map.get(current.getPositionX()).get(current.getPositionY() + 1);
            }
            if (randomN == n8) {
                tileToUpdate = map.get(current.getPositionX()).get(current.getPositionY() - 1);
            }

            if (tileToUpdate != null && tileToUpdate.getType() == TileMapping.FREE.getTile())
                tilesToUpdate.add(tileToUpdate);

            tilesToUpdate.add(randomN);
            tilesToUpdate.add(current);
        }

        return tilesToUpdate;
    }

    public List<Tile> connectDoors() {
        List<List<Tile>> map = WorldManager.getMapFromLocation(this.getXGlobal(), this.getYGlobal(), this.getZGlobal());
        int rows = WorldManager.getLocationAtIndex(this.getXGlobal(), this.getYGlobal(), this.getZGlobal()).getMapWidth();
        int cols = WorldManager.getLocationAtIndex(this.getXGlobal(), this.getYGlobal(), this.getZGlobal()).getMapHeight();
        List<Tile> tilesToUpdate = new ArrayList<>();
        Map<String, Integer> tracker = new HashMap<>();
        List<Tile> doors = new ArrayList<>();

        int i = 0;
        for (List<Tile> room : this.rooms) {
            Tile door = null;
            List<Tile> endpoints = new ArrayList<>();
            for (Tile tile : room) {
                endpoints.clear();
                if (tile.getType() == TileMapping.DOOR.getTile()) {
                    door = tile;
                    break;
                }
            }
            for (int x = i + 1; x < this.rooms.size() - 1; x++) {
                for (int y = 0; y < this.rooms.get(x).size(); y++) {
                    if (this.rooms.get(x).get(y).getType() == TileMapping.DOOR.getTile()) {
                        endpoints.add(this.rooms.get(x).get(y));
                    }
                }
            }
            for (Tile doorToConnect : endpoints) {
                int found = 0;
                for (Tile repeatedPair : doors) {
                    if (doorToConnect == repeatedPair || door == repeatedPair) {
                        found++;
                    }
                }

                if (found >= 2) continue;

                AStar aStar = new AStar(map, rows, cols, door, doorToConnect, new Wall());
                while (!aStar.isStuck() && !aStar.getCurrentNode().isEndNode()) {
                    aStar.setCurrentNode(aStar.computeNext(aStar.getCurrentNode()));
                }
                doors.add(door);
                doors.add(doorToConnect);
                for (AStarNode node : aStar.getBestPathList()) {
                    if (!WorldManager.tryToAcessTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), node.getPosition().getX(), node.getPosition().getY())) continue;
                    if (tracker.get(node.getPosition().getX() + "," + node.getPosition().getY()) != null) {
                        int prev = tracker.get(node.getPosition().getX() + "," + node.getPosition().getY());
                        tracker.put(node.getPosition().getX() + "," + node.getPosition().getY(), ++prev);
                    } else tracker.put(node.getPosition().getX() + "," + node.getPosition().getY(), 1);
                    if (tracker.get(node.getPosition().getX() + "," + node.getPosition().getY()) > 1) continue;
                    if (node.isStartNode() || node.isEndNode()) continue;
                    if (WorldManager.tryGetTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), node.getPosition().getX(), node.getPosition().getY()).getType() == TileMapping.DOOR.getTile()) continue;
                    if (WorldManager.tryGetTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), node.getPosition().getX(), node.getPosition().getY()).getType() == TileMapping.FREE.getTile()) continue;
                    tilesToUpdate.add(WorldManager.tryGetTile(this.getXGlobal(), this.getYGlobal(), this.getZGlobal(), node.getPosition().getX(), node.getPosition().getY()));
                }
            }
            i++;
        }
        return tilesToUpdate;
    }

}
