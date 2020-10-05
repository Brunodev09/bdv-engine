package examples.dungeon;

import engine.Bdv;
import engine.api.BdvScriptGL;
import engine.api.ChunkManagerAPI;
import engine.api.EntityAPI;
import engine.api.InputAPI;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;
import engine.texture.SpriteSheet;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.input.InputManager;
import examples.dungeon.input.Keyboard;
import examples.dungeon.input.Mouse;
import examples.dungeon.objects.Actor;
import examples.dungeon.objects.Camera;
import examples.dungeon.objects.Knight;
import examples.dungeon.objects.Torch;
import examples.dungeon.objects.Player;
import examples.dungeon.system.Render;
import examples.dungeon.system.Turn;
import examples.dungeon.tiles.GrassTile;
import examples.dungeon.tiles.Tile;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BdvScriptGL {

    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    // @TODO - Have a List of actors in each tile instead of a single one

    private final Dimension tileSize;
    private final Random random = new Random();
    public InputAPI inputAPI;
    Render renderer;
    Dimension cameraDimensions = new Dimension(25, 25);
    Player player;
    Camera camera;
    Turn turn = new Turn();
    boolean firstTime = true;

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/assetsComplete").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 0, new Rectangle(783, 1176));

    public Game() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1440, 900);
        this.background = new RGBAf(0, 0, 0, 255);
        this.FPS = 60;
        this.tileSize = new Dimension(this.resolution.width / cameraDimensions.width,
                this.resolution.height / cameraDimensions.height);
        this.camera2d.setSpeed(tileSize.width / tileSize.height);
        this.logFps = true;

        this.chunkRendering = true;
        this.chunkManagers = new ArrayList<>();
        this.chunkManagers.add(new ChunkManagerAPI(sprite));
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
//        // formula to calculate how many rooms to generate based on the player level
//        final int numberOfRooms = 50;
//        final int roomMaxWidth = 11;
//        final int roomMaxHeight = 11;
//        final int roomMinWidth = 3;
//        final int roomMinHeight = 3;

//        WorldManager.newDungeonLocation(0, 0, -1, 100, 100);
//
//
////        WorldManager.generateDungeonLocationLayoutDFS(0, 0, -1, 0,
////                numberOfRooms,
////                roomMaxWidth, roomMaxHeight,
////                roomMinWidth, roomMinHeight);
//        WorldManager.generateDungeonLocationLayoutRandomWalk(0, 0, -1, 0, 2000, 10);

        WorldManager.newEmptyFieldLocation(0,0, -1, 100, 100, GrassTile.class);

        Tile playerSpawnTile = WorldManager.getMapFromLocation(0, 0, -1).get(
                WorldManager.getLocationAtIndex(0, 0, -1).getMapWidth() / 2).get(
                WorldManager.getLocationAtIndex(0, 0, -1).getMapHeight() / 2);
        camera = new Camera(WorldManager.getLocationAtIndex(0, 0, -1), playerSpawnTile, 50, 50);

        camera.setCurrentLocation(WorldManager.getLocationAtIndex(0, 0, -1));

        Tile torchSpawnTile = WorldManager.getMapFromLocation(0, 0, -1)
                .get((WorldManager.getLocationAtIndex(0, 0, -1).getMapWidth() / 2) + 5)
                .get((WorldManager.getLocationAtIndex(0, 0, -1).getMapHeight() / 2) + 5);
        Actor torch = new Torch(WorldManager.getLocationAtIndex(0, 0, -1), torchSpawnTile, 50, 50);

        Tile knightSpawnTile = WorldManager.getMapFromLocation(0, 0, -1)
                .get((WorldManager.getLocationAtIndex(0, 0, -1).getMapWidth() / 2) + 10)
                .get((WorldManager.getLocationAtIndex(0, 0, -1).getMapHeight() / 2) + 15);
        Knight knight = new Knight(WorldManager.getLocationAtIndex(0, 0, -1), knightSpawnTile, 50, 50);

        turn.addToJobQueue(camera);
        turn.addToJobQueue(torch);
        turn.addToJobQueue(knight);

        // Setting up input and renderer
        // ==============================
        camera.setTileSizeForMouseCursor(this.tileSize.width, this.tileSize.height);
        Keyboard keyboard = new Keyboard(camera);
        Mouse mouse = new Mouse(camera);

        inputAPI = new InputAPI();

        inputAPI.registerKeyCallback(keyboard);
        inputAPI.registerMouseCallback(mouse);

        InputManager.newInstance(keyboard, mouse);

        if (this.chunkRendering)
            renderer = new Render(entities, chunkManagers, turn, camera, cameraDimensions, tileSize);
        else renderer = new Render(entities, turn, camera, cameraDimensions, tileSize);
        // ==============================

        renderer.initRender(WorldManager.getLocationAtIndex(0, 0, -1).getMap());
        renderer.render();
    }

    @Override
    public void update() {
        if (firstTime) {
            firstTime = false;
            inputAPI.setupInputListener();
        }
        if (turn.process()) renderer.render();
    }

    public static void main(String[] args) {
        try {
            new Bdv(Game.class);
        } catch (Exception exception) {
            LOG.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
