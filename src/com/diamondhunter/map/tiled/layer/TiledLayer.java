package com.diamondhunter.map.tiled.layer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.regex.Pattern;

import com.diamondhunter.map.tiled.map.TiledMap;
import com.diamondhunter.map.tiled.property.TiledPropertiable;
import com.diamondhunter.map.tiled.property.TiledProperties;
import com.diamondhunter.map.tiled.property.TiledProperty;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class TiledLayer implements TiledPropertiable {
    // ID of this layer
    protected final long id;

    // X coord in tiles
    protected int x;

    // Y coord in tiles
    protected int y;

    // Width of the layer
    protected int width;

    // Height of the layer
    protected int height;

    // Opacity of the layer
    protected float opacity;

    // Whether the object is shown (1) or hidden (0). Defaults to 1.
    protected boolean visible;

    // The name of the layer
    protected String name;

    // The element data
    protected Element layerElement;

    // The properties of this layer
    protected TiledProperties properties;

    // The map
    protected TiledMap map;

    // The layer type
    protected final Type type;

    // The file of this layer
    protected File file;

    public TiledLayer(TiledMap map, Type type, File file, Element layerElement) {
        this.type = type;
        this.map = map;
        this.file = file;
        this.layerElement = layerElement;
        if (layerElement != null)
            this.id = layerElement.hasAttribute("id") && layerElement != null
                    ? Long.parseLong(layerElement.getAttribute("id"))
                    : -1;
        else
            id = -1;

        if (layerElement != null)
            loadElement(layerElement);
    }

    // Render the layer
    public abstract void render(Graphics2D g, float scale);

    public void render(Graphics2D g) {
        render(g, 1);
    }

    protected void loadElement(Element layerElement) {
        this.layerElement = layerElement;

        // Reset/Initialize data
        clearData();

        if (layerElement.hasAttribute("x"))
            this.x = Integer.parseInt(layerElement.getAttribute("x"));

        if (layerElement.hasAttribute("y"))
            this.y = Integer.parseInt(layerElement.getAttribute("y"));

        if (layerElement.hasAttribute("width"))
            this.width = Integer.parseInt(layerElement.getAttribute("width"));

        if (layerElement.hasAttribute("height"))
            this.height = Integer.parseInt(layerElement.getAttribute("height"));

        if (layerElement.hasAttribute("name"))
            name = layerElement.getAttribute("name");
        else
            name = null;

        if (layerElement.hasAttribute("opacity"))
            this.opacity = Float.parseFloat(layerElement.getAttribute("opacity"));
        else
            this.opacity = 1;

        int visibleInt = layerElement.hasAttribute("visible") ? Integer.parseInt(layerElement.getAttribute("visible"))
                : 1;

        if (visibleInt == 0)
            visible = false;
        else if (visibleInt == 1)
            visible = true;

        // Load layer properties
        NodeList propertiesNodes = layerElement.getElementsByTagName("properties");

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
                    value = new File(file.getParentFile().getPath() + File.separator + stringValue);
                } else if (type.equals(TiledProperty.Type.FLOAT.getTiledType())) {
                    value = Float.parseFloat(stringValue);
                } else if (type.equals(TiledProperty.Type.INTEGER.getTiledType())) {
                    value = Integer.parseInt(stringValue);
                }

                properties.addProperty(name, value);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearData() {
        if (properties == null)
            properties = new TiledProperties();
        properties.clearProperties();
    }

    public TiledMap getTiledMap() {
        return map;
    }

    public File getFile() {
        return file;
    }

    public long getID() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName(){
        return name;
    }

    public float getOpcacity() {
        return opacity;
    }

    public boolean isVisible() {
        return visible;
    }

    public Type getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProperties(TiledProperties properties) {
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TiledProperties getProperties() {
        return properties;
    }

    public enum Type {
        TILE(0, "Tile Layer"), OBJECT(1, "Object Layer"), IMAGE(2, "Image Layer");

        private final int tiledId;
        private String tiledName;

        Type(int tiledId, String tiledName) {
            this.tiledId = tiledId;
            this.tiledName = tiledName;
        }

        public int getTiledID() {
            return tiledId;
        }

        public String getTiledName() {
            return tiledName;
        }
    }
}