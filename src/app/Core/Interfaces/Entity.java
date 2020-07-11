package app.Core.Interfaces;
import app.Math.Dimension;
import app.Math.RGBA;
import app.Math.Vector2f;
import app.Video.Texture;


public class Entity {
    public static int _c = 0;
    private int id;
    private Model mdl;
    private Vector2f position;
    private Vector2f previousPosition;
    private Vector2f initialPosition;
    private Vector2f speed;
    private Vector2f middle;
    private Dimension dimension;
    private RGBA color;
    private boolean player;
    private Entity referenced;
    private boolean following;
    private boolean lockMovement;
    private String message;
    private Texture texture;

    public Entity() {
        this.id = ++_c;
    }

    public Entity(Vector2f position, Vector2f speed, Dimension dimension, RGBA color) {
        this.id = ++_c;
        this.mdl = Model.RECTANGLE;
        this.position = position;
        this.initialPosition = new Vector2f(this.position.x, this.position.y);
        this.speed = speed;
        this.dimension = dimension;
        this.color = color;
    }

    public Entity(Vector2f position, Vector2f speed, Dimension dimension, RGBA color, Model primitive) {
        this.id = ++_c;
        this.mdl = primitive;
        this.position = position;
        this.initialPosition = new Vector2f(this.position.x, this.position.y);
        this.speed = speed;
        this.dimension = dimension;
        this.color = color;
    }

    public Entity(Vector2f position, Vector2f speed, Dimension dimension, Texture texture) {
        this.id = ++_c;
        this.mdl = Model.TEXTURE;
        this.position = position;
        this.initialPosition = new Vector2f(this.position.x, this.position.y);
        this.speed = speed;
        this.dimension = dimension;
        this.texture = texture;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Vector2f getPreviousPosition() {
        return previousPosition;
    }

    public Vector2f getInitialPosition() {
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

    public Entity getReferenced() {
        return referenced;
    }

    public int getId() {
        return id;
    }

    public Model getMdl() {
        return mdl;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSpeed() {
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

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public void setPosition(Vector2f position) {
        this.previousPosition = new Vector2f(this.position.x, this.position.y);
        this.position = position;
    }

    public void setReferenced(Entity referenced) {
        this.referenced = referenced;
    }

    public void setSpeed(Vector2f speed) {
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

}
