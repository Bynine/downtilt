package challenges;

import java.util.List;

import challenges.Challenge.Difficulty;

public abstract class Victory {
	protected int longestCombo;
	protected int time;
	protected int kos;
	protected int deaths;

	public static final int NOTUSED = -1;

	public enum Ranking{
		F, D, C, B, A, S, X
	}

	public int getCombo(){
		return longestCombo;
	}

	public int getTime(){
		return time;
	}

	public int getKOs(){
		return kos;
	}
	
	public int getDeaths(){
		return deaths;
	}

	public abstract int getScore();
	public abstract Ranking getRanking();

	public static class AdventureVictory extends Victory{
		final Difficulty difficulty;

		AdventureVictory(List<Integer> longestCombos, int time, int deaths, Difficulty difficulty){
			this.difficulty = difficulty;
			for (int i: longestCombos) this.longestCombo += i;
			this.time = time / 3600;
			this.kos = NOTUSED;
			this.deaths = deaths;
		}

		public int getScore(){
			int score = 0;
			int minute = 60;
					if (getTime() < minute * 3) score = 150;
			else	if (getTime() < minute * 4) score = 120;
			else	if (getTime() < minute * 5) score = 100;
			else	if (getTime() < minute * 6) score = 80;
			else	if (getTime() < minute * 7) score = 50;
			else	if (getTime() < minute * 8) score = 40;
			else 	if (getTime() < minute * 9)	score = 35;
			else 	if (getTime() < minute * 10)score = 30;
			else 	if (getTime() < minute * 11)score = 25;
			else 	if (getTime() < minute * 12)score = 15;
			else 	if (getTime() < minute * 13)score = 5;
			return score + longestCombo;
		}

		public Ranking getRanking(){
			if (getScore() < 30) return Ranking.F;
			if (getScore() < 40) return Ranking.D;
			if (getScore() < 55) return Ranking.C;
			if (getScore() < 70) return Ranking.B;
			if (getScore() < 80) return Ranking.A;
			if (getScore() < 90) return Ranking.S;
			else return Ranking.X;
		}

	}

	public static class TrialVictory extends Victory{

		TrialVictory(int longestCombo, int kos){
			this.longestCombo = longestCombo;
			this.time = NOTUSED;
			this.kos = kos;
			this.deaths = NOTUSED;
		}

		public int getScore(){
			return kos + longestCombo * 3;
		}

		public Ranking getRanking(){
			if (getScore() < 15) return Ranking.F;
			if (getScore() < 25) return Ranking.D;
			if (getScore() < 35) return Ranking.C;
			if (getScore() < 43) return Ranking.B;
			if (getScore() < 50) return Ranking.A;
			if (getScore() < 55) return Ranking.S;
			else return Ranking.X;
		}

	}

	public static class EndlessVictory extends Victory{

		EndlessVictory(int longestCombo, int kos){
			this.longestCombo = longestCombo;
			this.time = NOTUSED;
			this.kos = kos;
			this.deaths = NOTUSED;
		}

		public int getScore(){
			return kos + longestCombo * 4;
		}

		public Ranking getRanking(){
			if (getScore() < 15) return Ranking.F;
			if (getScore() < 25) return Ranking.D;
			if (getScore() < 40) return Ranking.C;
			if (getScore() < 55) return Ranking.B;
			if (getScore() < 70) return Ranking.A;
			if (getScore() < 100) return Ranking.S;
			else return Ranking.X;
		}

	}

}

