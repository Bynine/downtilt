package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import challenges.Victory;
import challenges.Victory.Ranking;

public class VictoryScreen extends Menu{

	private Victory currentVictory;

	public void start(Victory v){
		canBack = false;
		currentVictory = v;
		SaveHandler.writeScore(v.getNumberX(), v.getNumberY(), v.getScore());
		SaveHandler.save();

		tile = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/tile_victory.png")));
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
		bigFont.draw(batch, "VICTORY!", startX + centerX, startY);
		
		bigFont.draw(batch, "RANK: ", startX + centerX, startY -= dec * 2);
		
		Ranking r = currentVictory.getRanking(currentVictory.getScore());
		bigFont.setColor(GlobalRepo.getColorByRanking(r));
		bigFont.draw(batch, r.toString(), startX + centerX + 200, startY);
		bigFont.setColor(Color.GOLDENROD);
		
		if (currentVictory.getScore() >= Victory.AdventureVictory.A && currentVictory.getNumberX() == 0){
			if (currentVictory.getNumberY() == 1 && !SaveHandler.AdvancedUnlocked()) {
				font.draw(batch, "Unlocked Advanced difficulty!", startX + centerX, startY -= dec);
			}
			else if (currentVictory.getNumberY() == 2 && !SaveHandler.NightmareUnlocked()) {
				font.draw(batch, "Unlocked Nightmare difficulty!", startX + centerX, startY -= dec);
			}
		}
		else startY -= dec;
		if (currentVictory.getTime() != Victory.NOTUSED) font.draw(batch, "TIME TAKEN:    " + GlobalRepo.getTimeString(currentVictory.getTime()), startX, startY -= dec);
		if (currentVictory.getCombo() != Victory.NOTUSED) font.draw(batch, "LONGEST COMBO: " + currentVictory.getCombo(), startX, startY -= dec);
		if (currentVictory.getKOs() != Victory.NOTUSED) font.draw(batch, "ENEMIES KO'd:  " + currentVictory.getKOs(), startX, startY -= dec);
		if (currentVictory.getDeaths() != Victory.NOTUSED) font.draw(batch, "DEATHS: " + currentVictory.getDeaths(), startX, startY -= dec);
		font.draw(batch, "SCORE TOTAL:   " + currentVictory.getScore(), startX, startY -= dec);
		bigFont.draw(batch, "Press " + DowntiltEngine.getPrimaryInputHandler().getNormalString() + " to continue", startX, startY -= dec);
		batch.end();
	}

	@Override
	protected void advance(){
		DowntiltEngine.startGameMenu();
	}

}
