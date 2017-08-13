package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class OptionMenu extends Menu {

	private MenuOption<Integer> choices = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(0, "musicvolume"),
			new Choice<Integer>(0, "sfxvolume"),
			new Choice<Integer>(0, "screenshake")
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
	private List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, musicVolume, sfxVolume, screenShake));

	OptionMenu(int[] opts){
		cho = choices;
		opt = options;
		canAdvance = false;
		tile = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/tile_diamond.png")));
		musicVolume.setCursor(opts[0]);
		sfxVolume.setCursor(opts[1]);
		screenShake.setCursor(opts[2]);
	}

	@Override
	protected void draw(){
		int posX = 230;
		int startY = 500;
		int posY = startY;
		final int dec = 60;
		super.draw();
		batch.begin();

		font.draw(batch, appendCursors("Music Volume: ", musicVolume) + musicVolume.selected().desc, posX, posY -= dec);
		font.draw(batch, appendCursors("SFX Volume:   ", sfxVolume) + sfxVolume.selected().desc, posX, posY -= dec);
		font.draw(batch, appendCursors("Screen Shake: ", screenShake) + screenShake.selected().desc, posX, posY -= dec);
		batch.draw(cursor, posX - 48, startY - dec - 16 - cho.cursorPos() * (dec + 1));

		batch.end();
	}
	
	public void setOptions(){
		DowntiltEngine.setMusicVolume(musicVolume.selected().t);
		DowntiltEngine.setSFXVolume(sfxVolume.selected().t);
		DowntiltEngine.setScreenShake(screenShake.selected().t);
	}

	@Override
	protected void back(){
		setOptions();
		SaveHandler.writeOptions(musicVolume.cursorPos(), sfxVolume.cursorPos(), screenShake.cursorPos());
		SaveHandler.save();
		DowntiltEngine.startHomeMenu();
	}

}
