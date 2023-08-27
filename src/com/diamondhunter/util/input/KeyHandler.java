package com.diamondhunter.util.input;

import com.diamondhunter.graphics.window.Display;
import com.diamondhunter.graphics.window.Window;
import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.logging.Logger;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyHandler implements KeyListener {

    private Display display;
    private Map<Integer, Key> keys;
    private Key currentKey, lastKey;

    public KeyHandler(DiamondHunter diamondHunter) {
        this(diamondHunter.getWindow());
    }

    public KeyHandler(Window window) {
        this(window.getDisplay());
    }

    public KeyHandler(Display display) {
        boolean addListener = true;

        for (int i = 0; i < display.getKeyListeners().length; i++) {
            if (display.getKeyListeners()[i].equals(this)) {
                addListener = false;
                break;
            }
        }

        if (addListener)
            display.addKeyListener(this);
        this.display = display;

        keys = new ConcurrentHashMap<>();
    }

    public synchronized void update() {
        if(!display.isFocusOwner()){
            for (Key key : keys.values()) {
                key.released = true;
                key.clicked = false;
                key.pressed = false;
                key.typed = false;
            }
        }

        for (Key key : keys.values()) {
            key.released = false;
            key.clicked = false;
        }
    }

    public void dispose() {
        display.removeKeyListener(this);
    }

    public synchronized void forceReleaseKeys() {
        for (Key key : keys.values()) {
            key.released = true;
            key.clicked = false;
            key.pressed = false;
            key.typed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        checkKey(e.getKeyCode());

        Key key = getKey(e.getKeyCode());
        if(key != null) {
            key.clicked = true;
            key.clicked = false;
            key.pressed = true;
            key.released = false;
            key.typed = false;
        }
        lastKey = key;
        currentKey = key;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        checkKey(e.getKeyCode());

        Key key = getKey(e.getKeyCode());
        if(key != null) {
            key.clicked = false;
            key.pressed = false;
            key.released = true;
            key.typed = false;
        }
        lastKey = key;
        currentKey = null;
    }

    private void checkKey(int keyCode) {
        if (keyCode == KeyEvent.VK_UNDEFINED)
            return;
        if (!keys.containsKey(keyCode))
            keys.put(keyCode, new Key(keyCode));
    }

    public Key getKey(int keyCode) {
        if (keyCode == KeyEvent.VK_UNDEFINED)
            return null;
        if (!keys.containsKey(keyCode))
            keys.put(keyCode, new Key(keyCode));
        return keys.get(keyCode);
    }

    public Key getKey(KeyEvent event) {
        return getKey(event.getKeyCode());
    }

    @Deprecated
    public Key getKey(String key) {
        return getKey(keyCodeForKey(key.toUpperCase()));
    }

    private int keyCodeForKey(String key) {
        int keyCode;

        String parsedKey = key.toUpperCase().replaceAll(" ", "_").replaceAll("NUMPAD_\\+", "PLUS").replaceAll("NUMPAD_/", "SLASH")
                .replaceAll("NUMPAD-0", "0").replaceAll("NUMPAD-1", "1").replaceAll("NUMPAD-2", "2").replaceAll("NUMPAD-3", "3")
                .replaceAll("NUMPAD-4", "4").replaceAll("NUMPAD-5", "5").replaceAll("NUMPAD-6", "6").replaceAll("NUMPAD-7", "7")
                .replaceAll("NUMPAD-8", "8").replaceAll("NUMPAD-9", "9").replaceAll("NUMPAD_\\*", "ASTERISK").replaceAll("NUMPAD_-", "MINUS")
                .replaceAll("NUMPAD_\\.", "PERIOD").replaceAll("CTRL", "CONTROL").replaceAll("PRINT_SCREEN", "PRINTSCREEN")
                .replaceAll("BACKSPACE", "BACK_SPACE");

        try {
            keyCode = KeyEvent.class.getField("VK_" + parsedKey).getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Unknown key: " + "VK_" + parsedKey);
            return -1;
        }

        return keyCode;
    }

    public Key getCurrentKey() {
        return currentKey;
    }

    public Key getLastKey() {
        return lastKey;
    }

    public static class Key {
        private int keyCode;

        private boolean pressed;
        private boolean released;
        private boolean typed;
        private boolean clicked;

        @Deprecated
        private Key(String key) {
            String parsedKey = key.toUpperCase().replaceAll(" ", "_").replaceAll("NUMPAD_\\+", "PLUS").replaceAll("NUMPAD_/", "SLASH")
                    .replaceAll("NUMPAD-0", "0").replaceAll("NUMPAD-1", "1").replaceAll("NUMPAD-2", "2").replaceAll("NUMPAD-3", "3")
                    .replaceAll("NUMPAD-4", "4").replaceAll("NUMPAD-5", "5").replaceAll("NUMPAD-6", "6").replaceAll("NUMPAD-7", "7")
                    .replaceAll("NUMPAD-8", "8").replaceAll("NUMPAD-9", "9").replaceAll("NUMPAD_\\*", "ASTERISK").replaceAll("NUMPAD_-", "MINUS")
                    .replaceAll("NUMPAD_\\.", "PERIOD").replaceAll("CTRL", "CONTROL").replaceAll("PRINT_SCREEN", "PRINTSCREEN")
                    .replaceAll("BACKSPACE", "BACK_SPACE");

            try {
                this.keyCode = KeyEvent.class.getField("VK_" + parsedKey).getInt(null);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Unknown key: " + "VK_" + parsedKey);
            }
        }

        private Key(int keyCode) {
            this.keyCode = keyCode;
        }

        public String getKeyText() {
            return KeyEvent.getKeyText(keyCode);
        }

        public boolean isPressed() {
            return pressed;
        }

        public boolean isReleased() {
            return released;
        }

        public boolean isTyped() {
            return typed;
        }

        public boolean isClicked() {
            return clicked;
        }

        public int isPressedInt() {
            return pressed ? 1 : 0;
        }

        public int isReleasedInt() {
            return released ? 1 : 0;
        }

        public int isTypedInt() {
            return typed ? 1 : 0;
        }

        public int isClickedInt() {
            return clicked ? 1 : 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (obj instanceof Number) {
                return keyCode == ((Number) obj).intValue();
            } else if (obj instanceof Key) {
                return keyCode == ((Key) obj).keyCode;
            }

            return false;
        }
    }
}
