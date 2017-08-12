package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;

public class WaveGenerator {

	public static final int DIFF_TEST	=-1;
	public static final int DIFF_ZEN 	= 0;
	public static final int DIFF_EASY	= 1;
	public static final int DIFF_MIDD	= 2;
	public static final int DIFF_HARD	= 3;
	public static final int DIFF_OHNO	= 4;

	protected static List<EnemySpawner> zenList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(EnemyRepo.zenbasic), 999, 2, 10)
			));

	static Wave generate(int difficulty){
		return new Wave(getDifficultyEnemySpawner(difficulty));
	}
	
	static Wave generateEndless(int difficulty) {
		EnemySpawner enemySpawner = getDifficultyEnemySpawner(difficulty);
		enemySpawner.setToEndless();
		return new Wave(enemySpawner);
	}
	
	private static EnemySpawner getDifficultyEnemySpawner(int difficulty){
		return newEnemySpawner(zenList);
	}

	private static EnemySpawner newEnemySpawner(List<EnemySpawner> enemyList){
		return new EnemySpawner(GlobalRepo.getRandomElementFromList(enemyList));
	}

}
