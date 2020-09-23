package engine.api.events;

public interface BdvMouseEvent {
    void onMove(double x, double y);
    void onLeftClick(boolean clicked);
    void onRightClick(boolean clicked);
    void onReleaseLeftClick(boolean released);
    void onReleaseRightClick(boolean released);
}
