package examples.dungeon.system;


import examples.dungeon.objects.Actor;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private List<Actor> jobs = new ArrayList<>();

    public void addToJobQueue(Actor obj) {
        jobs.add(obj);
    }

    public boolean process() {
        boolean action = false;
        for (Actor actor : jobs) {
            if (actor.move()) action = true;
        }
        return action;
    }
}
