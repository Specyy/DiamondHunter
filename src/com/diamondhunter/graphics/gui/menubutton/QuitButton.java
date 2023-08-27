package com.diamondhunter.graphics.gui.menubutton;

import com.diamondhunter.graphics.gui.base.GUIButton;
import com.diamondhunter.graphics.gui.base.GUIElementEvent;
import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

public class QuitButton extends GUIButton {

    public QuitButton(Vector2f position) {
        super(position);
    }

    public QuitButton(Vector2f position, AABB bounds) {
        super(position, bounds);
    }

    public QuitButton(Vector2f position, Sprite sprite, int buttonWidth, int buttonHeight) {
        super(position, sprite, buttonWidth, buttonHeight);
    }

    public QuitButton(Vector2f position, AABB bounds, Sprite sprite, int buttonWidth, int buttonHeight) {
        super(position, bounds, sprite, buttonWidth, buttonHeight);
    }

    @Override
    public void update() {

    }

    @Override
    public void guiPressed(GUIElementEvent event) {

    }

    @Override
    public void guiClicked(GUIElementEvent event) {
        // If the button is clicked, exit the game
        // TODO Some game cleanup (as in saving) may be needed here
        System.exit(0);
    }

    @Override
    public void guiReleased(GUIElementEvent event) {

    }

    @Override
    public void guiDragged(GUIElementEvent event) {

    }

    @Override
    public void guiHeld(GUIElementEvent event) {

    }

    @Override
    public void guiHovered(GUIElementEvent event) {

    }
}
