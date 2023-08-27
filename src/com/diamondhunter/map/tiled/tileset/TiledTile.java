package com.diamondhunter.map.tiled.tileset;

import com.diamondhunter.map.tiled.layer.object.TiledObjectGroup;
import com.diamondhunter.map.tiled.map.TiledMap;
import com.diamondhunter.map.tiled.property.TiledPropertiable;
import com.diamondhunter.map.tiled.property.TiledProperties;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class TiledTile implements TiledPropertiable {
    private TiledMap map;
    private TiledTileset tileset;

    private final long gid;

    private TiledProperties properties;

    private BufferedImage image;

    TiledObjectGroup objectGroup;

    public TiledTile(TiledMap map, TiledTileset tileset, long gid, TiledObjectGroup objectGroup,
            TiledProperties properties) {
        this.map = map;
        this.tileset = tileset;
        this.gid = gid;
        this.objectGroup = objectGroup;
        int intGid = (int) (gid - 1);

        image = tileset.getImage().getSubimage(intGid % tileset.getTileColumns() * tileset.getTileWidth(),
                intGid / tileset.getTileColumns() * tileset.getTileHeight(), tileset.getTileWidth(),
                tileset.getTileHeight());
        clearData();
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

    public void render(Graphics2D g, int x, int y) {
        render(g, 1, x, y);
    }

    public void render(Graphics2D g, float scale, int x, int y) {
        g.drawImage(image, (int)(x * scale), (int)(y * scale), (int)(image.getWidth() * scale), (int)(image.getHeight() * scale), null);
    }

    public TiledMap getTiledMap() {
        return map;
    }

    public TiledTileset getTiledTileset() {
        return tileset;
    }

    public File getImageFile() {
        return tileset.getImageFile();
    }

    public BufferedImage getImage() {
        return image;
    }

    public long getGid() {
        return gid;
    }

    public TiledObjectGroup getObjectGroup() {
        return objectGroup;
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
    public boolean equals(Object obj) {
        if (obj instanceof Number) {
            return gid == ((Number) obj).longValue();
        } else if (obj instanceof BufferedImage) {
            return obj.equals(image);
        } else if (obj instanceof TiledTile) {
            TiledTile objTile = (TiledTile) obj;
            return gid == objTile.gid && map == objTile.map && tileset == objTile.tileset && image == objTile.image
                    && properties.equals(objTile.properties);
        }

        return false;
    }
}