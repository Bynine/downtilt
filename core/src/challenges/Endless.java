package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.*;

public class Endless extends Mode {
	
	private final Stage stage;

	private List<Wave> wave = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 4, 2, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.bomb), 4, 2, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy, EnemyRepo.shoot, EnemyRepo.fly), 3, 3, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.fly, EnemyRepo.bomb), 4, 4, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.weakfly), 10, 10, 10))
			
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 8, 8, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.bomb), 8, 8, 90))
			
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy, EnemyRepo.shoot, EnemyRepo.fly), 6, 6, 90))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.fly, EnemyRepo.bomb), 10, 10, 100))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.weakfly), 25, 25, 10))
			
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatbasic, EnemyRepo.fatshoot), 4, 2, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatfly, EnemyRepo.fatbomb), 4, 2, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatheavy, EnemyRepo.fatshoot, EnemyRepo.fatfly), 3, 3, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatbasic, EnemyRepo.fatshoot, EnemyRepo.fatfly, EnemyRepo.fatbomb), 4, 4, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatbomb), 6, 4, 10))
			
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatbasic, EnemyRepo.fatshoot), 8, 8, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatfly, EnemyRepo.fatbomb), 8, 8, 10))
			
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatheavy, EnemyRepo.fatshoot, EnemyRepo.fatfly), 8, 8, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatbasic, EnemyRepo.fatshoot, EnemyRepo.fatfly, EnemyRepo.fatbomb), 10, 10, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatbomb), 250, 25, 10))
			));

	List<Challenge> challengeList = new ArrayList<Challenge>();

	public Endless(Stage stage){
		this.stage = stage;
		challengeList.add(new ChallengeEndless(stage, wave));
	}
	
	@Override
	List<Challenge> getChallengeList() {
		return challengeList;
	}
	
	int getKOs(){
		int kos = 0;
		for (Wave w: wave) kos += w.getNumKilled();
		return kos;
	}

	Victory getVictory(){
		return new Victory.EndlessVictory(stage, bonuses);
	}
	
	@Override
	protected void addValidBonus(Bonus newBonus){
		if (!newBonus.adventureOnly) bonuses.add(newBonus);
	}

}
