package challenges;

import java.util.List;

import challenges.Adventure.Difficulty;

public abstract class Victory {
	protected int longestCombo;
	protected int time;
	protected int kos;

	public static final int NOTUSED = -1;

	public enum Ranking{
		F, D, C, B, A, S
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

	public abstract int getScore();
	public abstract Ranking getRanking();

	public static class AdventureVictory extends Victory{
		final Difficulty difficulty;

		AdventureVictory(List<Integer> longestCombos, int time, Difficulty difficulty){
			this.difficulty = difficulty;
			for (int i: longestCombos) this.longestCombo += i;
			this.time = time / 3600;
			this.kos = NOTUSED;
		}

		public int getScore(){
			int score = 0;
			if (getTime() < 340) score = 100;
			else	if (getTime() < 400) score = 50;
			else	if (getTime() < 480) score = 40;
			else 	if (getTime() < 540) score = 35;
			else 	if (getTime() < 600) score = 30;
			else 	if (getTime() < 720) score = 25;
			else 	if (getTime() < 780) score = 15;
			else 	if (getTime() < 900) score = 5;
			return score + longestCombo;
		}

		public Ranking getRanking(){
			if (getScore() < 30) return Ranking.F;
			if (getScore() < 40) return Ranking.D;
			if (getScore() < 50) return Ranking.C;
			if (getScore() < 65) return Ranking.B;
			if (getScore() < 80) return Ranking.A;
			else return Ranking.S;
		}

	}

	public static class TrialVictory extends Victory{

		TrialVictory(int longestCombo, int kos){
			this.longestCombo = longestCombo;
			this.time = NOTUSED;
			this.kos = kos;
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
			else return Ranking.S;
		}

	}

	public static class EndlessVictory extends Victory{

		EndlessVictory(int longestCombo, int kos){
			this.longestCombo = longestCombo;
			this.time = NOTUSED;
			this.kos = kos;
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
			else return Ranking.S;
		}

	}

}

