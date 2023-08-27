package com.diamondhunter.map.tiled.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.diamondhunter.map.tiled.layer.TiledLayer;
import com.diamondhunter.map.tiled.layer.TiledTileLayer;
import com.diamondhunter.map.tiled.layer.object.TiledObjectGroup;
import com.diamondhunter.map.tiled.property.TiledPropertiable;
import com.diamondhunter.map.tiled.property.TiledProperties;
import com.diamondhunter.map.tiled.property.TiledProperty;

import com.diamondhunter.map.tiled.tileset.TiledTileset;
import com.diamondhunter.util.loading.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TiledMap implements TiledPropertiable {

    private File file;

    private String version;
    private boolean infinite;

    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;

    private TiledProperties properties;

    private List<TiledTileset> tilesets;
    private List<TiledLayer> layers;

    public TiledMap() {
    }

    public TiledMap(File file) {
        try {
            loadMap(file);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public TiledMap(String path) {
        this(ResourceLoader.retrieveResource(path));
    }

    public TiledMap(URL path) {
        try {
            loadMap(new File(path.toExternalForm()));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public TiledMap(URI path) {
        try {
            loadMap(new File(path));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(URL path) throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        loadMap(path.toURI());
    }

    public void loadMap(URI path) throws ParserConfigurationException, IOException, SAXException {
        loadMap(new File(path));
    }

    public void loadMap(File file) throws ParserConfigurationException, IOException, SAXException {
        this.file = file;

        // Document parsing
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(this.file.getPath());

        // Normalize document
        doc.getDocumentElement().normalize();

        // Initialize data
        clearData();

        // Map attributes
        this.width = Integer.parseInt(doc.getDocumentElement().getAttribute("width"));
        this.height = Integer.parseInt(doc.getDocumentElement().getAttribute("height"));
        this.tileWidth = Integer.parseInt(doc.getDocumentElement().getAttribute("tilewidth"));
        this.tileHeight = Integer.parseInt(doc.getDocumentElement().getAttribute("tileheight"));

        int infiniteInt = Integer.parseInt(doc.getDocumentElement().getAttribute("infinite"));

        if (infiniteInt == 0)
            this.infinite = false;
        else if (infiniteInt == 1)
            this.infinite = true;

        // Load map properties
        NodeList propertiesNodes = doc.getDocumentElement().getElementsByTagName("properties");

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
                    int hexColor = Integer.parseInt(stringValue, 16); // 16 = Base of color (Base 16)
                    value = new Color(hexColor);
                } else if (type.equals(TiledProperty.Type.FILE.getTiledType())) {
                    value = new File(this.file.getParentFile().getPath() + File.separator + stringValue);
                } else if (type.equals(TiledProperty.Type.FLOAT.getTiledType())) {
                    value = Float.parseFloat(stringValue);
                } else if (type.equals(TiledProperty.Type.INTEGER.getTiledType())) {
                    value = Integer.parseInt(stringValue);
                } else {
                    value = stringValue;
                }

                properties.addProperty(name, value);
            }
        }

        // Tilset attributes
        NodeList tilesetList = doc.getElementsByTagName("tileset");

        for (int i = 0; i < tilesetList.getLength(); i++) {
            Node tilesetNode = tilesetList.item(i);
            Element tilesetElement = (Element) tilesetNode;

            // Tileset firstgid
            long firstgid = Long.parseLong(tilesetElement.getAttribute("firstgid"));
            tilesets.add(new TiledTileset(this, firstgid, tilesetElement));
        }

        // Layers

        // Tile layer
        NodeList tileLayerNodes = doc.getElementsByTagName("layer");

        for (int i = 0; i < tileLayerNodes.getLength(); i++) {
            layers.add(new TiledTileLayer(this, file, (Element) tileLayerNodes.item(i)));
        }

        // Object layer
        NodeList objectLayerNodes = doc.getElementsByTagName("objectgroup");

        for (int i = 0; i < objectLayerNodes.getLength(); i++) {
            layers.add(new TiledObjectGroup(this, file, (Element) objectLayerNodes.item(i)));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void clearData() {
        if (properties == null)
            properties = new TiledProperties();

        if (tilesets == null)
            tilesets = new LinkedList<>();

        if (layers == null)
            layers = new LinkedList<>();

        // Data clearing
        tilesets.clear();
        layers.clear();
    }

    public void render(Graphics2D g, float scale, Class<? extends TiledLayer>... layerClasses) {
        TiledLayer[] layers = getLayers(layerClasses);

        for (int i = 0; i < layers.length; i++) {
            layers[i].render(g, scale);
        }
    }

    public void render(Graphics2D g, Class<? extends TiledLayer>... layerClasses) {
        render(g, 1, layerClasses);
    }

    public TiledLayer[] getLayers(Class<? extends TiledLayer>... layerClasses) {
        List<TiledLayer> layers = new LinkedList<>();

        for (int i = 0; i < getLayers().size(); i++) {
            TiledLayer layer = getLayer(i);

            for (int j = 0; j < layerClasses.length; j++) {
                if (layerClasses[j].isAssignableFrom(layer.getClass()))
                    layers.add(layer);
            }
        }

        return layers.toArray(new TiledLayer[0]);
    }

    public TiledLayer getLayer(int index) {
        return getLayers().get(index);
    }

    public TiledLayer getLayer(String name) {
        for (int i = 0; i < getLayers().size(); i++) {
            TiledLayer layer = getLayer(i);

            if (layer.getName() != null && layer.getName().equals(name))
                return layer;
        }

        return null;
    }

    public List<TiledLayer> getLayers() {
        return layers;
    }

    public TiledTileset getTileset(int index) {
        return getTilesets().get(index);
    }

    public TiledTileset getTileset(int firstgid, int lastgid) {
        for (int i = 0; i < getTilesets().size(); i++) {
            TiledTileset tileset = getTilesets().get(i);
            if (tileset.getFirstGid() == firstgid && tileset.getLastGid() == lastgid)
                return tileset;
        }

        return null;
    }

    public TiledTileset getTilesetByName(String name) {
        return getTilesetByName(name, false);
    }

    public TiledTileset getTilesetByName(String name, boolean ignoreCase) {
        for (int i = 0; i < getTilesets().size(); i++) {
            TiledTileset tileset = getTilesets().get(i);

            if (ignoreCase) {
                if (tileset.getName().equalsIgnoreCase(name))
                    return tileset;
            } else {
                if (tileset.getName().equals(name))
                    return tileset;
            }
        }

        return null;
    }

    public List<TiledTileset> getTilesets() {
        return tilesets;
    }

    public File getFile() {
        return file;
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
}
