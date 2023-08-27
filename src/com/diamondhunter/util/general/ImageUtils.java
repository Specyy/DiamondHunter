package com.diamondhunter.util.general;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class ImageUtils {

    public boolean pixelsEqual(BufferedImage image1, BufferedImage image2){
        if(image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) return false;

        for(int y = 0; y < image1.getHeight(); y++){
            for(int x = 0; x < image1.getWidth(); x++){
                if(image1.getRGB(x, y) != image2.getRGB(x, y)) return false;
            }
        }

        return true;
    }
}
