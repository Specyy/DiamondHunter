package com.diamondhunter.graphics.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window extends JFrame implements WindowListener {

    private int width, height;
    private Display display;
    private boolean minimized;

    public Window(String title) {
        this(title, Toolkit.getDefaultToolkit().getScreenSize());
    }

    public Window(String title, Display display) {
        addWindowListener(this);

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        this.width = display.getWidth();
        this.height = display.getHeight();
        this.display = display;

        setContentPane(display);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Window(String title, int width, int height) {
        this(title, width, height, true);
    }

    public Window(String title, int width, int height, boolean visible) {
        addWindowListener(this);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        this.width = width;
        this.height = height;

        display = new Display(width, height);
        setContentPane(display);
        pack();

        setLocationRelativeTo(null);
        setVisible(visible);
    }

    public Window(String title, Dimension size, boolean visible) {
        addWindowListener(this);

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        this.width = size.width;
        this.height = size.height;

        display = new Display(size.width, size.height);
        setContentPane(display);
        pack();

        setLocationRelativeTo(null);
        setVisible(visible);
    }

    public Window(String title, Dimension size) {
        this(title, size, true);
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
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

    public boolean isMinimized() {
        return minimized;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        minimized = false;
    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {
        minimized = true;
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        minimized = false;
    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
