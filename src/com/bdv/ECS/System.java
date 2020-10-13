package com.bdv.ECS;


import java.util.List;

public abstract class System {
    private Signature signature;
    private List<Entity> entities;

    public System() {}

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        int it = 0;
        for (Entity itEntity : entities) {
            if (entity.equals(itEntity)) {
                entities.remove(it);
                break;
            }
            it++;
        }
    }

    public Signature getSignature() {
        return signature;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public <T> void requireComponent() {
        final int componentId = Component.getId();
        signature.getSet().set(componentId);
    }
}
