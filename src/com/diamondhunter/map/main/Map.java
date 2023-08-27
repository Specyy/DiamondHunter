package com.diamondhunter.map.main;

import com.diamondhunter.map.tiled.map.TiledMap;
import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;
import com.diamondhunter.util.loading.ResourceLoader;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.*;
import java.net.URL;

public class Map {

    /**
     * The map to be used for this {@code Map} class parsed as a
     * {@link TiledMap} object.
     */
    private TiledMap map;

    /**
     * The position of the map on the screen. This can also be accessed
     * by using {@link Vector2f#getWorldVar() getWorldVar()}. This variable
     * is what changes the position of every entity on the screen.
     *
     * @see Vector2f#getWorldVar()
     */
    private Vector2f pos;

	/**
	 * Creates a map with
	 * @param path
	 */
	public Map(String path) {
        this(ResourceLoader.retrieveResource(path));
    }

    public Map(URL path) {
    	// Load the map
        map = new TiledMap(path);

        // Assign this world var to the current one
        pos = Vector2f.getWorldVar();
    }

    public void input(KeyHandler keyHandler, MouseHandler mouseHandler) {

    }

    public void render(Graphics2D g) {
        map.render(g);
    }

    public void update() {

    }

	/**
	 * Returns the map used for this {@code Map} class parsed as a
	 * {@link TiledMap} object.
	 *
	 * @return Return the {@link TiledMap} for this class
	 * @see #map
	 */
	public TiledMap getTiledMap() {
        return map;
    }
}
