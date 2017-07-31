package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.*;

public class Endless extends Mode {

	private List<Wave> wave = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.fly, EnemyRepo.bomb, EnemyRepo.heavy), 0, 3, 60))
			));

	List<Challenge> challengeList = new ArrayList<Challenge>();

	public Endless(Stage stage){
		challengeList.add(new ChallengeEndless(stage, wave));
	}
	
	@Override
	List<Challenge> getChallengeList() {
		return challengeList;
	}

}
