package com.diamondhunter.graphics.sprite;

import com.diamondhunter.util.general.Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class SpriteSheet {
    private Sprite[][] sprites;
    private Sprite sprite;

    private int spriteWidth;
    private int spriteHeight;

    private int spriteWidthAmount;
    private int spriteHeightAmount;

    public SpriteSheet(String path, int spriteWidth, int spriteHeight) {
        this(new Sprite(path), spriteWidth, spriteHeight);
    }

    public SpriteSheet(URL path, int spriteWidth, int spriteHeight) {
        this(new Sprite(path), spriteWidth, spriteHeight);
    }

    public SpriteSheet(File path, int spriteWidth, int spriteHeight) {
        this(new Sprite(path), spriteWidth, spriteHeight);
    }

    public SpriteSheet(Sprite spr, int spriteWidth, int spriteHeight) {
        this.sprite = spr;
        this.spriteWidth = Utils.getNumberUtils().clamp(spriteWidth, 1, sprite.getWidth());
        this.spriteHeight = Utils.getNumberUtils().clamp(spriteHeight, 1, sprite.getHeight());
        spriteWidthAmount = sprite.getWidth() / this.spriteWidth;
        spriteHeightAmount = sprite.getHeight() / this.spriteHeight;

        sprites = new Sprite[spriteWidthAmount][spriteHeightAmount];

        for (int x = 0; x < sprites.length; x++) {
            for (int y = 0; y < sprites[x].length; y++) {
                sprites[x][y] = sprite.getSubSprite(x * this.spriteWidth, y * this.spriteHeight, this.spriteWidth, this.spriteHeight);
            }
        }
    }

    @Deprecated
    public SpriteSheet(Sprite[][] sprites) {
        this.sprites = sprites;
        spriteWidth = sprites[0][0].getWidth();
        spriteHeight = sprites[0][0].getHeight();
        spriteWidthAmount = sprites[0].length;
        spriteHeightAmount = sprites.length;
    }

    public Sprite getSprite(int x, int y) {
        return sprites[x][y];
    }

    public Sprite getSprite(int num) {
        return sprites[num % spriteWidthAmount][num / spriteWidthAmount];
    }

    public Sprite[] getSpritesRow(int row) {
        Sprite[][] spriteArray = new Sprite[spriteHeightAmount][spriteWidthAmount];

        for (int x = 0; x < sprites.length; x++) {
            for (int y = 0; y < sprites[x].length; y++) {
                spriteArray[y][x] = sprites[x][y];
            }
        }

        return spriteArray[row];
    }

    public Sprite[] getSpritesColumn(int column) {
        return sprites[column];
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Sprite[] getSpritesArray() {
        List<Sprite> sprites = new LinkedList<>();

        for (int y = 0; y < this.sprites.length; y++) {
            for (int x = 0; x < this.sprites[y].length; x++) {
                sprites.add(this.sprites[x][y]);
            }
        }

        return sprites.toArray(new Sprite[0]);
    }

    public Sprite[][] getSprites() {
        return sprites;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int getSpriteCountWidth() {
        return spriteWidthAmount;
    }

    public int getSpriteCountHeight() {
        return spriteHeightAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof BufferedImage) {
            return Utils.getImageUtils().pixelsEqual(sprite.getImage(), (BufferedImage) obj) || sprite.getImage().equals(obj);
        } else if (obj instanceof Sprite) {
            return sprite.equals(obj);
        } else if (obj instanceof SpriteSheet) {
            return sprite.equals(((SpriteSheet) obj).sprite)
                    && spriteWidth == ((SpriteSheet) obj).spriteWidth && spriteHeight == ((SpriteSheet) obj).spriteHeight
                    && spriteWidthAmount == ((SpriteSheet) obj).spriteWidthAmount && spriteHeightAmount == ((SpriteSheet) obj).spriteHeightAmount;
        }

        return super.equals(obj);
    }
}
