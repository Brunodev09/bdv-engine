package examples.dungeon.objects;

import engine.api.EntityAPI;
import engine.texture.SpriteSheet;
import examples.dungeon.generation.Location;
import examples.dungeon.tiles.Tile;

import java.awt.*;
import java.io.File;

public abstract class InstalledObject implements Cloneable {
    protected Location currentLocation;

    protected Tile previousTile;
    protected Tile currentTile;
    protected Tile nextTile;

    protected int width;
    protected int height;

    protected String type;

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic2").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 0);
    protected EntityAPI entityObject;

    public InstalledObject(Tile tile) {
        this.currentTile = tile;
        tile.setInstalledObject(this);
    }

    public InstalledObject(Location location, Tile tile, int width, int height) {
        this.currentTile = tile;
        this.width = width;
        this.height = height;
        this.currentLocation = location;
        tile.setInstalledObject(this);
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
        tile.setInstalledObject(this);
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
        previousTile.setInstalledObject(null);
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
