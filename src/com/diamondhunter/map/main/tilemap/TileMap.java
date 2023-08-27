package com.diamondhunter.map.main.tilemap;

import com.diamondhunter.map.main.block.Block;
import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class TileMap {
	protected com.diamondhunter.map.main.Map map;
	protected Map<String, Block> blocks;

	public TileMap(com.diamondhunter.map.main.Map map) {
		this.map = map;
		blocks = new LinkedHashMap<>();
	}

	public void render(Graphics2D g) {
		for (Block block : blocks.values()) {
			block.render(g);
		}
	}

	public void update() {
		for (Block block : blocks.values()) {
			block.update();
		}
	}

	public void input(KeyHandler keyHandler, MouseHandler mouseHandler) {
		for (Block block : blocks.values()) {
			block.input(keyHandler, mouseHandler);
		}
	}

	public com.diamondhunter.map.main.Map getMap() {
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Map){
			return blocks.equals(obj);
		} else if(obj instanceof com.diamondhunter.map.main.Map){
			return map.equals(obj);
		}

		return super.equals(obj);
	}

	public Map<String, Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(Map<String, Block> blocks) {
		this.blocks = blocks;
	}
}
