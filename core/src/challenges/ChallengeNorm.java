package challenges;

import java.util.List;

import maps.*;

public class ChallengeNorm extends Challenge {
	
	int inc = 0;
	int initDifficulty;

	/*
	 * Creates a new, regular challenge. Defeat x waves, then continue.
	 */
	public ChallengeNorm(Stage stage, List<Wave> waves){
		super(stage, waves);
	}
	
	@Override
	public String getEnemyCounter() {
		return "WAVES LEFT: " + (waves.size() + 1);
	}
	
}
