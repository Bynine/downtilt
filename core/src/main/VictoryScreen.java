package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import challenges.Victory;
import challenges.Victory.Ranking;

public class VictoryScreen extends Menu{

	private Victory currentVictory;
	private boolean drawConclusion = false;
	private static TextureRegion sceneConclusion = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/scene_conclusion.png")));

	public void start(Victory v){
		if (v instanceof Victory.AdventureVictory) {
			drawConclusion = true;
		}
		else {
			drawConclusion = false;
		}
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

		if (drawConclusion) {
			batch.begin();
			batch.draw(sceneConclusion, 0, 0, GraphicsHandler.SCREENWIDTH, GraphicsHandler.SCREENHEIGHT);
			batch.end();
		}
		else drawBackground();
		drawForeground();
		
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
	protected void drawForeground(){
		batch.begin();
		if (!drawConclusion) batch.draw(menu, 0, 0);
		batch.draw(logo, 8, 0, logo.getRegionWidth() * logomod, logo.getRegionHeight() * logomod);
		batch.draw(title, 350, 560);
		final int posY = 50;
		if (canAdvance) navFont.draw(batch, "ADVANCE: " + DowntiltEngine.getPrimaryInputHandler().getNormalString(), 1000, posY);
		if (canBack) navFont.draw(batch, "BACK: " + DowntiltEngine.getPrimaryInputHandler().getSpecialString(), 200, posY);
		batch.end();
	}

	@Override
	protected void advance(){
		new SFX.Advance().play();
		DowntiltEngine.startGameMenu();
	}

}
