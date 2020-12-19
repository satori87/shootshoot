package com.bg.badgame.core;

import com.bg.bearplane.engine.BearTool;

public class Prop {
	public Position position = new Position();
	public float rotation;
	public float radius;
	public int id, type;
	public boolean npc = false;

	public Prop(int id) {
		this.id = id;
		type = BearTool.rndInt(0, 6);
		position.x = (float) ((Math.random() * (BadGame.GAME_WIDTH-40) * 2) / 2) + 40;
		position.y = (float) ((Math.random() * (BadGame.GAME_HEIGHT-40) * 2) / 2) + 40;
		rotation = BearTool.randInt(0, 359);
		radius = 16;
	}
}
