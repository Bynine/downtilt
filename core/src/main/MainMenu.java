package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import entities.*;

class MainMenu extends Menu {

//	private static List<PlayerType> playableCharacters = new ArrayList<PlayerType>(Arrays.asList(
//			new PlayerType(Hero.class, "Hero")
//			));
//	private static MenuOption<PlayerType> p1Char = new MenuOption<PlayerType>(playableCharacters);
//	private static MenuOption<PlayerType> p2Char = new MenuOption<PlayerType>(playableCharacters);
	private static MenuOption<Integer> players = new MenuOption<Integer>(Arrays.asList(
			1, 2
			));
	private static MenuOption<String> choices = new MenuOption<String>(Arrays.asList(/*"P1CHAR", "P2CHAR",*/ "PLAYERS"));
	private static List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, /*p1Char, p2Char,*/ players));
	
	private static TextureRegion cursor = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/cursor.png")));
	private static TextureRegion title = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/title.png")));
	private static final Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
	
	public static void initialize(){
		Menu.initialize();
		menuMusic.setLooping(true);
		begin();
	}
	
	public static void begin(){
		if (DowntiltEngine.musicOn()) menuMusic.play();
	}

	enum MenuChoice{
		CHARACTER, PLAYERS
	}

	static void update() {
		Menu.update();
		if (DowntiltEngine.getPrimaryInputHandler().flickLeft())	handleCursor(-1, options, choices);
		if (DowntiltEngine.getPrimaryInputHandler().flickRight())	handleCursor(1, options,choices);
		if (DowntiltEngine.getPrimaryInputHandler().flickUp())		choices.moveCursor(-1);
		if (DowntiltEngine.getPrimaryInputHandler().flickDown())	choices.moveCursor(1);
		if (DowntiltEngine.getPrimaryInputHandler().attackHold())	start();

		draw();
	}

	private static void draw(){
		int posX = 400;
		int startY = 600;
		int dec = 60;
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.07f, 0.09f, 0.09f, 1);
		batch.begin();
		
		batch.draw(title, posX, startY + 50);

		font.draw(batch, startStr,									posX, startY);
//		font.draw(batch, "P1 CHAR: " + p1Char.selected().name,		posX, startY -= dec);
//		
//		if (players.selected() == 1) font.setColor(0.4f, 0.4f, 0.4f, 0.4f);
//		font.draw(batch, "P2 CHAR: " + p2Char.selected().name,		posX, startY -= dec);
//		font.setColor(fontColor);
		
		String playerMode = "";
		switch(players.selected()){
		case 1: playerMode = "SOLO >"; break;
		default: playerMode = "< COOP"; break;
		}
		font.draw(batch, "PLAYER MODE: " + playerMode , posX, startY -= dec);

		batch.draw(cursor, posX - 50, 582 - dec * (choices.cursorPos() + 1));
		batch.end();
	}

	private static void start(){
		//ArrayList<PlayerType> newPlayers = new ArrayList<PlayerType>(Arrays.asList(p1Char.selected(), p2Char.selected()));
		ArrayList<PlayerType> newPlayers = new ArrayList<PlayerType>(Arrays.asList(new PlayerType(Hero.class, "Hero"), new PlayerType(Hero.class, "Hero")));
		DowntiltEngine.startNewChallenge(makePlayers(players.selected(), newPlayers), 0);
		menuMusic.stop();
	}


}
