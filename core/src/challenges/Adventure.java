package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import challenges.Challenge.Difficulty;
import main.DowntiltEngine;
import maps.*;

/**
 * Adventure mode. A series of challenges.
 */

public class Adventure extends Mode{

	private final Difficulty difficulty;
	
	List<Wave> waveDebug = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb), 1, 1, 10))
			));

	/**
	 * Adds challenges depending on difficulty and debug mode.
	 */
	private void initializeChallengeList(){
		switch (difficulty){
		case Beginner:{
			if (DowntiltEngine.debugOn()) {
				challengeList.add(new ChallengeBoss(new Stage_Boss(), waveBoss1, Difficulty.Beginner));
				//challengeList.add(0, new ChallengeAdventure(new Stage_Standard(), waveDebug));
			}
			else{
				challengeList.add(new ChallengeAdventure(new Stage_Standard(), waveStandard1));
				challengeList.add(new ChallengeAdventure(new Stage_Rooftop(), waveRooftop1));
				challengeList.add(new ChallengeAdventure(new Stage_Truck(), waveTruck1));
				challengeList.add(new ChallengeAdventure(new Stage_Blocks(), waveBlocks1));
				challengeList.add(new ChallengeAdventure(new Stage_Mushroom(), waveMushroom1));
				challengeList.add(new ChallengeBoss(new Stage_Boss(), waveBoss1, difficulty));
			}
		} break;
		case Standard: {
			challengeList.add(new ChallengeAdventure(new Stage_Standard(), waveStandard2));
			challengeList.add(new ChallengeAdventure(new Stage_Rooftop(), waveRooftop2));
			challengeList.add(new ChallengeAdventure(new Stage_Truck(), waveTruck2));
			challengeList.add(new ChallengeTimed(new Stage_Space(), waveSpace2, 150));
			challengeList.add(new ChallengeAdventure(new Stage_Blocks(), waveBlocks2));
			challengeList.add(new ChallengeAdventure(new Stage_Mushroom(), waveMushroom2));
			challengeList.add(new ChallengeAdventure(new Stage_Sky(), waveSky2));
			challengeList.add(new ChallengeBoss(new Stage_Boss(), waveBoss2, difficulty));
		} break;
		case Advanced: {
			challengeList.add(new ChallengeAdventure(new Stage_Standard(), waveStandard3));
			challengeList.add(new ChallengeAdventure(new Stage_Truck(), waveTruck3));
			challengeList.add(new ChallengeAdventure(new Stage_Rooftop(), waveRooftop3));
			challengeList.add(new ChallengeAdventure(new Stage_Mushroom(), waveMushroom3));
			challengeList.add(new ChallengeAdventure(new Stage_Blocks(), waveBlocks3));
			challengeList.add(new ChallengeAdventure(new Stage_Sky(), waveSky3));
			challengeList.add(new ChallengeTimed(new Stage_Space(), waveSpace3, 120));
			Collections.shuffle(challengeList);
			challengeList.add(new ChallengeBoss(new Stage_Boss(), waveBoss3, difficulty));
		} break;
		case Nightmare: {
			challengeList.add(new ChallengeTimed(new Stage_Truck(), waveTruck4, 61));
			challengeList.add(new ChallengeAdventure(new Stage_Rooftop(), waveRooftop4));
			challengeList.add(new ChallengeAdventure(new Stage_Mushroom(), waveMushroom4));
			challengeList.add(new ChallengeAdventure(new Stage_Blocks(), waveBlocks4));
			challengeList.add(new ChallengeAdventure(new Stage_Sky(), waveSky4));
			challengeList.add(new ChallengeTimed(new Stage_Space(), waveSpace4, 120));
			Collections.shuffle(challengeList);
			challengeList.add(new ChallengeAdventure(new Stage_Standard(), waveStandard4));
			challengeList.add(new ChallengeBoss(new Stage_Boss(), waveBoss4, difficulty));
		} break;
		default: break;
		}
	}

	/**
	 * Default constructor. Used for normal gameplay.
	 */
	public Adventure(Difficulty diff){
		difficulty = diff;
		activeChallengeIndex = 0;
		initializeChallengeList();
	}

	/**
	 * Debug constructor used to skip ahead in stage order.
	 */
	public Adventure(Difficulty diff, int index){
		difficulty = diff;
		activeChallengeIndex = index;
		initializeChallengeList();
	}

	/**
	 * Selection of wave lists for each challenge.
	 */

	/* BEGINNER */
	private List<Wave> waveStandard1 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 1, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot), 1, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 2, 2, 90))
			));
	private List<Wave> waveRooftop1 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 3, 2, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly), 1, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 3, 2, 80))
			));
	private List<Wave> waveTruck1 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.shoot), 4, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy), 1, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.heavy, EnemyRepo.basic), 4, 3, 80))
			));
	private List<Wave> waveBlocks1 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.basic), 4, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 4, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb), 3, 1, 60))
			));
	private List<Wave> waveMushroom1 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 5, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.heavy), 3, 2, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.bomb, EnemyRepo.shoot, EnemyRepo.heavy), 5, 3, 120))
			));
	private List<Wave> waveBoss1 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 1, 2, 120))
			));

	/* STANDARD */
	private List<Wave> waveStandard2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 2, 1, 30))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot), 2, 1, 30))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 6, 2, 90))
			));
	private List<Wave> waveRooftop2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 3, 3, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly), 2, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 6, 3, 80))
			));
	private List<Wave> waveTruck2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy), 2, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.shoot), 6, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.heavy, EnemyRepo.basic), 5, 3, 80))
			));
	private List<Wave> waveSpace2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.basic), 5, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.heavy, EnemyRepo.fly), 5, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.shoot), 5, 4, 20))
			));
	private List<Wave> waveBlocks2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.bomb), 6, 3, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy), 2, 2, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 7, 4, 120))
			));
	private List<Wave> waveMushroom2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 7, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.heavy), 5, 2, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb), 16, 8, 20))
			));
	private List<Wave> waveSky2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 6, 4, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy), 3, 2, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.heavy), 8, 8, 180))
			));
	private List<Wave> waveBoss2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.heavy), 1, 3, 120))
			));

	/* ADVANCED */
	private List<Wave> waveStandard3 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 5, 3, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.fly), 6, 3, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 6, 3, 60))
			));
	private List<Wave> waveRooftop3 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.heavy), 4, 2, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 6, 3, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.shoot), 6, 3, 80))
			));
	private List<Wave> waveTruck3 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy, EnemyRepo.basic, EnemyRepo.shoot), 4, 3, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.weakfly), 15, 8, 30))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.fatheavy, EnemyRepo.bomb), 7, 3, 60))
			));
	private List<Wave> waveBlocks3 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.basic), 7, 3, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.shoot), 6, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.bomb), 7, 4, 120))
			));
	private List<Wave> waveMushroom3 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 6, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.heavy), 5, 2, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb), 25, 12, 20))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatheavy), 1, 1, 120))
			));
	private List<Wave> waveSpace3 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.basic), 6, 3, 20))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.heavy, EnemyRepo.fly), 6, 3, 20))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb, EnemyRepo.shoot, EnemyRepo.basic), 6, 4, 20))
			));
	private List<Wave> waveSky3 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 6, 6, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy, EnemyRepo.fatheavy), 4, 2, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.bomb), 6, 7, 20))
			));
	private List<Wave> waveBoss3 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb, EnemyRepo.fly, EnemyRepo.heavy, EnemyRepo.basic, EnemyRepo.shoot), 1, 3, 90))
			));

	/* NIGHTMARE */
	private List<Wave> waveStandard4 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.heavy, EnemyRepo.bomb), 30, 6, 90))
			));
	private List<Wave> waveRooftop4 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 8, 8, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.fatfly), 8, 4, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot), 4, 4, 70))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.fly), 9, 6, 80))
			));
	private List<Wave> waveTruck4 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.weakbasic), 24, 12, 20))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatheavy), 1, 1, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.weakfly), 24, 12, 20))
			));
	private List<Wave> waveBlocks4 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 5, 5, 90))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 5, 5, 90))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb, EnemyRepo.fly, EnemyRepo.basic), 5, 5, 90))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.bomb, EnemyRepo.fly, EnemyRepo.basic), 5, 5, 90))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy, EnemyRepo.shoot, EnemyRepo.bomb, EnemyRepo.fly, EnemyRepo.basic), 5, 5, 90))
			));
	private List<Wave> waveMushroom4 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot), 10, 4, 30))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.heavy), 6, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot), 10, 10, 90))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb), 30, 30, 3))
			));
	private List<Wave> waveSpace4 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.fatbasic), 6, 4, 20))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb, EnemyRepo.shoot, EnemyRepo.basic), 6, 5, 20))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatfly, EnemyRepo.fatheavy, EnemyRepo.fatfly), 6, 3, 20))
			));
	private List<Wave> waveSky4 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.fatheavy), 6, 2, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.weakfly), 50, 20, 10))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy), 4, 4, 180))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatshoot, EnemyRepo.fatbomb), 6, 6, 120))
			));
	private List<Wave> waveBoss4 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.shoot, EnemyRepo.heavy, EnemyRepo.bomb), 1, 4, 10))
			));


	List<Challenge> challengeList = new ArrayList<Challenge>(Arrays.asList());

	@Override
	List<Challenge> getChallengeList() {
		return challengeList;
	}
	
	@Override
	protected void addPendingBonuses(){
		List<Bonus> consolidatedBonuses = new ArrayList<Bonus>();
		for (Bonus newBonus: pendingBonuses){
			boolean shouldAdd = true;
			for (Bonus bonus: consolidatedBonuses){
				if (bonus.getClass() == newBonus.getClass()) {
					if (newBonus instanceof Bonus.MultBonus) ((Bonus.MultBonus)bonus).increase();
					shouldAdd = false;
				}
			}
			if (shouldAdd) consolidatedBonuses.add(newBonus);
		}
		super.addPendingBonuses();
		if (activeChallengeIndex < challengeList.size() - 1) {
			DowntiltEngine.startRoundEnd(consolidatedBonuses, Victory.getBonusScore(bonuses));
		}
	}
	
	@Override
	public int getTime(){
		int seconds = 0;
		for (Challenge c: getChallengeList()){
			seconds += c.getSeconds();
		}
		return seconds/3600;
	}
	
	@Override
	Victory getVictory(){
		bonuses.add(new Bonus.TimeBonus(getTime()));
		List<Integer> longestComboList = new ArrayList<Integer>();
		for (Challenge c: getChallengeList()){
			longestComboList.add(c.getLongestCombo());
		}
		return new Victory.AdventureVictory(difficulty, bonuses);
	}
	
	@Override
	public Difficulty getDifficulty(){
		return difficulty;
	}
	
	@Override
	public void pendValidBonus(Bonus newBonus){
		if (!newBonus.singleOnly) super.pendValidBonus(newBonus);
	}

}
