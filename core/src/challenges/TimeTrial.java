package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Stage;

public class TimeTrial extends Mode {

	private List<Wave> wave = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.fly, EnemyRepo.bomb, EnemyRepo.heavy), 0, 10, 180))
			));
	List<Challenge> challengeList = new ArrayList<Challenge>();
	
	public TimeTrial(Stage stage){
		challengeList.add(new ChallengeTimeTrial(stage, wave, 60));
	}
	
	@Override
	List<Challenge> getChallengeList() {
		return challengeList;
	}
	
	int getKOs(){
		return wave.get(0).getNumKilled();
	}
	
	Victory getVictory(){
		return new Victory.TrialVictory(getCombo(), getKOs());
	}

}
