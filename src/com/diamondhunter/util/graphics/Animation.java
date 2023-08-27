package com.diamondhunter.util.graphics;

import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.general.Utils;

public class Animation {
    public static final float DEFAULT_DELAY = -1.0f;

    private Sprite[] frames;
    private double delay;

    private int startIndex;
    private int endIndex;

    private int index;
    private long count;

    public Animation(){
        delay = DEFAULT_DELAY;
    }

    public Animation(Sprite[] frames){
        this(frames, DEFAULT_DELAY);
    }

    public Animation(Sprite[] frames, double delaySeconds){
        this(frames, delaySeconds, 0);
    }

    public Animation(Sprite[] frames, double delaySeconds, int startIndex){
        this(frames, delaySeconds, startIndex, frames.length - 1);
    }

    public Animation(Sprite[] frames, double delaySeconds, int startIndex, int endIndex) {
        this.frames = frames;
        this.delay = delaySeconds;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        index = startIndex;
        count = 0;
    }

    public void update(){
        if(delay <= -1.0f || Double.isInfinite(delay) || Double.isNaN(delay)) return;

        double targetHertz = DiamondHunter.getTargetHertz();

        count++;

        if(count >= delay * targetHertz){
            index++;
            count = 0;
        }

        if(index < startIndex)
            index = startIndex;

        if(index > endIndex)
            index = startIndex;
    }

    public Sprite getCurrentSprite(){
        return frames[index];
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setAnimationIndex(int index) {
        this.index = Utils.getNumberUtils().clamp(index, startIndex, endIndex);
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getAnimationIndex() {
        return index;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setDelay(double delaySeconds) {
        this.delay = delaySeconds;
    }

    public double getDelay() {
        return delay;
    }

    public void setFrames(Sprite[] frames) {
        this.frames = frames;
        index = startIndex;
        count = 0;
    }

    public Sprite[] getFrames() {
        return frames;
    }
}
