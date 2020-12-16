package com.bg.bearplane.engine;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bg.bearplane.gui.Scene;
import com.bg.bearplane.net.BearNet;
import com.bg.bearplane.net.NetworkRegistrar;
import com.bg.bearplane.net.TCPClient;
import com.esotericsoftware.kryo.util.IntMap;

public class Bearplane extends com.badlogic.gdx.Game {

	public static Bearable game;
	public GameScreen gameScreen;
	public static BaseAssets assets;
	public NetworkRegistrar network;
	
	public static IntMap<Timer> timers = new IntMap<Timer>();

	public Bearplane(Bearable bearableGame, String[] args) {		
		super();		
		game = bearableGame;		
		assets = game.getAssets();
		Log.init(args);
		if(game instanceof TCPClient)  {
			network = (NetworkRegistrar)((BearNet)game).getNetwork();
		}
	}

	public static BaseConfig loadConfig(String filename, BaseConfig config) {
		if (BearTool.exists(filename)) {
			config = (BaseConfig)BearTool.importJSON(filename, config.getClass());
		} else {
			saveConfig(filename, config);
		}
		return config;
	}
	
	public static void saveConfig(String filename, BaseConfig config) {
		BearTool.exportJSON(filename, config);
	}
	
	public static void addTimer(int interval) {
		timers.put(interval, new Timer(game, interval));
	}
	
	@Override
	public void create() {
		try {
			game.create();
			Scene.init();
			if (game instanceof TCPClient && network != null) {
				TCPClient c = (TCPClient) game;
				network.registerClasses(c.client);
			}
			game.addScenes();
			game.addTimers();
			LoadScene ls = new LoadScene();
			ls.game = game;
			Scene.addScene("load", ls);
			Scene.change("load");
			gameScreen = new GameScreen(game);
			setScreen(gameScreen);
			preloadAssets();
		} catch (Exception e) {
			Log.error(e);
		}
	}
	
	public static void createApplication(Bearplane engine) {
		new LwjglApplication(engine, engine.getApplicationConfiguration());		
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.manager.dispose();
		game.dispose();
	}

	private static void preloadAssets() {
		assets.preloadNecessities();
		assets.preload();
	}

	public static void loadAssets() {
		assets.loadNecessities();
		assets.load();
	}

	public static void updateAssetManager() {
		try {
			assets.manager.update();
		} catch (Exception e) {
			Log.error(e);
		}
	}

	public static void loaded() {
		game.loaded();
	}

	public static boolean isAssetLoadingDone() {
		try {
			return assets.manager.isFinished();
		} catch (Exception e) {
			Log.error(e);
		}
		return false;
	}

	public static float getAssetLoadProgress() {
		try {
			return assets.manager.getProgress();
		} catch (Exception e) {
			Log.error(e);
		}
		return 0;
	}

	public LwjglApplicationConfiguration getApplicationConfiguration() {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		try {
			String name = game.getGameName();
			int windowWidth;
			int windowHeight;
			int dm = game.getDisplayMode();
			Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();				
			if(dm == DisplayMode.WINDOW) {
				cfg.undecorated = false;
				windowWidth = game.getWindowWidth();
				windowHeight = game.getWindowHeight();				
			} else {			
				cfg.undecorated = true;
				windowWidth = dimension.width;
				windowHeight = dimension.height;
			}		
			cfg.addIcon(game.getAssetsPath() + "/icon.png", FileType.Local);
			cfg.title = name;
			cfg.width = windowWidth;
			cfg.height = windowHeight;
			cfg.resizable = game.isResizable();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			cfg.y = screenSize.height / 2 - cfg.height / 2 - 32;
			cfg.x = 0;
			cfg.y = 0;
	        cfg.resizable = false;	        
	        cfg.vSyncEnabled = game.isvSync();
	        cfg.fullscreen = dm == DisplayMode.FULLSCREEN;
			if (cfg.fullscreen) {
				cfg.width = (int) Math.round(screenSize.getWidth());
				cfg.height = (int) Math.round(screenSize.getHeight());
				cfg.resizable = false;
			}
		} catch (Exception e) {
			Log.error(e);
		}
		return cfg;
	}

}
