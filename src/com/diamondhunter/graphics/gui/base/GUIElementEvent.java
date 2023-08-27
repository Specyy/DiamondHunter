package com.diamondhunter.graphics.gui.base;

import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;

public class GUIElementEvent {

    private GUIElement source;
    private GUIElementListener listener;
    private KeyHandler keyHandler;
    private MouseHandler mouseHandler;

    GUIElementEvent(GUIElement element, GUIElementListener listener, KeyHandler keyHandler, MouseHandler mouseHandler){
        this.source = element;
        this.listener = listener;
        this.keyHandler = keyHandler;
        this.mouseHandler = mouseHandler;
    }

    public GUIElement getSource() {
        return source;
    }

    public GUIElementListener getListener() {
        return listener;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public MouseHandler getMouseHandler() {
        return mouseHandler;
    }
}
