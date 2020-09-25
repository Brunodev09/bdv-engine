package examples.dungeon.system;

import engine.api.EntityAPI;
import engine.math.Dimension;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.objects.Actor;
import examples.dungeon.objects.Camera;
import examples.dungeon.player.Player;
import examples.dungeon.tiles.Tile;
import examples.dungeon.tiles.VoidTile;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Render {

    // @TODO - Optimize rendering

    private static final Logger LOG = Logger.getLogger(Render.class.getName());

    private List<EntityAPI> entities;
    private final Actor player;
    private Dimension cameraDimensions;
    private Dimension tileSize;
    private List<List<Tile>> previousValidChunk = new ArrayList<>();
    private Actor previousPlayerObject;
    private boolean edges;
    private List<EntityAPI> extraLayer = new ArrayList<>();
    private Turn turn;

    public Render(List<EntityAPI> entities, Turn turn, Actor player, Dimension cameraDimensions, Dimension tileSize) {
        this.entities = entities;
        this.player = player;
        this.cameraDimensions = cameraDimensions;
        this.tileSize = tileSize;
        this.turn = turn;
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

    public void renderTile(List<List<Tile>> map, Tile tile, int x, int y, int xGL, int yGL) {
        EntityAPI entityAPI = map.get(x).get(y).getEntityObject();
        entityAPI.setShouldRender(true);
        entityAPI.translate(new Vector3f(
                (float) tileSize.width * xGL,
                (float) tileSize.height * yGL, 0));
        entityAPI.setSpriteSheet(tile.getSprite());
        if (tile.isHidden() && tile.getLight() == null && !tile.isSelected())
            entityAPI.setRgbVector(new Vector3f(0.2f, 0.2f, 0.2f));
        else if (!tile.isHidden() && tile.getLight() == null && !tile.isSelected()) entityAPI.setRgbVector(null);
        if (tile.isSelected()) entityAPI.setRgbVector(new Vector3f(1.0f, 1.0f, 0));
        if (tile.getLight() != null && !tile.isSelected()) entityAPI.setRgbVector(tile.getLight());
        if (tile.getActor() != null && tile.getActor().getEntityObject() != null && !tile.getActor().getType().equals("light")) {
            int depth = 1;
            if (tile.getActor().getType().equals("player")) depth = 2;
            EntityAPI object = tile.getActor().getEntityObject();
            object.translate(new Vector3f(
                    (float) tileSize.width * xGL,
                    (float) tileSize.height * yGL, depth));
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

                Rectangle subImageSize = map.get(x).get(y).getSprite().getTile();
                Rectangle fullImageSize = map.get(x).get(y).getSprite().getFullImageSize();
                float uOffset = map.get(x).get(y).getSprite().getTileX();
                float vOffset = map.get(x).get(y).getSprite().getTileY();
                float u = (float) subImageSize.width / fullImageSize.width;
                float v = (float) subImageSize.height / fullImageSize.height;
//                float[] uvBuffer = new float[]{
//                        (u + (u * uOffset)), (v + (vOffset * v)),
//                        (u * uOffset), (v + (vOffset * v)),
//                        (u * uOffset), (vOffset * v),
//                        (u + (u * uOffset)), (vOffset * v)
//                };
                float[] uvBuffer = new float[]{
                        (u * uOffset), (vOffset * v),
                        (u + (u * uOffset)), (vOffset * v),
                        (u + (u * uOffset)), (v + (vOffset * v)),
                        (u * uOffset), (v + (vOffset * v)),
                };

                entityAPI.setRenderSpriteRetroCompatibility(false);
                entityAPI.setUv(uvBuffer);
                map.get(x).get(y).setEntityObject(entityAPI);
                entityAPI.setShouldRender(false);
                entityAPI.setSpriteSheet(map.get(x).get(y).getSprite());
                this.entities.add(entityAPI);

                if (map.get(x).get(y).getActor() != null && !map.get(x).get(y).getActor().getType().equals("light")
                        && !map.get(x).get(y).getActor().getType().equals("camera")) {
                    Actor object = map.get(x).get(y).getActor();
                    EntityAPI entityAPIForObject = new EntityAPI(null,
                            new Vector3f(0, 0, 1),
                            new Dimension(object.getWidth(), object.getHeight()),
                            new Vector2f(0, 0));

                    Rectangle subImageSizeActor = object.getSprite().getTile();
                    Rectangle fullImageSizeActor = object.getSprite().getFullImageSize();
                    float uOffsetActor = object.getSprite().getTileX();
                    float vOffsetActor = object.getSprite().getTileY();
                    float uActor = (float) subImageSizeActor.width / fullImageSizeActor.width;
                    float vActor = (float) subImageSizeActor.height / fullImageSizeActor.height;

                    float[] uvBufferActor = new float[]{
                            (uActor * uOffsetActor), (vOffsetActor * vActor),
                            (uActor + (uActor * uOffsetActor)), (vOffsetActor * vActor),
                            (uActor + (uActor * uOffsetActor)), (vActor + (vOffsetActor * vActor)),
                            (uActor * uOffsetActor), (vActor + (vOffsetActor * vActor)),
                    };
                    entityAPIForObject.setRenderSpriteRetroCompatibility(false);
                    entityAPIForObject.setUv(uvBufferActor);
                    object.setEntityObject(entityAPIForObject);
                    entityAPI.setRenderSpriteRetroCompatibility(false);
                    entityAPIForObject.setShouldRender(false);
                    entityAPIForObject.setSpriteSheet(map.get(x).get(y).getActor().getSprite());
                    this.entities.add(entityAPIForObject);
                }
            }
        }
    }

    public void renderChunkFromMap(List<List<Tile>> chunkToRender, List<List<Tile>> map) {
        int x = 0;
        for (int i = -cameraDimensions.width / 2; i < cameraDimensions.width / 2; i++) {
            if (x >= cameraDimensions.width - 1) break;
            int y = 0;
            for (int j = -cameraDimensions.height / 2; j < cameraDimensions.height / 2; j++) {
                if (y >= cameraDimensions.height - 1) break;
                Tile tile = chunkToRender.get(x).get(y);
                renderTile(map, tile, tile.getPositionX(), tile.getPositionY(), i, j);
                y++;
            }
            x++;
        }
    }

    public List<List<Tile>> getPlayerChunk(Actor player) throws CloneNotSupportedException {
        Location location = player.getCurrentLocation();
        Tile playerTile = player.getCurrentTile();
        List<List<Tile>> tilesToRender = new ArrayList<>();
        int validChunkNumber = cameraDimensions.width - 1;

        for (int i = 0; i < location.getMapWidth(); i++) {
            List<Tile> chunk = new ArrayList<>();
            for (int j = 0; j < location.getMapHeight(); j++) {
                if (i > playerTile.getPositionX() - (cameraDimensions.width / 2) && i < playerTile.getPositionX() + (cameraDimensions.width / 2) &&
                        j > playerTile.getPositionY() - (cameraDimensions.height / 2) && j < playerTile.getPositionY() + (cameraDimensions.height / 2)) {

                    Tile tileToAdd = WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(), i, j);
                    if (tileToAdd != null) {
                        tileToAdd.getEntityObject().setShouldRender(true);
                        chunk.add(tileToAdd);
                    }

                } else {
                    Tile tileToRemove = WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(), i, j);
                    if (tileToRemove != null) {
                        tileToRemove.getEntityObject().setShouldRender(false);
                        if (tileToRemove.getActor() != null) {
                            tileToRemove.getActor().getEntityObject().setShouldRender(false);
                        }
                    }
                }
            }
            if (chunk.isEmpty()) continue;
            tilesToRender.add(chunk);
        }
        // Adding possibly missing tiles
        for (List<Tile> tiles : tilesToRender) {
            if (tiles.size() < validChunkNumber) {
                int numTiles = validChunkNumber - tiles.size();
                for (int i = 0; i < numTiles; i++) {
                    for (int j = 0; j < validChunkNumber; j++) {
                        Tile voidTile = new VoidTile();
                        tiles.add(voidTile);
                    }
                }
            }
        }
        // Adding possibly missing layers of tiles
        if (tilesToRender.size() < validChunkNumber) {
            int numLayers = validChunkNumber - tilesToRender.size();
            for (int i = 0; i < numLayers; i++) {
                List<Tile> newChunk = new ArrayList<>();
                for (int j = 0; j < validChunkNumber; j++) {
                    Tile voidTile = new VoidTile();
                    newChunk.add(voidTile);
                }
                tilesToRender.add(newChunk);
            }
        }

        // Hiding all tiles
        for (List<Tile> tiles : tilesToRender) {
            for (Tile tile : tiles) {
                if (tile.getLight() == null)
                    tile.setHidden(true);
            }
        }

        if (player.getType().equals("player")) {
            ((Player) player).setChunk(tilesToRender);
            return player.rayCastingFOV(tilesToRender);
        }
        // if free camera-mode
        else {
            ((Camera) player).setChunk(tilesToRender);
            for (Actor actor : turn.getJobs()) {
                if (!actor.getType().equals("camera"))
                    actor.applyFOV(tilesToRender);
            }
            return tilesToRender;
        }

//        if (tilesToRender.size() == validChunkNumber) {
//            previousPlayerObject = (Player) player.clone();
//            if (!previousValidChunk.isEmpty()) {
//                previousValidChunk.clear();
//            }
//            for (int index = 0; index < tilesToRender.size(); index++) {
//                List<Tile> tiles = tilesToRender.get(index);
//                List<Tile> cpList = new ArrayList<>();
//                for (Tile tile : tiles) {
//                    cpList.add((Tile) tile.clone());
//                }
//                previousValidChunk.add(new ArrayList<>(cpList));
//            }
//        } else if (validChunkNumber >= location.getMapWidth() / 2) {
//            return tilesToRender;
//        } else return renderEdges(player);
//
//        if (edges) {
//            edges = false;
//            return previousValidChunk;
//        }
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
}
