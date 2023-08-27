package com.diamondhunter.map.tiled.layer;

import java.awt.Graphics2D;
import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.diamondhunter.map.tiled.map.TiledMap;
import com.diamondhunter.map.tiled.tileset.TiledTile;
import com.diamondhunter.map.tiled.tileset.TiledTileset;
import org.w3c.dom.Element;

public class TiledTileLayer extends TiledLayer {

    private TiledTileLayerData data;
    private Map<String, TiledTile> tiles;

    public TiledTileLayer(TiledMap map, File file, Element layerElement) {
        super(map, Type.TILE, file, layerElement);
    }

    @Override
    protected void loadElement(Element layerElement) {
        super.loadElement(layerElement);

        Element dataElement = (Element) layerElement.getElementsByTagName("data").item(0);
        String encodingString = dataElement.getAttribute("encoding");
        String rawData = dataElement.getTextContent();

        Encoding encoding = null;

        for (Encoding enc : Encoding.values()) {
            if (enc.tiledName.equals(encodingString)) {
                encoding = enc;
                break;
            }
        }

        data = new TiledTileLayerData(rawData, encoding);

        if (encoding == Encoding.CSV) {
            for (int i = 0; i < (width * height); i++) {
                long tileGid = Long.parseLong(data.getSplitData()[i].trim());
                TiledTile tile = null;

                for (int j = 0; j < map.getTilesets().size(); j++) {
                    TiledTileset tileset = map.getTileset(j);

                    if (tileset.containsTile(tileGid)) {
                        tile = tileset.getTile(tileGid);
                        break;
                    }
                }

                if (tile != null)
                    tiles.put((i % width) + "," + (i / width), tile);
            }
        }
    }

    public void render(Graphics2D g, float scale) {
        for (Map.Entry<String, TiledTile> tilesEntry : tiles.entrySet()) {
            if (tilesEntry.getValue() != null) {
                String[] position = tilesEntry.getKey().split(Pattern.quote(","));
                int x = Integer.parseInt(position[0]);
                int y = Integer.parseInt(position[1]);

                if (tilesEntry.getValue() != null)
                    g.drawImage(tilesEntry.getValue().getImage(),
                            (int) (x * tilesEntry.getValue().getTiledTileset().getTileWidth() * scale),
                            (int) (y * tilesEntry.getValue().getTiledTileset().getTileHeight() * scale),
                            (int) (tilesEntry.getValue().getTiledTileset().getTileWidth() * scale),
                            (int) (tilesEntry.getValue().getTiledTileset().getTileHeight() * scale), null);
            }
        }
    }

    @Override
    public void clearData() {
        super.clearData();

        if (tiles == null)
            tiles = new LinkedHashMap<>();
        tiles.clear();
    }

    public TiledTile getTile(int x, int y) {
        return tiles.get(x + "," + y);
    }

    public TiledTile getTile(int count) {
        return getTile(count % width, count / width);
    }

    public TiledTile getTile(long tileGid) {
        for (TiledTile tile : tiles.values()) {
            if (tile.getGid() == tileGid)
                return tile;
        }

        return null;
    }

    public Collection<TiledTile> getTiles() {
        return tiles.values();
    }

    public TiledTileLayerData getData() {
        return data;
    }

    public class TiledTileLayerData {
        private String rawData;
        private String[] splitRawData;
        private Encoding encoding;

        private TiledTileLayerData(String rawData, Encoding encoding) {
            this.rawData = rawData;
            this.encoding = encoding;
            this.splitRawData = encoding == Encoding.CSV ? rawData.split(Pattern.quote(",")) : rawData.split("");
        }

        public TiledTileLayer getLayer(){
            return TiledTileLayer.this;
        }

        public Encoding getEncoding() {
            return encoding;
        }

        public String[] getSplitData() {
            return splitRawData;
        }

        public String getRawData() {
            return rawData;
        }
    }

    public enum Encoding {
        CSV(0, "csv"), BASE64(1, "base64");

        private final int id;
        private String tiledName;

        Encoding(int id, String tiledName) {
            this.id = id;
            this.tiledName = tiledName;
        }

        public int getID() {
            return id;
        }

        public String getTiledName() {
            return tiledName;
        }
    }
}