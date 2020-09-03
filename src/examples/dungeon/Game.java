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
import examples.dungeon.player.Player;
import examples.dungeon.system.Input;
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

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/basic").getAbsolutePath();
    private static final String SPRITESHEET_FILE_PATH2 = new File("src/examples/dungeon/assets/basic2").getAbsolutePath();

    SpriteSheet wall = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 5, 3);
    SpriteSheet dirt = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 0, 3);
    SpriteSheet stone = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 3, 2);

    SpriteSheet playerSprite = new SpriteSheet(SPRITESHEET_FILE_PATH2, new Rectangle(39, 39), 1, 1);

    private static final int ROWS = 100;
    private static final int COLS = 100;
    private final Dimension tileSize;
    private final Random random = new Random();
    Player player;

    public Game() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.FPS = 60;
        this.tileSize = new Dimension(this.resolution.width / ROWS, this.resolution.height / COLS);
        this.camera2d.setSpeed(tileSize.width / tileSize.height);
        this.logFps = false;

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
        player = new Player(playerSpawnTile.getPositionX(), playerSpawnTile.getPositionY());
        player.setCurrentLocation(WorldManager.getLocationAtIndex(0, 0, -1));
        player.setPreviousTile(playerSpawnTile);
        WorldManager.populateMapWithTile(0, 0, -1,
                WorldManager.getLocationAtIndex(0, 0, -1).getMapWidth() / 2,
                WorldManager.getLocationAtIndex(0, 0, -1).getMapHeight() / 2,
                player.getPlayerTile());
        WorldManager.generateDungeonLocationLayout(0, 0, -1, 0,
                numberOfRooms,
                roomMaxWidth, roomMaxHeight,
                roomMinWidth, roomMinHeight);
        render();
    }

    @Override
    public void update() {
        if (Input.movePlayerOnMap(player, camera2d)) {
            this.render();
        }
    }

    public void render() {
        this.renderEntireMap();
    }


    public void renderChunk(List<List<Tile>> chunkToRender) {

    }

    public void renderChunkFromPlayerCamera(List<List<Tile>> chunkToRender, EntityAPI player) {

    }


    // @TODO - Method to render tile
    public void renderEntireMap() {
        List<List<Tile>> world = WorldManager.getMapFromLocation(0, 0, -1);
        Location location = WorldManager.getLocationAtIndex(0, 0, -1);
        int xIterator = 0;
        for (int i = -location.getMapWidth() / 2; i < location.getMapWidth() / 2; i++) {
            int yIterator = 0;
            for (int j = -location.getMapHeight() / 2; j < location.getMapHeight() / 2; j++) {
                EntityAPI entityAPI = null;
                if (world.get(yIterator).get(xIterator).getType() == TileMapping.PLAYER.getTile()) {

                    EntityAPI playerEntity = null;

                    if (world.get(yIterator).get(xIterator).getEntityObject() == null) {
                        playerEntity = new EntityAPI(null, new Vector3f(
                                0, 0, 1),
                                new Dimension(50, 50),
                                new Vector2f(0, 0));
                        world.get(yIterator).get(xIterator).setEntityObject(playerEntity);
                        player.setPlayerEntity(playerEntity);
                        playerEntity.setSpriteSheet(playerSprite);
                        this.entities.add(playerEntity);
                    } else {
                        playerEntity = world.get(yIterator).get(xIterator).getEntityObject();
                        playerEntity.translate(new Vector3f(
                                (float) tileSize.width * i,
                                (float) tileSize.height * j, 1));
                    }

//                    playerEntity.setPlayer(true);

                    if (player.getPreviousTile().getEntityObject() == null) {
                        entityAPI = new EntityAPI(null,
                                new Vector3f(
                                        (float) tileSize.width * i,
                                        (float) tileSize.height * j, 0),
                                new Dimension(tileSize.width, tileSize.height),
                                new Vector2f(0, 0));
                        player.getPreviousTile().setEntityObject(entityAPI);
                        this.entities.add(entityAPI);
                    } else entityAPI = player.getPreviousTile().getEntityObject();

                    switch (player.getPreviousTile().getType()) {
                        case 0:
                            entityAPI.setSpriteSheet(dirt);
                            break;
                        case 1:
                            entityAPI.setSpriteSheet(stone);
                            break;
                    }
                }
                if (world.get(yIterator).get(xIterator).getType() == TileMapping.FREE.getTile()) {
                    if (world.get(yIterator).get(xIterator).getEntityObject() == null) {
                        entityAPI = new EntityAPI(null,
                                new Vector3f(
                                        (float) tileSize.width * i,
                                        (float) tileSize.height * j, 0),
                                new Dimension(tileSize.width, tileSize.height),
                                new Vector2f(0, 0));
                        world.get(yIterator).get(xIterator).setEntityObject(entityAPI);
                        this.entities.add(entityAPI);
                    } else entityAPI = world.get(yIterator).get(xIterator).getEntityObject();

                    entityAPI.setSpriteSheet(dirt);
                }
                else if (world.get(yIterator).get(xIterator).getType() == TileMapping.WALL.getTile()) {
                    if (world.get(yIterator).get(xIterator).getEntityObject() == null) {
                        entityAPI = new EntityAPI(null,
                                new Vector3f(
                                        (float) tileSize.width * i,
                                        (float) tileSize.height * j, 0),
                                new Dimension(tileSize.width, tileSize.height),
                                new Vector2f(0, 0));
                        world.get(yIterator).get(xIterator).setEntityObject(entityAPI);
                        this.entities.add(entityAPI);
                    } else entityAPI = world.get(yIterator).get(xIterator).getEntityObject();
                    entityAPI.setSpriteSheet(wall);
                }
                else if (world.get(yIterator).get(xIterator).getType() == TileMapping.STONE.getTile()) {
                    if (world.get(yIterator).get(xIterator).getEntityObject() == null) {
                        entityAPI = new EntityAPI(null,
                                new Vector3f(
                                        (float) tileSize.width * i,
                                        (float) tileSize.height * j, 0),
                                new Dimension(tileSize.width, tileSize.height),
                                new Vector2f(0, 0));
                        world.get(yIterator).get(xIterator).setEntityObject(entityAPI);
                        this.entities.add(entityAPI);
                    } else entityAPI = world.get(yIterator).get(xIterator).getEntityObject();
                    entityAPI.setSpriteSheet(stone);
                }
                if (entityAPI == null) continue;
                yIterator++;
            }
            xIterator++;
        }
    }

    public void renderOrUpdateTile(List<List<Tile>> world) {

    }

    public static void main(String[] args) {
        try {
            new Bdv(Game.class);
        } catch (Exception exception) {
            LOG.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
