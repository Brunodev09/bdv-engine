package examples.dungeon.mob;

import examples.dungeon.objects.Actor;
import examples.dungeon.tiles.Tile;

import java.util.List;

public class Action {
    private final ActionTypes actionType;
    private List<Tile> pathway;
    private int lastPath;
    private Tile targetTile;
    private Actor targetActor;
    private boolean isCompleted = false;

    public Action(ActionTypes actionType, Tile target) {
        this.actionType = actionType;
        this.targetTile = target;
    }

    public Action(ActionTypes actionType, Actor target) {
        this.actionType = actionType;
        this.targetActor = target;
    }

    public ActionTypes getActionType() {
        return actionType;
    }

    public List<Tile> getPathway() {
        return pathway;
    }

    public int getLastPath() {
        return lastPath;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
