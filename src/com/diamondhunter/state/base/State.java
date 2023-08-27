package com.diamondhunter.state.base;

import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;

import java.awt.*;

public abstract class State {

    private final Type type;
    private StateManager manager;

    private boolean paused;

    public State(StateManager manager, Type type) {
        this.type = type;
        this.manager = manager;
        this.manager.addExistingState(this);
    }

    private void deactivate() {
        manager.removeActiveState(this);
    }

    private void activate() {
        manager.addActiveState(this);
    }

    public void setPaused(boolean paused){
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public final void setActivated(boolean activated) {
        if (activated)
            activate();
        else deactivate();
    }

    public final boolean isActivated(){
        return manager.isActiveState(this);
    }

    public abstract void update();

    public abstract void input(KeyHandler keyHandler, MouseHandler mouseHandler);

    public abstract void render(Graphics2D g);

    public StateManager getManager() {
        return manager;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        TEST(-1, "A state used to test game updates or fix bugs."), 
        MENU(0, "The menu state."), 
        PLAY(1, "The play state."), 
        PAUSE(2, "The pause state."), 
        WIN(4, "The win state.");

        final int id;
        String description;

        Type(final int id, String description) {
            this.id = id;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getID() {
            return id;
        }
    }

}
