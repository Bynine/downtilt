package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import challenges.Victory;
import challenges.Victory.Ranking;

public class RankingScreen extends Menu{

	private MenuOption<Integer> choices = new MenuOption<Integer>(Arrays.asList(
			new Choice<Integer>(0, "modes")
			));
	private MenuOption<Float> modes = new MenuOption<Float>(Arrays.asList(
			new Choice<Float>(0.0f, "Adventure"),
			new Choice<Float>(0.0f, "Time Trial"),
			new Choice<Float>(0.0f, "Endless")
			));
	private List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, modes));

	private static final Victory
	av = new Victory.AdventureVictory(new ArrayList<Integer>(), 0, 0, null),
	tv = new Victory.TrialVictory(0, 0, null),
	ev = new Victory.EndlessVictory(0, 0, null);

	RankingScreen(){
		cho = choices;
		opt = options;
		canAdvance = false;
		tile = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/tile_flex.png")));
	}

	@Override
	protected void draw(){
		int posX = 264;
		int startY = 500;
		int posY = startY;
		final int dec = 32;
		final int numAdventureDifficulties = 4;
		int[][] scores = SaveHandler.getScores();

		super.draw();
		batch.begin();

		int x = modes.cursorPos();
		font.draw(batch, appendCursors("MODE: ", modes) + modes.selected().desc, posX - (dec * 2), posY);
		for (int y = 0; y < SaveHandler.arrayY; ++y){
			String prepend = "";
			Ranking r;
			int score = scores[x][y];
			switch(x){
			case 0: r = av.getRanking(score); break;
			case 1: r = tv.getRanking(score); break;
			case 2: r = ev.getRanking(score); break;
			default: r = Ranking.F;
			}
			if (score == 0) r = Ranking.N;
			
			if (x == 0) prepend = GlobalRepo.getDifficultyName(y);
			else prepend = GlobalRepo.getStageName(y);
			prepend += ": ";
			if (!(x == 0 && y >= numAdventureDifficulties)) {
				font.draw(batch, prepend + score, posX, posY - ((2 + y) * dec));
				font.setColor(GlobalRepo.getColorByRanking(r));
				font.draw(batch, r.toString(), posX + 700, posY - ((2 + y) * dec));
				font.setColor(fontColor);
			}
		}
		batch.end();
	}

	@Override
	protected void back(){
		DowntiltEngine.startHomeMenu();
	}

}
