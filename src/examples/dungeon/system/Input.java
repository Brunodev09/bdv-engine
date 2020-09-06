package examples.dungeon.system;

import engine.api.InputAPI;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.player.Player;
import examples.dungeon.tiles.Tile;
import java.util.List;
import java.util.logging.Logger;

public class Input {
    private static Logger LOG = Logger.getLogger(Input.class.getName());

    public static boolean movePlayerOnMap(Player player) {
        boolean triggerRender = false;
        Location location = player.getCurrentLocation();
        List<String> keysPressed = InputAPI.listenForKeyboard();
        int x = player.getCurrentLocation().getXGlobal();
        int y = player.getCurrentLocation().getYGlobal();
        int z = player.getCurrentLocation().getZGlobal();

        for (String keyPressed : keysPressed) {
            if (keyPressed.equals("W")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getPlayerTile().getPositionX(), player.getPlayerTile().getPositionY() - 1)) break;
                // getting the tile the player is actively standing on
                Tile currentPlayerTile = player.getPreviousTile();
                // getting the tile the player is going to be
                Tile newTile = WorldManager.tryGetTile(x, y, z, player.getPlayerTile().getPositionX(),
                        player.getPlayerTile().getPositionY() - 1);
                // moving the player
                movePlayer(player, currentPlayerTile, newTile);
                triggerRender = true;
            }
            else if (keyPressed.equals("S")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getPlayerTile().getPositionX(), player.getPlayerTile().getPositionY() + 1)) break;
                // getting the tile the player is actively standing on
                Tile currentPlayerTile = player.getPreviousTile();
                // getting the tile the player is going to be
                Tile newTile = WorldManager.tryGetTile(x, y, z, player.getPlayerTile().getPositionX(),
                        player.getPlayerTile().getPositionY() + 1);
                // moving the player
                movePlayer(player, currentPlayerTile, newTile);
                triggerRender = true;
            }
            else if (keyPressed.equals("D")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getPlayerTile().getPositionX() + 1, player.getPlayerTile().getPositionY())) break;
                // getting the tile the player is actively standing on
                Tile currentPlayerTile = player.getPreviousTile();
                // getting the tile the player is going to be
                Tile newTile = WorldManager.tryGetTile(x, y, z, player.getPlayerTile().getPositionX() + 1,
                        player.getPlayerTile().getPositionY());
                // moving the player
                movePlayer(player, currentPlayerTile, newTile);
                triggerRender = true;
            }
           else if (keyPressed.equals("A")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getPlayerTile().getPositionX() - 1, player.getPlayerTile().getPositionY())) break;
                // getting the tile the player is actively standing on
                Tile currentPlayerTile = player.getPreviousTile();
                // getting the tile the player is going to be
                Tile newTile = WorldManager.tryGetTile(x, y, z, player.getPlayerTile().getPositionX() - 1,
                        player.getPlayerTile().getPositionY());
                // moving the player
                movePlayer(player, currentPlayerTile, newTile);
                triggerRender = true;
            }
        }
        return triggerRender;
    }

    private static void movePlayer(Player player, Tile currentPlayerTile, Tile newTile) {
        int x = player.getCurrentLocation().getXGlobal();
        int y = player.getCurrentLocation().getYGlobal();
        int z = player.getCurrentLocation().getZGlobal();
        // checking if next tile is walkable
        if (!newTile.isSolid()) {
            // setting the tile the player currently resides into the tile it previously were before the player
            WorldManager.trySetTile(x, y, z, player.getPlayerTile().getPositionX(), player.getPlayerTile().getPositionY(),
                    currentPlayerTile);
            // setting the previous tile as the one that the player currently resides
            player.setPreviousTile(newTile);
            // sets the player to the new position on the map
            WorldManager.trySetTile(x, y, z, newTile.getPositionX(), newTile.getPositionY(), player.getPlayerTile());
            // updates player position on its object
            player.getPlayerTile().setPosition(newTile.getPositionX(), newTile.getPositionY());
        }
    }
}
