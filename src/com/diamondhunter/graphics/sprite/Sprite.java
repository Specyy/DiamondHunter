package com.diamondhunter.graphics.sprite;

import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.map.tiled.tileset.TiledTile;
import com.diamondhunter.util.general.Utils;
import com.diamondhunter.util.loading.ImageLoader;
import com.diamondhunter.util.loading.ResourceLoader;
import com.diamondhunter.util.logging.Logger;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.net.URL;
import java.util.Vector;

public class Sprite {
    private BufferedImage image;
    private File file;

    private Vector2f rotPoint;
    private float rotAngle;

    private float alpha = 1.0f;
    private int alphaRule = -1;

    @Deprecated
    public Sprite(BufferedImage image) {
        this.image = image;
    }

    public Sprite(TiledTile tile) {
        this.image = tile.getImage();
        this.file = tile.getImageFile();
    }

    public Sprite(URL path) {
        try {
            this.image = ImageLoader.loadImage(path);
            this.file = new File(path.toExternalForm());
        } catch (Exception e) {
            if (DiamondHunter.isDebugMode()) {
                DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could create sprite for \"" + path + "\":");

                e.printStackTrace();
            } else {
                DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could setup sprite for image path \"" + path + "\"");
            }
        }
    }

    public Sprite(String path) {
        try {
            URL loadedPath = ResourceLoader.retrieveResource(path);
            this.image = ImageLoader.loadImage(loadedPath);
            this.file = new File(loadedPath.toExternalForm());
        } catch (Exception e) {
            if (DiamondHunter.isDebugMode()) {
                DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could create sprite for \"" + path + "\":");

                e.printStackTrace();
            } else {
                DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could setup sprite for image path \"" + path + "\"");
            }
        }
    }

    public Sprite(File path) {
        try {
            this.image = ImageLoader.loadImage(path);
            this.file = path;
        } catch (Exception e) {
            if (DiamondHunter.isDebugMode()) {
                DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could create sprite for \"" + path + "\":");

                e.printStackTrace();
            } else {
                DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.ERROR, "Could setup sprite for image path \"" + path + "\"");
            }
        }
    }

    private Sprite(File file, BufferedImage image) {
        this.file = file;
        this.image = image;
    }

    public Sprite getSubSprite(int startX, int startY) {
        return getSubSprite(startX, startY, getWidth() - startX, getHeight() - startY);
    }

    public Sprite getSubSprite(int x, int y, int width, int height) {
        return new Sprite(file, image.getSubimage(x, y, width, height));
    }

    public void render(Graphics2D g, Vector2f position) {
        render(g, position, image.getWidth(), image.getHeight());
    }

    public void render(Graphics2D g, Vector2f position, int width, int height) {
    	render(g, position.getPosition().x, position.getPosition().y, width, height);
    }
    
    public void render(Graphics2D g, float x, float y, int width, int height) {
        float rotX = rotPoint == null ? x : rotPoint.getPosition().x;
        float rotY = rotPoint == null ? y : rotPoint.getPosition().y;

        AffineTransform oldTransform = g.getTransform();
        Composite oldComposite = g.getComposite();

        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rotAngle), rotX, rotY);
        g.setTransform(at);
        g.setComposite(AlphaComposite.getInstance(alphaRule == -1 ? AlphaComposite.SRC_OVER : alphaRule, alpha));

        g.drawImage(image, (int) x, (int) y, width, height, null);

        g.setTransform(oldTransform);
        g.setComposite(oldComposite);
    }

    public float getAlphaRule() {
        return alphaRule;
    }

    public void setAlphaRule(int alphaRule) {
        this.alphaRule = alphaRule;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = Utils.getNumberUtils().clamp(alpha, 0.0f, 1.0f);
    }

    public Vector2f getRotationPoint() {
        return rotPoint;
    }

    public void setRotationPoint(Vector2f rotPoint) {
        this.rotPoint = rotPoint;
    }

    public float getRotationAngle() {
        return rotAngle;
    }

    public void setRotationAngle(float rotAngle) {
        this.rotAngle = rotAngle;
    }

    public SpriteSheet toSpriteSheet() {
        return new SpriteSheet(this, getWidth(), getHeight());
    }

    public Sprite[] toArray() {
        return new Sprite[]{this};
    }

    public BufferedImage[] toImageArray() {
        return new BufferedImage[]{image};
    }

    public BufferedImage getImage() {
        return image;
    }

    public File getFile() {
        return file;
    }

    public int getType() {
        return image.getType();
    }

    public ColorModel getColorModel() {
        return image.getColorModel();
    }

    public WritableRaster getRaster() {
        return image.getRaster();
    }

    public WritableRaster getAlphaRaster() {
        return image.getAlphaRaster();
    }

    public int getRGB(int x, int y) {
        return image.getRGB(x, y);
    }

    public int[] getRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize) {
        return image.getRGB(startX, startY, w, h, rgbArray, offset, scansize);
    }

    public void setRGB(int x, int y, int rgb) {
        image.setRGB(x, y, rgb);
    }

    public void setRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize) {
        image.setRGB(startX, startY, w, h, rgbArray, offset, scansize);
    }

    public int getWidth() {
        return image == null ? 0 : image.getWidth();
    }

    public int getHeight() {
        return image == null ? 0 : image.getHeight();
    }

    public int getWidth(ImageObserver observer) {
        return image == null ? 0 : image.getWidth(observer);
    }

    public int getHeight(ImageObserver observer) {
        return image == null ? 0 : image.getHeight(observer);
    }

    public ImageProducer getSource() {
        return image == null ? null : image.getSource();
    }

    public Object getProperty(String name, ImageObserver observer) {
        return image == null ? null : image.getProperty(name, observer);
    }

    public Object getProperty(String name) {
        return image == null ? null : image.getProperty(name);
    }

    protected BufferedImage getSubimage(int x, int y, int w, int h) {
        return image == null ? null : image.getSubimage(x, y, w, h);
    }

    public boolean isAlphaPremultiplied() {
        return image != null && image.isAlphaPremultiplied();
    }

    public void coerceData(boolean isAlphaPremultiplied) {
        if (image == null) return;
        image.coerceData(isAlphaPremultiplied);
    }

    public Vector<RenderedImage> getSources() {
        return image == null ? null : image.getSources();
    }

    public String[] getPropertyNames() {
        return image == null ? null : image.getPropertyNames();
    }

    public int getMinX() {
        return image == null ? 0 : image.getMinX();
    }

    public int getMinY() {
        return image == null ? 0 : image.getMinY();
    }

    public SampleModel getSampleModel() {
        return image == null ? null : image.getSampleModel();
    }

    public int getNumXTiles() {
        return image == null ? 0 : image.getNumXTiles();
    }

    public int getNumYTiles() {
        return image == null ? 0 : image.getNumYTiles();
    }

    public int getMinTileX() {
        return image == null ? 0 : image.getMinTileX();
    }

    public int getMinTileY() {
        return image == null ? 0 : image.getMinTileY();
    }

    public int getTileWidth() {
        return image == null ? 0 : image.getTileWidth();
    }

    public int getTileHeight() {
        return image == null ? 0 : image.getTileHeight();
    }

    public int getTileGridXOffset() {
        return image == null ? 0 : image.getTileGridXOffset();
    }

    public int getTileGridYOffset() {
        return image == null ? 0 : image.getTileGridYOffset();
    }

    public Raster getTile(int tileX, int tileY) {
        return image == null ? null : image.getTile(tileX, tileY);
    }

    public Raster getData() {
        return image == null ? null : image.getData();
    }

    public void setData(Raster r) {
        if (image == null) return;
        image.setData(r);
    }

    public Raster getData(Rectangle rect) {
        return image == null ? null : image.getData(rect);
    }

    public WritableRaster copyData(WritableRaster outRaster) {
        return image == null ? null : image.copyData(outRaster);
    }

    public void addTileObserver(TileObserver to) {
        if (image == null) return;
        image.addTileObserver(to);
    }

    public void removeTileObserver(TileObserver to) {
        if (image == null) return;
        image.removeTileObserver(to);
    }

    public boolean isTileWritable(int tileX, int tileY) {
        return image == null ? null : image.isTileWritable(tileX, tileY);
    }

    public Point[] getWritableTileIndices() {
        return image == null ? null : image.getWritableTileIndices();
    }

    public boolean hasTileWriters() {
        return image != null && image.hasTileWriters();
    }

    public WritableRaster getWritableTile(int tileX, int tileY) {
        return image == null ? null : image.getWritableTile(tileX, tileY);
    }

    public void releaseWritableTile(int tileX, int tileY) {
        if (image == null) return;
        image.releaseWritableTile(tileX, tileY);
    }

    public int getTransparency() {
        return image == null ? 0 : image.getTransparency();
    }

    public Image getScaledInstance(int width, int height, int hints) {
        return image == null ? null : image.getScaledInstance(width, height, hints);
    }

    public void flush() {
        if (image == null) return;
        image.flush();
    }

    public ImageCapabilities getCapabilities(GraphicsConfiguration gc) {
        return image == null ? null : image.getCapabilities(gc);
    }

    public float getAccelerationPriority() {
        return image == null ? 0 : image.getAccelerationPriority();
    }

    public void setAccelerationPriority(float priority) {
        if (image == null) return;
        image.setAccelerationPriority(priority);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof BufferedImage) {
            return Utils.getImageUtils().pixelsEqual(image, (BufferedImage) obj);
        } else if (obj instanceof Sprite) {
            return Utils.getImageUtils().pixelsEqual(image, ((Sprite) obj).image);
        } else if (obj instanceof SpriteSheet) {
            return equals(((SpriteSheet) obj).getSprite());
        }

        return super.equals(obj);
    }
}
