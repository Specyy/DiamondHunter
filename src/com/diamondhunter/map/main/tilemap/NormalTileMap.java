package com.diamondhunter.map.main.tilemap;

import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.map.main.Map;
import com.diamondhunter.map.main.block.Block;
import com.diamondhunter.map.main.block.NormalBlock;
import com.diamondhunter.map.tiled.layer.TiledTileLayer;
import com.diamondhunter.map.tiled.tileset.TiledTile;
import com.diamondhunter.util.logic.Vector2f;

public class NormalTileMap extends TileMap {

    private final TiledTileLayer layer;

    public NormalTileMap(Map map, TiledTileLayer.TiledTileLayerData data) {
        super(map);
        this.layer = data.getLayer();

        for (int i = 0; i < data.getSplitData().length; i++) {
            String indexDataString = data.getSplitData()[i];
            indexDataString = indexDataString.stripLeading().stripTrailing().trim();
            long indexData = Long.parseLong(indexDataString);
            TiledTile tile = this.layer.getTile(indexData);

            Vector2f pos = new Vector2f((i % layer.getWidth()) * tile.getTiledTileset().getTileWidth(), (i / layer.getWidth()) * tile.getTiledTileset().getTileHeight());
            Sprite sprite = new Sprite(tile);
            Block block = new NormalBlock(pos, sprite);
            block.setScale(1);
            blocks.put((i % layer.getWidth()) + "," + (i / layer.getWidth()), block);
        }
    }

    public TiledTileLayer getReferenceLayer() {
        return layer;
    }
}
