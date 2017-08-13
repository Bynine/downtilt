package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
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

class GameMenu extends Menu {

	private final String str_ADVENTURE = "Adventure";
	private final String str_TIMETRIAL = "Time Trial";
	private final String str_ENDLESS = "Endless";
	private final String str_TUTORIAL = "Tutorial";
	private final String str_TRAINING = "Training";
	private MenuOption<String> mode = new MenuOption<String>(Arrays.asList(
			new Choice<String>(str_TRAINING, "Test out your moves against dummies!"),
			new Choice<String>(str_TUTORIAL, "Learn how to play!"),
			new Choice<String>(str_ADVENTURE, "Battle through a variety of stages!"),
			new Choice<String>(str_TIMETRIAL, "Knock 'em out quick! Combos extend your time!"),  
			new Choice<String>(str_ENDLESS, "Survive endless enemies! Combos restore your health!")
			));

	private MenuOption<Class<? extends Stage>> stages = new MenuOption<Class<? extends Stage>>(Arrays.asList(
			new Choice<Class<? extends Stage>>(Stage_Standard.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Rooftop.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Truck.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Blocks.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Mushroom.class, ""),
			new Choice<Class<? extends Stage>>(Stage_Space.class, ""), 
			new Choice<Class<? extends Stage>>(Stage_Sky.class, "")
			));

	private MenuOption<Difficulty> difficulty = new MenuOption<Difficulty>(Arrays.asList(
			new Choice<Difficulty>(Difficulty.Beginner, "Easygoing, perfect for a first time."),
			new Choice<Difficulty>(Difficulty.Standard, "A solid standard experience."),
			new Choice<Difficulty>(Difficulty.Advanced, "Random stages and extra challenge."),
			new Choice<Difficulty>(Difficulty.Nightmare, "Good luck!")
			));

	private MenuOption<Integer> players = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(1, ""),
			new Choice<Integer>(2, "")
			));

	private MenuOption<Integer> choices = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(0, "mode"),
			new Choice<Integer>(0, "diff"),
			new Choice<Integer>(0, "stage"),
			new Choice<Integer>(0, "player")
			));
	private List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, mode, difficulty, stages, players));
	private final Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));

	GameMenu(){
		super();
		opt = options;
		cho = choices;
		stages.randomize();
		menuMusic.setLooping(true);
		tile = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/tile_basic.png")));
		begin();
	}

	public void begin(){
		if (DowntiltEngine.musicOn()) {
			menuMusic.setVolume(DowntiltEngine.getMusicVolume() / 8.0f);
			menuMusic.play();
		}
		if (!SaveHandler.AdvancedUnlocked()) {
			difficulty.getChoices().get(2).selectable = false;
			difficulty.getChoices().get(2).desc = "Unlock by A-ranking Standard difficulty!";
		}
		if (!SaveHandler.NightmareUnlocked()) {
			difficulty.getChoices().get(3).selectable = false;
			difficulty.getChoices().get(3).desc = "Unlock by A-ranking Advanced difficulty!";
		}
	}

	enum MenuChoice{
		CHARACTER, PLAYERS
	}

	@Override
	protected void draw(){
		int posX = 230;
		int startY = 510;
		int posY = startY;
		int dec = 30;
		super.draw();
		batch.begin();

		bigFont.draw(batch, startStr, posX + 180, posY);

		posY -= dec;
		font.draw(batch, appendCursors("MODE:   ", mode) + mode.selected().t, posX, posY -= dec);
		font.draw(batch, mode.getDesc(), posX, posY -= dec);

		if (greyOutDifficultySelect()) font.setColor(greyOut, greyOut, greyOut, greyOut);
		font.draw(batch, appendCursors("RANK:   ", difficulty) + GlobalRepo.getDifficultyName(difficulty.cursorPos()), posX, posY -= dec);
		if (greyOutDifficultySelect()) font.setColor(greyOut, greyOut, greyOut, 0);
		if (!difficulty.selected().selectable) font.setColor(Color.RED);
		font.draw(batch, difficulty.getDesc(), posX, posY -= dec);
		font.setColor(fontColor);
		
		if (greyOutStageSelect()) font.setColor(greyOut, greyOut, greyOut, greyOut);
		font.draw(batch, appendCursors("STAGE:  ", stages) + GlobalRepo.getStageName(stages.cursorPos()), posX, posY -= dec);
		if (greyOutStageSelect()) font.setColor(greyOut, greyOut, greyOut, 0);
		font.draw(batch, stages.getDesc(), posX, posY -= dec);
		font.setColor(fontColor);

		String playerMode = "";
		switch(players.selected().t){
		case 1: playerMode = "Solo"; break;
		default: playerMode = "Co-op"; break;
		}
		font.draw(batch, appendCursors("PLAYERS:", players) + playerMode, posX, posY -= dec);
		font.draw(batch, players.getDesc(), posX, posY -= dec);

		posY -= dec * 2;
		font.draw(batch, "P1: " + DowntiltEngine.getPrimaryInputHandler().getControllerName(), posX,  posY -= dec);
		if (DowntiltEngine.getPlayers().size() > 1){
			if (players.selected().t == 1) font.setColor(0.4f, 0.4f, 0.4f, 0.4f);
			font.draw(batch, "P2: " + DowntiltEngine.getSecondaryInputHandler().getControllerName(), posX,  posY -= dec);
			font.setColor(fontColor);
		}

		batch.draw(cursor, posX - 48, startY - dec - 48 - cho.cursorPos() * 2 * (dec + 1));
		batch.end();
	}
	
	private boolean greyOutDifficultySelect(){
		return mode.selected().t != str_ADVENTURE;
	}
	
	private boolean greyOutStageSelect(){
		return mode.selected().t == str_ADVENTURE || mode.selected().t == str_TUTORIAL;
	}

	@Override
	protected void advance(){
		
		for (MenuOption<?> mo: options) if (!mo.selected().selectable) {
			new SFX.Error().play();
			return;
		}
		
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
	
	@Override
	protected void back(){
		DowntiltEngine.startHomeMenu();
	}

	private Stage getStage(){
		try {
			return stages.selected().t.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			System.out.println("Couldn't get stage " + stages.selected().t + "! Reason: " + e);
			return null;
		}
	}


}
