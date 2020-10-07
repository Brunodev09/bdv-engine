package examples;

import engine.api.EntityAPI;
import engine.api.BdvScriptGL;
import engine.Bdv;
import engine.entities.Camera2D;
import engine.math.*;
import engine.math.Dimension;
import engine.texture.SpriteSheet;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GL_ASTAR extends BdvScriptGL {

    private static final Logger LOGGER = Logger.getLogger(GL_ASTAR.class.getName());

    private final int rows = 100;
    private final int cols = 100;
    private final boolean allowDiagonal = false;
    private final Dimension tileSize;
    private final Random random = new Random();

    private final List<AStarNode> closedList = new ArrayList<>();
    private final List<AStarNode> openList = new ArrayList<>();
    private final List<AStarNode> bestPathList = new ArrayList<>();
    private final List<AStarNode> allNodesList = new ArrayList<>();

    private final int[][] matrix = new int[rows][cols];

    private final Vector2i start = new Vector2i();
    private final Vector2i end = new Vector2i();

    private boolean stuck = false;

    private AStarNode startNode;
    private AStarNode endNode;
    private AStarNode currentNode;

    String filePath = new File("src/examples/res/spritesheet2").getAbsolutePath();

    final SpriteSheet red = new SpriteSheet(filePath, new Rectangle(256, 256), 0, 0);
    final SpriteSheet green = new SpriteSheet(filePath, new Rectangle(256, 256), 1, 0);
    final SpriteSheet black = new SpriteSheet(filePath, new Rectangle(256, 256), 2, 1);
    final SpriteSheet white = new SpriteSheet(filePath, new Rectangle(256, 256), 0, 1);

    final SpriteSheet darkRed = new SpriteSheet(filePath, new Rectangle(256, 256), 0, 2);
    final SpriteSheet darkGreen = new SpriteSheet(filePath, new Rectangle(256, 256), 1, 2);
    final SpriteSheet lightBlue = new SpriteSheet(filePath, new Rectangle(256, 256), 2, 2);

    public GL_ASTAR() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.FPS = 60;

        start.setX(random.nextInt(this.rows));
        start.setY(random.nextInt(this.cols));

        end.setX(random.nextInt(this.rows));
        end.setY(random.nextInt(this.cols));

        this.tileSize = new Dimension((resolution.width / rows), (resolution.height / cols));
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (i == this.start.getX() && j == this.start.getY()) this.matrix[i][j] = 2;
                else if (i == this.end.getX() && j == this.end.getY()) this.matrix[i][j] = 3;
                else if (random.nextInt(10) < 3) this.matrix[i][j] = 4;
                else this.matrix[i][j] = 1;
            }
        }
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.matrix[i][j] == 1) {
                    AStarNode node = new AStarNode();
                    node.setPosition(new Vector2i(i, j));
                    allNodesList.add(node);
                } else if (this.matrix[i][j] == 2) {
                    AStarNode node = new AStarNode();
                    node.setPosition(new Vector2i(i, j));
                    node.setStartNode(true);
                    node.setgCost(0);

                    this.startNode = node;
                    this.currentNode = node;
                    allNodesList.add(node);
                } else if (this.matrix[i][j] == 3) {
                    AStarNode node = new AStarNode();
                    node.setPosition(new Vector2i(i, j));
                    node.setEndNode(true);

                    this.endNode = node;
                    allNodesList.add(node);
                } else {
                    AStarNode node = new AStarNode();
                    node.setPosition(new Vector2i(i, j));
                    node.setWall(true);
                    allNodesList.add(node);
                }
            }

        }
        this.renderFirst();
    }

    @Override
    public void update() {
        this.camera2d.move();
        if (!this.stuck && !this.currentNode.isEndNode()) this.currentNode = this.computeNext(this.currentNode);
    }

    public void renderFirst() {
        int ptrI = 0;
        int ptrJ = 0;
        for (int i = -rows / 2; i < rows / 2; i++) {
            ptrJ = 0;
            for (int j = -cols / 2; j < cols / 2; j++) {
                if (matrix[ptrI][ptrJ] == 1) {
                    EntityAPI entityAPI = new EntityAPI(white, new Vector3f(tileSize.width * i, tileSize.height * j, 0),  new Dimension(tileSize.width, tileSize.height), new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(white);
                    entities.add(entityAPI);

                    AStarNode node = findNode(new Vector2i(ptrI, ptrJ));
                    node.setEntityId(entityAPI.getId());
                } else if (matrix[ptrI][ptrJ] == 2) {
                    EntityAPI entityAPI = new EntityAPI(green, new Vector3f(tileSize.width * i, tileSize.height * j, 0),  new Dimension(tileSize.width, tileSize.height), new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(green);
                    entities.add(entityAPI);

                    AStarNode node = findNode(new Vector2i(ptrI, ptrJ));
                    node.setEntityId(entityAPI.getId());
                } else if (matrix[ptrI][ptrJ] == 3) {
                    EntityAPI entityAPI = new EntityAPI(red, new Vector3f(tileSize.width * i, tileSize.height * j, 0),  new Dimension(tileSize.width, tileSize.height), new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(red);
                    entities.add(entityAPI);

                    AStarNode node = findNode(new Vector2i(ptrI, ptrJ));
                    node.setEntityId(entityAPI.getId());
                } else {
                    EntityAPI entityAPI = new EntityAPI(black, new Vector3f(tileSize.width * i, tileSize.height * j, 0),  new Dimension(tileSize.width, tileSize.height), new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(black);
                    entities.add(entityAPI);

                    AStarNode node = findNode(new Vector2i(ptrI, ptrJ));
                    node.setEntityId(entityAPI.getId());
                }
                ptrJ++;
            }
            ptrI++;
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

    public EntityAPI findEntity(int id) {
        for (EntityAPI entityAPI : this.entities) {
            if (entityAPI.getId() == id) return entityAPI;
        }
        return null;
    }

    public void lookForBestPathAfterEndNodeFound() {
        AStarNode next = this.currentNode;
        if (!this.currentNode.isStartNode()) findEntity(this.currentNode.getEntityId()).setSpriteSheet(lightBlue);
        this.bestPathList.add(next);
        while (next != null) {
            if (next.isStartNode()) break;
            next = next.getParent();
            if (!next.isStartNode()) findEntity(next.getEntityId()).setSpriteSheet(lightBlue);
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
        if (!node.isWall() && !node.isStartNode() && !node.isEndNode()) {
            findEntity(node.getEntityId()).setSpriteSheet(darkGreen);
        }

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
            findEntity(node.getEntityId()).setSpriteSheet(darkRed);
            node.setClosed(true);
            this.closedList.add(node);
        }
    }

    public AStarNode computeNext(AStarNode node) {
        AStarNode nextNode = null;
        Vector2i position = node.getPosition();

        LOGGER.log(Level.INFO, position.getX() + " " + position.getY());

        if (position.getX() != rows - 1 && this.matrix[position.getX() + 1] != null && this.matrix[position.getX() + 1][position.getY()] != 4) {
            this.addToOpenList(findNode(new Vector2i(position.getX() + 1, position.getY())));
        }
        if (position.getX() != 0 && this.matrix[position.getX() - 1] != null && this.matrix[position.getX() - 1][position.getY()] != 4) {
            this.addToOpenList(findNode(new Vector2i(position.getX() - 1, position.getY())));
        }
        if (position.getY() != cols - 1 && this.matrix[position.getX()] != null && this.matrix[position.getX()][position.getY() + 1] != 4) {
            this.addToOpenList(findNode(new Vector2i(position.getX(), position.getY() + 1)));
        }
        if (position.getY() != 0 && this.matrix[position.getX()] != null && this.matrix[position.getX()][position.getY() - 1] != 4) {
            this.addToOpenList(findNode(new Vector2i(position.getX(), position.getY() - 1)));
        }
        if (this.allowDiagonal) {
            if (position.getY() != cols - 1 && position.getX() != 0 && this.matrix[position.getX() - 1] != null && this.matrix[position.getX() - 1][position.getY() + 1] != 4) {
                this.addToOpenList(findNode(new Vector2i(position.getX() - 1, position.getY() + 1)));
            }
            if (position.getX() != rows - 1 && position.getY() != cols - 1 && this.matrix[position.getX() + 1] != null && this.matrix[position.getX() + 1][position.getY() + 1] != 4) {
                this.addToOpenList(findNode(new Vector2i(position.getX() + 1, position.getY() + 1)));
            }
            if (position.getY() != 0 && position.getX() != 0 && this.matrix[position.getX() - 1] != null && this.matrix[position.getX() - 1][position.getY() - 1] != 4) {
                this.addToOpenList(findNode(new Vector2i(position.getX() - 1, position.getY() - 1)));
            }
            if (position.getX() != rows - 1 && position.getY() != 0 && this.matrix[position.getX() + 1] != null && this.matrix[position.getX() + 1][position.getY() - 1] != 4) {
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

    public static void main(String[] args) {
        try {
            new Bdv(GL_ASTAR.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }

}

class AStarNodeComparator implements Comparator<AStarNode> {
    public int compare(AStarNode a, AStarNode b) {
        int weightA = 0;
        int weightB = 0;
        if (a.getfCost() == b.getfCost()) {
            if (a.gethCost() == b.gethCost()) weightA += 500;
            else if (a.gethCost() > b.gethCost()) weightB += 500;
            else weightA += 500;
        } else if (b.getfCost() > a.getfCost()) weightA += 500;
        else if (b.getfCost() < a.getfCost()) weightB += 500;
        return weightB - weightA;
    }
}

class AStarNode {
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
