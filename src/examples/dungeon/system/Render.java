package examples.dungeon.system;

import engine.api.EntityAPI;
import engine.math.Dimension;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.objects.Actor;
import examples.dungeon.player.Player;
import examples.dungeon.tiles.Tile;
import examples.dungeon.tiles.VoidTile;
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
    private final Actor player;
    private Dimension cameraDimensions;
    private Dimension tileSize;
    private List<List<Tile>> previousValidChunk = new ArrayList<>();
    private Actor previousPlayerObject;
    private boolean edges;
    private List<EntityAPI> extraLayer = new ArrayList<>();

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

    public void renderTile(List<List<Tile>> map, Tile tile, int x, int y, int xGL, int yGL) {
        EntityAPI entityAPI = map.get(x).get(y).getEntityObject();
        entityAPI.setShouldRender(true);
        entityAPI.translate(new Vector3f(
                (float) tileSize.width * xGL,
                (float) tileSize.height * yGL, 0));
        entityAPI.setSpriteSheet(tile.getSprite());
        if (tile.isHidden() && tile.getLight() == null) entityAPI.setRgbVector(new Vector3f(0.2f, 0.2f, 0.2f));
        else if (!tile.isHidden() && tile.getLight() == null) entityAPI.setRgbVector(null);
        if (tile.getLight() != null) entityAPI.setRgbVector(tile.getLight());
        if (tile.getActor() != null && tile.getActor().getEntityObject() != null && !tile.getActor().getType().equals("light")) {
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
                if (map.get(x).get(y).getActor() != null && !map.get(x).get(y).getActor().getType().equals("light")) {
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
            for (int j = 0; j < location.getMapWidth(); j++) {
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
        if (tilesToRender.size() != validChunkNumber) {
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
        return rayCastingFOV(tilesToRender, player);
    }

    public List<List<Tile>> applyFOV(List<List<Tile>> chunk, Actor actor) {
        for (List<Tile> tiles : chunk) {
            for (Tile tile : tiles) {
                if (tile.isSolid()) continue;
                int x = player.getCurrentLocation().getXGlobal();
                int y = player.getCurrentLocation().getYGlobal();
                int z = player.getCurrentLocation().getZGlobal();
                List<Tile> obstacles = new ArrayList<>();
                List<Tile> neighbors = WorldManager.tryToGetNeighbor(x, y, z, tile.getPositionX(), tile.getPositionY());
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
                    } else if (tile.getPositionX() - obstacle.getPositionX() < 0) {
                        right = true;
                    }
                    if (tile.getPositionY() - obstacle.getPositionY() > 0) {
                        up = true;
                    } else if (tile.getPositionY() - obstacle.getPositionY() < 0) {
                        down = true;
                    }

                    if (left && up) diagonalLeftUp = true;
                    else if (left && down) diagonalLeftDown = true;
                    else if (right && up) diagonalRightUp = true;
                    else if (right && down) diagonalRightDown = true;

                    if (tile.getPositionX() - actor.getCurrentTile().getPositionX() > 0) {
                        leftFromActor = true;
                    } else if (tile.getPositionX() - actor.getCurrentTile().getPositionX() < 0) {
                        rightFromActor = true;
                    }
                    if (tile.getPositionY() - actor.getCurrentTile().getPositionY() > 0) {
                        upFromActor = true;
                    } else if (tile.getPositionY() - actor.getCurrentTile().getPositionY() < 0) {
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
                    } else if ((right && rightFromActor) || (left && leftFromActor) || (up && upFromActor) || (down && downFromActor)) {
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

    public List<List<Tile>> rayCastingFOV(List<List<Tile>> chunk, Actor actor) {
        int fov = 20;
        int rayOriginX = actor.getCurrentTile().getPositionX();
        int rayOriginY = actor.getCurrentTile().getPositionY();
        int xG = actor.getCurrentLocation().getXGlobal();
        int yG = actor.getCurrentLocation().getYGlobal();
        int zG = actor.getCurrentLocation().getZGlobal();

        boolean cast2pi = true;
        boolean castPi2 = true;
        boolean castPi = true;
        boolean cast32Pi = true;
        boolean castDiagonal1 = true;
        boolean castDiagonal2 = true;
        boolean castDiagonal3 = true;
        boolean castDiagonal4 = true;
        boolean castDiagonal5 = true;
        boolean castDiagonal6 = true;
        boolean castDiagonal7 = true;
        boolean castDiagonal8 = true;
        boolean castDiagonal9 = true;
        boolean castDiagonal10 = true;
        boolean castDiagonal11 = true;
        boolean castDiagonal12 = true;

        for (List<Tile> tiles : chunk) {
            for (Tile tile : tiles) {
                if (tile.getLight() == null)
                    tile.setHidden(true);
            }
        }

        for (int x = 1; x < fov - 1; x++) {
            for (int y = 1; y < fov - 1; y++) {
                Tile target1 = null;
                Tile target2 = null;
                Tile target3 = null;
                Tile target4 = null;
                Tile target5 = null;
                Tile target6 = null;
                Tile target7 = null;
                Tile target8 = null;
                Tile target9 = null;
                Tile target10 = null;
                Tile target11 = null;
                Tile target12 = null;
                Tile target13 = null;
                Tile target14 = null;
                Tile target15 = null;
                Tile target16 = null;

                // 0 degrees // 2pi
                if (cast2pi)
                    target1 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY);
                // pi / 2
                if (castPi2)
                    target2 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX, rayOriginY - y);
                // pi
                if (castPi)
                    target3 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY);
                // (3/2) * pi
                if (cast32Pi)
                    target4 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX, rayOriginY + y);
                // diagonals
                if (castDiagonal1)
                    target5 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY + x);
                if (castDiagonal2)
                    target6 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY + x);
                if (castDiagonal3)
                    target7 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY - x);
                if (castDiagonal4)
                    target8 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY - x);

                if (castDiagonal5)
                    target9 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY + y);
                if (castDiagonal6)
                    target10 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY + y);
                if (castDiagonal7)
                    target11 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - x, rayOriginY - y);
                if (castDiagonal8)
                    target12 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + x, rayOriginY - y);

                if (castDiagonal9)
                    target13 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + y, rayOriginY + x);
                if (castDiagonal10)
                    target14 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - y, rayOriginY + x);
                if (castDiagonal11)
                    target15 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX - y, rayOriginY - x);
                if (castDiagonal12)
                    target16 = WorldManager.tryGetTile(xG, yG, zG, rayOriginX + y, rayOriginY - x);

                if (target1 != null && target1.isSolid()) {
                    cast2pi = false;
                    target1.setHidden(false);
                } else if (target1 != null && !target1.isSolid()) {
                    target1.setHidden(false);
                }
                if (target2 != null && target2.isSolid()) {
                    castPi2 = false;
                    target2.setHidden(false);
                } else if (target2 != null && !target2.isSolid()) {
                    target2.setHidden(false);
                }
                if (target3 != null && target3.isSolid()) {
                    castPi = false;
                    target3.setHidden(false);
                } else if (target3 != null && !target3.isSolid()) {
                    target3.setHidden(false);
                }
                if (target4 != null && target4.isSolid()) {
                    cast32Pi = false;
                    target4.setHidden(false);
                } else if (target4 != null && !target4.isSolid()) {
                    target4.setHidden(false);
                }
                if (target5 != null && target5.isSolid()) {
                    castDiagonal1 = false;
                    target5.setHidden(false);
                } else if (target5 != null && !target5.isSolid()) {
                    target5.setHidden(false);
                }
                if (target6 != null && target6.isSolid()) {
                    castDiagonal2 = false;
                    target6.setHidden(false);
                } else if (target6 != null && !target6.isSolid()) {
                    target6.setHidden(false);
                }
                if (target7 != null && target7.isSolid()) {
                    castDiagonal3 = false;
                    target7.setHidden(false);
                } else if (target7 != null && !target7.isSolid()) {
                    target7.setHidden(false);
                }
                if (target8 != null && target8.isSolid()) {
                    castDiagonal4 = false;
                    target8.setHidden(false);
                } else if (target8 != null && !target8.isSolid()) {
                    target8.setHidden(false);
                }
                if (target9 != null && target9.isSolid()) {
                    castDiagonal5 = false;
                    target9.setHidden(false);
                } else if (target9 != null && !target9.isSolid()) {
                    target9.setHidden(false);
                }
                if (target10 != null && target10.isSolid()) {
                    castDiagonal6 = false;
                    target10.setHidden(false);
                } else if (target10 != null && !target10.isSolid()) {
                    target10.setHidden(false);
                }
                if (target11 != null && target11.isSolid()) {
                    castDiagonal7 = false;
                    target11.setHidden(false);
                } else if (target11 != null && !target11.isSolid()) {
                    target11.setHidden(false);
                }
                if (target12 != null && target12.isSolid()) {
                    castDiagonal8 = false;
                    target12.setHidden(false);
                } else if (target12 != null && !target12.isSolid()) {
                    target12.setHidden(false);
                }
                if (target13 != null && target13.isSolid()) {
                    castDiagonal9 = false;
                    target13.setHidden(false);
                } else if (target13 != null && !target13.isSolid()) {
                    target13.setHidden(false);
                }
                if (target14 != null && target14.isSolid()) {
                    castDiagonal10 = false;
                    target14.setHidden(false);
                } else if (target14 != null && !target14.isSolid()) {
                    target14.setHidden(false);
                }
                if (target15 != null && target15.isSolid()) {
                    castDiagonal11 = false;
                    target15.setHidden(false);
                } else if (target15 != null && !target15.isSolid()) {
                    target15.setHidden(false);
                }
                if (target16 != null && target16.isSolid()) {
                    castDiagonal12 = false;
                    target16.setHidden(false);
                } else if (target16 != null && !target16.isSolid()) {
                    target16.setHidden(false);
                }
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
        return rayCastingFOV(previousValidChunk, player);
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
