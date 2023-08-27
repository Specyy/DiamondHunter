package com.diamondhunter.util.input;

import java.util.ArrayList;
import java.util.List;

public enum MovementDirection {
    NO_DIRECTION("NONE", 0),
    UP("UP", -1),
    DOWN("DOWN", 1),
    LEFT("LEFT", -1),
    RIGHT("RIGHT", 1);

    String name;
    int direction;

    MovementDirection(String name, int dir) {
        this.name = name;
        this.direction = dir;
    }

    public static MovementDirection[] fromDirection(int dir) {
        if (dir != -1 && dir != 1) return null;

        List<MovementDirection> dirs = new ArrayList<>();

        for (MovementDirection mov : values()) {
            if (mov.getDirection() == dir)
                dirs.add(mov);
        }

        return dirs.toArray(new MovementDirection[0]);
    }

    public static MovementDirection fromName(String name) {
        if (name == null) return null;

        for (MovementDirection mov : values()) {
            if (mov.name.equals(name.toUpperCase()))
                return mov;
        }

        return null;
    }

    public MovementDirection getOpposite() {
        if (this == UP) {
            return DOWN;
        } else if (this == DOWN) {
            return UP;
        }

        if (this == LEFT) {
            return RIGHT;
        } else if (this == RIGHT) {
            return LEFT;
        }

        return MovementDirection.NO_DIRECTION;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
