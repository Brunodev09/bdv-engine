package app.Core.Interfaces;
import app.Math.Dimension;
import app.Math.Point;
import app.Math.RGBA;
import app.Math.Vector2D;
import app.Video.Texture;


public class Entity {
    public static int _c = 0;
    private int id;
    private Model mdl;
    private Point<Integer> position;
    private Point<Integer> previousPosition;
    private Point<Integer> initialPosition;
    private Point<Float> speed;
    private Dimension dimension;
    private RGBA color;
    private Point<Number> middle;
    private boolean player;
    private Vector2D vector;
    private Entity referenced;
    private boolean following;
    private boolean lockMovement;
    private String message;
    private Texture texture;

    public Entity() {
        this.id = ++_c;
    }

    public Entity(Point<Integer> position, Point<Float> speed, Dimension dimension, RGBA color) {
        this.id = ++_c;
        this.mdl = Model.RECTANGLE;
        this.position = position;
        this.initialPosition = new Point<>(this.position.x, this.position.y);
        this.speed = speed;
        this.dimension = dimension;
        this.color = color;
    }

    public Entity(Point<Integer> position, Point<Float> speed, Dimension dimension, RGBA color, Model primitive) {
        this.id = ++_c;
        this.mdl = primitive;
        this.position = position;
        this.initialPosition = new Point<>(this.position.x, this.position.y);
        this.speed = speed;
        this.dimension = dimension;
        this.color = color;
    }

    public Entity(Point<Integer> position, Point<Float> speed, Dimension dimension, Texture texture) {
        this.id = ++_c;
        this.mdl = Model.TEXTURE;
        this.position = position;
        this.initialPosition = new Point<>(this.position.x, this.position.y);
        this.speed = speed;
        this.dimension = dimension;
        this.texture = texture;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Point<Integer> getPreviousPosition() {
        return previousPosition;
    }

    public Point<Integer> getInitialPosition() {
        return initialPosition;
    }

    public boolean getFollowing() {
        return following;
    }

    public boolean getLockMovement() {
        return lockMovement;
    }

    public boolean getPlayer() {
        return player;
    }

    public Vector2D getVector() {
        return vector;
    }

    public Entity getReferenced() {
        return referenced;
    }

    public int getId() {
        return id;
    }

    public Model getMdl() {
        return mdl;
    }

    public Point<Number> getMiddle() {
        return middle;
    }

    public Point<Integer> getPosition() {
        return position;
    }

    public Point<Float> getSpeed() {
        return speed;
    }

    public RGBA getColor() {
        return color;
    }

    public String getMessage() {
        return message;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setColor(RGBA color) {
        this.color = color;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLockMovement(boolean lockMovement) {
        this.lockMovement = lockMovement;
    }

    public void setMdl(Model mdl) {
        this.mdl = mdl;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMiddle(Point<Number> middle) {
        this.middle = middle;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public void setPosition(Point<Integer> position) {
        this.previousPosition = new Point<>(this.position.x, this.position.y);
        this.position = position;
    }

    public void setReferenced(Entity referenced) {
        this.referenced = referenced;
    }

    public void setSpeed(Point<Float> speed) {
        this.speed = speed;
    }

    public void setSpeedX(float sx) {
        this.speed.x = sx;
    }

    public void setSpeedY(float sy) {
        this.speed.y = sy;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setVector(Vector2D vector) {
        this.vector = vector;
    }
}
