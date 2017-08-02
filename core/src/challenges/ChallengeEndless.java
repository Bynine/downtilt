package challenges;

import java.util.Collections;
import java.util.List;

import entities.Fighter;
import main.DowntiltEngine;
import maps.*;

public class ChallengeEndless extends Challenge {

	/**
	 * Creates an endless challenge. 
	 */
	public ChallengeEndless(Stage stage, List<Wave> waves){
		super(stage, waves);
		soloLives = 1;
		coopLives = 1;
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

	@Override
	public void failChallenge(){
		succeedChallenge();
	}

	public void resolveCombo(float mod){
		for (Fighter player: DowntiltEngine.getPlayers()) {
			player.takeDamage(mod * -3);
		}
	}

}
