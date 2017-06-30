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

	private int index = 0;

	/**
	 * Selection of wave lists for each challenge.
	 */
	List<Wave> wave1 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 3, 1, 60, false))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 3, 2, 120, false))
			));
	List<Wave> wave2 = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 3, 2, 60, false))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.flies), 3, 2, 120, false))
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
			new  ChallengeNorm(new Stage_Standard(), wave1)
			,new ChallengeNorm(new Stage_Dungeon(), wave2)
			,new ChallengeNorm(new Stage_Mushroom(), wave3)
			,new ChallengeNorm(new Stage_Columns(), wave4)
			,new ChallengeNorm(new Stage_Flat(), wave5)
			));

	public Challenge getActiveChallenge(){
		if (index >= challengeList.size()) return challengeList.get(challengeList.size() - 1);
		return challengeList.get(index);
	}

	/**
	 * Starts a new challenge after finishing the old one, and boots back to menu after the final challenge.
	 */
	public void update(){
		if (!getActiveChallenge().started) getActiveChallenge().begin();
		getActiveChallenge().update();
		if (getActiveChallenge().finished()) {
			for (Fighter player: DowntiltEngine.getPlayers()) player.refresh();
			index++;
		}
		if (index > challengeList.size()) win();
	}

	void win(){
		new SFX.Victory().play();
		DowntiltEngine.returnToMenu();
	}

}
