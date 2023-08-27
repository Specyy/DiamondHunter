package com.diamondhunter.util.sound;

import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class ComplexSoundPlayer extends SoundPlayer {

    protected boolean shouldPlay = false;
    protected boolean shouldPause = false;
    protected boolean shouldStop = false;

    private SimpleSoundPlayer simpleSoundPlayer = new SimpleSoundPlayer(this);

    public ComplexSoundPlayer(SoundThread thread, URL url) {
        super(url);
        thread.addSoundPlayer(this);
    }

    public ComplexSoundPlayer(URL url) {
        super(url);
    }

    public ComplexSoundPlayer(URI uri) {
        super(uri);
    }

    public ComplexSoundPlayer(File file) {
        super(file);
    }

    @Override
    public void playSound() {
        shouldPlay = true;
        shouldPause = false;
        shouldStop = false;
    }

    public void playSound(long microPosition){
        simpleSoundPlayer.setMicrosecondPosition(microPosition);
        playSound();
    }

    public void playSound(int framePosition){
        simpleSoundPlayer.setFramePosition(framePosition);
        playSound();
    }

    @Override
    public void pauseSound() {
        shouldPlay = false;
        shouldPause = true;
        shouldStop = false;
    }

    public void pauseSound(long resumeMicroPosition){
        simpleSoundPlayer.microPausePosition = resumeMicroPosition;
        pauseSound();
    }

    public void pauseSound(int resumeFramePosition){
        simpleSoundPlayer.framePausePosition = resumeFramePosition;
        pauseSound();
    }

    @Override
    public void stopSound() {
        shouldPlay = false;
        shouldPause = false;
        shouldStop = true;
    }

    @Override
    public void setLoopingPoint(int start, int end) {
        simpleSoundPlayer.setLoopingPoint(start, end);
    }

    @Override
    public void setLooping(boolean looping) {
        simpleSoundPlayer.setLooping(looping);
    }

    @Override
    public void loop(int count) {
        simpleSoundPlayer.loop(count);
    }

    @Override
    public void close() {
        simpleSoundPlayer.close();
    }

    @Override
    public boolean isPlaying() {
        return simpleSoundPlayer.isPlaying();
    }

    @Override
    public boolean isPaused() {
        return simpleSoundPlayer.isPaused();
    }

    @Override
    public boolean isFinishedPlayback() {
        return simpleSoundPlayer.isFinishedPlayback();
    }

    @Override
    public InputStream getInputStream() {
        return simpleSoundPlayer.getInputStream();
    }

    @Override
    public void setMicrosecondPosition(long microPosition) {
        simpleSoundPlayer.setMicrosecondPosition(microPosition);
    }

    @Override
    public void setFramePosition(int framePosition) {
        simpleSoundPlayer.setFramePosition(framePosition);
    }

    @Override
    public int getFramePosition() {
        return simpleSoundPlayer.getFramePosition();
    }

    @Override
    public long getLongFramePosition() {
        return simpleSoundPlayer.getLongFramePosition();
    }

    @Override
    public long getMicrosecondPosition() {
        return simpleSoundPlayer.getMicrosecondPosition();
    }

    protected SimpleSoundPlayer getSimpleSoundPlayer() {
        return simpleSoundPlayer;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SimpleSoundPlayer){
            return simpleSoundPlayer.equals(obj);
        }

        return super.equals(obj);
    }
}
