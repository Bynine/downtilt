package main;

import challenges.Victory;

public class VictoryScreen extends Menu{

	private Victory currentVictory;

	void update(){
		super.update();
		draw();
	}

	public void start(Victory v){
		currentVictory = v;
		SaveHandler.writeScore(v.getNumberX(), v.getNumberY(), v.getScore());
		SaveHandler.save();
	}

	@Override
	protected void draw(){
		int startY = 500;
		int startX = 300;
		int centerX = 260;
		int dec = 50;

		super.draw();
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
		specialFont.draw(batch, "Press A to continue", startX, startY -= dec);
		batch.end();
	}
	
	@Override
	protected void advance(){
		DowntiltEngine.startGameMenu();
	}

}
