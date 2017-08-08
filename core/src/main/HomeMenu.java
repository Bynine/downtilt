package main;

import java.util.Arrays;

public class HomeMenu extends Menu {

	private MenuOption<Integer> choices = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(0, "gamemenu"),
			new Choice<Integer>(0, "options"),
			new Choice<Integer>(0, "rankings"),
			new Choice<Integer>(0, "credits")
			));
	
	HomeMenu(){
		cho = choices;
	}
	
	@Override
	protected void draw(){
		int posX = 230;
		int startY = 510;
		int posY = startY;
		final int dec = 30;
		super.draw();
		batch.begin();

		font.draw(batch, "Play!", posX, posY -= dec);
		font.draw(batch, "Options", posX, posY -= dec);
		font.draw(batch, "Rankings", posX, posY -= dec);
		font.draw(batch, "Credits", posX, posY -= dec);
		batch.draw(cursor, posX - 48, startY - 48 - cho.cursorPos() * (dec + 1));
		
		batch.end();
	}
	
	@Override
	protected void advance(){
		switch(choices.cursorPos()){
		case 0: DowntiltEngine.startGameMenu(); break;
		case 1: DowntiltEngine.startOptionMenu(); break;
		case 2: DowntiltEngine.startRankingScreen(); break;
		case 3: DowntiltEngine.startCreditScreen(); break;
		default: break;
		}
	}
}
