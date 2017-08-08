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

	protected BitmapFont font = new BitmapFont(), specialFont = new BitmapFont();
	protected SpriteBatch batch;
	protected final String startStr = "Press A to begin!";
	protected final Color fontColor = Color.WHITE;
	protected TextureRegion menu = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/menu.png")));
	protected TextureRegion cursor = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/cursor.png")));
	protected final float greyOut = 0.3f;

	protected List<MenuOption<?>> opt = new ArrayList<MenuOption<?>>();
	protected MenuOption<Integer> cho = new MenuOption<Integer>(Arrays.asList(new Choice<Integer>(0, "")));

	Menu(){
		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nes.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		parameter.color = fontColor;
		font = generator.generateFont(parameter);
		parameter.size = 32;
		parameter.color = Color.GOLDENROD;
		specialFont = generator.generateFont(parameter);
		generator.dispose();
	}

	void update(){
		DowntiltEngine.getPrimaryInputHandler().update();
		if (DowntiltEngine.getPrimaryInputHandler().flickLeft())	handleCursor(-1, opt, cho);
		if (DowntiltEngine.getPrimaryInputHandler().flickRight())	handleCursor(1, opt,cho);
		if (DowntiltEngine.getPrimaryInputHandler().flickUp())		cho.moveCursor(-1);
		if (DowntiltEngine.getPrimaryInputHandler().flickDown())	cho.moveCursor(1);
		if (DowntiltEngine.getPrimaryInputHandler().menuAdvance())	advance();
		if (DowntiltEngine.getPrimaryInputHandler().menuBack())		back();
		draw();
	}

	protected void draw(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(112.0f/255.0f, 233.0f/255.0f, 0.99f, 1);
		batch.begin();
		batch.draw(menu, 0, 0);
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
		private final Timer waitToMoveTimer = new Timer(3);

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
		}

	}

	protected class Choice<T> {
		final T t;
		final String desc;
		Choice(T t, String desc){
			this.t = t;
			this.desc = desc;
		}	
	}

}
