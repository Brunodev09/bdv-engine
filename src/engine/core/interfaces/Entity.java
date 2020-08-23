package engine.core.interfaces;
import engine.math.Dimension;
import engine.math.RGBA;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.texture.Texture;


public class Entity {
    public static int _c = 0;
    private int id;
    private Model mdl;
    private Vector3f position;
    private Vector3f previousPosition;
    private Vector3f initialPosition;
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

    public Entity(Vector3f position, Vector2f speed, Dimension dimension, RGBA color) {
        this.id = ++_c;
        this.mdl = Model.RECTANGLE;
        this.position = new Vector3f(position.x, position.y, position.z);
        this.initialPosition = new Vector3f(position.x, position.y, position.z);
        this.speed = speed;
        this.dimension = dimension;
        this.color = color;
    }

    public Entity(Vector3f position, Vector2f speed, Dimension dimension, RGBA color, Model primitive) {
        this.id = ++_c;
        this.mdl = primitive;
        this.position = new Vector3f(position.x, position.y, position.z);
        this.initialPosition = new Vector3f(position.x, position.y, position.z);
        this.speed = speed;
        this.dimension = dimension;
        this.color = color;
    }

    public Entity(Vector3f position, Vector2f speed, Dimension dimension, Texture texture) {
        this.id = ++_c;
        this.mdl = Model.TEXTURE;
        this.position = new Vector3f(position.x, position.y, position.z);
        this.initialPosition = new Vector3f(position.x, position.y, position.z);
        this.speed = speed;
        this.dimension = dimension;
        this.texture = texture;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Vector3f getPreviousPosition() {
        return previousPosition;
    }

    public Vector3f getInitialPosition() {
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

    public Vector3f getPosition() {
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

    public void setPosition(Vector3f position) {
        this.previousPosition = new Vector3f(this.position.x, this.position.y, this.position.z);
        this.position = position;
    }

    public void setReferenced(Entity referenced) {
        this.referenced = referenced;
    }

    public void setSpeed(Vector2f speed) {
        this.speed = speed;
    }

    public void setSpeedX(float sx) {
        this.speed.setX(sx);
    }

    public void setSpeedY(float sy) {
        this.speed.setY(sy);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

}
