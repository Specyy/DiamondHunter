package com.diamondhunter.state.game;

import com.diamondhunter.map.main.Map;
import com.diamondhunter.state.base.State;
import com.diamondhunter.state.base.StateManager;
import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;

import java.awt.*;

public class PlayState extends State {

    private Map map;
    /**
     * Constructs a {@code PlaySTate} with the given arguments.
     *
     * @param manager The state manager used for this menu.
     */
    public PlayState(StateManager manager) {
        super(manager, Type.PLAY);
        //map = new Map("game/map/Map1/untitled.tmx");
    }

    @Override
    public void update() {

    }

    @Override
    public void input(KeyHandler keyHandler, MouseHandler mouseHandler) {

    }

    @Override
    public void render(Graphics2D g) {
        map.render(g);
    }
}
