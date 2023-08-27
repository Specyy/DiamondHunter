package com.diamondhunter.graphics.font;

import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.net.URI;
import java.net.URL;

public class TextFont extends Font {
    private java.awt.Font font;

    public TextFont(String path) {
        super(path);
    }

    public TextFont(URL path) {
        super(path);
    }

    public TextFont(URI path) {
        super(path);
    }

    @Override
    public void renderText(Graphics2D g, String text, float x, float y) {
        java.awt.Font oldFont = g.getFont();

        g.setFont(font);
        g.drawString(renderPrefix + text + renderSuffix, (int) x, (int) y);

        g.setFont(oldFont);
    }

    @Override
    @Deprecated
    public AABB.Rectangle getStringBounds(String str) {
        return super.getStringBounds(str);
    }

    @Override
    public AABB.Rectangle getStringBounds(Graphics2D g, String str) {
        java.awt.Font oldFont = g.getFont();
        g.setFont(font);

        Rectangle2D r = g.getFontMetrics().getStringBounds(str, g);
        AABB.Rectangle rec = new AABB.Rectangle(new Vector2f(), (float) r.getWidth(), (float) r.getHeight());

        g.setFont(oldFont);
        return rec;
    }

    public void setStyle(int style) {
        font = getFont().deriveFont(style);
    }

    public int getStyle() {
        return getFont().getStyle();
    }

    @Override
    public void setSize(float size) {
        super.setSize(size);
        font = getFont().deriveFont(size);
    }

    @Override
    public float getSize() {
        return font.getSize2D();
    }

    public java.awt.Font getFont() {
        return font;
    }
}
