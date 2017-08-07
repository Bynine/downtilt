package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Stage_Standard;

public class Tutorial extends Mode {
	
	List<Challenge> challengeList = new ArrayList<Challenge>();
	
	private List<Wave> wave = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.zen), 2, 2, 60))
			));
	
	public Tutorial(){
		wave.get(0).setEndless();
		challengeList.add(new ChallengeTutorial(new Stage_Standard(), wave));
	}

	@Override
	List<Challenge> getChallengeList() {
		return challengeList;
	}

	@Override
	Victory getVictory(){
		return null;
	}

}
