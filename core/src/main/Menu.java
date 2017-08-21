package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import entities.Basic;
import entities.Fighter;
import timers.Timer;

abstract class Menu {

	protected BitmapFont font = new BitmapFont(), navFont = new BitmapFont(), bigFont = new BitmapFont();
	protected SpriteBatch batch;
	protected String startStr;
	protected final Color fontColor = Color.WHITE, lockedColor = Color.RED;
	protected final float greyOut = 0.3f;
	protected boolean canAdvance = true, canBack = true;
	protected TextureRegion menu = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/menu.png")));
	protected TextureRegion cursor = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/cursor.png")));
	protected TextureRegion title = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/title.png")));
	protected TextureRegion tile = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/tile_sample.png")));
	protected TextureRegion logo = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/logo.png")));
	protected final int cursorMod = -128;

	protected List<MenuOption<?>> opt = new ArrayList<MenuOption<?>>();
	protected MenuOption<Integer> cho = new MenuOption<Integer>(Arrays.asList(new Choice<Integer>(0, "")));

	Menu(){
		startStr = "Press " + DowntiltEngine.getPrimaryInputHandler().getNormalString() + " to begin!";
		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nes.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		parameter.color = fontColor;
		font = generator.generateFont(parameter);
		parameter.color = new Color(245.0f/255.0f, 98.0f/255.0f, 5.0f/255.0f, 1);
		navFont = generator.generateFont(parameter);
		parameter.size = 32;
		bigFont = generator.generateFont(parameter);
		generator.dispose();
	}

	void update(){
		DowntiltEngine.getPrimaryInputHandler().update();
		if (DowntiltEngine.getPrimaryInputHandler().flickLeft())	handleCursor(-1, opt, cho);
		if (DowntiltEngine.getPrimaryInputHandler().flickRight())	handleCursor(1, opt,cho);
		if (DowntiltEngine.getPrimaryInputHandler().flickUp())		cho.moveCursor(-1);
		if (DowntiltEngine.getPrimaryInputHandler().flickDown())	cho.moveCursor(1);
		if (DowntiltEngine.getPrimaryInputHandler().menuAdvance() && DowntiltEngine.getDeltaTime() > 5)	advance();
		if (DowntiltEngine.getPrimaryInputHandler().menuBack() && DowntiltEngine.getDeltaTime() > 5)		back();
		draw();
	}

	protected void checkLocked(boolean b, Choice<?> choice, String descLock, String descUnlock){
		if (!b){
			choice.unlocked = false;
			choice.desc = descLock;
		}
		else {
			choice.unlocked = true;
			choice.desc = descUnlock;
		}
	}

	protected final int logomod = 2;
	protected void draw(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batch.begin();

		int dispX = DowntiltEngine.getDeltaTime() % tile.getRegionWidth();
		int dispY = DowntiltEngine.getDeltaTime() % tile.getRegionHeight();
		for (int i = 0; i < 21; ++i){
			for (int j = 0; j < 11; ++j){
				batch.draw(tile, i * tile.getRegionWidth() - dispX, j * tile.getRegionHeight() - dispY) ;
			}
		}

		batch.draw(menu, 0, 0);
		batch.draw(logo, 8, 0, logo.getRegionWidth() * logomod, logo.getRegionHeight() * logomod);
		batch.draw(title, 350, 560);
		final int posY = 50;
		if (canAdvance) navFont.draw(batch, "ADVANCE: " + DowntiltEngine.getPrimaryInputHandler().getNormalString(), 1000, posY);
		if (canBack) navFont.draw(batch, "BACK: " + DowntiltEngine.getPrimaryInputHandler().getSpecialString(), 200, posY);
		batch.end();
	}

	protected void advance(){
		/**/
	}

	protected void back(){
		/**/
	}

	protected void handleCursor(int mov, List<MenuOption<?>> options, MenuOption<Integer> choices){
		if (options.size() < choices.cursorPos() + 1) return;
		options.get(choices.cursorPos() + 1).moveCursor(mov);
	}

	protected String appendCursors(String str, MenuOption<?> menuOption){
		if (menuOption.cursorPos() == 0) str = "  ".concat(str);
		else str = "< ".concat(str);
		if (menuOption.cursorPos() == menuOption.getSize() - 1) str = str.concat("   ");
		else str = str.concat(" > ");
		return str;
	}

	protected List<Fighter> makePlayers(int num, ArrayList<PlayerType> newPlayers){
		List<Fighter> playerList = new ArrayList<Fighter>();
		for (int i = 0; i < num; ++i){
			Fighter player = new Basic(0, 0, 0);
			try {
				int j = i;
				if (j >= newPlayers.size()) j = newPlayers.size() - 1;
				player = newPlayers.get(j).type.getDeclaredConstructor(float.class, float.class, int.class).newInstance(0, 0, 0);
			} catch (Exception e) { 
				e.printStackTrace();
			}
			playerList.add(player);
		}
		return playerList;
	}

	protected class PlayerType{
		final Class<? extends Fighter> type;
		final String name;

		PlayerType(Class<? extends Fighter> type, String name){
			this.type = type;
			this.name = name;
		}

		PlayerType(Class<? extends Fighter> type){
			this.type = type;
			this.name = type.getName();
		}
	}

	protected class MenuOption <T> {
		private int cursor = 0;
		private final List<Choice<T>> choices;
		private final Timer waitToMoveTimer = new Timer(1);

		MenuOption(List<Choice<T>> lst){
			this.choices = lst;
			DowntiltEngine.addTimer(waitToMoveTimer);
		}

		Choice<T> selected(){
			return choices.get(cursor);
		}

		int cursorPos(){
			return cursor;
		}

		public String getDesc(){
			if (choices.get(cursor).desc.isEmpty()) return "";
			else return "   (" + (choices.get(cursor).desc) + ")";
		}

		void moveCursor(int mov){
			if (cursor + mov > -1 && cursor + mov < choices.size() && waitToMoveTimer.timeUp())	{
				cursor += mov;
				new SFX.LightHit().play(0.3f);
				waitToMoveTimer.reset();
			}
		}

		public int getSize(){
			return choices.size();
		}

		public void setCursor(int i) {
			cursor = i;
		}

		public void randomize() {
			cursor = ((int) (Math.random() * choices.size()) );
			while(!selected().unlocked){
				cursor = ((int) (Math.random() * choices.size()) );
			}
		}

		public List<Choice<T>> getChoices(){
			return choices;
		}

	}

	protected class Choice<T> {

		final T t;
		String desc;
		boolean unlocked = true;

		Choice(T t, String desc){
			this.t = t;
			this.desc = desc;
		}	

	}

}
