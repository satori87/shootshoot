package com.bg.badgame.core;

import java.io.File;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.bg.bearplane.engine.BaseAssets;

public class Assets extends BaseAssets {

	public static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	
	public void preload() {		
		//add any customer preloading here
		File f = new File("sfx");
		preloadAllPNGFromDir("assets");	
		for (String pathname : f.list()) {
            // Print the names of files and directories
        	//manager.load("sfx/" + pathname, Sound.class);
        	//Sound sound = Gdx.audio.newSound(Gdx.files.internal("sfx/" + pathname));
        	//sounds.put(pathname.substring(0,pathname.length()-4),sound);
           // System.out.println(pathname.substring(0,pathname.length()-4));
        }
		
	}

	public void load() {
		//add any custom loading here
		File f = new File("sfx");

        // Populates the array with names of files and directories
        //pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : f.list()) {
            // Print the names of files and directories
        	//Sound sound = manager.get("sfx/" + pathname);
        	Sound sound = Gdx.audio.newSound(Gdx.files.local("sfx/" + pathname));
        	sounds.put(pathname.substring(0,pathname.length()-4),sound);
           // System.out.println(pathname.substring(0,pathname.length()-4));
        }
		//
	}
}
