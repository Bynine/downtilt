package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import main.DowntiltEngine.Palette;
import maps.Stage_Mushroom;
import maps.Stage_Sky;
import maps.Stage_Space;

public class OptionMenu extends Menu {

	private MenuOption<Integer> choices = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(0, "palettes"),
			new Choice<Integer>(0, "musicvolume"),
			new Choice<Integer>(0, "sfxvolume"),
			new Choice<Integer>(0, "screenshake")
			));
	
	private final String str_MUSHROOMNAME = "Wild";
	private final String str_SPACENAME = "Solemn";
	private final String str_SKYNAME = "Sunset";
	private final String str_NIGHTMARENAME = "Spooky";
	private final String str_ADVENTURENAME = "Professional";
	private final String str_TIMETRIALNAME = "Radioactive";
	private final String str_ENDLESSNAME = "Hornet";
	private MenuOption<Palette> palettes = new MenuOption<Palette>(Arrays.asList(
			new Choice<Palette>(Palette.NORMAL, "Standard"),
			new Choice<Palette>(Palette.MUSHROOM, str_MUSHROOMNAME),
			new Choice<Palette>(Palette.SPACE, str_SPACENAME),
			new Choice<Palette>(Palette.SKY, str_SKYNAME),
			new Choice<Palette>(Palette.NIGHTMARE, str_NIGHTMARENAME),
			new Choice<Palette>(Palette.ADVENTURE, str_ADVENTURENAME),
			new Choice<Palette>(Palette.TIMETRIAL, str_TIMETRIALNAME),
			new Choice<Palette>(Palette.ENDLESS, str_ENDLESSNAME)
			));
	private List<Choice<Float>> volumeList = Arrays.asList(
			new Choice<Float>(0.0f, "MUTE"),
			new Choice<Float>(0.25f, "QUIET"),
			new Choice<Float>(1.0f, "NORMAL"), 
			new Choice<Float>(1.5f, "LOUD"),  
			new Choice<Float>(2.0f, "EARBUSTER")
			);
	private MenuOption<Float> musicVolume = new MenuOption<Float>(volumeList);
	private MenuOption<Float> sfxVolume = new MenuOption<Float>(volumeList);
	private MenuOption<Float> screenShake = new MenuOption<Float>(Arrays.asList(
			new Choice<Float>(0.0f, "OFF"),
			new Choice<Float>(0.25f, "SOFT"),
			new Choice<Float>(1.0f, "NORMAL"),
			new Choice<Float>(1.5f, "YIKES!!")
			));
	private List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, palettes, musicVolume, sfxVolume, screenShake));
	private final TextureRegion playerImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/stand.png")));
	private final TextureRegion background = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/background.png")));

	OptionMenu(int[] opts){
		cho = choices;
		opt = options;
		canAdvance = false;
		tile = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/tile_diamond.png")));
		musicVolume.setCursor(opts[0]);
		sfxVolume.setCursor(opts[1]);
		screenShake.setCursor(opts[2]);
		palettes.setCursor(opts[3]);
	}
	
	public void begin(){
		checkLocked(SaveHandler.PaletteMushroomUnlocked(), palettes.getChoices().get(1),
				"Unlock by A-ranking\n" + Stage_Mushroom.getName() + " Time Trial!", str_MUSHROOMNAME);
		checkLocked(SaveHandler.PaletteSpaceUnlocked(), palettes.getChoices().get(2), 
				"Unlock by A-ranking\n" + Stage_Space.getName() + " Endless!", str_SPACENAME);
		checkLocked(SaveHandler.PaletteSkyUnlocked(), palettes.getChoices().get(3), 
				"Unlock by A-ranking\n" + Stage_Sky.getName() + " Time Trial!", str_SKYNAME);
		checkLocked(SaveHandler.PaletteNightmareUnlocked(), palettes.getChoices().get(4), 
				"Unlock by A-ranking Nightmare difficulty!", str_NIGHTMARENAME);
		checkLocked(SaveHandler.PaletteSAdventureUnlocked(), palettes.getChoices().get(5), 
				"Unlock by S-ranking any Adventure!", str_ADVENTURENAME);
		checkLocked(SaveHandler.PaletteSTimeTrialUnlocked(), palettes.getChoices().get(6), 
				"Unlock by S-ranking any Time Trial!", str_TIMETRIALNAME);
		checkLocked(SaveHandler.PaletteSEndlessUnlocked(), palettes.getChoices().get(7), 
				"Unlock by S-ranking any Endless!", str_ENDLESSNAME);
	}

	@Override
	protected void draw(){
		int posX = 230;
		int startY = 460;
		int posY = startY;
		final int dec = 60;
		super.draw();
		batch.begin();
		batch.draw(cursor, posX + cursorMod, startY - dec - 16 - cho.cursorPos() * (dec));

		if (!palettes.selected().unlocked) font.setColor(lockedColor);
		font.draw(batch, appendCursors("Palette:      ", palettes) + palettes.selected().desc, posX, posY -= dec);
		font.setColor(fontColor);
		
		int playerMod = 2;
		float imagePosX = posX + 800;
		float imagePosY = posY + 28;
		batch.draw(background, imagePosX - 36, imagePosY - 8);
		batch.setShader(DowntiltEngine.getShaderFromPalette(palettes.selected().t));
		batch.draw(playerImage, imagePosX - 4, imagePosY - 1, playerImage.getRegionWidth() * playerMod, playerImage.getRegionHeight() * playerMod);
		batch.setShader(null);
		
		font.draw(batch, appendCursors("Music Volume: ", musicVolume) + musicVolume.selected().desc, posX, posY -= dec);
		font.draw(batch, appendCursors("SFX Volume:   ", sfxVolume) + sfxVolume.selected().desc, posX, posY -= dec);
		font.draw(batch, appendCursors("Screen Shake: ", screenShake) + screenShake.selected().desc, posX, posY -= dec);

		batch.end();
	}
	
	public void setOptions(){
		DowntiltEngine.setMusicVolume(musicVolume.selected().t);
		DowntiltEngine.setSFXVolume(sfxVolume.selected().t);
		DowntiltEngine.setScreenShake(screenShake.selected().t);
		if (palettes.selected().unlocked) DowntiltEngine.setActivePalette(palettes.selected().t);
		else palettes.setCursor(0);
	}

	@Override
	protected void back(){
		new SFX.Back().play();
		setOptions();
		SaveHandler.writeOptions(musicVolume.cursorPos(), sfxVolume.cursorPos(), screenShake.cursorPos(), palettes.cursorPos());
		SaveHandler.save();
		DowntiltEngine.startHomeMenu();
	}

}
