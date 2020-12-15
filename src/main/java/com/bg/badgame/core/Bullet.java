package com.bg.badgame.core;

import com.bg.bearplane.engine.BearTool;
import com.gdx420.badgame.scenes.PlayScene;

public class Bullet {
	public int type;
	public float x, y, rot, rad;
	public Object owner;

	public boolean active = true;
	public boolean dead = false;
	public long diestamp = 0;

	public Bullet(int type, float x, float y, float rot, Object owner) {
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.rad = 1;
		this.type = type;
		this.owner = owner;
		int a = BearTool.randInt(0, 7);
		Assets.sounds.get("f" + a).play();
	}

	public void update(PlayScene scene, long tick) {
		if (!active)
			return;
		if (!dead) {
			float nx = x;
			float ny = y;
			nx += Math.cos(Math.toRadians(rot)) * 6;
			ny += Math.sin(Math.toRadians(rot)) * 6;
			Object col = scene.collides(owner, nx, ny, rad);
			if (col == null) {
				x = nx;
				y = ny;
			} else {
				trigger(tick, col);
			}
		} else {
			if (tick > diestamp) {
				active = false;
			}
		}
	}

	void trigger(long tick, Object col) {
		int a = 0;
		if(owner instanceof Player) {
			if(col instanceof Enemy) {
				Player p = (Player) owner;
				Enemy e = (Enemy) col;
				p.hit(e);
				a = BearTool.randInt(0, 4);
				Assets.sounds.get("he" + a).play();
			} else {
				a = BearTool.randInt(0, 4);
				Assets.sounds.get("h" + a).play();
			}
			
		} else if (owner instanceof Enemy){
			if(col instanceof Player) {
				Enemy e = (Enemy) owner;
				Player p = (Player) col;
				e.hit(p);
				a = BearTool.randInt(0, 4);
				Assets.sounds.get("hp" + a).play();
			} else {
				a = BearTool.randInt(0, 4);
				Assets.sounds.get("h" + a).play();
			}
			
		}
		dead = true;
		diestamp = tick + 250;
	}

	public void render(PlayScene scene) {
		if (!active)
			return;
		if (dead) {
			scene.draw(Assets.textures.get("bullet" + type + "e"), x - 16, y - 16, rot, 1f);
		} else {
			scene.draw(Assets.textures.get("bullet" + type), x - 16, y - 16, rot, 1f);
		}
	}
}
