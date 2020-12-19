package com.bg.badgame.core;

import com.bg.bearplane.engine.Log;
import com.gdx420.badgame.scenes.PlayScene;
import com.bg.badgame.core.DirectionUtility.CardinalDirection;

public class Player {
	// singleton pattern
	public static Player getInstance() {
		return instance;
	}
	private static final Player instance = new Player();	
	private Player() {
		resetPlayer();
	}
	
	public void resetPlayer() {
		hp = 100;
		position = new Position(200, 300);
	}
	
	public Position position = new Position(200, 300);
	
	public CardinalDirection direction = CardinalDirection.NORTH;

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
		BadGame.getInstance().resetLevel();
		Player.hp = 100;
	}
	
	public void move(Position moveDelta) {
		position.x += moveDelta.x;
		position.y += moveDelta.y;
	}
}
