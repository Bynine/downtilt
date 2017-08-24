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
		font.draw(batch, "SCORE TOTAL:   " + currentVictory.getScore(), startX, posY -= dec);
		if (DowntiltEngine.getMode().getTime() > 0) {
			font.draw(batch, "TIME TOTAL:   " + GlobalRepo.getTimeString(DowntiltEngine.getMode().getTime()), startX, posY -= dec);
		}
		GlobalRepo.drawBonusList(startX + 600, startY, currentVictory.getBonuses(), batch, font);
		
		batch.end();
	}

	@Override
	protected void advance(){
		new SFX.Advance().play();
		DowntiltEngine.startGameMenu();
	}

}
