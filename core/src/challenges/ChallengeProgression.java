package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.*;
import main.DowntiltEngine;
import main.GlobalRepo;
import main.SFX;
import maps.*;

/**
 * A substitute for the eventual world map. Controls active challenge.
 */

public class ChallengeProgression {

	private int activeChallengeIndex;

	/**
	 * Default constructor. Used for normal gameplay.
	 */
	public ChallengeProgression(){
		activeChallengeIndex = 0;
	}

	/**
	 * Debug constructor used to skip ahead in stage order.
	 */
	public ChallengeProgression(int index){
		activeChallengeIndex = index;
	}

	/**
	 * Selection of wave lists for each challenge.
	 */
	List<Wave> waveTest = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.zen), 8, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 3, 2, 120))
			));
	
	List<Wave> waveStandard = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 3, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoots), 3, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoots), 5, 2, 90))
			));
	private List<Wave> waveRooftop = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoots), 3, 3, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies), 3, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies, EnemyRepo.basic), 6, 3, 80))
			));
	private List<Wave> waveBlocks = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoots, EnemyRepo.basic), 7, 3, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies, EnemyRepo.bombs), 8, 4, 120))
			));
	private List<Wave> waveForest = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies, EnemyRepo.basic), 6, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoots), 5, 2, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bombs), 20, 8, 20))
			));
	private List<Wave> waveFinal = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies, EnemyRepo.shoots), 10, 4, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.bombs), 10, 10, 40))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies, EnemyRepo.basic, EnemyRepo.shoots, EnemyRepo.bombs), 20, 4, 30))
			));


	/**
	 * List of challenges to be iterated through.
	 */
	List<Challenge> challengeList = new ArrayList<Challenge>(Arrays.asList(
			new  ChallengeNorm(new Stage_Standard(), waveTest)
			,new ChallengeNorm(new Stage_Rooftop(), waveRooftop)
			,new ChallengeNorm(new Stage_Blocks(), waveBlocks)
			,new ChallengeNorm(new Stage_Mushroom(), waveForest)
			,new ChallengeNorm(new Stage_Sky(), waveFinal)

//			new  ChallengeNorm(new Stage_Standard(), waveStandard)
//			,new ChallengeNorm(new Stage_Rooftop(), waveRooftop)
//			,new ChallengeNorm(new Stage_Blocks(), waveBlocks)
//			,new ChallengeNorm(new Stage_Mushroom(), waveForest)
//			,new ChallengeNorm(new Stage_Sky(), waveFinal)
			));

	public Challenge getActiveChallenge(){
		if (activeChallengeIndex >= challengeList.size()) return challengeList.get(challengeList.size() - 1);
		return challengeList.get(activeChallengeIndex);
	}

	/**
	 * Starts a new challenge after finishing the old one..
	 */
	public void update(){
		if (GlobalRepo.musicToggle) getActiveChallenge().getStage().getMusic().play();
		if (!getActiveChallenge().started) getActiveChallenge().begin();
		getActiveChallenge().update();
		if (getActiveChallenge().finished()) {
			for (Fighter player: DowntiltEngine.getPlayers()) player.refresh();
			getActiveChallenge().getStage().getMusic().stop();
			activeChallengeIndex++;
		}
		if (activeChallengeIndex > challengeList.size()) win();
	}

	/**
	 * Activates when all challenges are completed.
	 */
	void win(){
		new SFX.Victory().play();
		DowntiltEngine.returnToMenu();
	}

}
