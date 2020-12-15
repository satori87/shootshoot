package com.bg.badgame.core;

import java.util.ArrayList;
import java.util.List;

import com.bg.bearplane.engine.BearTool;

public class Map {
	public int id;
	public String name;
	public List<Prop> props = new ArrayList<Prop>();
	public List<Enemy> enemies = new ArrayList<Enemy>();
	public List<Bullet> bullets = new ArrayList<Bullet>();
	public int g = 0;
	
	public int key = -1;
	public int lock = -1;

	public Map(int id, String name) {
		this.id = id;
		this.name = name;
		int n = BearTool.randInt(7, 16);
		for (int i = 0; i < n; i++) {
			props.add(new Prop(i));
		}
		g = BearTool.randInt(0, 3);
		//Prop p = new Prop(0);
		//p.npc = true;
		//p.type = 0;
		//p.x = 400;
		//p.y = 400;
		//props.add(p);
		n = BearTool.randInt(4, 8);
		for (int i = 0; i < n; i++) {
			enemies.add(new Enemy(i));
		}

		
	}
}
