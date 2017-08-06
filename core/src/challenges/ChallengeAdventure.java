package challenges;

import java.util.List;

import maps.*;

public class ChallengeAdventure extends Challenge {

	/**
	 * Creates a new, regular challenge. Defeat x waves, then continue.
	 */
	public ChallengeAdventure(Stage stage, List<Wave> waves){
		super(stage, waves);
	}
	
}
