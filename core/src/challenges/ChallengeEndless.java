package challenges;

import main.GlobalRepo;
import maps.*;

public class ChallengeEndless extends Challenge {
	
	int inc = 0;
	int initDifficulty;

	public ChallengeEndless(int difficulty){
		super(difficulty);
		stageList.add(getStage());
		setup(difficulty);
	}
	
	public ChallengeEndless(int difficulty, Stage stage){
		super(difficulty);
		stageList.add(stage);
		setup(difficulty);
	}
	
	private void setup(int difficulty){
		initDifficulty = difficulty;
		numLives = 5;
		combatPosition.set(GlobalRepo.TILE * 22, GlobalRepo.TILE * 6);
		begin();
		startCombatHelper(null, combatPosition);
		activeWave = WaveGenerator.generate(difficulty);
	}
	
	private Stage getStage(){
				if (Math.random() < 1.0/3.0) return new Stage_Flat();
				if (Math.random() < 1.0/2.0) return new Stage_Columns();
										 	 return new Stage_Standard();
	}
	
	@Override
	public String getEnemyCounter() {
		return "SURVIVED: " + prevWaves.size();
	}
	
}
