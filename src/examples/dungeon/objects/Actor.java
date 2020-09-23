package examples.dungeon.objects;

import engine.api.EntityAPI;
import engine.texture.SpriteSheet;
import examples.dungeon.generation.Location;
import examples.dungeon.tiles.Tile;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Actor implements Cloneable {
    protected Location currentLocation;

    protected Tile previousTile;
    protected Tile currentTile;
    protected Tile nextTile;

    protected int width;
    protected int height;

    protected String type;

    protected int movementCost = 1;

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic2").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 0);
    protected EntityAPI entityObject;

    protected List<Actor> subActors = new ArrayList<>();

    public Actor(Tile tile) {
        this.currentTile = tile;
        tile.setActor(this);
    }

    public Actor(Location location, Tile tile, int width, int height, String type) {
        this.currentTile = tile;
        this.width = width;
        this.height = height;
        this.currentLocation = location;

        if (!type.equals("light"))
            tile.setActor(this);
    }

    public boolean action() {
        return true;
    }


    public static String getSpritesheetFilePath() {
        return SPRITESHEET_FILE_PATH;
    }

    public void setSprite(SpriteSheet sprite) {
        this.sprite = sprite;
    }

    public void setEntityObject(EntityAPI entityObject) {
        this.entityObject = entityObject;
    }

    public void setCurrentTile(Tile tile) {
        this.currentTile = tile;
        tile.setActor(this);
    }

    public Tile getTile() {
        return currentTile;
    }

    public EntityAPI getEntityObject() {
        return entityObject;
    }

    public SpriteSheet getSprite() {
        return sprite;
    }

    public Tile getPreviousTile() {
        return previousTile;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public Tile getNextTile() {
        return nextTile;
    }

    public void setPreviousTile(Tile previousTile) {
        previousTile.setActor(null);
        this.previousTile = previousTile;
    }

    public void setNextTile(Tile nextTile) {
        this.nextTile = nextTile;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMovementCost() {
        return movementCost;
    }

    public void setMovementCost(int movementCost) {
        this.movementCost = movementCost;
    }

    public boolean move() {
        return false;
    }

    public List<Actor> getSubActors() {
        return subActors;
    }

    public void setSubActors(List<Actor> subActors) {
        this.subActors = subActors;
    }

    public void addSubActor(Actor subActor) {
        this.subActors.add(subActor);
    }

    public boolean mouse() {
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
