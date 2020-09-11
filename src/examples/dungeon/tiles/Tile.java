package examples.dungeon.tiles;

import engine.api.EntityAPI;
import engine.texture.SpriteSheet;
import examples.dungeon.objects.InstalledObject;
import examples.dungeon.system.TileMapping;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile implements Cloneable {
    protected int type = TileMapping.FREE.getTile();
    protected int positionX;
    protected int positionY;
    protected boolean solid = false;
    protected EntityAPI entityObject;
    protected Map<Object, Object> scriptProperties = new HashMap<>();
    protected static String spriteSheet = new File("src/examples/dungeon/assets/basic").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(spriteSheet, new Rectangle(39, 39), 3, 2);
    protected InstalledObject installedObject;

    public Tile() {
    }

    public Tile(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    public Tile(int x, int y, boolean solid) {
        this.positionX = x;
        this.positionY = y;
        this.solid = solid;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getType() {
        return type;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPosition(int xPosition, int yPosition) {
        this.positionX = xPosition;
        this.positionY = yPosition;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public void setEntityObject(EntityAPI entityObject) {
        this.entityObject = entityObject;
    }

    public EntityAPI getEntityObject() {
        return entityObject;
    }

    public Map<Object, Object> getScriptProperties() {
        return scriptProperties;
    }

    public SpriteSheet getSprite() {
        return sprite;
    }

    public static String getSpritesheetFilePath() {
        return spriteSheet;
    }

    public InstalledObject getInstalledObject() {
        return installedObject;
    }

    public void setInstalledObject(InstalledObject installedObject) {
        this.installedObject = installedObject;
    }

    public void setSprite(SpriteSheet sprite) {
        this.sprite = sprite;
    }

    public void setScriptProperties(Map<Object, Object> scriptProperties) {
        this.scriptProperties = scriptProperties;
    }

    public static void setSpritesheetFilePath(String spritesheetFilePath) {
        spriteSheet = spritesheetFilePath;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
