package examples.dungeon.algorithms.astar;

import engine.math.Geometry;
import engine.math.Vector2i;
import examples.dungeon.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @TODO - Make obstacles a list
public class AStar {
    private final int rows;
    private final int cols;
    private final Tile tileStart;
    private final Tile tileEnd;

    private final List<AStarNode> closedList = new ArrayList<>();
    private final List<AStarNode> openList = new ArrayList<>();
    private final List<AStarNode> bestPathList = new ArrayList<>();
    private final List<AStarNode> allNodesList = new ArrayList<>();
    private final Vector2i start = new Vector2i();
    private final Vector2i end = new Vector2i();
    private boolean stuck = false;
    private boolean allowDiagonal = false;
    private AStarNode startNode;
    private AStarNode endNode;
    private AStarNode currentNode;
    private List<List<Tile>> matrix;
    private Tile obstacle;

    public AStar(List<List<Tile>> map, int rows, int cols, Tile tileStart, Tile tileEnd, Tile obstacle) {
        this.matrix = map;
        this.obstacle = obstacle;
        this.rows = rows;
        this.cols = cols;
        this.tileStart = tileStart;
        this.tileEnd = tileEnd;
        this.init();
    }

    public void init() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (this.matrix.get(i).get(j).getPositionX() == tileStart.getPositionX() && this.matrix.get(i).get(j).getPositionY() == tileStart.getPositionY()) {
                    AStarNode node = new AStarNode();
                    node.setPosition(new Vector2i(i, j));
                    node.setStartNode(true);
                    node.setgCost(0);

                    this.startNode = node;
                    this.currentNode = node;
                    allNodesList.add(node);
                }
                else if (this.matrix.get(i).get(j).getPositionX() == tileEnd.getPositionX() && this.matrix.get(i).get(j).getPositionY() == tileEnd.getPositionY()) {
                    AStarNode node = new AStarNode();
                    node.setPosition(new Vector2i(i, j));
                    node.setEndNode(true);

                    this.endNode = node;
                    allNodesList.add(node);
                }
                else if (this.matrix.get(i).get(j).getType() == obstacle.getType()) {
                    AStarNode node = new AStarNode();
                    node.setPosition(new Vector2i(i, j));
                    node.setWall(true);
                    allNodesList.add(node);
                }
                else {
                    AStarNode node = new AStarNode();
                    node.setPosition(new Vector2i(i, j));
                    allNodesList.add(node);
                }
            }
        }
    }

    public boolean isInOpenList(Vector2i vec2) {
        for (AStarNode node : this.openList) {
            if (node.getPosition().getX() == vec2.getX() && node.getPosition().getY() == vec2.getY()) {
                node.setRepeated(true);
                return true;
            }
        }
        return false;
    }

    public AStarNode findNode(Vector2i vec2) {
        for (AStarNode node : this.allNodesList) {
            if (node.getPosition().getX() == vec2.getX() && node.getPosition().getY() == vec2.getY()) {
                return node;
            }
        }
        return null;
    }

    public void lookForBestPathAfterEndNodeFound() {
        AStarNode next = this.currentNode;
        this.bestPathList.add(next);
        while (next != null) {
            if (next.isStartNode()) break;
            next = next.getParent();
            this.bestPathList.add(next);
        }
    }

    public Map<String, Double> calculateCosts(AStarNode node, AStarNode neighbour) {
        final Map<String, Double> costReturn = new HashMap<>();

        double g = Geometry.distanceBetweenPoints(node.getPosition().getY(), node.getPosition().getY(),
                neighbour.getPosition().getX(), neighbour.getPosition().getY()) +
                Geometry.distanceBetweenPoints(neighbour.getPosition().getX(), neighbour.getPosition().getY(),
                        this.startNode.getPosition().getX(), this.startNode.getPosition().getY());
        double h = Geometry.distanceBetweenPoints(node.getPosition().getX(), node.getPosition().getY(),
                this.endNode.getPosition().getX(), this.endNode.getPosition().getY());
        double f = g + h;

        costReturn.put("G", g);
        costReturn.put("H", h);
        costReturn.put("F", f);

        return costReturn;
    }

    public void applyCostsToNode(AStarNode node) {
        Map<String, Double> costs = calculateCosts(node, this.currentNode);

        if (node.isClosed()) return;

        if (!node.isStartNode()) {
            if (node.isRepeated() && costs.get("G") >= node.getfCost()) return;
            node.setParent(this.currentNode);
            node.setfCost(costs.get("F"));
            node.sethCost(costs.get("H"));
            node.setgCost(costs.get("G"));

            if (node.isEndNode()) {
                this.lookForBestPathAfterEndNodeFound();
            }
        }
    }

    public void addToOpenList(AStarNode node) {
        boolean isRepeated = this.isInOpenList(node.getPosition());
        this.applyCostsToNode(node);
        if (!isRepeated && !node.isClosed()) {
            this.openList.add(node);
        }
    }

    public void addToClosedList(AStarNode node) {
        if (!node.isStartNode() && !node.isEndNode() && !node.isWall() && !node.isClosed()) {
            node.setClosed(true);
            this.closedList.add(node);
        }
    }

    public AStarNode computeNext(AStarNode node) {
        AStarNode nextNode = null;
        Vector2i position = node.getPosition();

        if (position.getX() != rows - 1 && this.matrix.get(position.getX() + 1) != null && this.matrix.get(position.getX() + 1).get(position.getY()).getType() != obstacle.getType()) {
            this.addToOpenList(findNode(new Vector2i(position.getX() + 1, position.getY())));
        }
        if (position.getX() != 0 && this.matrix.get(position.getX() - 1) != null && this.matrix.get(position.getX() - 1).get(position.getY()).getType() != obstacle.getType()) {
            this.addToOpenList(findNode(new Vector2i(position.getX() - 1, position.getY())));
        }
        if (position.getY() != cols - 1 && this.matrix.get(position.getX()) != null && this.matrix.get(position.getX()).get(position.getY() + 1).getType() != obstacle.getType()) {
            this.addToOpenList(findNode(new Vector2i(position.getX(), position.getY() + 1)));
        }
        if (position.getY() != 0 && this.matrix.get(position.getX()) != null && this.matrix.get(position.getX()).get(position.getY() - 1).getType() != obstacle.getType()) {
            this.addToOpenList(findNode(new Vector2i(position.getX(), position.getY() - 1)));
        }
        if (this.allowDiagonal) {
            if (position.getY() != cols - 1 && position.getX() != 0 && this.matrix.get(position.getX() - 1) != null && this.matrix.get(position.getX() - 1).get(position.getY() + 1).getType() != obstacle.getType()) {
                this.addToOpenList(findNode(new Vector2i(position.getX() - 1, position.getY() + 1)));
            }
            if (position.getX() != rows - 1 && position.getY() != cols - 1 && this.matrix.get(position.getX() + 1) != null && this.matrix.get(position.getX() + 1).get(position.getY() + 1).getType() != obstacle.getType()) {
                this.addToOpenList(findNode(new Vector2i(position.getX() + 1, position.getY() + 1)));
            }
            if (position.getY() != 0 && position.getX() != 0 && this.matrix.get(position.getX() - 1) != null && this.matrix.get(position.getX() - 1).get(position.getY() - 1).getType() != obstacle.getType()) {
                this.addToOpenList(findNode(new Vector2i(position.getX() - 1, position.getY() - 1)));
            }
            if (position.getX() != rows - 1 && position.getY() != 0 && this.matrix.get(position.getX() + 1) != null && this.matrix.get(position.getX() + 1).get(position.getY() - 1).getType() != obstacle.getType()) {
                this.addToOpenList(findNode(new Vector2i(position.getX() + 1, position.getY() - 1)));
            }
        }

        if (this.openList.isEmpty()) {
            this.stuck = true;
            return null;
        }

        this.openList.sort(new AStarNodeComparator());
        this.addToClosedList(this.openList.get(0));
        nextNode = this.openList.get(0);
        this.openList.remove(0);

        return nextNode;
    }

    public AStarNode getCurrentNode() {
        return currentNode;
    }

    public AStarNode getEndNode() {
        return endNode;
    }

    public AStarNode getStartNode() {
        return startNode;
    }

    public List<AStarNode> getAllNodesList() {
        return allNodesList;
    }

    public List<AStarNode> getBestPathList() {
        return bestPathList;
    }

    public List<AStarNode> getClosedList() {
        return closedList;
    }

    public List<AStarNode> getOpenList() {
        return openList;
    }

    public Vector2i getEnd() {
        return end;
    }

    public Vector2i getStart() {
        return start;
    }

    public void setEndNode(AStarNode endNode) {
        this.endNode = endNode;
    }

    public void setStartNode(AStarNode startNode) {
        this.startNode = startNode;
    }

    public void setAllowDiagonal(boolean allowDiagonal) {
        this.allowDiagonal = allowDiagonal;
    }

    public void setCurrentNode(AStarNode currentNode) {
        this.currentNode = currentNode;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public List<List<Tile>> getMap() {
        return matrix;
    }

    public Tile getTileEnd() {
        return tileEnd;
    }

    public Tile getTileStart() {
        return tileStart;
    }

    public List<List<Tile>> getMatrix() {
        return matrix;
    }

    public Tile getObstacle() {
        return obstacle;
    }

    public boolean isAllowDiagonal() {
        return allowDiagonal;
    }

    public boolean isStuck() {
        return stuck;
    }
}
