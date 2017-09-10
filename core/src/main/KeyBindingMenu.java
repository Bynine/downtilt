package main;

public class KeyBindingMenu extends Menu {
	
	KeyBindingMenu(){
		canAdvance = false;
	}

	@Override
	protected void back(){
		new SFX.Back().play();
		DowntiltEngine.startHomeMenu();
	}
	
}
