package examples.dungeon.system;

import engine.api.InputAPI;
import engine.entities.Camera2D;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.player.Player;
import examples.dungeon.tiles.Tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Input {
    private static Logger LOG = Logger.getLogger(Input.class.getName());
    public Input() {
    }

    public static boolean movePlayerOnMap(Player player, Camera2D camera) {
        boolean triggerRender = false;
        Location location = player.getCurrentLocation();
        List<List<Tile>> map = player.getCurrentLocation().getMap();
        List<String> keysPressed = InputAPI.listenForKeyboard();

        for (String keyPressed : keysPressed) {
            if (keyPressed.equals("W")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getPlayerTile().getPositionX() - 1, player.getPlayerTile().getPositionY())) break;
                Tile previousTileBeforePlayer = player.getPreviousTile();
                Tile newTile = map.get(player.getPlayerTile().getPositionX() - 1).get(player.getPlayerTile().getPositionY());
                movePlayer(map, player, previousTileBeforePlayer, newTile);
                camera.move();
                triggerRender = true;
            }
            if (keyPressed.equals("A")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getPlayerTile().getPositionX(), player.getPlayerTile().getPositionY() - 1)) break;
                Tile previousTileBeforePlayer = player.getPreviousTile();
                Tile newTile = map.get(player.getPlayerTile().getPositionX()).get(player.getPlayerTile().getPositionY() - 1);
                movePlayer(map, player, previousTileBeforePlayer, newTile);
                camera.move();
                triggerRender = true;
            }
            if (keyPressed.equals("D")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getPlayerTile().getPositionX(), player.getPlayerTile().getPositionY() + 1)) break;
                Tile previousTileBeforePlayer = player.getPreviousTile();
                Tile newTile = map.get(player.getPlayerTile().getPositionX()).get(player.getPlayerTile().getPositionY() + 1);
                movePlayer(map, player, previousTileBeforePlayer, newTile);
                camera.move();
                triggerRender = true;
            }
            if (keyPressed.equals("S")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getPlayerTile().getPositionX() + 1, player.getPlayerTile().getPositionY())) break;
                Tile previousTileBeforePlayer = player.getPreviousTile();
                Tile newTile = map.get(player.getPlayerTile().getPositionX() + 1).get(player.getPlayerTile().getPositionY());
                movePlayer(map, player, previousTileBeforePlayer, newTile);
                camera.move();
                triggerRender = true;
            }
        }
        return triggerRender;
    }

    private static void movePlayer(List<List<Tile>> map, Player player, Tile previousTileBeforePlayer, Tile newTile) {
        // checking if next tile is walkable
        if (!newTile.isSolid()) {
            // setting the tile the player currently resides into the tile it previously were before the player
            map.get(player.getPlayerTile().getPositionX()).set(player.getPlayerTile().getPositionY(), previousTileBeforePlayer);
            // setting the previous tile as the one that the player currently resides
            player.setPreviousTile(newTile);
            // sets the player to the new position on the map
            map.get(newTile.getPositionX()).set(newTile.getPositionY(), player.getPlayerTile());
            // updates player position on its object
            player.getPlayerTile().setPosition(newTile.getPositionX(), newTile.getPositionY());
        }
    }
}
