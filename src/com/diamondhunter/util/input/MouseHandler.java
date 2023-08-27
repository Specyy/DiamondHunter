package com.diamondhunter.util.input;

import com.diamondhunter.graphics.window.Display;
import com.diamondhunter.graphics.window.Window;
import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class MouseHandler implements MouseListener, MouseMotionListener {

    // Possible Mouse Buttons
    public static final int NO_BUTTON = -1;
    public static final int LEFT_BUTTON = 0;
    public static final int MIDDLE_BUTTON = 1;
    public static final int RIGHT_BUTTON = 2;
    public static final int BACK_MOUSE_BUTTON = 3;
    public static final int FRONT_MOUSE_BUTTON = 4;

    private Component component;
    private Mouse mouse;

    private List<MouseListener> subListeners;

    private int buttonReleaseCounter = 0;

    public MouseHandler(DiamondHunter diamondHunter) {
        this(diamondHunter.getWindow());
    }

    public MouseHandler(Window window) {
        this(window.getDisplay());
    }

    public MouseHandler(Display display) {
        this.component = display;
        createListener();
        subListeners = new ArrayList<>();
        mouse = new Mouse(this);
    }

    private void createListener() {
        MouseListener[] mouselisteners = component.getMouseListeners();
        boolean containsMouseListener = false;

        for (int i = 0; i < mouselisteners.length; i++) {
            if (mouselisteners[i] == this) {
                containsMouseListener = true;
                break;
            }
        }

        if (!containsMouseListener)
            component.addMouseListener(this);

        MouseMotionListener[] mouseMotionListeners = component.getMouseMotionListeners();
        boolean containsMouseMotionListener = false;

        for (int i = 0; i < mouseMotionListeners.length; i++) {
            if (mouseMotionListeners[i] == this) {
                containsMouseMotionListener = true;
                break;
            }
        }

        if (!containsMouseMotionListener)
            component.addMouseMotionListener(this);
    }

    public void dispose() {
        MouseListener[] mouselisteners = component.getMouseListeners();
        boolean containsMouseListener = false;

        for (int i = 0; i < mouselisteners.length; i++) {
            if (mouselisteners[i] == this) {
                containsMouseListener = true;
                break;
            }
        }

        if (containsMouseListener)
            component.removeMouseListener(this);

        MouseMotionListener[] mouseMotionListeners = component.getMouseMotionListeners();
        boolean containsMouseMotionListener = false;

        for (int i = 0; i < mouseMotionListeners.length; i++) {
            if (mouseMotionListeners[i] == this) {
                containsMouseMotionListener = true;
                break;
            }
        }

        if (containsMouseMotionListener)
            component.removeMouseMotionListener(this);
    }

    public synchronized void addSubListener(MouseListener listener) {
        subListeners.add(listener);
    }

    public synchronized void removeSubListener(MouseListener listener) {
        subListeners.remove(listener);
    }

    public List<MouseListener> getSubListeners() {
        return subListeners;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        if (component == null) return;

        dispose();
        this.component = component;
        createListener();
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void update() {
        /*
         * This update method if used to toggle off values (such as if the mouse is)
         * being pressed or released) after one frame because those values should not
         * be valid for more than 1 frame.
         */

        if (mouse.pressed || mouse.released || mouse.clicked || mouse.moved)
            buttonReleaseCounter++;

        if (buttonReleaseCounter >= 1) {
            mouse.pressed = false;
            mouse.released = false;
            mouse.clicked = false;
            mouse.moved = false;
            buttonReleaseCounter = 0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Check if component is focused
        if(!component.hasFocus()) return;

        try {
            // Only update the mouse position
            mouse.position.x = e.getPoint().x;
            mouse.position.y = e.getPoint().y;

            // Update button press
            mouse.clicked = true;
            mouse.pressed = false;
            mouse.held = false;
            mouse.entered = false;
            mouse.exited = false;
            mouse.dragged = false;
            mouse.moved = false;
        } catch (Exception ignored) {
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Check if component is focused
        if(!component.hasFocus()) return;

        try {
            // Only update the mouse position
            mouse.position.x = e.getPoint().x;
            mouse.position.y = e.getPoint().y;

            // Update button press
            mouse.pressed = true;
            mouse.clicked = false;
            mouse.held = true;
            mouse.released = false;
            mouse.entered = false;
            mouse.exited = false;
            mouse.dragged = false;
            mouse.moved = false;
        } catch (Exception ignored) {
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Check if component is focused
        if(!component.hasFocus()) return;

        try {
            // Only update the mouse position
            mouse.position.x = e.getPoint().x;
            mouse.position.y = e.getPoint().y;

            // Update button press
            mouse.released = true;
            mouse.pressed = false;
            mouse.clicked = false;
            mouse.held = false;
            mouse.entered = false;
            mouse.exited = false;
            mouse.dragged = false;
            mouse.moved = false;
        } catch (Exception ignored) {
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Check if component is focused
        if(!component.hasFocus()) return;

        try {
            // Only update the mouse position
            mouse.position.x = e.getPoint().x;
            mouse.position.y = e.getPoint().y;

            // Update button press
            mouse.entered = true;
            mouse.pressed = false;
            mouse.clicked = false;
            mouse.held = false;
            mouse.released = false;
            mouse.exited = false;
            mouse.dragged = false;
            mouse.moved = false;
        } catch (Exception ignored) {
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Check if component is focused
        if(!component.hasFocus()) return;

        try {
            // Only update the mouse position
            mouse.position.x = e.getPoint().x;
            mouse.position.y = e.getPoint().y;

            // Update button press
            mouse.exited = true;
            mouse.entered = false;
            mouse.pressed = false;
            mouse.clicked = false;
            mouse.held = false;
            mouse.released = false;
            mouse.dragged = false;
            mouse.moved = false;
        } catch (Exception ignored) {
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Check if component is focused
        if(!component.hasFocus()) return;

        try {
            // Only update the mouse position
            mouse.position.x = e.getPoint().x;
            mouse.position.y = e.getPoint().y;

            // Update button press
            mouse.dragged = true;
            mouse.exited = false;
            mouse.entered = false;
            mouse.pressed = false;
            mouse.clicked = false;
            mouse.held = false;
            mouse.released = false;
            mouse.moved = false;
        } catch (Exception ignored) {
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        // Check if component is focused
        if(!component.hasFocus()) return;

        try {
            // Only update the mouse position
            mouse.position.x = e.getPoint().x;
            mouse.position.y = e.getPoint().y;

            // Update button press
            mouse.moved = true;
            mouse.dragged = false;
            mouse.exited = false;
            mouse.entered = false;
            mouse.pressed = false;
            mouse.clicked = false;
            mouse.held = false;
            mouse.released = false;
        } catch (Exception ignored) {
        }
    }

    public static class Mouse {

        private Vector2f position, screenPosition;
        private MouseHandler mouseHandler;

        private boolean pressed, held, released, clicked, dragged, moved, exited, entered;

        private Mouse(MouseHandler mouseHandler) {
            this.mouseHandler = mouseHandler;
            this.position = new Vector2f();
            screenPosition = new Vector2f((float) MouseInfo.getPointerInfo().getLocation().getX(), (float) MouseInfo.getPointerInfo().getLocation().getY());
        }

        public Vector2f getPosition() {
            return position;
        }

        public float getX() {
            return position.x;
        }

        public float getY() {
            return position.y;
        }

        public Vector2f getScreenPosition() {
            screenPosition.x = (float) MouseInfo.getPointerInfo().getLocation().getX();
            screenPosition.y = (float) MouseInfo.getPointerInfo().getLocation().getY();

            return screenPosition;
        }

        public float getScreenX() {
            return getScreenPosition().getPosition().x;
        }

        public float getScreenY() {
            return getScreenPosition().getPosition().y;
        }

        public boolean isPressed() {
            return pressed;
        }

        public boolean isHeld() {
            return held;
        }

        public boolean isReleased() {
            return released;
        }

        public boolean isClicked() {
            return clicked;
        }

        public boolean isDragged() {
            return dragged;
        }

        public boolean isMoved() {
            return moved;
        }

        public boolean isExited() {
            return exited;
        }

        public boolean isEntered() {
            return entered;
        }

        public MouseHandler getMouseHandler() {
            return mouseHandler;
        }
    }
}
