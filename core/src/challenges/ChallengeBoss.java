package challenges;

import java.util.List;

import entities.Boss;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import maps.Stage;

public class ChallengeBoss extends ChallengeEndless {

	private final Boss boss = new Boss(GlobalRepo.TILE * 15, GlobalRepo.TILE * 11);
	private final Difficulty difficulty;

	public ChallengeBoss(Stage stage, List<Wave> waves, Difficulty difficulty) {
		super(stage, waves);
		for (Wave w: waves) w.setEndless();
		this.difficulty = difficulty;
		lifeSetting = 4;
	}

	protected void startChallenge(){
		super.startChallenge();
		boss.set(difficulty);
		MapHandler.addEntity(boss);
	}
	
	public String getWaveCounter() {
		return "HEALTH: " + boss.getHealth();
	}
	
	@Override
	protected void nextWaveChecker(){
		if (inSuccessState()) succeedChallenge();
	}
	
	@Override
	protected boolean inSuccessState(){
		return boss.getHealth() <= 0;
	}

	@Override
	public void failChallenge(){
		new SFX.Error().play();
		startChallenge();
	}
	
	@Override
	public void resolveCombo(float mod){
		/**/
	}

}
