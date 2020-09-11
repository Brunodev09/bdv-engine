package examples.dungeon.system;

import engine.api.InputAPI;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.objects.InstalledObject;
import examples.dungeon.tiles.Tile;
import java.util.List;
import java.util.logging.Logger;

public class Input {
    private static Logger LOG = Logger.getLogger(Input.class.getName());

    public static boolean movePlayerOnMap(InstalledObject player) {
        boolean triggerRender = false;
        Location location = player.getCurrentLocation();
        List<String> keysPressed = InputAPI.listenForKeyboard();

        for (String keyPressed : keysPressed) {
            if (keyPressed.equals("W")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getCurrentTile().getPositionX(), player.getCurrentTile().getPositionY() - 1)) break;
                movePlayer(player, WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getCurrentTile().getPositionX(), player.getCurrentTile().getPositionY() - 1));
                triggerRender = true;
            }
            else if (keyPressed.equals("S")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getCurrentTile().getPositionX(), player.getCurrentTile().getPositionY() + 1)) break;
                movePlayer(player, WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getCurrentTile().getPositionX(), player.getCurrentTile().getPositionY() + 1));
                triggerRender = true;
            }
            else if (keyPressed.equals("D")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getCurrentTile().getPositionX() + 1, player.getCurrentTile().getPositionY())) break;
                movePlayer(player, WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getCurrentTile().getPositionX() + 1, player.getCurrentTile().getPositionY()));
                triggerRender = true;
            }
           else if (keyPressed.equals("A")) {
                // checking if tile is accessible
                if (!WorldManager.tryToAcessTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getCurrentTile().getPositionX() - 1, player.getCurrentTile().getPositionY())) break;
                movePlayer(player, WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(),
                        player.getCurrentTile().getPositionX() - 1, player.getCurrentTile().getPositionY()));
                triggerRender = true;
            }
        }
        return triggerRender;
    }

    private static void movePlayer(InstalledObject player, Tile newTile) {
        Tile currentPlayerTile = player.getCurrentTile();
        if (!newTile.isSolid()) {
            player.setPreviousTile(currentPlayerTile);
            player.setCurrentTile(newTile);
            player.setNextTile(null);
        }
    }
}
