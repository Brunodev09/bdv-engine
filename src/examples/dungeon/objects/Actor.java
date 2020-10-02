package examples.dungeon.objects;

import engine.api.EntityAPI;
import engine.texture.SpriteSheet;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.mob.FactionTypes;
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
    protected int fov = 20;
    protected boolean mob = false;

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic2").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 0, new Rectangle(783, 393));
    protected EntityAPI entityObject;
    protected FactionTypes faction = FactionTypes.OBJECT;

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
        if (tile.getActor() != null) {
            tile.setActor(this);
        }
        this.currentTile = tile;
    }

    public void setPreviousTile(Tile previousTile) {
        if (previousTile.getActor() != null && previousTile.getActor().getType().equals(getType())) {
            previousTile.setActor(null);
        }

        this.previousTile = previousTile;
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

    public int getFov() {
        return fov;
    }

    public void setFov(int fov) {
        this.fov = fov;
    }

    public List<List<Tile>> applyFOV(List<List<Tile>> chunk) {
        return rayCastingFOV(chunk);
    }

    public List<List<Tile>> rayCastingFOV(List<List<Tile>> chunk) {
        int rayOriginX = this.getCurrentTile().getPositionX();
        int rayOriginY = this.getCurrentTile().getPositionY();
        int xG = this.getCurrentLocation().getXGlobal();
        int yG = this.getCurrentLocation().getYGlobal();
        int zG = this.getCurrentLocation().getZGlobal();

        boolean cast2pi = true;
        boolean castPi2 = true;
        boolean castPi = true;
        boolean cast32Pi = true;
        boolean castDiagonal1 = true;
        boolean castDiagonal2 = true;
        boolean castDiagonal3 = true;
        boolean castDiagonal4 = true;
        boolean castDiagonal5 = true;
        boolean castDiagonal6 = true;
        boolean castDiagonal7 = true;
        boolean castDiagonal8 = true;
        boolean castDiagonal9 = true;
        boolean castDiagonal10 = true;
        boolean castDiagonal11 = true;
        boolean castDiagonal12 = true;

        for (int x = 1; x < fov - 1; x++) {
            for (int y = 1; y < fov - 1; y++) {
                Tile target1 = null;
                Tile target2 = null;
                Tile target3 = null;
                Tile target4 = null;
                Tile target5 = null;
                Tile target6 = null;
                Tile target7 = null;
                Tile target8 = null;
                Tile target9 = null;
                Tile target10 = null;
                Tile target11 = null;
                Tile target12 = null;
                Tile target13 = null;
                Tile target14 = null;
                Tile target15 = null;
                Tile target16 = null;

                // 0 degrees // 2pi
                if (cast2pi)
                    target1 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY);
                // pi / 2
                if (castPi2)
                    target2 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX, rayOriginY - y);
                // pi
                if (castPi)
                    target3 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY);
                // (3/2) * pi
                if (cast32Pi)
                    target4 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX, rayOriginY + y);
                // diagonals
                if (castDiagonal1)
                    target5 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY + x);
                if (castDiagonal2)
                    target6 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY + x);
                if (castDiagonal3)
                    target7 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY - x);
                if (castDiagonal4)
                    target8 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY - x);

                if (castDiagonal5)
                    target9 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY + y);
                if (castDiagonal6)
                    target10 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY + y);
                if (castDiagonal7)
                    target11 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY - y);
                if (castDiagonal8)
                    target12 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY - y);

                if (castDiagonal9)
                    target13 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + y, rayOriginY + x);
                if (castDiagonal10)
                    target14 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - y, rayOriginY + x);
                if (castDiagonal11)
                    target15 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - y, rayOriginY - x);
                if (castDiagonal12)
                    target16 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + y, rayOriginY - x);

                if (target1 != null && target1.isSolid()) {
                    cast2pi = false;
                    target1.setHidden(false);
                } else if (target1 != null && !target1.isSolid()) {
                    target1.setHidden(false);
                }
                if (target2 != null && target2.isSolid()) {
                    castPi2 = false;
                    target2.setHidden(false);
                } else if (target2 != null && !target2.isSolid()) {
                    target2.setHidden(false);
                }
                if (target3 != null && target3.isSolid()) {
                    castPi = false;
                    target3.setHidden(false);
                } else if (target3 != null && !target3.isSolid()) {
                    target3.setHidden(false);
                }
                if (target4 != null && target4.isSolid()) {
                    cast32Pi = false;
                    target4.setHidden(false);
                } else if (target4 != null && !target4.isSolid()) {
                    target4.setHidden(false);
                }
                if (target5 != null && target5.isSolid()) {
                    castDiagonal1 = false;
                    target5.setHidden(false);
                } else if (target5 != null && !target5.isSolid()) {
                    target5.setHidden(false);
                }
                if (target6 != null && target6.isSolid()) {
                    castDiagonal2 = false;
                    target6.setHidden(false);
                } else if (target6 != null && !target6.isSolid()) {
                    target6.setHidden(false);
                }
                if (target7 != null && target7.isSolid()) {
                    castDiagonal3 = false;
                    target7.setHidden(false);
                } else if (target7 != null && !target7.isSolid()) {
                    target7.setHidden(false);
                }
                if (target8 != null && target8.isSolid()) {
                    castDiagonal4 = false;
                    target8.setHidden(false);
                } else if (target8 != null && !target8.isSolid()) {
                    target8.setHidden(false);
                }
                if (target9 != null && target9.isSolid()) {
                    castDiagonal5 = false;
                    target9.setHidden(false);
                } else if (target9 != null && !target9.isSolid()) {
                    target9.setHidden(false);
                }
                if (target10 != null && target10.isSolid()) {
                    castDiagonal6 = false;
                    target10.setHidden(false);
                } else if (target10 != null && !target10.isSolid()) {
                    target10.setHidden(false);
                }
                if (target11 != null && target11.isSolid()) {
                    castDiagonal7 = false;
                    target11.setHidden(false);
                } else if (target11 != null && !target11.isSolid()) {
                    target11.setHidden(false);
                }
                if (target12 != null && target12.isSolid()) {
                    castDiagonal8 = false;
                    target12.setHidden(false);
                } else if (target12 != null && !target12.isSolid()) {
                    target12.setHidden(false);
                }
                if (target13 != null && target13.isSolid()) {
                    castDiagonal9 = false;
                    target13.setHidden(false);
                } else if (target13 != null && !target13.isSolid()) {
                    target13.setHidden(false);
                }
                if (target14 != null && target14.isSolid()) {
                    castDiagonal10 = false;
                    target14.setHidden(false);
                } else if (target14 != null && !target14.isSolid()) {
                    target14.setHidden(false);
                }
                if (target15 != null && target15.isSolid()) {
                    castDiagonal11 = false;
                    target15.setHidden(false);
                } else if (target15 != null && !target15.isSolid()) {
                    target15.setHidden(false);
                }
                if (target16 != null && target16.isSolid()) {
                    castDiagonal12 = false;
                    target16.setHidden(false);
                } else if (target16 != null && !target16.isSolid()) {
                    target16.setHidden(false);
                }
            }
        }
        return chunk;
    }

    public boolean isMob() {
        return mob;
    }

    public FactionTypes getFaction() {
        return faction;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
