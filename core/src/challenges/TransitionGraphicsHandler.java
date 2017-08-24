package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.DowntiltEngine;
import main.GlobalRepo;
import main.GraphicsHandler;
import main.SFX;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class TransitionGraphicsHandler {
		
	private static SpriteBatch batch = null;
	private static BitmapFont font = null;
	private static final Timer readyGoTimer = new Timer(90), finishTimer = new Timer(90), failureTimer = new Timer(90);
	private static final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(readyGoTimer, finishTimer, failureTimer));
	private static Animation readyGo = GlobalRepo.makeAnimation("sprites/graphics/letsknockemout.png", 2, 1, 60, PlayMode.LOOP);
	private static TextureRegion currentFinish;
	private static TextureRegion finish1 = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/finish1.png")));
	private static TextureRegion finish2 = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/finish2.png")));
	private static TextureRegion finish3 = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/finish3.png")));
	private static Animation failure = GlobalRepo.makeAnimation("sprites/graphics/ko.png", 1, 1, 60, PlayMode.LOOP);
	private static TextureRegion victoryOverlay = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/overlay_victory.png")));
	private static TextureRegion menuSlim = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/menuSlim.png")));
	private static TextureRegion border = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/border.png")));
	
	public static void update(){
		if (batch == null) {
			batch = new SpriteBatch();
			currentFinish = finish1;
		}
		if (font == null){
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nes.ttf"));
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
			parameter.color = Color.WHITE;
			parameter.size = 16;
			parameter.spaceY = 2;
			font = generator.generateFont(parameter);
		}
		for (Timer t: timerList) t.countUp();
		
		batch.begin();
		if (!failureTimer.timeUp()) batch.draw(failure.getKeyFrame(failureTimer.getCounter()), 380, 500);
		else if (!finishTimer.timeUp()) batch.draw(currentFinish, 320, 500);
		else if (!readyGoTimer.timeUp()) batch.draw(readyGo.getKeyFrame(readyGoTimer.getCounter()), 360, 500);
		batch.end();
	}
	
	static void readyGo(){
		new SFX.ReadySetGo().play();
		readyGoTimer.reset();
		DowntiltEngine.wait(60);
	}
	
	static void finish(){
		if (Math.random() < 1.0/3.0) currentFinish = finish1;
		else if (Math.random() < 1.0/2.0) currentFinish = finish2;
		else currentFinish = finish3;
		new SFX.Finish().play();
		finishTimer.reset();
		DowntiltEngine.wait(60);
	}
	
	static void failure(){
		failureTimer.reset();
		DowntiltEngine.wait(60);
	}
	
	public static void drawTransition(List<Bonus> bonuses, int totalScore){	
		final int startX = 200;
		final int startY = 600;
		final int bonusListXMod = 600;
		final int menuSlimXMod = -32;
		final int centerMod = 340;
		int posY = startY;
		int dec = 24;
		int score = 0;
		for (Bonus b: bonuses){
			score += b.getScore();
		}
		batch.begin();
		batch.draw(victoryOverlay, 0, 0, GraphicsHandler.SCREENWIDTH, GraphicsHandler.SCREENWIDTH);
		batch.draw(menuSlim, startX + menuSlimXMod, startY - menuSlim.getRegionHeight());
		batch.draw(menuSlim, startX + menuSlimXMod + bonusListXMod, startY - menuSlim.getRegionHeight());
		batch.draw(border, 0, 0, GraphicsHandler.SCREENWIDTH, GraphicsHandler.SCREENHEIGHT);
		font.draw(batch, "ROUND COMPLETE!", startX + centerMod, posY + dec * 2);
		font.draw(batch, "Press " + DowntiltEngine.getPrimaryInputHandler().getNormalString() + " to continue", startX + centerMod - 40, 30);
		font.draw(batch, "Round Score: " + score, startX, posY -= dec);
		font.draw(batch, "Total Score: " + totalScore, startX, posY -= dec);
		GlobalRepo.drawBonusList(startX + bonusListXMod, startY - dec, bonuses, batch, font);
		batch.end();
	}
	
}
