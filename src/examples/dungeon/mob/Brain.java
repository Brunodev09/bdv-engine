package examples.dungeon.mob;

import examples.dungeon.algorithms.astar.AStar;
import examples.dungeon.objects.Actor;
import examples.dungeon.tiles.Tile;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Brain {
    private final LinkedList<BrainNode> core = new LinkedList<>();
    private final Deque<Tile> pathway = new LinkedList<>();

    private AStar astar;

    private final List<Actor> enemies = new ArrayList<>();
    private final List<Actor> friends = new ArrayList<>();
    private final List<Tile> burrows = new ArrayList<>();

    public Tile getNextTile() {
        return pathway.getLast();
    }
}
