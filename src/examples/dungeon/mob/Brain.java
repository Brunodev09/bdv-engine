package examples.dungeon.mob;

import examples.dungeon.algorithms.astar.AStar;
import examples.dungeon.algorithms.astar.AStarNode;
import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.objects.Actor;
import examples.dungeon.tiles.Stone;
import examples.dungeon.tiles.Tile;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Brain {
    private final LinkedList<BrainNode> core = new LinkedList<>();
    private final Actor brainOwner;

    private AStar aStar;

    private final List<Actor> enemies = new ArrayList<>();
    private final List<Actor> friends = new ArrayList<>();
    private final List<Tile> burrows = new ArrayList<>();
    private final List<Action> actions = new ArrayList<>();

    private Action decision;
    private Tile nextTile;
    private final Location location;

    public Brain(Actor owner) {
        this.brainOwner = owner;
        this.location = owner.getCurrentLocation();
    }

    public void decide() {
        Tile target = WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(), (location.getMapWidth() / 2) + 5, (location.getMapHeight() / 2) + 5);
        if (target == null) return;
        decision = new Action(ActionTypes.MOVE, target);
        aStar = new AStar(location.getMap(), location.getMapWidth(), location.getMapHeight(), brainOwner.getCurrentTile(), target, new Stone());
    }

    public Tile getNextTile() {
        if (decision == null || decision.isCompleted()) {
            decide();
        }
        if (!aStar.isStuck() && !aStar.getCurrentNode().isEndNode()) {
            AStarNode node = aStar.computeNext(aStar.getCurrentNode());
            nextTile = WorldManager.tryGetTile(location.getXGlobal(), location.getYGlobal(), location.getZGlobal(), node.getPosition().getX(), node.getPosition().getY());
            if (nextTile == null) return null;
            aStar.setCurrentNode(node);
        }
        return nextTile;
    }
}
