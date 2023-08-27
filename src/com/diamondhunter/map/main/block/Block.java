package com.diamondhunter.map.main.block;

import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.Graphics2D;

public abstract class Block {
    protected Vector2f pos;
    protected Sprite sprite;
    protected float scale;

    public Block(Vector2f pos, Sprite sprite){
        this.pos = pos;
        this.sprite = sprite;
    }

    public abstract void update();
    public void input(KeyHandler keyHandler, MouseHandler mouseHandler){}
    public void render(Graphics2D g){
        sprite.render(g, pos.getWorldVector(), (int) getWidth(), (int) getHeight());
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setPosition(Vector2f pos) {
        this.pos = pos;
    }

    public Vector2f getPosition() {
        return pos;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public float getWidth(){
        return sprite.getWidth() * 1;
    }

    public float getHeight(){
        return sprite.getHeight() * 1;
    }
}
