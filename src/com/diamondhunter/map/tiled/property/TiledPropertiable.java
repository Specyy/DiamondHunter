package com.diamondhunter.map.tiled.property;

import com.diamondhunter.map.tiled.map.TiledMap;
import com.diamondhunter.map.tiled.tileset.TiledTileset;

/**
 * A {@code TiledPropertiable} is a Tiled object that has any sort of properties
 * attach to itself. In terms of the Tiled Map Editor, a Tile, Object, Map,
 * Image or Tilset all have properties of their own.A {@link TiledMap}, and
 * {@link TiledTileset} are two classes that how properties attached to them.
 */
public interface TiledPropertiable {
    /**
     * Sets the properties for this propertiable instance to a given
     * {@link TiledProperties} object. This instance will then assign the property
     * values from the given object to its own property values.
     * 
     * @param properties The {@link TiledProperties} to assign this instance's
     *                   properties values to.
     */
    void setProperties(TiledProperties properties);

    /**
     * Returns the assigned properties for this propertiable object. If this object
     * does not have any properties, there list of properties will be empty. To
     * assign properties to this object, use
     * {@link #setProperties(TiledProperties)}.
     * 
     * @return Returns the assigned properties for this propertiable object.
     */
    TiledProperties getProperties();

    /**
     * Clears the data of this given {@code TiledPropertiable} object. This data
     * does not necessarily need to be property data and can also be other types of
     * data such as image data or tile data.
     */
    void clearData();
}
