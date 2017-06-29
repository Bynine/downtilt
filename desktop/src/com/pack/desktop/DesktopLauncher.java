package com.pack.desktop;

import main.GraphicsHandler;
import main.DowntiltEngine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	
	private static final int FPS = 60;
	private static final int DONTRENDER = -1;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Down Tilt";
		cfg.vSyncEnabled = false;
		cfg.foregroundFPS = FPS;
		cfg.backgroundFPS = DONTRENDER;
		cfg.width = GraphicsHandler.SCREENWIDTH;
		cfg.height= GraphicsHandler.SCREENHEIGHT;
		new LwjglApplication(new DowntiltEngine(), cfg);
	}
	
}