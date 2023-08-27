package com.diamondhunter.graphics.font;

import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.loading.ResourceLoader;
import com.diamondhunter.util.logging.Logger;
import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.Graphics2D;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class Font {
	protected String renderPrefix;
	protected String renderSuffix;

	protected float size;

	protected File path;

	public Font(String path) {
		this(ResourceLoader.retrieveResource(path));
	}

	public Font(URL path) {
		this.path = new File(path.toExternalForm());
	}

	public Font(URI path) {
		this.path = new File(path);
	}

	public abstract void renderText(Graphics2D g, String text, float x, float y);

	public void renderText(Graphics2D g, String text, Vector2f pos) {
		renderText(g, text, pos.getPosition().x, pos.getPosition().y);
	}

	public AABB.Rectangle getStringBounds(String str) {
		return getStringBounds(null, str);
	}

	public abstract AABB.Rectangle getStringBounds(Graphics2D g, String str);

	public void setSize(float size) {
		this.size = size;
	}

	public float getSize() {
		return size;
	}

	public void setRenderPrefix(String renderPrefix) {
		this.renderPrefix = renderPrefix;
	}

	public String getRenderPrefix() {
		return renderPrefix;
	}

	public void setRenderSuffix(String renderSuffix) {
		this.renderSuffix = renderSuffix;
	}

	public String getRenderSuffix() {
		return renderSuffix;
	}
}
