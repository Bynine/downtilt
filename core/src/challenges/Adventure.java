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

public class Adventure {

	private int activeChallengeIndex;

	/**
	 * Decides between debug beginning round and intended one.
	 */
	private void initializeChallengeList(){
		if (DowntiltEngine.debugOn()) {
			challengeList.add(0, new ChallengeEndless(new Stage_Boss(), waveDebug));
		}
		else {
			challengeList.add(0, new ChallengeNorm(new Stage_Standard(), waveStandard));
		}
	}

	/**
	 * Default constructor. Used for normal gameplay.
	 */
	public Adventure(){
		activeChallengeIndex = 0;
		initializeChallengeList();
	}

	/**
	 * Debug constructor used to skip ahead in stage order.
	 */
	public Adventure(int index){
		activeChallengeIndex = index;
		initializeChallengeList();
	}

	/**
	 * Selection of wave lists for each challenge.
	 */

	private List<Wave> waveDebug = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.bomb, EnemyRepo.heavy,
					EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.bomb), 
					EnemySpawner.ENDLESS, 5, 50))
			));
	private List<Wave> waveStandard = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic), 2, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot), 2, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 7, 2, 90))
			));
	private List<Wave> waveRooftop = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 3, 3, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly), 3, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 6, 3, 80))
			));
	private List<Wave> waveTruck = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy), 2, 1, 60))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.shoot), 8, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.heavy, EnemyRepo.basic), 9, 3, 80))
			));
	private List<Wave> waveBlocks = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.basic), 7, 3, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fatheavy), 1, 1, 120))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.bomb), 8, 4, 120))
			));
	private List<Wave> waveMushroom = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic), 6, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.heavy), 5, 2, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb), 25, 8, 20))
			));
	private List<Wave> waveSpace = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.basic), 6, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.heavy, EnemyRepo.fly), 6, 3, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.bomb, EnemyRepo.shoot, EnemyRepo.basic), 8, 4, 20))
			));
	private List<Wave> waveSky = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.basic, EnemyRepo.shoot), 6, 4, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.heavy), 3, 2, 80))
			,new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.shoot, EnemyRepo.bomb), 6, 6, 40))
			));
	private List<Wave> waveBoss = new ArrayList<Wave>(Arrays.asList(
			new Wave(new EnemySpawner(Arrays.asList(EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.bomb, EnemyRepo.heavy,
					EnemyRepo.fly, EnemyRepo.basic, EnemyRepo.shoot, EnemyRepo.bomb), 
					EnemySpawner.ENDLESS, 5, 50))
			));

	/**
	 * List of challenges to be iterated through.
	 */
	List<Challenge> challengeList = new ArrayList<Challenge>(Arrays.asList(
			// intro stage, then...
			new  ChallengeNorm(new Stage_Rooftop(), waveRooftop)
			,new ChallengeNorm(new Stage_Truck(), waveTruck)
			,new ChallengeNorm(new Stage_Blocks(), waveBlocks)
			,new ChallengeNorm(new Stage_Mushroom(), waveMushroom)
			,new ChallengeNorm(new Stage_Space(), waveSpace)
			,new ChallengeNorm(new Stage_Sky(), waveSky)
			,new ChallengeEndless(new Stage_Boss(), waveBoss)
			));

	public Challenge getActiveChallenge(){
		if (activeChallengeIndex >= challengeList.size()) return challengeList.get(challengeList.size() - 1);
		return challengeList.get(activeChallengeIndex);
	}

	/**
	 * Starts a new challenge after finishing the old one..
	 */
	public void update(){
		if (DowntiltEngine.musicOn()) getActiveChallenge().getStage().getMusic().play();
		if (!getActiveChallenge().started) getActiveChallenge().startChallenge();
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