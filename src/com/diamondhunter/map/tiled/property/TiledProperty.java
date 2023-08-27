package com.diamondhunter.map.tiled.property;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class TiledProperty {

    private String key;
    private Object value;
    private Type type;

    public TiledProperty(String key) {
        this.key = key;
        updateType();
    }

    public TiledProperty(Map<String, Object> properties) {
        if (properties.size() > 1)
            throw new IllegalArgumentException("Cannot convert map: " + properties + " to a "
                    + getClass().getCanonicalName() + " because it is to large!");
        else {
            String key = properties.keySet().iterator().next();
            Object value = properties.get(key);

            this.key = key;
            this.value = value;

            updateType();
        }
    }

    public TiledProperty(String key, Object value) {
        if (key == null)
            throw new IllegalArgumentException("A " + getClass().getCanonicalName() + " does not support null keys!");
        else {
            this.key = key;
            this.value = value;

            updateType();
        }
    }

    private void updateType() {
        if (value == null) {
            type = null;
            return;
        }

        for (Type type : Type.values()) {
            if (type.getReferenceClass().equals(value.getClass())) {
                setType(type);
                break;
            }
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        if (key == null)
            throw new IllegalArgumentException("A " + getClass().getCanonicalName() + " does not support null keys!");
        else
            this.key = key;
    }

    public void setValue(Object value) {
        this.value = value;
        updateType();
    }

    public Object getValue() {
        return value;
    }

    @Deprecated
    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TiledProperty) {
            return key.equals(((TiledProperty) obj).key) && value.equals(((TiledProperty) obj).value);
        } else if (obj instanceof Properties) {
            if (((Properties) obj).size() > 1)
                return false;
            String key = ((Properties) obj).propertyNames().nextElement().toString();
            Object value = ((Properties) obj).get(key);

            return this.key.equals(key) && this.value.equals(value);
        } else if (obj instanceof Map) {
            if (((Map) obj).size() > 1)
                return false;

            String key = ((Map) obj).keySet().iterator().next().toString();
            Object value = ((Map) obj).get(key);

            return this.key.equals(key) && this.value.equals(value);
        }

        return Objects.equals(obj, value);
    }

    public String toString() {
        return key + "=" + value;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public enum Type {
        BOOLEAN(0, "bool", Boolean.class, true, false), COLOR(1, "color", Color.class, "#", "0x", "*,*,*"),
        FLOAT(2, "float", Float.class, "f", "F", ".", "-?\\\\d+(\\\\.\\\\d+)?"),
        FILE(3, "file", File.class, "../", "c:", "C:", "D:", "E:", "/Desktop", "/Users", "/Documents", ".txt", ".jar",
                ".yaml", ".yml", ".bat", ".properties", ".xml", ".png", ".jpeg", ".java", ".iml", ".class", ".tmx",
                ".tsx", ".js", ".html", ".json"),
        INTEGER(4, "int", Integer.class, "%d", "-?\\\\d+"), STRING(5, null, String.class, BOOLEAN.identifiers,
                COLOR.identifiers, FLOAT.identifiers, FILE.identifiers, INTEGER.identifiers);

        /**
         * Abstract type identifiers; should not be used to define a {@code Type}
         */
        private Object[] identifiers;
        private String tiledType;
        private Class<?> referenceClass;
        private final int tiledID;

        Type(int id, String tiledType, Class<?> referenceClass, Object... identifiers) {
            this.tiledID = id;
            this.referenceClass = referenceClass;
            this.tiledType = tiledType;
            this.identifiers = identifiers;
        }

        @Deprecated
        public void setTiledType(String tiledType) {
            this.tiledType = tiledType;
        }

        public String getTiledType() {
            return tiledType;
        }

        public int getTiledID() {
            return tiledID;
        }

        public void setIdentifiers(Object[] identifiers) {
            this.identifiers = identifiers;
        }

        public Object[] getIdentifiers() {
            return identifiers;
        }

        public void setReferenceClass(Class<?> referenceClass) {
            this.referenceClass = referenceClass;
        }

        public Class<?> getReferenceClass() {
            return referenceClass;
        }

        public boolean containsArrayIdentifiers() {
            if (identifiers == null)
                return false;

            for (int i = 0; i < identifiers.length; i++) {
                if (identifiers[i].getClass().isArray() || identifiers[0] instanceof Object[])
                    return true;
            }

            return false;
        }
    }
}
