package com.bg.badgame.core;

import com.bg.bearplane.engine.Bearable;
import com.bg.bearplane.engine.Bearplane;
import com.bg.bearplane.engine.Log;
import com.bg.bearplane.gui.Scene;
import com.gdx420.badgame.scenes.MenuScene;
import com.gdx420.badgame.scenes.OptionsScene;
import com.gdx420.badgame.scenes.PlayScene;
import com.gdx420.badgame.scenes.UpdateScene;

public class BadGame implements Bearable {
	// singleton pattern
	public static BadGame getInstance() {
		return instance;
	}
	private static final BadGame instance = new BadGame();	
	private BadGame() {
		config = (Config)Bearplane.loadConfig(CONFIG_FILE, config);
	}
	public static final boolean IS_RELEASE = false; // change to true for release
	public static final int GAME_WIDTH = 1366;
	public static final int GAME_HEIGHT = 768;
	public static final String GAME_NAME = "Shoot shoot";
	public static final String CLIENT_VERSION = "bananas17";
	public static final String EFFECTS_PATH = "assets/effects";
	public static final String ASSETS_PATH = "assets";
	public static final String CONFIG_FILE = "config.txt";
	
	public static Assets assets = new Assets();
	public Realm realm = new Realm(assets);
	public Config config = new Config();

	// timing
	long tick = 0;

	// state variables
	public boolean playing = false;

	// scenes
	public static OptionsScene optionsScene = new OptionsScene();
	public static UpdateScene updateScene = new UpdateScene();
	public static PlayScene playScene = new PlayScene();
	

	public void create() {
		Log.info(GAME_NAME + " Initializing");		
	}
	
	public void play(PlayScene.Characters characterSelected) {
		Scene.change("play");
		playScene.play(characterSelected);
	}
	
	public void resetLevel() {
		playScene.resetLevel();
	}

	@Override
	public void addTimers() {
		try {
			Bearplane.addTimer(250);
			Bearplane.addTimer(1000);
		} catch (Exception e) {
			Log.error(e);
		}
	}

	@Override
	public void doTimer(int interval) {
		try {
			switch (interval) {
			case 250:
				break;
			case 1000:
				secondTimer();
				break;
			}
		} catch (Exception e) {
			Log.error(e);
		}
	}

	void secondTimer() {

	}

	@Override
	public void update() {
		try {
			//shouldnt actually need to do a whole lot here :)
			//realm and scene have their updates called for them, there's timers, and most logic is more appropriately done in Realm or Map
		} catch (Exception e) {
			Log.error(e);
		}
	}

	@Override
	public void addScenes() {
		Scene.addScene("menu", new MenuScene());
		Scene.addScene("update", updateScene);
		Scene.addScene("options", optionsScene);
		Scene.addScene("play", playScene);
	}

	@Override
	public void loaded() {
		realm.load();
		Scene.change("menu");
	}

	@Override
	public void dispose() {
		assets.dispose();
	}

	@Override
	public int getGameWidth() {
		return GAME_WIDTH;
	}

	@Override
	public int getGameHeight() {
		return GAME_HEIGHT;
	}

	@Override
	public String getGameName() {
		return GAME_NAME;
	}

	public String getClientVersion() {
		return CLIENT_VERSION;
	}

	public String getEffectsPath() {
		return EFFECTS_PATH;
	}

	public String getAssetsPath() {
		return ASSETS_PATH;
	}

	public int getDisplayMode() {
		return config.DISPLAY_MODE;
	}

	public boolean isResizable() {
		return config.RESIZABLE;
	}

	public int getWindowWidth() {
		return config.WINDOW_WIDTH;
	}

	public int getWindowHeight() {
		return config.WINDOW_HEIGHT;
	}

	public boolean isvSync() {
		return config.ISVSYNC;
	}

	public Assets getAssets() {
		return assets;
	}

	public Object getNetwork() {
		return null;
	}

	public boolean isRelease() {
		return IS_RELEASE;
	}
	
	public Realm getRealm() {
		return realm;
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void show() {
		
	}

	@Override
	public void pause() {

		
	}
	
	public void setTick(long tick) {
		this.tick = tick;
	}
	
}
