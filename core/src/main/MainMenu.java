package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import challenges.Adventure;
import challenges.Endless;
import challenges.Mode;
import challenges.TimeTrial;
import maps.*;

class MainMenu extends Menu {
	
	private static final String str_ADVENTURE = "Adventure >";
	private static final String str_TIMETRIAL = "< Time Trial >";
	private static final String str_ENDLESS = "< Endless";
	private static MenuOption<String> mode = new MenuOption<String>(Arrays.asList(
			str_ADVENTURE, str_TIMETRIAL, str_ENDLESS
			));
	
	private static MenuOption<Class<? extends Stage>> stages = new MenuOption<Class<? extends Stage>>(Arrays.asList(
			Stage_Standard.class, Stage_Rooftop.class, Stage_Truck.class, Stage_Blocks.class,
			Stage_Mushroom.class, Stage_Space.class, Stage_Sky.class
			));
	private static MenuOption<Integer> players = new MenuOption<Integer>(Arrays.asList(
			1, 2
			));
	
	private static MenuOption<String> choices = new MenuOption<String>(Arrays.asList("MODE", "STAGE", "PLAYERS"));
	private static List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, mode, stages, players));

	private static TextureRegion cursor = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/cursor.png")));
	private static TextureRegion title = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/title.png")));
	private static TextureRegion menu = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/menu.png")));
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
		int posX = 200;
		int startY = 500;
		int dec = 60;
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.67f, 0.85f, 0.99f, 1);
		batch.begin();

		batch.draw(menu, 0, 0);
		int centerInc = 320;
		batch.draw(title, posX + centerInc, startY + 100);
		font.draw(batch, startStr, posX + centerInc, startY);

		font.draw(batch, "MODE: " + mode.selected(), posX, startY -= dec);
		if (mode.selected() == str_ADVENTURE) font.setColor(0.4f, 0.4f, 0.4f, 0.4f);
		font.draw(batch, "STAGE: " + getStage().getName(), posX, startY -= dec);
		font.setColor(fontColor);
		
		String playerMode = "";
		switch(players.selected()){
		case 1: playerMode = "Solo >"; break;
		default: playerMode = "< Co-op"; break;
		}
		font.draw(batch, "PLAYERS: " + playerMode , posX, startY -= dec);

		startY -= dec;
		font.draw(batch, "P1: " + DowntiltEngine.getPlayers().get(0).getInputHandler().getControllerName(), posX,  startY -= dec);
		if (DowntiltEngine.getPlayers().size() > 1){
			if (players.selected() == 1) font.setColor(0.4f, 0.4f, 0.4f, 0.4f);
			font.draw(batch, "P2: " + DowntiltEngine.getPlayers().get(1).getInputHandler().getControllerName(), posX,  startY -= dec/2);
			font.setColor(fontColor);
		}

		batch.draw(cursor, posX - 50, (420) - dec * (choices.cursorPos()));
		batch.end();
	}

	private static void start(){
		Mode selectMode;
		Stage stage = getStage();
		
		switch(mode.selected()){
		case str_ADVENTURE: {
			selectMode = new Adventure();
		} break;
		case str_TIMETRIAL: {
			selectMode = new TimeTrial(stage);
		} break;
		case str_ENDLESS: {
			selectMode = new Endless(stage);
		} break;
		default: {
			selectMode = null;
		} break;
		}
		
		DowntiltEngine.startMode(selectMode, players.selected(), 0);
		menuMusic.stop();
	}
	
	private static Stage getStage(){
		try {
			return stages.selected().getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			System.out.println(e);
			return  null;
		}
	}


}
