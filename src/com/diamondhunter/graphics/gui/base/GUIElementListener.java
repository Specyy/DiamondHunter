package com.diamondhunter.graphics.gui.base;

public interface GUIElementListener {
    // Called when the GUI is pressed
    void guiPressed(GUIElementEvent event);

    // Called when the GUI is clicked
    void guiClicked(GUIElementEvent event);

    // Called when the GUI is released
    void guiReleased(GUIElementEvent event);

    // Called when the GUI is dragged
    void guiDragged(GUIElementEvent event);

    // Called when the GUI is held
    void guiHeld(GUIElementEvent event);

    // Called when the GUI is hovered (when the mouse if over the GUI)
    void guiHovered(GUIElementEvent event);

    // Called when the GUI is entered
    void guiEntered(GUIElementEvent event);

    // Called when the GUI is exited
    void guiExited(GUIElementEvent event);
}
