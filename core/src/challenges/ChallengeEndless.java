package challenges;

import java.util.List;

import main.DowntiltEngine;
import maps.*;

public class ChallengeEndless extends Challenge {

	/**
	 * Creates an endless challenge. 
	 */
	public ChallengeEndless(Stage stage, List<Wave> waves){
		super(stage, waves);
		for (Wave w: waves) w.setEndless();
		soloLives = 1;
		coopLives = 1;
	}
	
	@Override
	public String getWaveCounter() {
		return "";
	}
	
	@Override
	public void failChallenge(){
		DowntiltEngine.returnToMenu();
	}
	
}
