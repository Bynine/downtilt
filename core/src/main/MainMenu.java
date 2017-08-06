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
import challenges.Challenge.Difficulty;
import challenges.Endless;
import challenges.Mode;
import challenges.TimeTrial;
import challenges.Training;
import challenges.Tutorial;
import maps.*;

class MainMenu extends Menu {

	private static final String str_ADVENTURE = "Adventure";
	private static final String str_TIMETRIAL = "Time Trial";
	private static final String str_ENDLESS = "Endless";
	private static final String str_TUTORIAL = "Tutorial";
	private static final String str_TRAINING = "Training";
	private static MenuOption<String> mode = new MenuOption<String>(Arrays.asList(
			new Choice<String>(str_TUTORIAL, "Learn how to play!"),
			new Choice<String>(str_TRAINING, "Test out your moves against dummies!"),
			new Choice<String>(str_ADVENTURE, "Battle through a variety of stages!"), 
			new Choice<String>(str_TIMETRIAL, "Knock 'em out quick! Combos extend your time!"),  
			new Choice<String>(str_ENDLESS, "Survive endless enemies! Combos restore your health!")
			));

	private static MenuOption<Class<? extends Stage>> stages = new MenuOption<Class<? extends Stage>>(Arrays.asList(
			new Choice<Class<? extends Stage>>(Stage_Standard.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Rooftop.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Truck.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Blocks.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Mushroom.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Space.class, ""), 
			new Choice<Class<? extends Stage>>(Stage_Sky.class, "")
			));

	private static MenuOption<Difficulty> difficulty = new MenuOption<Difficulty>(Arrays.asList(
			new Choice<Difficulty>(Difficulty.Beginner, "Easygoing, perfect for a first time."),
			new Choice<Difficulty>(Difficulty.Standard, "A solid standard experience."),
			new Choice<Difficulty>(Difficulty.Advanced, "Random stages and extra challenge."),
			new Choice<Difficulty>(Difficulty.Nightmare, "Good luck!")
			));

	private static MenuOption<Integer> players = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(1, ""),
			new Choice<Integer>(2, "")
			));

	private static MenuOption<Integer> choices = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(0, ""),
			new Choice<Integer>(0, ""),
			new Choice<Integer>(0, ""),
			new Choice<Integer>(0, "")
			));
	private static List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, mode, difficulty, stages, players));

	private static TextureRegion cursor = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/cursor.png")));
	private static TextureRegion title = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/title.png")));
	private static final Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));

	public static void initialize(){
		Menu.initialize();
		stages.randomize();
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
		int posX = 230;
		int startY = 510;
		int dec = 30;
		final float greyOut = 0.3f;
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(112.0f/255.0f, 233.0f/255.0f, 0.99f, 1);
		batch.begin();

		batch.draw(menu, 0, 0);
		batch.draw(title, posX + 120, startY + 50);
		specialFont.draw(batch, startStr, posX + 180, startY);

		startY -= dec;
		font.draw(batch, appendCursors("MODE:   ", mode) + mode.selected().t, posX, startY -= dec);
		font.draw(batch, mode.getDesc(), posX, startY -= dec);

		if (greyOutDifficultySelect()) font.setColor(greyOut, greyOut, greyOut, greyOut);
		font.draw(batch, appendCursors("RANK:   ", difficulty) + difficulty.selected().t, posX, startY -= dec);
		if (greyOutDifficultySelect()) font.setColor(greyOut, greyOut, greyOut, 0);
		font.draw(batch, difficulty.getDesc(), posX, startY -= dec);
		font.setColor(fontColor);

		
		if (greyOutStageSelect()) font.setColor(greyOut, greyOut, greyOut, greyOut);
		font.draw(batch, appendCursors("STAGE:  ", stages) + getStage().getName(), posX, startY -= dec);
		if (greyOutStageSelect()) font.setColor(greyOut, greyOut, greyOut, 0);
		font.draw(batch, stages.getDesc(), posX, startY -= dec);
		font.setColor(fontColor);

		String playerMode = "";
		switch(players.selected().t){
		case 1: playerMode = "Solo"; break;
		default: playerMode = "Co-op"; break;
		}
		font.draw(batch, appendCursors("PLAYERS:", players) + playerMode, posX, startY -= dec);
		font.draw(batch, players.getDesc(), posX, startY -= dec);

		startY -= dec * 2;
		font.draw(batch, "P1: " + DowntiltEngine.getPlayers().get(0).getInputHandler().getControllerName(), posX,  startY -= dec);
		if (DowntiltEngine.getPlayers().size() > 1){
			if (players.selected().t == 1) font.setColor(0.4f, 0.4f, 0.4f, 0.4f);
			font.draw(batch, "P2: " + DowntiltEngine.getPlayers().get(1).getInputHandler().getControllerName(), posX,  startY -= dec);
			font.setColor(fontColor);
		}

		batch.draw(cursor, posX - 50, (430) - dec * (2 * choices.cursorPos()));
		batch.end();
	}
	
	private static boolean greyOutDifficultySelect(){
		return mode.selected().t != str_ADVENTURE;
	}
	
	private static boolean greyOutStageSelect(){
		return mode.selected().t == str_ADVENTURE || mode.selected().t == str_TUTORIAL;
	}

	private static String appendCursors(String str, MenuOption<?> menuOption){
		if (menuOption.cursorPos() == 0) str = "  ".concat(str);
		else str = "< ".concat(str);
		if (menuOption.cursorPos() == menuOption.getSize() - 1) str = str.concat("   ");
		else str = str.concat(" > ");
		return str;
	}

	private static void start(){
		Mode selectMode = null;
		Stage stage = getStage();

		switch(mode.selected().t){
		case str_ADVENTURE: {
			selectMode = new Adventure(difficulty.selected().t);
		} break;
		case str_TIMETRIAL: {
			selectMode = new TimeTrial(stage);
		} break;
		case str_ENDLESS: {
			selectMode = new Endless(stage);
		} break;
		case str_TUTORIAL: {
			selectMode = new Tutorial();
		} break;
		case str_TRAINING: {
			selectMode = new Training(stage);
		} break;
		default: break;
		}

		DowntiltEngine.startMode(selectMode, players.selected().t, 0);
		menuMusic.stop();
	}

	private static Stage getStage(){
		try {
			return stages.selected().t.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			System.out.println("Couldn't get stage" + stages.selected().t + "! Reason: " + e);
			return null;
		}
	}


}
