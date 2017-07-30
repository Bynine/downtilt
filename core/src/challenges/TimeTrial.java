package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Stage;

public class TimeTrial extends Mode {

	private List<Wave> wave = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 4, 2, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.bomb), 4, 2, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy, EnemyRepo.shoot, EnemyRepo.fly), 3, 3, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.fly, EnemyRepo.bomb), 4, 4, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb), 6, 6, 10))
			));
	
	List<Challenge> challengeList = new ArrayList<Challenge>(Arrays.asList(
			));

	@Override
	List<Challenge> getChallengeList() {
		return challengeList;
	}
	
	public TimeTrial(Stage stage){
		challengeList.add(new ChallengeTimedEndless(stage, wave, 90));
	}

}
