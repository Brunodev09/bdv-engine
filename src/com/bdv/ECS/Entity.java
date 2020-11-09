package com.bdv.ECS;

import java.util.logging.Logger;

public class Entity {
    private final Logger logger = Logger.getLogger(Entity.class.getName());

    private final int id;
    private boolean kill = false;

    public SystemManager manager;

    public Entity(int id) {
        this.id = id;
    }

    public void setKill(boolean kill) {
        this.kill = kill;
    }

    public boolean isKill() {
        return kill;
    }

    public int getId() {
        return id;
    }

    public <T> void addComponent(Class<T> type, Object... args) {
        try {
            manager.<T>addComponent(this, type, args);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void updateComponent(Class<T> type, Object... args) {
        try {
            manager.<T>updateComponent(this, type, args);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T getComponent(Class<T> type) {
        return manager.getComponent(this, type);
    }

    public <T> boolean hasComponent(Class<T> type) {
        return manager.<T>hasComponent(this, type);
    }

    public <T> void removeComponent(Class<T> type) {
        manager.<T>removeComponent(this, type);
    }

}
