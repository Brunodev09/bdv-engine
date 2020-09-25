package examples.dungeon.input;


import engine.api.events.BdvMouseEvent;
import examples.dungeon.objects.Actor;
import examples.dungeon.objects.Camera;
import examples.dungeon.objects.Player;

public class Mouse implements BdvMouseEvent {
    private final Actor player;

    public Mouse(Actor player) {
        this.player = player;
    }

    public Actor getPlayer() {
        return player;
    }

    @Override
    public void onMove(double x, double y) {
        if (player.getType().equals("player")) {
            ((Player) player).setLatestCursorX(x);
            ((Player) player).setLatestCursorY(y);
        }
        else {
            ((Camera) player).setLatestCursorX(x);
            ((Camera) player).setLatestCursorY(y);
        }

    }

    @Override
    public void onLeftClick(boolean clicked) {
        if (player.getType().equals("player")) {
            ((Player) player).setLeftClicked(clicked);
        }
        else {
            ((Camera) player).setLeftClicked(clicked);
        }
    }

    @Override
    public void onRightClick(boolean clicked) {
        if (player.getType().equals("player")) {
            ((Player) player).setRightClicked(clicked);
        }
        else {
            ((Camera) player).setRightClicked(clicked);
        }
    }

    @Override
    public void onReleaseLeftClick(boolean released) {

    }

    @Override
    public void onReleaseRightClick(boolean released) {

    }
}
