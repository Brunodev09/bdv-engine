package examples.dungeon.tiles;

import engine.api.EntityAPI;
import examples.dungeon.system.TileMapping;

public abstract class Tile {
    protected int type = TileMapping.FREE.getTile();
    protected int positionX;
    protected int positionY;
    protected boolean solid = false;
    protected EntityAPI entityObject;

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

}
