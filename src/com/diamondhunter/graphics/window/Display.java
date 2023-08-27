package com.diamondhunter.graphics.window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Display extends JPanel {

    private int width, height;

    private BufferedImage display;
    private Graphics2D displayGraphics;

    private boolean isReady = false;

    public Display() {
        this(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public Display(int width, int height) {
        this(new Dimension(width, height));
    }

    public Display(Dimension size) {
        setDoubleBuffered(true);
        setSize(size);
        setFocusable(true);

        display = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        displayGraphics = display.createGraphics();
        requestFocus();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        isReady = true;
    }

    public boolean isReady() {
        return isReady;
    }

    @Deprecated
    public Graphics2D getDirectGraphics() {
        return (Graphics2D) super.getGraphics();
    }

    public Graphics2D getGraphics() {
        return displayGraphics;
    }

    public void update() {
        getDirectGraphics().drawImage(display, 0, 0, width, height, null);
        displayGraphics.clearRect(0, 0, width, height);
        getDirectGraphics().dispose();
    }

    @Override
    public void setBounds(Rectangle r) {
        try {
            setWidth(r.width);
            setHeight(r.height);
            super.setBounds(r);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        try {
            setWidth(width);
            setHeight(height);
            super.setBounds(x, y, width, height);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public void setSize(int width, int height) {
        try {
            setWidth(width);
            setHeight(height);
            super.setSize(width, height);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public void setSize(Dimension d) {
        try {
            setWidth(d.width);
            setHeight(d.height);
            super.setSize(d);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width < 1)
            throw new IllegalArgumentException("Cannot set width to " + width);

        if (this.width == width) return;

        this.width = width;

        Dimension size = new Dimension(this.width, height);

        setSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height < 1)
            throw new IllegalArgumentException("Cannot set height to " + height);

        if (this.height == height) return;

        this.height = height;

        Dimension size = new Dimension(width, this.height);

        setSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    // TODO Create display with size dimensions
    // TODO Set doubled buffered
    // TODO Create image to draw on
    // TODO Test drawing
}
