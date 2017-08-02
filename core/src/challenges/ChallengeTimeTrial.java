package challenges;

import java.util.List;

import entities.Fighter;
import main.DowntiltEngine;
import main.SFX;
import maps.Stage;

public class ChallengeTimeTrial extends ChallengeTimed {

	/**
	 * Rotates waves until time is up.
	 */
	private static final int BASICALLYINFINITELIVES = 999;
	ChallengeTimeTrial(Stage stage, List<Wave> waves, double sec) {
		super(stage, waves, sec);
		for (Wave w: waves) w.setEndless();
		soloLives = BASICALLYINFINITELIVES;
		coopLives = BASICALLYINFINITELIVES;
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
	protected boolean inFailState(){
		boolean shouldRestart = true;
		for (Fighter player: (DowntiltEngine.getPlayers() ) ){
			if (player.getLives() > 0) shouldRestart = false;
		}
		return shouldRestart;
	}
	
	@Override
	public void failChallenge(){
		new SFX.Error().play();
		DowntiltEngine.startMenu();
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
