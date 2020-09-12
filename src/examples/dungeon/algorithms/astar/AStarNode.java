package examples.dungeon.algorithms.astar;

import engine.math.Vector2i;

public class AStarNode {
    private final int id;

    private double fCost;
    private double hCost;
    private double gCost;
    private boolean startNode;
    private boolean endNode;
    private boolean wall;
    private Vector2i position;
    private boolean repeated;
    private AStarNode parent;
    private boolean closed;
    private int entityId;
    public static int global = 0;

    public AStarNode() {
        global++;
        this.id = global;
    }

    public double getfCost() {
        return fCost;
    }

    public double getgCost() {
        return gCost;
    }

    public double gethCost() {
        return hCost;
    }

    public void setfCost(double fCost) {
        this.fCost = fCost;
    }

    public void setgCost(double gCost) {
        this.gCost = gCost;
    }

    public void sethCost(double hCost) {
        this.hCost = hCost;
    }

    public int getId() {
        return id;
    }

    public void setStartNode(boolean startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(boolean endNode) {
        this.endNode = endNode;
    }

    public boolean isEndNode() {
        return endNode;
    }

    public boolean isStartNode() {
        return startNode;
    }

    public Vector2i getPosition() {
        return position;
    }

    public void setPosition(Vector2i position) {
        this.position = position;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public boolean isWall() {
        return wall;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public AStarNode getParent() {
        return parent;
    }

    public void setParent(AStarNode parent) {
        this.parent = parent;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isClosed() {
        return closed;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
}
