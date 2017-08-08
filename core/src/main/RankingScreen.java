package main;

public class RankingScreen extends Menu{
	
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
				switch(i){
				case 0: mode = "Adventure"; break;
				case 1: mode = "Time Trial"; break;
				case 2: mode = "Endless"; break;
				}
				if (j == 0) font.draw(batch, mode + ":", posX - (dec * 2), posY - (i * dec));
				int score = scores[i][j];
				font.draw(batch, "" + score, posX + ((j+2) * (dec * 2)), posY - (i * dec));
			}
		}
		batch.end();
	}
	
	@Override
	protected void back(){
		DowntiltEngine.startHomeMenu();
	}
	
}
