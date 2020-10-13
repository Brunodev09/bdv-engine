package com.bdv.ECS;

import com.bdv.pool.Pool;

import java.lang.reflect.InvocationTargetException;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class SystemManager {
    private Deque<Integer> freeIds;
    private List<Pool<Object>> componentPools;
    private List<Signature> entityComponentSignatures;
    private Set<Entity> createdEntities;
    private Set<Entity> killedEntities;
    private Map<Integer, System> systems;
    private int totalEntities = 0;

    private final Logger log = Logger.getLogger(SystemManager.class.getName());

    public Entity createEntity() {
        int entityId;

        if (freeIds.isEmpty()) {
            entityId = totalEntities++;

            if (entityId > entityComponentSignatures.size()) {
                entityComponentSignatures.add(new Signature());
            }
        } else {
            entityId = freeIds.getFirst();
            freeIds.pop();
        }

        Entity entity = new Entity(entityId);
        entity.manager = this;

        log.info("[SYSTEM_MANAGER] Created Entity with ID " + entity + " (total = " + totalEntities + ")");

        return entity;
    }

    public void destroyEntity(Entity entity) {
        final int entityId = entity.getId();

        freeIds.add(entityId);
        entityComponentSignatures.get(entityId).getSet().clear();

        for (Map.Entry<Integer, System> entry : systems.entrySet()) {
            entry.getValue().removeEntity(entity);
        }
    }

    public void killEntity(Entity entity) {
        killedEntities.add(entity);
    }

    public void update() {
        for (Entity itEntity : createdEntities) {
            this.addEntityToSystems(itEntity);
        }

        createdEntities.clear();

        for (Entity entity : killedEntities) {
            this.destroyEntity(entity);
        }

        killedEntities.clear();
    }

    public Signature getComponentSignature(Entity entity) {
        final int entityId = entity.getId();
        return entityComponentSignatures.get(entityId);
    }

    public void addEntityToSystems(Entity entity) {
        Signature compSign = this.getComponentSignature(entity);
        for (Map.Entry<Integer, System> entry : systems.entrySet()) {
            final Signature sysSignature = entry.getValue().getSignature();
            boolean isSimilar = compSign.equals(sysSignature);

            if (isSimilar) {
                entry.getValue().addEntity(entity);
            }
        }
    }

    public <T> void addComponent(Entity entity, Class<T> type) throws InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final int componentId = Component.<T>getId();
        final int entityId = entity.getId();

        if (componentPools.get(componentId) == null) {
            Pool<T> newComponentPool = new Pool<>();
            componentPools.set(componentId, (Pool<Object>) newComponentPool);
        }

        Pool<T> componentPool = (Pool<T>) componentPools.get(componentId);
        T component = getInstanceOfT(type);

        componentPool.set(entityId, component);
        entityComponentSignatures.get(entityId).getSet().set(componentId);
    }

    public <T> T getInstanceOfT(Class<T> aClass) throws IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        return aClass.getDeclaredConstructor().newInstance();
    }

    public <T> void removeComponent(Entity entity) {
        final int componentId = Component.<T>getId();
        final int entityId = entity.getId();
        entityComponentSignatures.get(entityId).getSet().set(componentId, false);
    }

    public <T> T getComponent(Entity entity) {
        final int componentId = Component.<T>getId();
        final int entityId = entity.getId();
        Pool<T> pool = (Pool<T>) componentPools.get(componentId);
        return pool.get(entityId);
    }

    public <T> boolean hasComponent(Entity entity) {
        final int componentId = Component.<T>getId();
        final int entityId = entity.getId();
        return entityComponentSignatures.get(entityId).getSet().get(componentId);
    }

}
