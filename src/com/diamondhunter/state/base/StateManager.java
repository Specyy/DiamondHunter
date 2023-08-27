package com.diamondhunter.state.base;

import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;

import java.awt.Graphics2D;
import java.lang.reflect.Array;
import java.util.*;

public final class StateManager {

    private Set<State> existingStates;
    private Set<State> activeStates;
    private Queue<State> pendingStates;

    private State fallBack;

    public StateManager() {
        this(null);
    }

    public StateManager(State fallback) {
        this.fallBack = fallback;

        existingStates = new LinkedHashSet<>();
        activeStates = new LinkedHashSet<>();
        pendingStates = new PriorityQueue<>();

        if (fallback != null)
            addExistingState(fallback);
    }

    public void update() {
        for (State state : activeStates) {
            if (!state.isPaused())
                state.update();
        }
    }

    public void input(KeyHandler keyHandler, MouseHandler mouseHandler) {
        for (State state : activeStates) {
            state.input(keyHandler, mouseHandler);
        }
    }

    public void render(Graphics2D g) {
        for (State state : activeStates) {
            state.render(g);
        }
    }

    public void addExistingState(State state) {
        existingStates.add(state);
    }

    public void addPendingState(State state) {
        addExistingState(state);
        pendingStates.offer(state);
    }

    public void addActiveState(State state) {
        addExistingState(state);
        activeStates.add(state);
    }

    @Deprecated
    public void addActiveState(Class<? extends State> state) {
        for(State st: existingStates){
            if(st.getClass().equals(state)) {
                addActiveState(st);
            }
        }
    }

    public boolean isActiveState(State state) {
        return activeStates.contains(state);
    }

    public void removePendingState(State state) {
        pendingStates.remove(state);
    }

    public boolean isPendingState(State state) {
        return pendingStates.contains(state);
    }

    public void removeActiveState(State state) {
        if (activeStates.remove(state) && !pendingStates.isEmpty()) {
            activeStates.add(pendingStates.poll());
        }
    }

    public <T extends State> T[] getExistingStates(Class<T> stateClass){
        List<State> states = new ArrayList<>();

        for(State state : existingStates){
            if(state.getClass().equals(stateClass))
                states.add(state);
        }

        return Arrays.copyOf(states.toArray(), states.size(), (Class<? extends T[]>) Array.newInstance(stateClass, states.size()).getClass());
    }

    public <T extends State> T[] getActiveStates(Class<T> stateClass){
        List<State> states = new ArrayList<>();

        for(State state : activeStates){
            if(state.getClass().equals(stateClass))
                states.add(state);
        }

        return Arrays.copyOf(states.toArray(), states.size(), (Class<? extends T[]>) Array.newInstance(stateClass, states.size()).getClass());
    }

    public <T extends State> T[] getPendingStates(Class<T> stateClass){
        List<State> states = new ArrayList<>();

        for(State state : pendingStates){
            if(state.getClass().equals(stateClass))
                states.add(state);
        }

        return Arrays.copyOf(states.toArray(), states.size(), (Class<? extends T[]>) Array.newInstance(stateClass, states.size()).getClass());
    }
    
    public void removeActiveStates() {
    	for(State state: activeStates) {
    		removeActiveState(state);
    	}
    }

    public Set<State> getExistingStates() {
        return existingStates;
    }

    public Queue<State> getPendingStates() {
        return pendingStates;
    }

    public void setFallbackState(State fallBack) {
        this.fallBack = fallBack;
    }

    public State getFallbackState() {
        return fallBack;
    }
}
