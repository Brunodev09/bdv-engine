package com.bdv.ECS;

import java.lang.reflect.InvocationTargetException;

public class Entity {
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

    public <T> void addComponent(Class<T> type) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        manager.<T>addComponent(this, type);
    }

    public <T> T getComponent() {
        return manager.getComponent(this);
    }

    public <T> boolean hasComponent() {
        return manager.<T>hasComponent(this);
    }

    public <T> void removeComponent() {
        manager.<T>removeComponent(this);
    }


}
