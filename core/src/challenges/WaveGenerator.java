package challenges;

import inputs.Brain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;
import entities.*;

public class WaveGenerator {

	public static final int DIFF_TEST	=-1;
	public static final int DIFF_ZEN 	= 0;
	public static final int DIFF_EASY	= 1;
	public static final int DIFF_MIDD	= 2;
	public static final int DIFF_HARD	= 3;
	public static final int DIFF_OHNO	= 4;

	protected static Enemy zen = new Enemy(Basic.class, Brain.Recover.class);
	protected static Enemy bad = new Enemy(Basic.Bad.class, Brain.MookBrain.class);
	protected static Enemy basic = new Enemy(Basic.class, Brain.MookBrain.class);
	protected static Enemy bombs = new Enemy(Basic.Bomb.class, Brain.MookBrain.class);
	protected static Enemy beefy = new Enemy(Basic.Beefy.class, Brain.MookBrain.class);
	protected static Enemy bonks = new Enemy(Basic.Bonkers.class, Brain.MookBrain.class);
	protected static Enemy baffle = new Enemy(Basic.Baffle.class, Brain.MookBrain.class);

	protected static List<EnemySpawner> zenList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(zen), 999, 2, 10, false)
			));
	protected static List<EnemySpawner> easyList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(bad, bad, basic), 6, 2, 90, false)
			,new EnemySpawner(Arrays.asList(bad, basic),	4, 2, 120, false)
			,new EnemySpawner(Arrays.asList(bad),	8, 2, 60, false)
			,new EnemySpawner(Arrays.asList(basic),	4, 1, 60, false)
			));
	protected static List<EnemySpawner> middList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(basic, basic, bombs), 6, 2, 120, false)
			,new EnemySpawner(Arrays.asList(basic), 12, 4, 90, false)
			,new EnemySpawner(Arrays.asList(beefy),	4, 1, 60, false)
			));
	protected static List<EnemySpawner> hardList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(bombs, basic, beefy), 8, 3, 80, false)
			,new EnemySpawner(Arrays.asList(bombs), 15, 6, 10, false)
			,new EnemySpawner(Arrays.asList(bonks), 1, 1, 120, false)
			));
	protected static List<EnemySpawner> ohnoList = new ArrayList<EnemySpawner>(Arrays.asList(
			new EnemySpawner(Arrays.asList(bombs, beefy), 8, 3, 90, false)
			,new EnemySpawner(Arrays.asList(bonks), 2, 1, 120, false)
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
