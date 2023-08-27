package com.diamondhunter.util.loading;

import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.logging.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class ImageLoader extends ResourceLoader {
    protected ImageLoader() {
    }

    public static BufferedImage loadImage(String path) {
        return loadImage(retrieveResource(path));
    }

    public static BufferedImage loadImage(URL path) {
        try {
            return ImageIO.read(path);
        } catch (Exception e) {
            if (DiamondHunter.isDebugMode()) {
                if (path == null)
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not load image!");
                else
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not load image: \"" + path.getPath() + "\"");

                e.printStackTrace();
            } else {
                if (path == null)
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not load image! :");
                else
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not load image: \"" + path.getPath() + "\"");

            }
        }

        return null;
    }

    public static BufferedImage loadImage(File path) {
        try {
            return ImageIO.read(path);
        } catch (Exception e) {
            if (DiamondHunter.isDebugMode()) {
                if (path == null)
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not load image! :");
                else
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not load image: \"" + path.getPath() + "\"");

                e.printStackTrace();
            } else {
                if (path == null)
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not load image!");
                else
                    DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could not load image \"" + path.getPath() + "\"");
            }
        }

        return null;
    }
}
