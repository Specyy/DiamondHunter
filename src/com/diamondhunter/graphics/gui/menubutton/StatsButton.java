package com.diamondhunter.graphics.gui.menubutton;

import com.diamondhunter.graphics.gui.base.GUIButton;
import com.diamondhunter.graphics.gui.base.GUIElementEvent;
import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

public class StatsButton extends GUIButton {

    public StatsButton(Vector2f position, Sprite sprite, int spriteWidth, int spriteHeight) {
        super(position, sprite, spriteWidth, spriteHeight);
    }

    public StatsButton(Vector2f position, AABB bounds, Sprite sprite, int spriteWidth, int spriteHeight) {
        super(position, bounds, sprite, spriteWidth, spriteHeight);
    }

    @Override
    public void update() {

    }

    @Override
    public void guiPressed(GUIElementEvent event) {

    }

    @Override
    public void guiClicked(GUIElementEvent event) {
        // TODO Change state to Stats State
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
