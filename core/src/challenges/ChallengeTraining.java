package challenges;

import java.util.List;

import maps.*;

public class ChallengeTraining extends ChallengeEndless {

	public ChallengeTraining(Stage stage, List<Wave> waves){
		super(stage, waves);
		lifeSetting = Challenge.BASICALLYINFINITELIVES;
	}

}
