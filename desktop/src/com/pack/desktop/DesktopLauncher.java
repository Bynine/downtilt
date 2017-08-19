package com.pack.desktop;

import main.GraphicsHandler;
import main.DowntiltEngine;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	
	private static final int FPS = 55;
	private static final int DONTRENDER = -1;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "KNOCK 'EM OUT";
		cfg.vSyncEnabled = false;
		cfg.foregroundFPS = FPS;
		cfg.backgroundFPS = DONTRENDER;
		cfg.addIcon("sprites/graphics/icon16.png", FileType.Classpath);
		cfg.addIcon("sprites/graphics/icon32.png", FileType.Classpath);
		cfg.width = GraphicsHandler.SCREENWIDTH;
		cfg.height= GraphicsHandler.SCREENHEIGHT;
		new LwjglApplication(new DowntiltEngine(), cfg);
	}
	
}