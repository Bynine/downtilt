package main;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HomeMenu extends Menu {

	protected TextureRegion bg = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/homebg.png")));
	private MenuOption<Integer> choices = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(0, "gamemenu"),
			new Choice<Integer>(0, "options"),
			new Choice<Integer>(0, "rankings"),
			new Choice<Integer>(0, "credits")
			));
	
	HomeMenu(){
		cho = choices;
		canBack = false;
	}
	
	@Override
	protected void draw(){
		int posX = 230;
		int startY = 510;
		int posY = startY;
		final int dec = 60;
		
		batch.begin();
		batch.draw(bg, 0, 0, GraphicsHandler.SCREENWIDTH, GraphicsHandler.SCREENHEIGHT);
		batch.draw(title, 350, 560);
		batch.draw(logo, 8, 0, logo.getRegionWidth() * logomod, logo.getRegionHeight() * logomod);
		batch.draw(cursor, posX + cursorMod, startY - dec - 18 - cho.cursorPos() * (dec));
		font.draw(batch, "Play!", posX, posY -= dec);
		font.draw(batch, "Customize/Options", posX, posY -= dec);
		font.draw(batch, "Records", posX, posY -= dec);
		font.draw(batch, "Credits", posX, posY -= dec);
		navFont.draw(batch, "ADVANCE: " + DowntiltEngine.getPrimaryInputHandler().getNormalString(), 1000, posY - dec * 3);
		font.draw(batch, "Version: " + DowntiltEngine.version, 1000, posY - dec * 4);
		
		batch.end();
	}
	
	@Override
	protected void advance(){
		new SFX.Advance().play();
		switch(choices.cursorPos()){
		case 0: DowntiltEngine.startGameMenu(); break;
		case 1: DowntiltEngine.startOptionMenu(); break;
		case 2: DowntiltEngine.startRankingScreen(); break;
		case 3: DowntiltEngine.startCreditScreen(); break;
		default: break;
		}
	}
}
