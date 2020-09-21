package examples.dungeon.system;


import examples.dungeon.objects.Actor;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private List<Actor> jobs = new ArrayList<>();

    public void addToJobQueue(Actor obj) {
        jobs.add(obj);
    }
// @TODO - Don't re-render entire chunk if only 1-2 tiles are oscilating
    public boolean process() {
        boolean action = false;
        for (Actor actor : jobs) {
            action = actor.action();
            for (Actor subActor : actor.getSubActors()) {
                subActor.action();
            }
            if (actor.getType().equals("player") && !action) break;
        }
        return action;
    }
}
