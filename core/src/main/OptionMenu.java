package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionMenu extends Menu {
	
	private MenuOption<Integer> choices = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(0, "musicvolume"),
			new Choice<Integer>(0, "sfxvolume")
			));
	private List<Choice<Float>> volumeList = Arrays.asList(
			new Choice<Float>(0.0f, "MUTE"),
			new Choice<Float>(0.5f, "QUIET"),
			new Choice<Float>(1.0f, "NORMAL"), 
			new Choice<Float>(1.5f, "LOUD"),  
			new Choice<Float>(2.0f, "EARBUSTER")
			);
	private MenuOption<Float> musicvolume = new MenuOption<Float>(volumeList);
	private MenuOption<Float> sfxvolume = new MenuOption<Float>(volumeList);
	private List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, musicvolume, sfxvolume));
	
	OptionMenu(){
		cho = choices;
		opt = options;
	}

	@Override
	protected void back(){
		DowntiltEngine.startHomeMenu();
	}
	
}
