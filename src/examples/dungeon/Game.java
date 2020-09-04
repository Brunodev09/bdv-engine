package examples.dungeon;

import engine.Bdv;
import engine.api.BdvScriptGL;
import engine.api.EntityAPI;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.player.Player;
import examples.dungeon.system.Input;
import examples.dungeon.system.TileMapping;
import examples.dungeon.tiles.Tile;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends BdvScriptGL {

    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    private final Dimension tileSize;
    private final Random random = new Random();
    Dimension cameraDimensions = new Dimension(20, 20);
    Player player;

    public Game() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.FPS = 60;
        this.tileSize = new Dimension(this.resolution.width / cameraDimensions.width,
                this.resolution.height / cameraDimensions.height);
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
        WorldManager.trySetTile(0, 0, -1,
                WorldManager.getLocationAtIndex(0, 0, -1).getMapWidth() / 2,
                WorldManager.getLocationAtIndex(0, 0, -1).getMapHeight() / 2,
                player.getPlayerTile());
        WorldManager.generateDungeonLocationLayout(0, 0, -1, 0,
                numberOfRooms,
                roomMaxWidth, roomMaxHeight,
                roomMinWidth, roomMinHeight);
        initRender(WorldManager.getLocationAtIndex(0, 0, -1).getMap());
        render();
    }

    @Override
    public void update() {
        if (Input.movePlayerOnMap(player)) {
            this.render();
        }
    }

    public void render() {
        renderChunkFromMap(getPlayerChunk(player), player.getCurrentLocation().getMap());
    }


    public void renderChunkFromMap(List<List<Tile>> chunkToRender, List<List<Tile>> map) {
        int x = 0;
        for (int i = -chunkToRender.size() / 2; i < chunkToRender.size() / 2; i++) {
            int y = 0;
            for (int j = -chunkToRender.size() / 2; j < chunkToRender.get(x).size() / 2; j++) {
                if (chunkToRender.get(x).get(y).getType() == TileMapping.PLAYER.getTile()) {
                    renderPlayer(player, player.getPlayerTile().getPositionX(), player.getPlayerTile().getPositionY(), i, j);
                } else {
                    Tile tile = chunkToRender.get(x).get(y);
                    renderTile(map, tile, tile.getPositionX(), tile.getPositionY(), i, j);
                }
                y++;
            }
            x++;
        }
    }

    public List<List<Tile>> getPlayerChunk(Player player) {
        Location location = player.getCurrentLocation();
        Tile playerTile = player.getPlayerTile();
        List<List<Tile>> tilesToRender = new ArrayList<>();

        for (int i = 0; i < location.getMapWidth(); i++) {
            List<Tile> chunk = new ArrayList<>();
            for (int j = 0; j < location.getMapHeight(); j++) {
                if (i >= playerTile.getPositionX() - cameraDimensions.width && i <= playerTile.getPositionX() + cameraDimensions.width) {
                    if (j >= playerTile.getPositionY() - cameraDimensions.height && j <= playerTile.getPositionY() + cameraDimensions.height) {
                        Tile tileToAdd = WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(), i, j);
                        if (tileToAdd != null) {
                            tileToAdd.getEntityObject().setShouldRender(true);
                            chunk.add(tileToAdd);
                        }
                    }
                } else {
                    Tile tileToAdd = WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(), i, j);
                    if (tileToAdd != null) tileToAdd.getEntityObject().setShouldRender(false);
                }
            }
            if (chunk.isEmpty()) continue;
            tilesToRender.add(chunk);
        }

        return tilesToRender;
    }

    // @TODO - Theres a bug here
    public void renderPlayer(Player player, int x, int y, int xGL, int yGL) {
        List<List<Tile>> map = player.getCurrentLocation().getMap();
        EntityAPI playerEntity = null;

        playerEntity = map.get(x).get(y).getEntityObject();
        playerEntity.translate(new Vector3f(
                (float) tileSize.width * xGL,
                (float) tileSize.height * yGL, 1));

//                    playerEntity.setPlayer(true);
        playerEntity.setShouldRender(true);
//        renderTile(map, player.getPreviousTile(), player.getPreviousTile().getPositionX(), player.getPreviousTile().getPositionY(), 0, 0);
    }


    public void renderTile(List<List<Tile>> map, Tile tile, int x, int y, int xGL, int yGL) {
        EntityAPI entityAPI = map.get(x).get(y).getEntityObject();
        entityAPI.setShouldRender(true);
        entityAPI.translate(new Vector3f(
                (float) tileSize.width * xGL,
                (float) tileSize.height * yGL, 0));
        entityAPI.setSpriteSheet(tile.getSprite());
    }

    public void initRender(List<List<Tile>> map) {
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                if (map.get(x).get(y).getType() == TileMapping.PLAYER.getTile()) {
                    EntityAPI playerEntity = new EntityAPI(null, new Vector3f(
                            0, 0, 1),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    map.get(x).get(y).setEntityObject(playerEntity);
                    player.setPlayerEntity(playerEntity);
                    playerEntity.setSpriteSheet(player.getPlayerTile().getSprite());
                    playerEntity.setShouldRender(false);

                    EntityAPI entityAPI = new EntityAPI(null,
                            new Vector3f(0, 0, 0),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    player.getPreviousTile().setEntityObject(entityAPI);
                    player.getPreviousTile().getEntityObject().setSpriteSheet(player.getPlayerTile().getSprite());
                    player.getPreviousTile().getEntityObject().setShouldRender(false);

                    this.entities.add(entityAPI);
                    this.entities.add(playerEntity);
                } else {
                    EntityAPI entityAPI = new EntityAPI(null,
                            new Vector3f(0, 0, 0),
                            new Dimension(tileSize.width, tileSize.height),
                            new Vector2f(0, 0));
                    map.get(x).get(y).setEntityObject(entityAPI);
                    entityAPI.setShouldRender(false);
                    entityAPI.setSpriteSheet(map.get(x).get(y).getSprite());
                    this.entities.add(entityAPI);
                }
            }
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
