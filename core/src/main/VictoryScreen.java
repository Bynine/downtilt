package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import challenges.Victory;

public class VictoryScreen extends Menu{
	
	private static Victory currentVictory;

	static void update(){
		Menu.update();
		if (DowntiltEngine.getPrimaryInputHandler().chargeHold())	start();
		draw();
	}
	
	public static void start(Victory v){
		currentVictory = v;
		SaveHandler.writeScore(0, 0, v.getScore());
		SaveHandler.save();
	}
	
	private static void draw(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.67f, 0.85f, 0.99f, 1);
		int startY = 500;
		int startX = 300;
		int centerX = 260;
		int dec = 50;
		
		batch.begin();
		batch.draw(menu, 0, 0);
		specialFont.draw(batch, "VICTORY!", startX + centerX, startY);
		specialFont.draw(batch, "RANK: " + currentVictory.getRanking(), startX + centerX, startY -= dec * 2);
		startY -= dec;
		if (currentVictory.getTime() != Victory.NOTUSED) font.draw(batch, "TIME TAKEN:    " + GlobalRepo.getTimeString(currentVictory.getTime()), startX, startY -= dec);
		if (currentVictory.getCombo() != Victory.NOTUSED) font.draw(batch, "LONGEST COMBO: " + currentVictory.getCombo(), startX, startY -= dec);
		if (currentVictory.getKOs() != Victory.NOTUSED) font.draw(batch, "ENEMIES KO'd:  " + currentVictory.getKOs(), startX, startY -= dec);
		if (currentVictory.getDeaths() != Victory.NOTUSED) font.draw(batch, "DEATHS: " + currentVictory.getDeaths(), startX, startY -= dec);
		font.draw(batch, "SCORE TOTAL:   " + currentVictory.getScore(), startX, startY -= dec);
		specialFont.draw(batch, "Press Y to continue", startX, startY -= dec);
		batch.end();
	}
	
	private static void start(){
		DowntiltEngine.startMenu();
	}
	
}
