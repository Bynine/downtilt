package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Stage;

public class TimeTrial extends Mode {
	
	private final Stage stage;

	private List<Wave> wave = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.fly, EnemyRepo.heavy), 0, 5, 180))
			));
	List<Challenge> challengeList = new ArrayList<Challenge>();
	
	public TimeTrial(Stage stage){
		this.stage = stage;
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
		return new Victory.TrialVictory(stage, bonuses);
	}
	
	@Override
	public void pendValidBonus(Bonus newBonus){
		if (newBonus.timetrial) super.pendValidBonus(newBonus);
	}

}
