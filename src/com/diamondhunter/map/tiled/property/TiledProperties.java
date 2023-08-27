package com.diamondhunter.map.tiled.property;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TiledProperties {
    private Set<TiledProperty> properties;

    public TiledProperties() {
        properties = ConcurrentHashMap.newKeySet();
    }

    public TiledProperties(TiledProperty initialProperty) {
        this();
        addProperty(initialProperty);
    }

    public TiledProperties(TiledProperties initialProperties) {
        this();
        addProperties(initialProperties);
    }

    public TiledProperty addProperty(String key, Object value) {
        TiledProperty property = new TiledProperty(key, value);

        if (properties.contains(property))
            return null;

        addProperty(property);

        return property;
    }

    public boolean addProperty(TiledProperty property) {
        return properties.add(property);
    }

    public TiledProperty getProperty(String key, TiledProperty defaultValue) {
        for (TiledProperty property : properties) {
            if (property.getKey().equals(key))
                return property;
        }

        return defaultValue;
    }

    public Object getProperty(String key, Object defaultValue) {
        for (TiledProperty property : properties) {
            if (property.getKey().equals(key))
                return property;
        }

        return defaultValue;
    }

    public Object getProperty(String key) {
        return getProperty(key, null).getValue();
    }

    public TiledProperty getPropertyObject(String key) {
        return getProperty(key, (TiledProperty) null);
    }

    public boolean addProperties(TiledProperties properties) {
        return this.properties.addAll(properties.properties);
    }

    public boolean addProperties(Map<String, Object> properties) {
        boolean result = false;

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            result |= addProperty(entry.getKey(), entry.getValue()) != null;
        }

        return result;
    }

    public boolean addProperties(Collection<? extends TiledProperty> properties) {
        return this.properties.addAll(properties);
    }

    public Set<Object> getPropertyValues() {
        Set<Object> keys = new LinkedHashSet<>();

        for (TiledProperty property : properties) {
            keys.add(property.getValue());
        }

        return keys;
    }

    public boolean removeProperty(String propertyKey) {
        for (TiledProperty property : properties) {
            if (property.getKey().equals(propertyKey)) {
                return removeProperty(property);
            }
        }

        return false;
    }

    public boolean removeProperty(TiledProperty property) {
        return properties.remove(property);
    }

    public void clearProperties() {
        properties.clear();
    }

    public boolean replaceProperty(TiledProperty property, TiledProperty other, boolean createIfNoExists) {
        boolean removed, added;

        if (createIfNoExists) {
            removeProperty(property);
            added = addProperty(other);

            removed = true;
        } else {
            removed = removeProperty(property);
            added = addProperty(other);
        }

        return removed && added;
    }

    public boolean replaceProperty(TiledProperty property, TiledProperty other) {
        return replaceProperty(property, other, true);
    }

    public Set<String> getPropertyKeys() {
        Set<String> keys = new LinkedHashSet<>();

        for (TiledProperty property : properties) {
            keys.add(property.getKey());
        }
        return keys;
    }

    public Iterator<TiledProperty> getIterator() {
        return properties.iterator();
    }

    public int getPropertyCount() {
        return properties.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Collection) {
            return properties.equals(obj);
        } else if (obj instanceof TiledPropertiable) {
            return equals((TiledProperties) ((TiledPropertiable) obj).getProperties());
        } else if (obj instanceof TiledProperties) {
            return equals(((TiledProperties) obj).properties);
        }

        return super.equals(obj);
    }

    @Override
    public String toString(){
        return properties.toString();
    }
}
