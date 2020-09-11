package examples.dungeon.system;

import engine.api.EntityAPI;
import engine.math.Dimension;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.objects.InstalledObject;
import examples.dungeon.player.Player;
import examples.dungeon.tiles.Tile;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Render {

    // @TODO - Optimize rendering

    private static final Logger LOG = Logger.getLogger(Render.class.getName());

    private List<EntityAPI> entities;
    private final InstalledObject player;
    private Dimension cameraDimensions;
    private Dimension tileSize;
    private List<List<Tile>> previousValidChunk = new ArrayList<>();
    private InstalledObject previousPlayerObject;
    private boolean edges;

    public Render(List<EntityAPI> entities, InstalledObject player, Dimension cameraDimensions, Dimension tileSize) {
        this.entities = entities;
        this.player = player;
        this.cameraDimensions = cameraDimensions;
        this.tileSize = tileSize;
    }

    public void setCameraDimensions(Dimension cameraDimensions) {
        this.cameraDimensions = cameraDimensions;
    }

    public void setTileSize(Dimension tileSize) {
        this.tileSize = tileSize;
    }

    public void render() {
        try {
            renderChunkFromMap(getPlayerChunk(player), player.getCurrentLocation().getMap());
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            LOG.severe(cloneNotSupportedException.getMessage());
        }
    }

    public void renderFreeCameraMode() {

    }

    public void renderChunkFromMap(List<List<Tile>> chunkToRender, List<List<Tile>> map) {
        int x = 0;
        for (int i = -chunkToRender.size() / 2; i < chunkToRender.size() / 2; i++) {
            if (x >= chunkToRender.size()) break;
            int y = 0;
            for (int j = -chunkToRender.size() / 2; j < chunkToRender.get(x).size() / 2; j++) {
                if (y >= chunkToRender.get(x).size()) break;
                Tile tile = chunkToRender.get(x).get(y);
                renderTile(map, tile, tile.getPositionX(), tile.getPositionY(), i, j);
                y++;
            }
            x++;
        }
    }

    public List<List<Tile>> getPlayerChunk(InstalledObject player) throws CloneNotSupportedException {
        Location location = player.getCurrentLocation();
        Tile playerTile = player.getCurrentTile();
        List<List<Tile>> tilesToRender = new ArrayList<>();
        int validChunkNumber = (cameraDimensions.width * 2) - 1;

        for (int i = 0; i < location.getMapWidth(); i++) {
            List<Tile> chunk = new ArrayList<>();
            for (int j = 0; j < location.getMapHeight(); j++) {
                if (i > playerTile.getPositionX() - cameraDimensions.width && i < playerTile.getPositionX() + cameraDimensions.width) {
                    if (j > playerTile.getPositionY() - cameraDimensions.height && j < playerTile.getPositionY() + cameraDimensions.height) {
                        Tile tileToAdd = WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(), i, j);
                        if (tileToAdd != null) {
                            tileToAdd.getEntityObject().setShouldRender(true);
                            chunk.add(tileToAdd);
                        }
                    } else {
                        Tile tileToRemove = WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(), i, j);
                        if (tileToRemove != null) tileToRemove.getEntityObject().setShouldRender(false);
                    }
                }
            }
            if (chunk.isEmpty()) continue;
            tilesToRender.add(chunk);
        }

        if (tilesToRender.size() == validChunkNumber) {
            previousPlayerObject = (Player) player.clone();
            if (!previousValidChunk.isEmpty()) {
                previousValidChunk.clear();
            }
            for (int index = 0; index < tilesToRender.size(); index++) {
                List<Tile> tiles = tilesToRender.get(index);
                List<Tile> cpList = new ArrayList<>();
                for (Tile tile : tiles) {
                    cpList.add((Tile) tile.clone());
                }
                previousValidChunk.add(new ArrayList<>(cpList));
            }
        } else if (validChunkNumber >= location.getMapWidth() / 2) {
            return tilesToRender;
        } else return renderEdges(player);

        if (edges) {
            edges = false;
            return previousValidChunk;
        }
        return tilesToRender;
    }

    public List<List<Tile>> renderEdges(InstalledObject player) throws CloneNotSupportedException {
        edges = true;
        Tile playerTile = player.getCurrentTile();

        for (int i = 0; i < previousValidChunk.size(); i++) {
            for (int j = 0; j < previousValidChunk.get(i).size(); j++) {
                if (previousValidChunk.get(i).get(j).getInstalledObject() != null &&
                        previousValidChunk.get(i).get(j).getInstalledObject().getType().equals("player")) {
                    previousValidChunk.get(i).get(j).setInstalledObject(null);
                }
            }
        }
        for (int i = 0; i < previousValidChunk.size(); i++) {
            for (int j = 0; j < previousValidChunk.get(i).size(); j++) {
                if (previousValidChunk.get(i).get(j).getPositionX() == playerTile.getPositionX()) {
                    if (previousValidChunk.get(i).get(j).getPositionY() == playerTile.getPositionY()) {
                        previousValidChunk.get(i).get(j).setInstalledObject(playerTile.getInstalledObject());
                        previousPlayerObject = (Player) player.clone();
                    }
                }
            }
        }
        return previousValidChunk;
    }

    public Map<String, Integer> checkForDuplicateTile(List<List<Tile>> chunk) {
        Map<String, Integer> duplicate = new HashMap<>();
        Map<String, Integer> duplicateFinal = new HashMap<>();
        for (List<Tile> section : chunk) {
            for (Tile tile : section) {
                if (duplicate.get(tile.getPositionX() + "," + tile.getPositionY()) == null) {
                    duplicate.put(tile.getPositionX() + "," + tile.getPositionY(), 1);
                } else {
                    int occ = duplicate.get(tile.getPositionX() + "," + tile.getPositionY());
                    duplicate.put(tile.getPositionX() + "," + tile.getPositionY(), ++occ);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : duplicate.entrySet()) {
            if (entry.getValue() > 1) duplicateFinal.put(entry.getKey(), entry.getValue());
        }
        return duplicateFinal;
    }

    public void renderTile(List<List<Tile>> map, Tile tile, int x, int y, int xGL, int yGL) {
        EntityAPI entityAPI = map.get(x).get(y).getEntityObject();
        entityAPI.setShouldRender(true);
        entityAPI.translate(new Vector3f(
                (float) tileSize.width * xGL,
                (float) tileSize.height * yGL, 0));
        entityAPI.setSpriteSheet(tile.getSprite());
        if (tile.getInstalledObject() != null && tile.getInstalledObject().getEntityObject() != null) {
            EntityAPI object = tile.getInstalledObject().getEntityObject();
            object.translate(new Vector3f(
                    (float) tileSize.width * xGL,
                    (float) tileSize.height * yGL, 1));
            object.setShouldRender(true);
        }
    }

    public void initRender(List<List<Tile>> map) {
        for (int x = 0; x < map.size(); x++) {
            for (int y = 0; y < map.get(x).size(); y++) {
                EntityAPI entityAPI = new EntityAPI(null,
                        new Vector3f(0, 0, 0),
                        new Dimension(tileSize.width, tileSize.height),
                        new Vector2f(0, 0));
                map.get(x).get(y).setEntityObject(entityAPI);
                entityAPI.setShouldRender(false);
                entityAPI.setSpriteSheet(map.get(x).get(y).getSprite());
                this.entities.add(entityAPI);
                if (map.get(x).get(y).getInstalledObject() != null) {
                    InstalledObject object = map.get(x).get(y).getInstalledObject();
                    EntityAPI entityAPIForObject = new EntityAPI(null,
                            new Vector3f(0, 0, 1),
                            new Dimension(object.getWidth(), object.getHeight()),
                            new Vector2f(0, 0));
                    object.setEntityObject(entityAPIForObject);
                    entityAPIForObject.setShouldRender(false);
                    entityAPIForObject.setSpriteSheet(map.get(x).get(y).getInstalledObject().getSprite());
                    this.entities.add(entityAPIForObject);
                }
            }
        }
    }
}
