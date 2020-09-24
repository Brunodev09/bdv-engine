package examples.dungeon.algorithms.astar;

import java.util.Comparator;

public class AStarNodeComparator implements Comparator<AStarNode> {
    public int compare(AStarNode a, AStarNode b) {
        int weightA = 0;
        int weightB = 0;
        if (a.getfCost() == b.getfCost()) {
            if (a.gethCost() == b.gethCost()) {
                weightA += 500;
                weightB += 500;
            }
            else if (a.gethCost() > b.gethCost()) weightB += 500;
            else weightA += 500;
        } else if (b.getfCost() > a.getfCost()) weightA += 500;
        else if (b.getfCost() < a.getfCost()) weightB += 500;
        return weightA == weightB ? 0 : weightB - weightA;
    }
}
