package examples.dungeon.generation;

public abstract class Location {
    private int xWorldCoordinate;
    private int yWorldCoordinate;
    private int zWorldCoordinate;

    public int getxWorldCoordinate() {
        return xWorldCoordinate;
    }

    public int getyWorldCoordinate() {
        return yWorldCoordinate;
    }

    public int getzWorldCoordinate() {
        return zWorldCoordinate;
    }
}
