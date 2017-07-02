package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.*;
import main.DowntiltEngine;
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
	List<Wave> wave1 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoots), 3, 1, 60, false))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 3, 2, 120, false))
			));
	//	List<Wave> wave1 = new ArrayList<Wave>(Arrays.asList(
	//			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 3, 1, 60, false))
	//			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 4, 2, 120, false))
	//			));
	List<Wave> wave2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 3, 2, 60, false))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies), 3, 2, 120, false))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies, EnemyRepo.basic), 6, 3, 80, false))
			));
	List<Wave> wave3 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies, EnemyRepo.basic), 6, 3, 80, false))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bombs), 20, 8, 20, false))
			));
	List<Wave> wave4 = new ArrayList<Wave>(Arrays.asList(
			WaveGenerator.generate(4)
			));
	List<Wave> wave5 = new ArrayList<Wave>(Arrays.asList(
			WaveGenerator.generate(5)
			,WaveGenerator.generate(5)
			));


	/**
	 * List of challenges to be iterated through.
	 */
	List<Challenge> challengeList = new ArrayList<Challenge>(Arrays.asList(
			new  ChallengeNorm(new Stage_Blocks(), wave1)
			,new ChallengeNorm(new Stage_Rooftop(), wave2)
			,new ChallengeNorm(new Stage_Mushroom(), wave3)
			,new ChallengeNorm(new Stage_Columns(), wave4)
			,new ChallengeNorm(new Stage_Rooftop(), wave5)

			//			new  ChallengeNorm(new Stage_Standard(), wave1)
			//			,new ChallengeNorm(new Stage_Rooftop(), wave2)
			//			,new ChallengeNorm(new Stage_Mushroom(), wave3)
			//			,new ChallengeNorm(new Stage_Columns(), wave4)
			//			,new ChallengeNorm(new Stage_Dungeon(), wave5)
			));

	public Challenge getActiveChallenge(){
		if (activeChallengeIndex >= challengeList.size()) return challengeList.get(challengeList.size() - 1);
		return challengeList.get(activeChallengeIndex);
	}

	/**
	 * Starts a new challenge after finishing the old one..
	 */
	public void update(){
		if (DowntiltEngine.musicToggle) getActiveChallenge().getStage().getMusic().play();
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
