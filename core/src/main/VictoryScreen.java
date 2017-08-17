package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import challenges.Bonus;
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

	final int startY = 500;
	final int startX = 200;
	
	@Override
	protected void draw(){
		int posY = startY;
		int dec = 50;

		super.draw();
		batch.begin();
		bigFont.draw(batch, "VICTORY!", startX, posY);
		
		bigFont.draw(batch, "RANK: ", startX, posY -= dec * 2);
		
		Ranking r = currentVictory.getRanking(currentVictory.getScore());
		bigFont.setColor(GlobalRepo.getColorByRanking(r));
		bigFont.draw(batch, r.toString(), startX + 200, posY);
		bigFont.setColor(Color.GOLDENROD);
		drawBonusList();
		
		font.draw(batch, "SCORE TOTAL:   " + currentVictory.getScore(), startX, posY -= dec);
		batch.end();
	}
	
	private void drawBonusList(){
		final int stringLocationX = startX + 600;
		final int bonusLineHeight = 24;
		final int maxListHeight = 400;
		
		int listHeight = bonusLineHeight * currentVictory.getBonuses().size();
		int scrollDownDistance = 0;
		if (listHeight != 0) scrollDownDistance = -DowntiltEngine.getDeltaTime()/2 % listHeight;
		int i = 1;

		for (Bonus b: currentVictory.getBonuses()){
			
			String bonusString = b.getName();
			if (b.getMult() > 1) bonusString = b.getName() + " x" + b.getMult();
			if (b.getScore() > 0) bonusString = bonusString.concat(": " + b.getScore());
			
			int stringLocationY = 0;
			if (listHeight > maxListHeight) stringLocationY = startY + (scrollDownDistance - (i * bonusLineHeight)) % listHeight;
			else stringLocationY = startY + - (i * bonusLineHeight);
			if (stringLocationY < startY && stringLocationY > startY - maxListHeight) {
				font.draw(batch, bonusString, stringLocationX, stringLocationY);
			}
			i++;
		}
	}

	@Override
	protected void advance(){
		new SFX.Advance().play();
		DowntiltEngine.startGameMenu();
	}

}
