package com.diamondhunter.map.tiled.launch;

import com.diamondhunter.map.tiled.layer.TiledLayer;
import com.diamondhunter.map.tiled.layer.TiledTileLayer;
import com.diamondhunter.map.tiled.map.TiledMap;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Launcher implements KeyListener {

    private BufferStrategy bs;
    private Canvas canvas;
    private JFrame frame;
    private int width = 1280;
    private int height = width / 16 * 9;

    private Launcher(String[] args) {
        frame = new JFrame("Framed");
        frame.setDefaultCloseOperation(3);

        canvas = new Canvas();
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.addKeyListener(this);
        canvas.setFocusable(true);

        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        TiledMap map = new TiledMap("game/map/Map1/untitled.tmx");
        System.out.println(map.getLayers(TiledTileLayer.class)[0].getWidth() + "," + map.getLayers(TiledTileLayer.class)[0].getHeight());

        Thread thread = new Thread(() -> {
            while (true) {
                render(map);

                try {
                    Thread.yield();
                    Thread.sleep((1 / 60) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    float scale = 1;

    private void render(TiledMap map) {
        bs = canvas.getBufferStrategy();

        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        g.clearRect(0, 0, canvas.getPreferredSize().width, canvas.getPreferredSize().height);

        map.render(g, scale, TiledLayer.class);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Launcher(args));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            scale += 0.5f;
        }

        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            scale -= 0.5f;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

	}
}