package com.bg.badgame.core;

import com.bg.bearplane.engine.BearTool;

public class Prop {
	public float x, y, rot, rad;
	public int id, type;
	public boolean npc = false;

	public Prop(int id) {
		this.id = id;
		type = BearTool.rndInt(0, 6);
		x = (float) ((Math.random() * (BadGame.GAME_WIDTH-40) * 2) / 2) + 40;
		y = (float) ((Math.random() * (BadGame.GAME_HEIGHT-40) * 2) / 2) + 40;
		rot = BearTool.randInt(0, 359);
		rad = 16;
	}
}
