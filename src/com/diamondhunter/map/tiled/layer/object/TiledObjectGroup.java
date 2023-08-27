package com.diamondhunter.map.tiled.layer.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.diamondhunter.map.tiled.layer.TiledLayer;
import com.diamondhunter.map.tiled.map.TiledMap;
import com.diamondhunter.map.tiled.tileset.TiledTile;
import com.diamondhunter.map.tiled.tileset.TiledTileset;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TiledObjectGroup extends TiledLayer {

    private DrawOrder drawOrder;
    private Set<TiledObject> objects;

    public TiledObjectGroup(TiledMap map, File file, Element layerElement) {
        super(map, Type.OBJECT, file, layerElement);
    }

    @Override
    protected void loadElement(Element layerElement) {
        super.loadElement(layerElement);

        if (layerElement.hasAttribute("draworder")) {
            String drawOrder = layerElement.getAttribute("draworder");

            for (DrawOrder order : DrawOrder.values()) {
                if (order.tiledName.equals(drawOrder)) {
                    this.drawOrder = order;
                    break;
                }
            }
        }

        NodeList objectNodes = layerElement.getElementsByTagName("object");

        for (int i = 0; i < objectNodes.getLength(); i++) {
            Element objectElement = (Element) objectNodes.item(i);
            objects.add(new TiledObject(this, objectElement));
        }
    }

    @Override
    public void clearData() {
        super.clearData();

        if (objects == null)
            objects = ConcurrentHashMap.newKeySet();
    }

    public TiledObject getObjectNear(int x, int y) {
        return getObjectNear(x, y, 1);
    }

    public TiledObject getObjectNear(int x, int y, int radius) {
        Rectangle search = new Rectangle(x, y, radius, radius);

        for (TiledObject object : getObjects()) {
            if (object.getBounds().intersects(search)) {
                return object;
            }
        }

        return null;
    }

    public TiledObject getObject(int x, int y) {
        for (TiledObject object : getObjects()) {
            if (object.getX() == x && object.getY() == y)
                return object;
        }

        return null;
    }

    public TiledObject getObject(int x, int y, int width, int height) {
        for (TiledObject object : getObjects()) {
            if (object.getX() == x && object.getY() == y && object.getWidth() == width && object.getHeight() == height)
                return object;
        }

        return null;
    }

    @Override
    public void render(Graphics2D g, float scale) {
        if (objects != null && !objects.isEmpty()) {
            for (TiledObject object : objects) {
                if (object != null) {
                    Rectangle bounds = object.getBounds();
                    g.drawRect((int)(bounds.x * scale), (int)(bounds.y * scale), (int)(bounds.width * scale), (int)(bounds.height * scale));

                    if (object.getTileRefrenceGid() > 0) {
                        for (int i = 0; i < map.getTilesets().size(); i++) {
                            TiledTileset tileset = map.getTileset(i);

                            if (tileset.containsTile(object.getTileRefrenceGid())) {
                                TiledTile tile = tileset.getTile(object.getTileRefrenceGid());
                                tile.render(g, scale, bounds.x, bounds.y);
                            }
                        }
                    }
                }
            }
        }
    }

    public Set<TiledObject> getObjects() {
        return objects;
    }

    public DrawOrder getDrawOrder() {
        return drawOrder;
    }

    public enum DrawOrder {
        TOPDOWN(0, "topdown"), INDEX(1, "index");

        private final int id;
        private String tiledName;

        DrawOrder(int id, String tiledName) {
            this.id = id;
            this.tiledName = tiledName;
        }

        public int getID() {
            return id;
        }

        public String getTiledName() {
            return tiledName;
        }
    }

}