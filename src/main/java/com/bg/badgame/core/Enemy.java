package com.bg.badgame.core;

import com.bg.bearplane.engine.BearTool;
import com.bg.bearplane.engine.Log;
import com.gdx420.badgame.scenes.PlayScene;

public class Enemy {
	public int id, type;
	public float x, y, rot, rad;
	public String name;
	public boolean active = true;
	boolean dead = false;
	long attackstamp = 0;
	long attacktime = 1200;
	long movestamp = 0;
	long movetime = 500;

	int hp = 3;

	public Enemy(int id) {
		this.id = id;
		x = (float) ((Math.random() * BadGame.GAME_WIDTH * 2) / 2);
		y = (float) ((Math.random() * BadGame.GAME_HEIGHT * 2) / 2);
		rot = BearTool.randInt(0, 359);
		rad = 16;
		type = BearTool.randInt(0, 4);
		name = getEnemyName(type);
	}

	String getEnemyName(int type) {
		switch (type) {
		case 0:
			return "meme man";
		case 1:
			return "stoner guy";
		case 2:
			return "triumphant baby";
		case 3:
			return "that frog";
		case 4:
			return "i dont know memes very well";
		default:
			return "gene wilder";
		}
	}

	public void render(PlayScene scene) {
		if (!active)
			return;

		scene.draw(Assets.textures.get("enemy" + type), x - 32, y - 32, rot, 1f);
	}
	boolean m = false;
	public void update(PlayScene scene, long tick) {
		if (!active)
			return;
		boolean a = false;
		
		Player player = Player.getInstance();
		if (tick > movestamp) {
			m = false;
			movestamp = tick + movetime;
			float d = (float) BearTool.distance(x, y, player.position.x, player.position.y);
			if (d < 400 && d > 120) {
				a = true;
				m = true;
				rot = (float) Math.toDegrees(Math.atan2(player.position.y - y, player.position.x - x));
			} else if (d < 400) {
				a = true;
				m = false;
			} else {
				rot = (float) (Math.random() * 360);
				m = true;
			}
		} else if(m){
			float nx = x;
			float ny = y;
			nx += Math.cos(Math.toRadians(rot)) * 1;
			ny += Math.sin(Math.toRadians(rot)) * 1;
			if (scene.collides(this, nx, ny, rad) == null) {
				x = nx;
				y = ny;
			} else {
				movestamp = 0;
			}
			if (x < 0)
				x = 0;
			if (y < 0)
				y = 0;
			if (y >= BadGame.GAME_HEIGHT)
				y = BadGame.GAME_HEIGHT;
			if (x > BadGame.GAME_WIDTH)
				x = BadGame.GAME_WIDTH;
		}
		if (a && tick > attackstamp) {
			Map m;
			attackstamp = tick + attacktime;
			scene.maps[scene.currentMap].bullets.add(new Bullet(0, x, y, rot, this));
		}

	}

	public void die() {
		active = false;
	}

	public void hit(Player p) {
		Player.hp--;
		if(Player.hp <= 0) {
			p.die();
		}
	}

}