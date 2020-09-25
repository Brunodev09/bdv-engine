package examples.dungeon.objects;

import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.tiles.Tile;

import java.util.List;
import java.util.Objects;

public class Camera extends Actor {
    public Camera(Location location, Tile tile, int width, int height) {
        super(location, tile, width, height, "camera");
        setType("camera");
    }

    private String lastKeyPressed;
    private String lastKeyReleased;
    private boolean leftClicked;
    private boolean rightClicked;
    private double latestCursorX;
    private double latestCursorY;
    private Tile mouseSelectedTile;
    private java.util.List<java.util.List<Tile>> chunk;
    private int tilesizeX;
    private int tilesizeY;


    @Override
    public boolean mouse() {
        if (latestCursorX > 0 && latestCursorY > 0) {
            int xCoordToChunk = (int) latestCursorX / tilesizeX;
            int yCoordToChunk = (int) latestCursorY / tilesizeY;

            if (mouseSelectedTile != null) {
                mouseSelectedTile.setSelected(false);
            }

            try {
                mouseSelectedTile = chunk.get(xCoordToChunk).get(yCoordToChunk);
            } catch (Exception e) {
                // @TODO - Treat me!
            }


            if (mouseSelectedTile != null)
                mouseSelectedTile.setSelected(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean action() {
        return updateInput();
    }

    @Override
    public boolean move() {
        boolean triggerRender = false;
        Location location = this.getCurrentLocation();

        if (lastKeyReleased == null) return false;

        switch (lastKeyReleased) {
            case "W":
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        this.getCurrentTile().getPositionX(), this.getCurrentTile().getPositionY() - 1))
                    break;
                moveCamera(Objects.requireNonNull(WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        this.getCurrentTile().getPositionX(), this.getCurrentTile().getPositionY() - 1)));
                triggerRender = true;
                break;
            case "S":
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        this.getCurrentTile().getPositionX(), this.getCurrentTile().getPositionY() + 1))
                    break;
                moveCamera(Objects.requireNonNull(WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        this.getCurrentTile().getPositionX(), this.getCurrentTile().getPositionY() + 1)));
                triggerRender = true;
                break;
            case "D":
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        this.getCurrentTile().getPositionX() + 1, this.getCurrentTile().getPositionY()))
                    break;
                moveCamera(Objects.requireNonNull(WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        this.getCurrentTile().getPositionX() + 1, this.getCurrentTile().getPositionY())));
                triggerRender = true;
                break;
            case "A":
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        this.getCurrentTile().getPositionX() - 1, this.getCurrentTile().getPositionY()))
                    break;
                moveCamera(Objects.requireNonNull(WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        this.getCurrentTile().getPositionX() - 1, this.getCurrentTile().getPositionY())));
                triggerRender = true;
                break;
        }

        return triggerRender;
    }

    private boolean updateInput() {
        boolean result = move();
        this.lastKeyReleased = null;
        return result;
    }


    private void moveCamera(Tile newTile) {
        if (newTile.getActor() != null) return;
        Tile currentCameraTile = this.getCurrentTile();
        this.setPreviousTile(currentCameraTile);
        this.setCurrentTile(newTile);
        this.setNextTile(null);
    }

    public void setTileSizeForMouseCursor(int tilesizeX, int tilesizeY) {
        this.tilesizeX = tilesizeX;
        this.tilesizeY = tilesizeY;
    }

    public String getLastKeyPressed() {
        return lastKeyPressed;
    }

    public String getLastKeyReleased() {
        return lastKeyReleased;
    }

    public void setLastKeyPressed(String lastKeyPressed) {
        this.lastKeyPressed = lastKeyPressed;
    }

    public void setLeftClicked(boolean leftClicked) {
        this.leftClicked = leftClicked;
    }

    public void setRightClicked(boolean rightClicked) {
        this.rightClicked = rightClicked;
    }

    public boolean isLeftClicked() {
        return leftClicked;
    }

    public boolean isRightClicked() {
        return rightClicked;
    }

    public double getLatestCursorX() {
        return latestCursorX;
    }

    public double getLatestCursorY() {
        return latestCursorY;
    }

    public void setLatestCursorX(double latestCursorX) {
        this.latestCursorX = latestCursorX;
    }

    public void setLatestCursorY(double latestCursorY) {
        this.latestCursorY = latestCursorY;
    }

    public void setLastKeyReleased(String lastKeyReleased) {
        this.lastKeyReleased = lastKeyReleased;
    }

    public Tile getMouseSelectedTile() {
        return mouseSelectedTile;
    }

    public java.util.List<java.util.List<Tile>> getChunk() {
        return chunk;
    }

    public void setChunk(List<List<Tile>> chunk) {
        this.chunk = chunk;
    }
}
