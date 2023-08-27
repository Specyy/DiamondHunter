package com.diamondhunter.map.tiled.layer.object;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.util.regex.Pattern;

import com.diamondhunter.map.tiled.map.TiledMap;
import com.diamondhunter.map.tiled.property.TiledPropertiable;
import com.diamondhunter.map.tiled.property.TiledProperties;
import com.diamondhunter.map.tiled.property.TiledProperty;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TiledObject implements TiledPropertiable {
    // Unique ID of the object. Each object that is placed on a map gets a unique
    // id. Even if an object was deleted, no object gets the same ID. Can not be
    // changed in Tiled. (since Tiled 0.11)
    private final long id;

    // The name of the object. An arbitrary string.
    private String name;

    // The type of the object. An arbitrary string.
    private String type;

    // The x coordinate of the object in pixels.
    private int x;

    // The y coordinate of the object in pixels.
    private int y;

    // The width of the object in pixels (defaults to 0).
    private int width;

    // The height of the object in pixels (defaults to 0).
    private int height;

    // A reference to a tile (optional).
    private long gid;

    // Whether the object is shown (1) or hidden (0). Defaults to 1.
    private boolean visible;

    // The object group of this object
    private TiledObjectGroup group;

    // The element where the data is located
    private Element objectElement;

    // The properties of this object
    private TiledProperties properties;

    public TiledObject(TiledObjectGroup group, Element objectElement) {
        if (objectElement.hasAttribute("id"))
            id = Long.parseLong(objectElement.getAttribute("id"));
        else
            id = -1;

        this.group = group;
        loadElement(objectElement);
    }

    private void loadElement(Element objectElement) {
        this.objectElement = objectElement;

        // Reset/Initialize data
        clearData();

        // Load object data
        if (objectElement.hasAttribute("name"))
            name = objectElement.getAttribute("name");

        if (objectElement.hasAttribute("type"))
            type = objectElement.getAttribute("type");

        if (objectElement.hasAttribute("type"))
            type = objectElement.getAttribute("type");

        x = Integer.parseInt(objectElement.getAttribute("x"));
        y = Integer.parseInt(objectElement.getAttribute("y"));

        if (objectElement.hasAttribute("width"))
            width = Integer.parseInt(objectElement.getAttribute("width"));
        if (objectElement.hasAttribute("height"))
            height = Integer.parseInt(objectElement.getAttribute("height"));

        int visibleInt = objectElement.hasAttribute("visible") ? Integer.parseInt(objectElement.getAttribute("visible"))
                : 1;

        if (visibleInt == 0)
            visible = false;
        else if (visibleInt == 1)
            visible = true;

        if (objectElement.hasAttribute("gid"))
            gid = Long.parseLong(objectElement.getAttribute("gid"));

        // Load object properties
        NodeList propertiesNodes = objectElement.getElementsByTagName("properties");

        if (propertiesNodes.getLength() > 0) {
            Element propertiesElement = (Element) propertiesNodes.item(0);
            NodeList propertyNodes = propertiesElement.getElementsByTagName("property");

            for (int i = 0; i < propertyNodes.getLength(); i++) {
                Element propertyElement = (Element) propertyNodes.item(i);

                String name = propertyElement.getAttribute("name");
                String type = null;

                if (propertyElement.hasAttribute("type"))
                    type = propertyElement.getAttribute("type");

                String stringValue = propertyElement.getAttribute("value");

                Object value = null;

                // Check property type
                if (type == TiledProperty.Type.STRING.getTiledType()) {
                    value = stringValue;
                } else if (type.equals(TiledProperty.Type.BOOLEAN.getTiledType())) {
                    value = Boolean.parseBoolean(stringValue);
                } else if (type.equals(TiledProperty.Type.COLOR.getTiledType())) {
                    stringValue = stringValue.replaceFirst(Pattern.quote("#ff"), "");
                    int hexColor = Integer.parseInt(stringValue, 16);
                    value = new Color(hexColor);
                } else if (type.equals(TiledProperty.Type.FILE.getTiledType())) {
                    value = new File(group.getFile().getParentFile().getPath() + File.separator + stringValue);
                } else if (type.equals(TiledProperty.Type.FLOAT.getTiledType())) {
                    value = Float.parseFloat(stringValue);
                } else if (type.equals(TiledProperty.Type.INTEGER.getTiledType())) {
                    value = Integer.parseInt(stringValue);
                }

                properties.addProperty(name, value);
            }
        }
    }

    public Element getObjectElement() {
        return objectElement;
    }

    public TiledMap getTiledMap() {
        return getObjectGroup().getTiledMap();
    }

    public TiledObjectGroup getObjectGroup() {
        return group;
    }

    public boolean isVisible() {
        return visible;
    }

    public long getTileRefrenceGid() {
        return gid;
    }

    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }

    public int getHeight() {
        return height;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public void setProperties(TiledProperties properties) {
        this.properties = properties;
    }

    @Override
    public TiledProperties getProperties() {
        return properties;
    }

    @Override
    public void clearData() {
        if (properties == null)
            properties = new TiledProperties();
        properties.clearProperties();
    }

    @Override
    public String toString() {
        return getName();
    }
}