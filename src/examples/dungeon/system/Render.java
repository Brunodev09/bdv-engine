package examples.dungeon.system;

import engine.api.EntityAPI;
import engine.math.Dimension;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.objects.Actor;
import examples.dungeon.player.Player;
import examples.dungeon.tiles.Tile;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.xml.transform.stax.StAXResult;
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

    public Render(List<EntityAPI> entities, Actor player, Dimension cameraDimensions, Dimension tileSize) {
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

    public List<List<Tile>> getPlayerChunk(Actor player) throws CloneNotSupportedException {
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
        return applyFOV(tilesToRender, player);
    }

    public List<List<Tile>> applyFOV(List<List<Tile>> chunk, Actor actor) {
        for (List<Tile> tiles : chunk) {
            for (Tile tile : tiles) {
                if (tile.isSolid()) continue;
                int x = player.getCurrentLocation().getXGlobal();
                int y = player.getCurrentLocation().getYGlobal();
                int z = player.getCurrentLocation().getZGlobal();
                List<Tile> obstacles = new ArrayList<>();
                List<Tile> neighbors  = WorldManager.tryToGetNeighbor(x, y, z, tile.getPositionX(), tile.getPositionY());
                for (Tile neighbor : neighbors) {
                    if (neighbor.isSolid()) {
                        obstacles.add(neighbor);
                    }
                }
                boolean isHidden = false;
                for (Tile obstacle : obstacles) {
                    boolean left = false;
                    boolean right = false;
                    boolean up = false;
                    boolean down = false;
                    boolean diagonalLeftUp = false;
                    boolean diagonalLeftDown = false;
                    boolean diagonalRightUp = false;
                    boolean diagonalRightDown = false;

                    boolean leftFromActor = false;
                    boolean rightFromActor = false;
                    boolean upFromActor = false;
                    boolean downFromActor = false;
                    boolean diagonalLeftUpFromActor = false;
                    boolean diagonalLeftDownFromActor = false;
                    boolean diagonalRightUpFromActor = false;
                    boolean diagonalRightDownFromActor = false;

                    if (tile.getPositionX() - obstacle.getPositionX() > 0) {
                        left = true;
                    }
                    else if (tile.getPositionX() - obstacle.getPositionX() < 0) {
                        right = true;
                    }
                    if (tile.getPositionY() - obstacle.getPositionY() > 0) {
                        up = true;
                    }
                    else if (tile.getPositionY() - obstacle.getPositionY() < 0) {
                        down = true;
                    }

                    if (left && up) diagonalLeftUp = true;
                    else if (left && down) diagonalLeftDown = true;
                    else if (right && up) diagonalRightUp = true;
                    else if (right && down) diagonalRightDown = true;

                    if (tile.getPositionX() - actor.getCurrentTile().getPositionX() > 0) {
                        leftFromActor = true;
                    }
                    else if (tile.getPositionX() - actor.getCurrentTile().getPositionX() < 0) {
                        rightFromActor = true;
                    }
                    if (tile.getPositionY() - actor.getCurrentTile().getPositionY() > 0) {
                        upFromActor = true;
                    }
                    else if (tile.getPositionY() - actor.getCurrentTile().getPositionY() < 0) {
                        downFromActor = true;
                    }
                    if (leftFromActor && upFromActor) diagonalLeftUpFromActor = true;
                    else if (leftFromActor && downFromActor) diagonalLeftDownFromActor = true;
                    else if (rightFromActor && upFromActor) diagonalRightUpFromActor = true;
                    else if (rightFromActor && downFromActor) diagonalRightDownFromActor = true;

                    if ((diagonalLeftUp && diagonalLeftUpFromActor) ||
                            (diagonalRightUp && diagonalRightUpFromActor) ||
                            (diagonalLeftDown && diagonalLeftDownFromActor) ||
                            (diagonalRightDown && diagonalRightDownFromActor)) {
                        tile.setHidden(true);
                        isHidden = true;
                        break;
                    }
                    else if ((right && rightFromActor) || (left && leftFromActor) || (up && upFromActor) || (down && downFromActor)) {
                        tile.setHidden(true);
                        isHidden = true;
                        break;
                    }
                }
                if (!isHidden && tile.isHidden()) tile.setHidden(false);
            }
        }
        return chunk;
    }

    public List<List<Tile>> renderEdges(Actor player) throws CloneNotSupportedException {
        edges = true;
        Tile playerTile = player.getCurrentTile();

        for (int i = 0; i < previousValidChunk.size(); i++) {
            for (int j = 0; j < previousValidChunk.get(i).size(); j++) {
                if (previousValidChunk.get(i).get(j).getActor() != null &&
                        previousValidChunk.get(i).get(j).getActor().getType().equals("player")) {
                    previousValidChunk.get(i).get(j).setActor(null);
                }
            }
        }
        for (int i = 0; i < previousValidChunk.size(); i++) {
            for (int j = 0; j < previousValidChunk.get(i).size(); j++) {
                if (previousValidChunk.get(i).get(j).getPositionX() == playerTile.getPositionX()) {
                    if (previousValidChunk.get(i).get(j).getPositionY() == playerTile.getPositionY()) {
                        previousValidChunk.get(i).get(j).setActor(playerTile.getActor());
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
        if (tile.isHidden()) entityAPI.setRgbVector(new Vector3f(0.3f, 0.3f, 0.3f));
        else entityAPI.setRgbVector(null);
        if (tile.getActor() != null && tile.getActor().getEntityObject() != null) {
            EntityAPI object = tile.getActor().getEntityObject();
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
                if (map.get(x).get(y).getActor() != null) {
                    Actor object = map.get(x).get(y).getActor();
                    EntityAPI entityAPIForObject = new EntityAPI(null,
                            new Vector3f(0, 0, 1),
                            new Dimension(object.getWidth(), object.getHeight()),
                            new Vector2f(0, 0));
                    object.setEntityObject(entityAPIForObject);
                    entityAPIForObject.setShouldRender(false);
                    entityAPIForObject.setSpriteSheet(map.get(x).get(y).getActor().getSprite());
                    this.entities.add(entityAPIForObject);
                }
            }
        }
    }
}
