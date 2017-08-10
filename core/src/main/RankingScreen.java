package main;

import java.util.ArrayList;

import challenges.Victory;
import challenges.Victory.Ranking;

public class RankingScreen extends Menu{

	private static final Victory
	av = new Victory.AdventureVictory(new ArrayList<Integer>(), 0, 0, null),
	tv = new Victory.TrialVictory(0, 0, null),
	ev = new Victory.EndlessVictory(0, 0, null);

	@Override
	protected void draw(){
		int posX = 264;
		int posY = 500;
		final int dec = 32;

		super.draw();
		batch.begin();
		int[][] scores = SaveHandler.getScores();
		for (int i = 0; i < SaveHandler.arrayX; ++i){
			for (int j = 0; j < SaveHandler.arrayY; ++j){
				String mode = "";
				Ranking r;
				int score = scores[i][j];
				if (score == 0) r = Ranking.N;
				else{
					switch(i){
					case 0: mode = "Adventure"; r = av.getRanking(score); break;
					case 1: mode = "Time Trial"; r = tv.getRanking(score); break;
					case 2: mode = "Endless"; r = ev.getRanking(score); break;
					default: r = Ranking.F;
					}
				}
				if (j == 0) font.draw(batch, mode + ":", posX - (dec * 2), posY - (i * dec));
				font.draw(batch, "" + r, posX + ((j+2) * (dec * 2)), posY - (i * dec));
			}
		}
		batch.end();
	}

	@Override
	protected void back(){
		DowntiltEngine.startHomeMenu();
	}

}
