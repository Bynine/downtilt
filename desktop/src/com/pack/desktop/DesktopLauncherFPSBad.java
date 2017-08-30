package com.pack.desktop;

import main.GraphicsHandler;
import main.DowntiltEngine;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncherFPSBad {
	
	private static final int FPS = DowntiltEngine.FPSBad;
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
		DowntiltEngine.FPS = DowntiltEngine.FPSBad;
		new LwjglApplication(new DowntiltEngine(), cfg);
	}
	
}