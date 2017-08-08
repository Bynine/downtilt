package challenges;

import java.util.List;

import main.DowntiltEngine;
import main.SFX;
import maps.Stage;

public class ChallengeTimeTrial extends ChallengeTimed {

	/**
	 * Rotates waves until time is up.
	 */

	ChallengeTimeTrial(Stage stage, List<Wave> waves, double sec) {
		super(stage, waves, sec);
		for (Wave w: waves) w.setEndless();
		lifeSetting = BASICALLYINFINITELIVES;
	}
	
	@Override
	protected boolean inSuccessState(){
		return (getTimeNum() <= 0);
	}
	
	@Override
	protected void nextWaveChecker(){
		if (activeWave.getNumEnemies() == 0 && !activeWave.isEndless()) {
			if (currWaves.size() > 0) nextWave();
		}
		if (inSuccessState()) succeedChallenge();
	}
	
	@Override
	protected boolean inFailState(){
		return lives <= 0;
	}
	
	@Override
	public void failChallenge(){
		new SFX.Error().play();
		DowntiltEngine.startGameMenu();
	}
	
	@Override
	public String getWaveCounter() {
		return "";
	}
	
	@Override
	public void resolveCombo(float mod){
		time += mod * MINUTE / 2;
	}

}
