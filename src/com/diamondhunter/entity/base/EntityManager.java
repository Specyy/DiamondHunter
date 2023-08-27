package com.diamondhunter.entity.base;

import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EntityManager {
    private static final Map<String, EntityManager> managers = new ConcurrentHashMap<>();

    private String identifier;
    private List<Entity> entities;
    private Comparator<Entity> depthSorter = (o1, o2) -> Float.compare(o2.depth, o1.depth);

    private EntityManager(String identifier){
        this(identifier, new ArrayList<>());
    }

    private EntityManager(String identifier, List<Entity> startEntities){
        this.identifier = identifier;
        this.entities = new CopyOnWriteArrayList<>(startEntities);

        managers.put(identifier, this);
    }

    public static EntityManager createManager(String identifier){
        return containsLogger(identifier) ?  getManager(identifier) : new EntityManager(identifier);
    }

    public static EntityManager createManager(String identifier, List<Entity> startEntities){
        return containsLogger(identifier) ? getManager(identifier) : new EntityManager(identifier, startEntities);
    }

    public static boolean containsLogger(String identifier) {
        return getManager(identifier) != null;
    }

    public void setIdentifier(String identifier) {
        if (managers.containsKey(identifier))
            throw new IllegalArgumentException("Cannot set Entity Manager \"" + this.identifier + "\"'s name to \"" + identifier + "\" because an " +
                    "Entity Manager with that name already exists!");

        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void update(){
        for(int i = 0; i < this.entities.size(); i++){
            entities.get(i).update();
        }
    }

    public void render(Graphics2D g){
        entities.sort(depthSorter);

        for(int i = 0; i < this.entities.size(); i++){
            entities.get(i).render(g);
        }
    }

    public void input(KeyHandler keyHandler, MouseHandler mouseHandler){
        for(int i = 0; i < this.entities.size(); i++){
            entities.get(i).input(keyHandler, mouseHandler);
        }
    }

    public void removeEntity(int index){
        entities.remove(index);
    }

    public void removeEntity(Entity e){
        entities.remove(e);
    }

    public <T extends Entity> void removeEntity(Class<T> clazz){
        for(int i = 0; i < this.entities.size(); i++){
            Entity entity = this.entities.get(i);

            if(entity.getClass().equals(clazz))
                removeEntity(entity);
        }
    }

    public <T extends Entity> boolean containsEntity(Class<T> clazz){
        for(int i = 0; i < this.entities.size(); i++){
            Entity entity = this.entities.get(i);

            if(entity.getClass().equals(clazz))
                return true;
        }

        return false;
    }

    public boolean containsEntity(Entity e){
        return entities.contains(e);
    }

    public void addEntity(Entity e){
        entities.add(e);
    }

    public <T extends Entity> T[] getEntities(Class<T> clazz){
        List<Entity> entities = new ArrayList<>();

        for(int i = 0; i < this.entities.size(); i++){
            Entity entity = this.entities.get(i);

            if(entity.getClass().equals(clazz))
                entities.add(entity);
        }
        return Arrays.copyOf(entities.toArray(), entities.size(), (Class<? extends T[]>) Array.newInstance(clazz, entities.size()).getClass());
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public static EntityManager getManager(String identifier){
        return managers.get(identifier);
    }
}
