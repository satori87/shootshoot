package com.gdx420.badgame.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.bg.badgame.core.Assets;
import com.bg.badgame.core.BadGame;
import com.bg.bearplane.engine.BearTool;
import com.bg.bearplane.gui.Scene;

public class MenuScene extends Scene {

	public static String status = "";

	public MenuScene() {

	}

	public void start() {
		super.start();
		int a = BearTool.randInt(0, 1);
		Assets.sounds.get("intro" + a).play();
		Sound s = Assets.sounds.get("mus0");
		s.loop();
		int hw = BadGame.GAME_WIDTH / 2;
		int hh = BadGame.GAME_HEIGHT / 2;
		int y = 64;

		addLabel("status", hw, hh + 256, 2f, "", Color.WHITE, true);
		addFrame("frame", hw, hh - 60 + y, 288, 392, true, true);

		addLabel("title", hw, hh - 182 + y, 3f, BadGame.getInstance().getGameName(), Color.WHITE, true);

		addButton("play", hw, hh - 96 + y, 256, 48, "PLAY");
		addButton("lol", hw, hh - 32 + y, 256, 48, "Tutorial");
		// addButton("options", hw, hh + 32 + y, 256, 48, "Options");
		addButton("quit", hw, hh + 96 + y, 256, 48, "Quit");

		addButton("c0", hw - 400, 100, 356, 48, "Artillery Clinton");
		addButton("c1", hw, 100, 356, 48, "Tank Hill");
		addButton("c2", hw + 400, 100, 356, 48, "Thomas the Tank");

		buttons.get("c0").toggle = true;
		buttons.get("c1").toggle = true;
		buttons.get("c2").toggle = true;

		buttons.get("c0").toggled = false;
		buttons.get("c1").toggled = true;
		buttons.get("c2").toggled = false;

		
		
	}

	public void update() {
	}


	public void render() {
		getLabel("status").text = status;
		
		String s = "Michael Whitlock (Bearable Games), JokeR (Bearable Games, Teflon Rocket), Mac Canepi";
		drawFont(0,1366/2, 700,s,true,1.6f);
		s = "Created by";
		drawFont(0,1366/2, 650,s,true,3f);
	}

	PlayScene.Characters characterToPlay = PlayScene.Characters.HANK;
	
	@Override
	public void buttonPressed(String id) {
		//Assets.sounds.get("click").play();
		buttons.get("c0").toggled = false;
		buttons.get("c1").toggled = false;
		buttons.get("c2").toggled = false;
		int a = 0;
		switch (id) {
		case "play":
			BadGame.getInstance().play(characterToPlay);
			Assets.sounds.get("clickplay").play();
			break;
		case "lol":
			PlayScene.tutorial = true;
			BadGame.getInstance().play(characterToPlay);
			Assets.sounds.get("clicktutorial").play();
			break;
		case "options":
			Scene.change("options");
			break;
		case "quit":
			System.exit(0);
			break;
		case "c0":
			a = BearTool.randInt(0, 1);
			Assets.sounds.get("clinton" + a).play();
			buttons.get("c0").toggled = true;
			characterToPlay = PlayScene.Characters.HILLARY;
			break;
		case "c1":
			a = BearTool.randInt(0, 2);
			Assets.sounds.get("hank" + a).play();
			buttons.get("c1").toggled = true;
			characterToPlay = PlayScene.Characters.HANK;
			break;
		case "c2":
			a = BearTool.randInt(0, 2);
			Assets.sounds.get("thomas" + a).play();
			buttons.get("c2").toggled = true;
			characterToPlay = PlayScene.Characters.THOMAS;
			break;
		}
	}

	@Override
	public void enterPressedInField(String id) {

	}

	@Override
	public void enterPressedInList(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void listChanged(String id, int sel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDown(int x, int y, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseUp(int x, int y, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkBox(String id) {
		// TODO Auto-generated method stub

	}

}
