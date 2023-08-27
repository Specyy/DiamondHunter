package com.diamondhunter.map.tiled.tileset;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.map.tiled.layer.object.TiledObjectGroup;
import com.diamondhunter.map.tiled.map.TiledMap;
import com.diamondhunter.map.tiled.property.TiledPropertiable;
import com.diamondhunter.map.tiled.property.TiledProperties;
import com.diamondhunter.map.tiled.property.TiledProperty;
import com.diamondhunter.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TiledTileset implements TiledPropertiable {

    private final TiledMap map;

    private long firstgid;
    private long lastgid;

    private String name;

    private int tileWidth;
    private int tileHeight;
    private int tileCount;
    private int tileRows;
    private int tileColumns;

    private BufferedImage image;
    private File imagePath;

    private File tilesetFile;

    private TiledProperties properties;

    private Set<TiledTile> tiles;

    public TiledTileset(TiledMap map, long firstgid, Element tilesetElement) {
        this.map = map;
        this.firstgid = firstgid;
        properties = new TiledProperties();
        this.tilesetFile = map.getFile();

        // Tileset is in a different file (a complex tileset)
        if (tilesetElement.getChildNodes().getLength() == 0) {
            try {
                loadTilesetData(new File(this.tilesetFile.getParentFile().getPath() + File.separator
                        + tilesetElement.getAttribute("source")));
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        } else {
            // Tileset does not have a separate file and only has an image as a child
            // element

            loadTilesetElement(tilesetElement);
        }
    }

    private void loadTilesetData(File tilesetFile) throws ParserConfigurationException, SAXException, IOException {
        this.tilesetFile = tilesetFile;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(tilesetFile.getPath());

        Element tilesetElement = doc.getDocumentElement();

        // Load main tileset data
        loadTilesetElement(tilesetElement);

        // Load tileset properties
        NodeList propertiesNodes = tilesetElement.getElementsByTagName("properties");

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
                    value = new File(tilesetFile.getParentFile().getPath() + File.separator + stringValue);
                } else if (type.equals(TiledProperty.Type.FLOAT.getTiledType())) {
                    value = Float.parseFloat(stringValue);
                } else if (type.equals(TiledProperty.Type.INTEGER.getTiledType())) {
                    value = Integer.parseInt(stringValue);
                }

                properties.addProperty(name, value);
            }
        }

        // Load tiles
        NodeList tileNodes = tilesetElement.getElementsByTagName("tile");

        if (tileNodes.getLength() > 0) {
            for (int i = 0; i < tileNodes.getLength(); i++) {
                Element tileElement = (Element) tileNodes.item(i);
                long gid = Long.parseLong(tileElement.getAttribute("id"));
                TiledTile tile = getTile(gid);

                // Load tile properties
                NodeList tilePropertiesNodes = tileElement.getElementsByTagName("properties");

                if (tilePropertiesNodes.getLength() > 0) {
                    Element propertiesElement = (Element) tilePropertiesNodes.item(0);
                    NodeList propertyNodes = propertiesElement.getElementsByTagName("property");

                    for (int j = 0; j < propertyNodes.getLength(); j++) {
                        Element propertyElement = (Element) propertyNodes.item(j);

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
                            value = new File(
                                    tile.getImageFile().getParentFile().getPath() + File.separator + stringValue);
                        } else if (type.equals(TiledProperty.Type.FLOAT.getTiledType())) {
                            value = Float.parseFloat(stringValue);
                        } else if (type.equals(TiledProperty.Type.INTEGER.getTiledType())) {
                            value = Integer.parseInt(stringValue);
                        }

                        tile.getProperties().addProperty(name, value);
                    }
                }

                tile.objectGroup = new TiledObjectGroup(map, tilesetFile,
                        (Element) tileElement.getElementsByTagName("objectgroup").item(0));
            }
        }
    }

    private void loadTilesetElement(Element tilesetElement) {
        // Reset/Initialize data
        clearData();

        // Load data
        name = tilesetElement.getAttribute("name");
        tileWidth = Integer.parseInt(tilesetElement.getAttribute("tilewidth"));
        tileHeight = Integer.parseInt(tilesetElement.getAttribute("tileheight"));
        tileCount = Integer.parseInt(tilesetElement.getAttribute("tilecount"));
        tileColumns = Integer.parseInt(tilesetElement.getAttribute("columns"));

        imagePath = new File(tilesetFile.getParentFile().getPath() + File.separator
                + ((Element) tilesetElement.getElementsByTagName("image").item(0)).getAttribute("source"));

        imagePath = new File("F:\\Users\\alvyn\\Documents\\Diamond Hunter\\out\\production\\Diamond Hunter\\game\\map\\map-tileset-formatted.png");

        try {
            this.image = ImageIO.read(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int tileAmountWidth = image.getWidth() / tileWidth;
        int tileAmountHeight = image.getHeight() / tileHeight;

        this.tileRows = tileAmountHeight;

        this.lastgid = tileAmountWidth * tileAmountHeight + firstgid - 1;

        for (int y = 0; y < tileAmountHeight; y++) {
            for (int x = 0; x < tileAmountWidth; x++) {
                tiles.add(new TiledTile(map, this, firstgid + (x + y * tileAmountWidth), null, new TiledProperties()));
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

        if (tiles == null)
            tiles = ConcurrentHashMap.newKeySet();

        tiles.clear();
    }

    public TiledMap getTiledMap() {
        return map;
    }

    public Set<TiledTile> getTiles() {
        return tiles;
    }

    public boolean containsTile(TiledTile tile) {
        return containsTile(tile.getGid());
    }

    public boolean containsTile(long tileGid) {
        return tileGid >= firstgid && tileGid <= lastgid;
    }

    public TiledTile getTile(long gid) {
        for (TiledTile tile : getTiles()) {
            if (tile.getGid() == gid)
                return tile;
        }

        return null;
    }

    public long getFirstGid() {
        return firstgid;
    }

    public long getLastGid() {
        return lastgid;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileRows() {
        return tileRows;
    }

    public int getTileColumns() {
        return tileColumns;
    }

    public int getTileCount() {
        return tileCount;
    }

    public File getTilesetFile() {
        return tilesetFile;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public File getImageFile() {
        return imagePath;
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

    @Override
    public String toString() {
        return getName();
    }
}