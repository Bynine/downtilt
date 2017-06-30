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
			new EnemySpawner(Arrays.asList(EnemyRepo.zen), 999, 2, 10, false)
			));
	protected static List<EnemySpawner> easyList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(EnemyRepo.bad, EnemyRepo.bad, EnemyRepo.basic), 4, 2, 90, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.bad, EnemyRepo.basic),	3, 2, 120, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.bad),	6, 2, 60, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.basic),	3, 1, 60, false)
			));
	protected static List<EnemySpawner> middList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.basic, EnemyRepo.bombs), 6, 2, 120, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.basic, EnemyRepo.beefy), 6, 2, 120, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.basic), 12, 4, 90, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.beefy),	4, 1, 60, false)
			));
	protected static List<EnemySpawner> hardList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(EnemyRepo.bombs, EnemyRepo.basic, EnemyRepo.beefy), 8, 3, 80, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.beefy), 8, 3, 80, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.bombs), 15, 6, 10, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.bad),	30, 15, 5, false)
			));
	protected static List<EnemySpawner> ohnoList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(EnemyRepo.bombs, EnemyRepo.beefy), 8, 3, 90, false)
			,new EnemySpawner(Arrays.asList(EnemyRepo.beefy, EnemyRepo.bombs), 6, 4, 60, false)
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
		switch(difficulty){
		case DIFF_ZEN: return newEnemySpawner(zenList);
		case DIFF_EASY: return newEnemySpawner(easyList);
		case DIFF_MIDD: return newEnemySpawner(middList);
		case DIFF_HARD: return newEnemySpawner(hardList);
		case DIFF_OHNO: return newEnemySpawner(ohnoList);
		default: return newEnemySpawner(ohnoList);
		}
	}

	private static EnemySpawner newEnemySpawner(List<EnemySpawner> enemyList){
		return new EnemySpawner(GlobalRepo.getRandomElementFromList(enemyList));
	}

}
