package challenges;

import java.util.List;

import maps.*;

public class ChallengeEndless extends Challenge {

	/**
	 * Creates an endless challenge. 
	 */
	public ChallengeEndless(Stage stage, List<Wave> waves){
		super(stage, waves);
		for (Wave w: waves) w.setEndless();
	}
	
	public String getWaveCounter() {
		return "";
	}
	
}
