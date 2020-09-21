package examples.dungeon.player;

import engine.api.InputAPI;
import engine.texture.SpriteSheet;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.objects.Actor;
import examples.dungeon.tiles.Tile;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class Player extends Actor {

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic2").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 1, 1);

    public Player(Tile tile) {
        super(tile);
        tile.setActor(this);
    }

    public Player(Location location, Tile tile, int width, int height) {
        super(location, tile, width, height, "player");
        setType("player");
    }

    @Override
    public boolean action() {
        return move();
    }

    @Override
    public boolean move() {
        boolean triggerRender = false;
        Location location = this.getCurrentLocation();
        List<String> keysPressed = InputAPI.listenForKeyboard();

        label:
        for (String keyPressed : keysPressed) {
            switch (keyPressed) {
                case "W":
                    // checking if tile is accessible
                    if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                            this.getCurrentTile().getPositionX(), this.getCurrentTile().getPositionY() - 1))
                        break label;
                    movePlayer(Objects.requireNonNull(WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                            this.getCurrentTile().getPositionX(), this.getCurrentTile().getPositionY() - 1)));
                    triggerRender = true;
                    break;
                case "S":
                    // checking if tile is accessible
                    if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                            this.getCurrentTile().getPositionX(), this.getCurrentTile().getPositionY() + 1))
                        break label;
                    movePlayer(Objects.requireNonNull(WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                            this.getCurrentTile().getPositionX(), this.getCurrentTile().getPositionY() + 1)));
                    triggerRender = true;
                    break;
                case "D":
                    // checking if tile is accessible
                    if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                            this.getCurrentTile().getPositionX() + 1, this.getCurrentTile().getPositionY()))
                        break label;
                    movePlayer(Objects.requireNonNull(WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                            this.getCurrentTile().getPositionX() + 1, this.getCurrentTile().getPositionY())));
                    triggerRender = true;
                    break;
                case "A":
                    // checking if tile is accessible
                    if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                            this.getCurrentTile().getPositionX() - 1, this.getCurrentTile().getPositionY()))
                        break label;
                    movePlayer(Objects.requireNonNull(WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                            this.getCurrentTile().getPositionX() - 1, this.getCurrentTile().getPositionY())));
                    triggerRender = true;
                    break;
            }
        }
        return triggerRender;
    }

    private void movePlayer(Tile newTile) {
        Tile currentPlayerTile = this.getCurrentTile();
        if (!newTile.isSolid()) {
            this.setPreviousTile(currentPlayerTile);
            this.setCurrentTile(newTile);
            this.setNextTile(null);
        }
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
