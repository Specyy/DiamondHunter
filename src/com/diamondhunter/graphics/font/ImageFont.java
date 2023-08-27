package com.diamondhunter.graphics.font;

import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.graphics.sprite.SpriteSheet;
import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class ImageFont extends Font {
    private int letterWidth;
    private int letterHeight;

    private int letterSpacing;
    private int lineSpacing;

    private int letterXOffset;
    private int letterYOffset;

    private SpriteSheet fontSheet;

    public ImageFont(String path, int letterWidth, int letterHeight) {
        super(path);

        this.letterWidth = letterWidth;
        this.letterHeight = letterHeight;

        fontSheet = new SpriteSheet(path, this.letterWidth, this.letterHeight);
        initProperties();
    }

    public ImageFont(URL path, int letterWidth, int letterHeight) {
        super(path);

        this.letterWidth = letterWidth;
        this.letterHeight = letterHeight;

        fontSheet = new SpriteSheet(path, this.letterWidth, this.letterHeight);
        initProperties();
    }

    public ImageFont(URI path, int letterWidth, int letterHeight) throws MalformedURLException {
        this(path.toURL(), letterWidth, letterHeight);
    }

    private void initProperties(){
        letterSpacing = letterWidth + letterWidth / 4;
        lineSpacing = letterHeight + letterHeight / 4;
        letterXOffset = 0;
        letterYOffset = 0;
    }

    @Override
    public void renderText(Graphics2D g, String text, float x, float y) {
        String renderText = text;
        renderText = renderText.replace(System.lineSeparator(), "\n");

        float xPos = x;
        float yPos = y;

        if (renderText.contains("\n")) {
            renderText = renderText.substring(0, renderText.indexOf("\n"));
            renderText(g, text.replace(System.lineSeparator(), "\n").substring(text.replace(System.lineSeparator(), "\n").indexOf("\n") + 1),
                    x, y + lineSpacing);
        }

        for (int i = 0; i < renderText.length(); i++) {
            char ch = renderText.charAt(i);

            if (ch == '\t') {
                x += letterSpacing + letterXOffset;
            } else if (ch != ' ') {
                getLetter(ch).render(g, xPos, yPos, (int) (letterWidth * size), (int) (letterHeight * size));
            }

            xPos += letterSpacing + letterXOffset;
            yPos += letterYOffset;
        }
    }

    public Sprite getLetter(char ch) {
        int value = ch - 32; // 32 so that ! is the first value; which is the first letter in the font sheet
        return value < 1 ? getLetter(0) : getLetter(value);
    }

    public Sprite getLetter(int num) {
        return fontSheet.getSprite(num);
    }

    public Sprite getLetter(int x, int y) {
        return fontSheet.getSprite(x, y);
    }

    @Override
    public AABB.Rectangle getStringBounds(Graphics2D g, String str) {
        return null;
    }

    public int getLetterSpacing() {
        return letterSpacing;
    }

    public void setLetterSpacing(int letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public SpriteSheet getFontSheet() {
        return fontSheet;
    }

    public int getLetterXOffset() {
        return letterXOffset;
    }

    public void setLetterXOffset(int letterXOffset) {
        this.letterXOffset = letterXOffset;
    }

    public int getLetterYOffset() {
        return letterYOffset;
    }

    public void setLetterYOffset(int letterYOffset) {
        this.letterYOffset = letterYOffset;
    }
}
