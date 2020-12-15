package com.bg.badgame.core;

import com.bg.bearplane.engine.Log;
import com.gdx420.badgame.scenes.PlayScene;

public class Player {
	public static float x = 200;
	public static float y = 300;

	public static boolean[] key = new boolean[3];
	public static boolean[] lock = new boolean[3];

	public static int hp = 100;
	public void hit(Enemy e) {
		Log.debug("player hit enemy");
		e.hp--;
		if (e.hp <= 0) {
			e.die();
		}
		//PlayScene.showDialog("You hit the enemy. The enemy was hit by you. The gun, you fired. It's bullet, it reeached a target. Wow. Remind me not to piss you off.");
	}

	public void die() {
		PlayScene.showDialog("You got dead. Try again.");
		PlayScene.play.reset();
		PlayScene.level--;
		Player.hp = 100;
	}
}
