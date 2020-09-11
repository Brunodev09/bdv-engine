package examples.dungeon;

import engine.Bdv;
import engine.api.BdvScriptGL;
import engine.api.EntityAPI;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.objects.InstalledObject;
import examples.dungeon.player.Player;
import examples.dungeon.system.Input;
import examples.dungeon.system.Render;
import examples.dungeon.tiles.Tile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BdvScriptGL {

    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    private final Dimension tileSize;
    private final Random random = new Random();
    Render renderer;
    Dimension cameraDimensions = new Dimension(20, 20);
    InstalledObject player;

    public Game() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.FPS = 60;
        this.tileSize = new Dimension(this.resolution.width / cameraDimensions.width,
                this.resolution.height / cameraDimensions.height);
        this.camera2d.setSpeed(tileSize.width / tileSize.height);
        this.logFps = true;

        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        // formula to calculate how many rooms to generate based on the player level
        final int numberOfRooms = 50;
        final int roomMaxWidth = 11;
        final int roomMaxHeight = 11;
        final int roomMinWidth = 3;
        final int roomMinHeight = 3;

        WorldManager.newDungeonLocation(0, 0, -1, 100, 100);
        Tile playerSpawnTile = WorldManager.getMapFromLocation(0, 0, -1).get(
                WorldManager.getLocationAtIndex(0, 0, -1).getMapWidth() / 2).get(
                WorldManager.getLocationAtIndex(0, 0, -1).getMapHeight() / 2);
        player = new Player(WorldManager.getLocationAtIndex(0, 0, -1), playerSpawnTile, 50, 50);
        player.setType("player");
        renderer = new Render(entities, player, cameraDimensions, tileSize);
        player.setCurrentLocation(WorldManager.getLocationAtIndex(0, 0, -1));
        WorldManager.generateDungeonLocationLayout(0, 0, -1, 0,
                numberOfRooms,
                roomMaxWidth, roomMaxHeight,
                roomMinWidth, roomMinHeight);

        renderer.initRender(WorldManager.getLocationAtIndex(0, 0, -1).getMap());
        renderer.render();
    }

    @Override
    public void update() {
        if (Input.movePlayerOnMap(player)) {
            renderer.render();
        }
    }


    public static void main(String[] args) {
        try {
            new Bdv(Game.class);
        } catch (Exception exception) {
            LOG.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
