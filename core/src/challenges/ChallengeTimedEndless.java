package challenges;

import java.util.Collections;
import java.util.List;

import main.DowntiltEngine;
import main.SFX;
import maps.Stage;

public class ChallengeTimedEndless extends ChallengeTimed {

	/**
	 * Rotates waves until time is up.
	 */
	ChallengeTimedEndless(Stage stage, List<Wave> waves, double sec) {
		super(stage, waves, sec);
		soloLives = 3;
		coopLives = 2;
	}
	
	@Override
	protected void setActiveWave(){
		activeWave = currWaves.get(0);
		activeWave.restart();
		Collections.rotate(currWaves, -1);
	}
	
	@Override
	protected boolean inSuccessState(){
		return (getTimeMinSec()[0] <= 0 && getTimeMinSec()[1] <= 0);
	}
	
	@Override
	protected void nextWaveChecker(){
		if (activeWave.getNumEnemies() == 0 && !activeWave.isEndless()) {
			if (currWaves.size() > 0) nextWave();
		}
		if (inSuccessState()) succeedChallenge();
	}
	
	@Override
	public void failChallenge(){
		new SFX.Error().play();
		DowntiltEngine.returnToMenu();
	}
	
	@Override
	public String getWaveCounter() {
		return "";
	}

}
