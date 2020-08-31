package examples.dungeon;

import engine.Bdv;
import engine.api.BdvScriptGL;
import engine.api.EntityAPI;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;
import engine.texture.SpriteSheet;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.system.TileMapping;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BdvScriptGL {

    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/res/basic").getAbsolutePath();
    SpriteSheet wall = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 5, 3);
    SpriteSheet dirt = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 3);

    private static final int ROWS = 100;
    private static final int COLS = 100;
    private final Dimension tileSize;
    private final Random random = new Random();

    public Game() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.FPS = 60;
        this.tileSize = new Dimension(this.resolution.width / ROWS, this.resolution.height / COLS);

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

        WorldManager.newInstance(ROWS, COLS);
        WorldManager.addNewDungeon(1, numberOfRooms, roomMaxWidth, roomMaxHeight, roomMinWidth, roomMinHeight);
        render();
    }

    @Override
    public void update() {

    }

    public void render() {
        int[][] world = WorldManager.getWorld();
        int xIterator = 0;
        for (int i = -WorldManager.getROWS() / 2; i < WorldManager.getROWS() / 2; i++) {
            int yIterator = 0;
            for (int j = -WorldManager.getCOLS() / 2; j < WorldManager.getCOLS() / 2; j++) {
                EntityAPI entityAPI = null;
                if (world[yIterator][xIterator] == TileMapping.FREE.getTile()) {
                    entityAPI = new EntityAPI(null,
                            new Vector3f(
                                    (float) tileSize.width * i,
                                    (float) tileSize.height * j, 0),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(dirt);
                }
                else if (world[yIterator][xIterator] == TileMapping.WALL.getTile()) {
                    entityAPI = new EntityAPI(null,
                            new Vector3f(
                                    (float) tileSize.width * i,
                                    (float) tileSize.height * j, 0),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(wall);
                }
                if (entityAPI == null) continue;
                this.entities.add(entityAPI);
                yIterator++;
            }
            xIterator++;
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
