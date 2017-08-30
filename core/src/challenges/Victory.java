package challenges;

import java.util.ArrayList;
import java.util.List;

import challenges.Challenge.Difficulty;
import maps.Stage;

public abstract class Victory {
	protected final List<Bonus> bonuses = new ArrayList<Bonus>();

	public static final int NOTUSED = -1;

	public enum Ranking{
		N, F, D, C, B, A, S, X
	}

	public Ranking getRanking(float score){
		return Ranking.F;
	}
	
	public List<Bonus> getBonuses() {
		return bonuses;
	}
	
	public int getScore(){
		return getBonusScore(bonuses);
	}
	
	public static int getBonusScore(List<Bonus> bonuses){
		int score = 0;
		for (Bonus b: bonuses){
			if (!(b instanceof Bonus.FactorBonus)) score += (b.getScore());
		}
		for (Bonus b: bonuses){
			if (b instanceof Bonus.FactorBonus) score *= (((Bonus.FactorBonus)b).getFactor());
		}
		return score;
	}
	
	public abstract int getNumberX();
	public abstract int getNumberY();

	public static class AdventureVictory extends Victory{
		final Difficulty difficulty;

		public AdventureVictory(Difficulty difficulty, List<Bonus> bonuses){
			this.difficulty = difficulty;
			this.bonuses.addAll(bonuses);
		}

		public static final int D = 20;
		public static final int C = 40;
		public static final int B = 65;
		public static final int A = 90;
		public static final int S = 120;
		public static final int X = 180;

		public Ranking getRanking(float score){
			if (score < D) return Ranking.F;
			if (score < C) return Ranking.D;
			if (score < B) return Ranking.C;
			if (score < A) return Ranking.B;
			if (score < S) return Ranking.A;
			if (score < X) return Ranking.S;
			else return Ranking.X;
		}

		public int getNumberX(){
			return 0;
		}

		public int getNumberY(){
			switch(difficulty){
			case Beginner: return 0;
			case Standard: return 1;
			case Advanced: return 2;
			case Nightmare: return 3;
			default: return 0;
			}
		}

	}

	public static class TrialVictory extends Victory{
		private final Stage stage;

		public TrialVictory(Stage stage, List<Bonus> bonuses){
			this.bonuses.addAll(bonuses);
			this.stage = stage;
		}

		public static final int D = 12;
		public static final int C = 20;
		public static final int B = 30;
		public static final int A = 40;
		public static final int S = 60;
		public static final int X = 80;

		public Ranking getRanking(float score){
			if (score < D) return Ranking.F;
			if (score < C) return Ranking.D;
			if (score < B) return Ranking.C;
			if (score < A) return Ranking.B;
			if (score < S) return Ranking.A;
			if (score < X) return Ranking.S;
			else return Ranking.X;
		}

		public int getNumberX(){
			return 1;
		}

		public int getNumberY(){
			return stage.getNumber();
		}

	}

	public static class EndlessVictory extends Victory{
		private final Stage stage;

		public EndlessVictory(Stage stage, List<Bonus> bonuses){
			this.bonuses.addAll(bonuses);
			this.stage = stage;
		}

		public static final int D = 5;
		public static final int C = 10;
		public static final int B = 20;
		public static final int A = 40;
		public static final int S = 60;
		public static final int X = 120;

		public Ranking getRanking(float score){
			if (score < D) return Ranking.F;
			if (score < C)  return Ranking.D;
			if (score < B)  return Ranking.C;
			if (score < A)  return Ranking.B;
			if (score < S)  return Ranking.A;
			if (score < X) return Ranking.S;
			else return Ranking.X;
		}

		public int getNumberX(){
			return 2;
		}

		public int getNumberY(){
			return stage.getNumber();
		}

	}

}

