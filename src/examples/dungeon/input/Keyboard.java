package examples.dungeon.input;

import engine.api.events.BdvKeyEvent;
import examples.dungeon.objects.Actor;
import examples.dungeon.objects.Camera;
import examples.dungeon.objects.Player;

public class Keyboard implements BdvKeyEvent {
    private final Actor player;

    public Keyboard(Actor player) {
        this.player = player;
    }

    public Actor getPlayer() {
        return player;
    }

    @Override
    public void onKeyEvent(String key, String action) {
        if (action.equals("RELEASE")) {
            if (player.getType().equals("player"))
                ((Player) player).setLastKeyReleased(key);
            else
                ((Camera) player).setLastKeyReleased(key);
        }
    }
}
