package com.diamondhunter.util.sound;

import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.logging.Logger;

import java.util.LinkedList;
import java.util.List;

public final class SoundThread implements Runnable {
    public static final String DEFAULT_NAME = DiamondHunter.getImplementation().getThread().getName() + " - " + "Sound";

    private Thread thread;

    private boolean running;
    private boolean paused;

    private List<ComplexSoundPlayer> soundPlayers = new LinkedList<>();

    public SoundThread() {
        this.thread = new Thread(this, DEFAULT_NAME);

        this.running = false;
    }

    public SoundThread(boolean start) {
        this.thread = new Thread(this, DEFAULT_NAME);

        if (start)
            start();
    }

    public synchronized void start() {
        if (running) return;

        running = true;
        thread.start();
    }

    public synchronized void stop() throws InterruptedException {
        if (!running) return;

        running = false;
        thread.join();
    }

    @Deprecated
    public synchronized void forceStop() {
        if (!running) return;

        running = false;
        thread.stop();
    }

    public synchronized boolean addSoundPlayer(ComplexSoundPlayer soundPlayer) {
        return this.soundPlayers.add(soundPlayer);
    }

    public synchronized boolean containsSoundPlayer(ComplexSoundPlayer soundPlayer) {
        return this.soundPlayers.contains(soundPlayer);
    }

    public synchronized boolean removeSoundPlayer(ComplexSoundPlayer soundPlayer) {
        return this.soundPlayers.remove(soundPlayer);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setSoundPlayers(List<ComplexSoundPlayer> soundPlayers) {
        this.soundPlayers = soundPlayers;
    }

    public List<ComplexSoundPlayer> getSoundPlayers() {
        return soundPlayers;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        setThread(thread, false);
    }

    public void setThread(Thread thread, boolean start) {
        // Make sure we do not assign this thread to null
        if (thread == null) return;

        // Force this thread to stop
        forceStop();

        // Change the thread
        this.thread = thread;

        if (start) {
            // Start the thread
            this.thread.start();
        }
    }

    @Override
    public void run() {
        while (running) {

            if (!paused) {
                for(int i = 0; i < soundPlayers.size(); i++){
                    ComplexSoundPlayer player = soundPlayers.get(i);

                    if(player.shouldPlay){
                        player.getSimpleSoundPlayer().playSound();
                        player.shouldPlay = false;
                    }

                    if(player.shouldPause){
                        player.getSimpleSoundPlayer().pauseSound();
                        player.shouldPause = false;
                    }

                    if(player.shouldStop){
                        player.getSimpleSoundPlayer().stopSound();
                        player.shouldStop = false;
                    }
                }
            }

            try {
                Thread.yield();
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            stop();
        } catch (InterruptedException e) {
            DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not end SoundThread, forcing stop...");
            forceStop();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof List) {
            return soundPlayers.equals(obj);
        } else if (obj instanceof Thread) {
            return thread.equals(obj);
        } else if (obj instanceof SimpleSoundPlayer) {
            return soundPlayers.size() == 1 && soundPlayers.get(0).equals(obj);
        } else if (obj instanceof SoundThread) {
            return running == ((SoundThread) obj).running && paused == ((SoundThread) obj).paused && soundPlayers.equals(((SoundThread) obj).soundPlayers);
        }

        return super.equals(obj);
    }
}
