package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import challenges.Adventure.Difficulty;
import entities.Boss;
import entities.BossEye;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import maps.Stage;

public class ChallengeBoss extends ChallengeEndless {

	private final Boss boss = new Boss();
	private final BossEye 
	left = new BossEye.Left(GlobalRepo.TILE * 13, GlobalRepo.TILE * 9, boss),
	right = new BossEye.Right(GlobalRepo.TILE * 29, GlobalRepo.TILE * 6, boss),
	down = new BossEye.Down(GlobalRepo.TILE * 19, GlobalRepo.TILE * 15, boss);
	private final List<BossEye> bossEyeList = new ArrayList<BossEye>(Arrays.asList(left, right, down));
	private final Difficulty difficulty;

	public ChallengeBoss(Stage stage, List<Wave> waves, Difficulty difficulty) {
		super(stage, waves);
		for (Wave w: waves) w.setEndless();
		this.difficulty = difficulty;
		soloLives = 5;
		coopLives = 3;
	}

	protected void startChallenge(){
		super.startChallenge();
		switch(difficulty){
		case BEGINNER: boss.setHealth(50); break;
		case STANDARD: boss.setHealth(100); break;
		case ADVANCED: boss.setHealth(175); break;
		case NIGHTMARE: boss.setHealth(300); break;
		}
		if (!right.isOpen())	right.toggleOpen();
		if (left.isOpen())		left.toggleOpen();
		if (down.isOpen())		down.toggleOpen();
		for (BossEye bossEye: bossEyeList) MapHandler.addEntity(bossEye);
	}

	public void rotateEyes(){
		if (right.isOpen()){
			right.toggleOpen();
			getRandomEye(down, left).toggleOpen();
		}
		else if (down.isOpen()){
			down.toggleOpen();
			getRandomEye(left, right).toggleOpen();
		}
		else if (left.isOpen()){
			left.toggleOpen();
			getRandomEye(down, right).toggleOpen();
		} 
	}

	private BossEye getRandomEye(BossEye a, BossEye b){
		if (Math.random() < 0.5) return a;
		else return b;
	}

	public String getWaveCounter() {
		return "HEALTH: " + boss.getHealth();
	}

	@Override
	public void failChallenge(){
		new SFX.Error().play();
		startChallenge();
	}

}
