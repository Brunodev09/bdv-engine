package examples.dungeon.input;

import engine.api.events.BdvKeyEvent;
import examples.dungeon.player.Player;

public class Keyboard implements BdvKeyEvent {
    private final Player player;

    public Keyboard(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void onKeyEvent(String key, String action) {
        if (action.equals("RELEASE")) {
            player.setLastKeyReleased(key);
        }
    }
}
