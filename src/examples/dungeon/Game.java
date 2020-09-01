package examples.dungeon;

import engine.Bdv;
import engine.api.BdvScriptGL;
import engine.api.EntityAPI;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;
import engine.texture.SpriteSheet;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.system.TileMapping;
import examples.dungeon.tiles.Tile;
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
    SpriteSheet stone = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 3, 2);


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

        WorldManager.newDungeonLocation(0, 0, -1, 100, 100);
        WorldManager.generateDungeonLocationLayout(0, 0, -1, 0, numberOfRooms, roomMaxWidth, roomMaxHeight, roomMinWidth, roomMinHeight);
        render();
    }

    @Override
    public void update() {

    }

    public void render() {
        List<List<Tile>> world = WorldManager.getMapFromLocation(0, 0, -1);
        Location location = WorldManager.getLocationAtIndex(0, 0, -1);
        int xIterator = 0;
        for (int i = -location.getMapWidth() / 2; i < location.getMapWidth() / 2; i++) {
            int yIterator = 0;
            for (int j = -location.getMapHeight() / 2; j < location.getMapHeight() / 2; j++) {
                EntityAPI entityAPI = null;
                if (world.get(yIterator).get(xIterator).getType() == TileMapping.FREE.getTile()) {
                    entityAPI = new EntityAPI(null,
                            new Vector3f(
                                    (float) tileSize.width * i,
                                    (float) tileSize.height * j, 0),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(dirt);
                }
                else if (world.get(yIterator).get(xIterator).getType() == TileMapping.WALL.getTile()) {
                    entityAPI = new EntityAPI(null,
                            new Vector3f(
                                    (float) tileSize.width * i,
                                    (float) tileSize.height * j, 0),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(wall);
                }
                else if (world.get(yIterator).get(xIterator).getType() == TileMapping.STONE.getTile()) {
                    entityAPI = new EntityAPI(null,
                            new Vector3f(
                                    (float) tileSize.width * i,
                                    (float) tileSize.height * j, 0),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    entityAPI.setSpriteSheet(stone);
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
