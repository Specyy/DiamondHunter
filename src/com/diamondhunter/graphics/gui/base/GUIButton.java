package com.diamondhunter.graphics.gui.base;

import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.graphics.sprite.SpriteSheet;
import com.diamondhunter.util.graphics.Animation;
import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

public abstract class GUIButton extends GUIElement implements GUIElementListener {
    protected Animation hoverAnimation;
    protected SpriteSheet hoverSpriteSheet;

    protected int buttonWidth;
    protected int buttonHeight;

    // Used if there is no sprite
    protected String text;

    public GUIButton(Vector2f position) {
        super(position);
        addElementListener(this);

        // Check and correct bounds
        if (bounds instanceof AABB.Rectangle) {
            ((AABB.Rectangle) bounds).setWidth(buttonWidth);
            ((AABB.Rectangle) bounds).setHeight(buttonHeight);
        } else if (bounds instanceof AABB.Circle) {
            // Not ideal because we assume the buttonWidth and buttonHeight are the same.
            ((AABB.Circle) bounds).setRadius(buttonWidth / 2);
        }
    }

    public GUIButton(Vector2f position, AABB bounds) {
        super(position, bounds);
        addElementListener(this);

        // Check and correct bounds
        if (bounds instanceof AABB.Rectangle) {
            ((AABB.Rectangle) bounds).setWidth(buttonWidth);
            ((AABB.Rectangle) bounds).setHeight(buttonHeight);
        } else if (bounds instanceof AABB.Circle) {
            // Not ideal because we assume the buttonWidth and buttonHeight are the same.
            ((AABB.Circle) bounds).setRadius(buttonWidth / 2);
        }
    }

    public GUIButton(Vector2f position, Sprite sprite, int buttonWidth, int buttonHeight) {
        super(position, sprite);
        addElementListener(this);

        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;

        hoverSpriteSheet = new SpriteSheet(sprite, buttonWidth, buttonHeight);
        hoverAnimation = new Animation(hoverSpriteSheet.getSpritesRow(0));

        hoverAnimation.setAnimationIndex(0);
        this.sprite = hoverAnimation.getCurrentSprite();

        // Check and correct bounds
        if (bounds instanceof AABB.Rectangle) {
            ((AABB.Rectangle) bounds).setWidth(buttonWidth);
            ((AABB.Rectangle) bounds).setHeight(buttonHeight);
        } else if (bounds instanceof AABB.Circle) {
            // Not ideal because we assume the buttonWidth and buttonHeight are the same.
            ((AABB.Circle) bounds).setRadius(buttonWidth / 2);
        }
    }

    public GUIButton(Vector2f position, AABB bounds, Sprite sprite, int buttonWidth, int buttonHeight) {
        super(position, bounds, sprite);
        addElementListener(this);

        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;

        hoverSpriteSheet = new SpriteSheet(sprite, buttonWidth, buttonHeight);
        hoverAnimation = new Animation(hoverSpriteSheet.getSpritesRow(0));

        hoverAnimation.setAnimationIndex(0);
        this.sprite = hoverAnimation.getCurrentSprite();
    }

    @Override
    public void guiEntered(GUIElementEvent event) {
        if (hoverAnimation != null) {
            hoverAnimation.setAnimationIndex(1);
            sprite = hoverAnimation.getCurrentSprite();
        }
    }

    @Override
    public void guiExited(GUIElementEvent event) {
        if (hoverAnimation != null) {
            hoverAnimation.setAnimationIndex(0);
            sprite = hoverAnimation.getCurrentSprite();
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getButtonWidth() {
        return buttonWidth;
    }

    public void setButtonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        hoverSpriteSheet = new SpriteSheet(sprite, buttonWidth, buttonHeight);
        hoverAnimation = new Animation(hoverSpriteSheet.getSpritesRow(0));
    }

    public int getButtonHeight() {
        return buttonHeight;
    }

    public void setButtonHeight(int buttonHeight) {
        this.buttonHeight = buttonHeight;
        hoverSpriteSheet = new SpriteSheet(sprite, buttonWidth, buttonHeight);
        hoverAnimation = new Animation(hoverSpriteSheet.getSpritesRow(0));
    }

    public Animation getHoverAnimation() {
        return hoverAnimation;
    }

    public void setHoverAnimation(Animation hoverAnimation) {
        this.hoverAnimation = hoverAnimation;
    }

    public SpriteSheet getHoverSpriteSheet() {
        return hoverSpriteSheet;
    }

    public void setHoverSpriteSheet(SpriteSheet hoverSpriteSheet) {
        this.hoverSpriteSheet = hoverSpriteSheet;
    }
}
