package examples.dungeon.system;


import examples.dungeon.objects.Actor;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private List<Actor> jobs = new ArrayList<>();
    private int latestJobIndex = 0;

    public void addToJobQueue(Actor obj) {
        jobs.add(obj);
    }
// @TODO - Don't re-render entire chunk if only 1-2 tiles are oscilating
    public boolean process() {
        boolean action = false;
        if (latestJobIndex == jobs.size() - 1) latestJobIndex = 0;

        for (int i = latestJobIndex; i < jobs.size(); i++) {
            action = jobs.get(i).action();
            for (Actor subActor : jobs.get(i).getSubActors()) {
                subActor.action();
            }
            if ((jobs.get(i).getType().equals("player") || jobs.get(i).getType().equals("camera")) && !action) {
                latestJobIndex = i;
                break;
            } else latestJobIndex = 0;
        }
        for (Actor actor : jobs) {
            if (actor.getType().equals("player")) {
                action = actor.mouse();
            }
            else if (actor.getType().equals("camera")) {
                actor.action();
                action = actor.mouse();
            }
        }
        return action;
    }

    public List<Actor> getJobs() {
        return jobs;
    }
}
