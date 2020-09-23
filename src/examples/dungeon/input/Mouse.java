package examples.dungeon.input;


import engine.api.events.BdvMouseEvent;
import examples.dungeon.player.Player;

public class Mouse implements BdvMouseEvent {
    private final Player player;

    public Mouse(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void onMove(double x, double y) {
        player.setLatestCursorX(x);
        player.setLatestCursorY(y);
    }

    @Override
    public void onLeftClick(boolean clicked) {
        player.setLeftClicked(clicked);
    }

    @Override
    public void onRightClick(boolean clicked) {
        player.setRightClicked(clicked);
    }

    @Override
    public void onReleaseLeftClick(boolean released) {

    }

    @Override
    public void onReleaseRightClick(boolean released) {

    }
}
