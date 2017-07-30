package challenges;

import java.util.Collections;
import java.util.List;

import main.DowntiltEngine;
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
	public void failChallenge(){
		DowntiltEngine.returnToMenu();
	}
	
	@Override
	protected void setActiveWave(){
		activeWave = currWaves.get(0);
		activeWave.restart();
		Collections.rotate(currWaves, -1);
	}
	
	@Override
	public String getWaveCounter() {
		return "";
	}

}
