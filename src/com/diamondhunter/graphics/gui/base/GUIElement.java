package com.diamondhunter.graphics.gui.base;

import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;
import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GUIElement {
    protected Vector2f pos;
    protected AABB bounds;
    protected Sprite sprite;

    protected Set<GUIElementListener> listeners;

    protected int renderWidthScale = 1;
    protected int renderHeightScale = 1;

    private boolean entered = false;
    private boolean exited = false;
    private boolean dragged = false;

    public GUIElement(Vector2f position) {
        this(position, new AABB.Rectangle(position, 1), null);
    }

    public GUIElement(Vector2f position, AABB bounds) {
        this(position, bounds, null);
    }

    public GUIElement(Vector2f position, Sprite sprite) {
        this(position, new AABB.Rectangle(position, sprite.getWidth(), sprite.getHeight()), sprite);
    }

    public GUIElement(Vector2f position, AABB bounds, Sprite sprite) {
        this.pos = position;
        this.bounds = bounds;
        this.sprite = sprite;
        listeners = ConcurrentHashMap.newKeySet();
    }

    public abstract void update();

    public void render(Graphics2D g) {
        sprite.render(g, pos, sprite.getWidth() * renderWidthScale, sprite.getHeight() * renderHeightScale);
    }

    public void input(KeyHandler keyHandler, MouseHandler mouseHandler) {
        MouseHandler.Mouse mouse = mouseHandler.getMouse();

        if (bounds.collides(mouse.getPosition())) {
            if (!entered) {
                entered = true;
                exited = false;

                // Entered
                for (GUIElementListener listener : listeners) {
                    GUIElementEvent event = new GUIElementEvent(this, listener, keyHandler, mouseHandler);
                    listener.guiEntered(event);
                }
            }

            // Hovered
            for (GUIElementListener listener : listeners) {
                GUIElementEvent event = new GUIElementEvent(this, listener, keyHandler, mouseHandler);
                listener.guiHovered(event);
            }

            // Pressed
            if (mouse.isPressed()) {
                for (GUIElementListener listener : listeners) {
                    GUIElementEvent event = new GUIElementEvent(this, listener, keyHandler, mouseHandler);
                    listener.guiPressed(event);
                }
            }

            // Clicked
            if (mouse.isClicked()) {
                for (GUIElementListener listener : listeners) {
                    GUIElementEvent event = new GUIElementEvent(this, listener, keyHandler, mouseHandler);
                    listener.guiClicked(event);
                }
            }

            // Released
            if (mouse.isReleased()) {
                for (GUIElementListener listener : listeners) {
                    GUIElementEvent event = new GUIElementEvent(this, listener, keyHandler, mouseHandler);
                    listener.guiReleased(event);
                }
            }

            if (mouse.isReleased() && dragged)
                dragged = false;

            // Dragged
            if (mouse.isDragged() || dragged) {
                for (GUIElementListener listener : listeners) {
                    GUIElementEvent event = new GUIElementEvent(this, listener, keyHandler, mouseHandler);
                    listener.guiDragged(event);
                }
            }

            if (mouse.isDragged() && !dragged)
                dragged = true;

            // Held
            if (mouse.isHeld()) {
                for (GUIElementListener listener : listeners) {
                    GUIElementEvent event = new GUIElementEvent(this, listener, keyHandler, mouseHandler);
                    listener.guiHeld(event);
                }
            }
        } else {
            if (!exited) {
                exited = true;
                entered = false;

                // Exited
                for (GUIElementListener listener : listeners) {
                    GUIElementEvent event = new GUIElementEvent(this, listener, keyHandler, mouseHandler);
                    listener.guiExited(event);
                }
            }
        }
    }

    public synchronized void addElementListener(GUIElementListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeElementListener(GUIElementListener listener) {
        listeners.remove(listener);
    }

    public Vector2f getPosition() {
        return pos;
    }

    public void setPosition(Vector2f pos) {
        this.pos = pos;
    }

    public AABB getBounds() {
        return bounds;
    }

    public void setBounds(AABB bounds) {
        this.bounds = bounds;
    }

    public int getRenderWidthScale() {
        return renderWidthScale;
    }

    public void setRenderWidthScale(int widthScale) {
        this.renderWidthScale = widthScale;
    }

    public int getRenderHeightScale() {
        return renderHeightScale;
    }

    public void setRenderHeightScale(int heightScale) {
        this.renderHeightScale = heightScale;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
