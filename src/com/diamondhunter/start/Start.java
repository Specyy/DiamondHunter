package com.diamondhunter.start;

import com.diamondhunter.hub.DiamondHunter;

import javax.swing.*;

public class Start {

    public Start(String[] args) {
        // Check for debug mode start
        if (args.length == 1) { // Check if there is only 1 argument. Could change later for debug password
            if (args[0].equalsIgnoreCase(DiamondHunter.getDebugModeArgument())) {
                DiamondHunter.setDebugMode(true);
            }
        }

        // Start the game
        DiamondHunter.getImplementation().start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Start(args));
    }
}
