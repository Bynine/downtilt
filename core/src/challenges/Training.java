package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Stage;

public class Training extends Mode {

	List<Challenge> challengeList = new ArrayList<Challenge>();
	
	private List<Wave> wave = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.zen), 2, 2, 60))
			));
	
	public Training(Stage stage){
		wave.get(0).setEndless();
		challengeList.add(new ChallengeTraining(stage, wave));
	}

	@Override
	List<Challenge> getChallengeList() {
		return challengeList;
	}

	@Override
	Victory getVictory(){
		return new Victory.EndlessVictory(Victory.NOTUSED, Victory.NOTUSED);
	}

}
