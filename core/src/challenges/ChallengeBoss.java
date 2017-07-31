package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.Boss;
import entities.BossEye;
import main.DowntiltEngine;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import maps.Stage;

public class ChallengeBoss extends ChallengeEndless {

	private final Boss boss = new Boss();
	private final BossEye 
	left = new BossEye.Left(GlobalRepo.TILE * 13, GlobalRepo.TILE * 8, boss),
	right = new BossEye.Right(GlobalRepo.TILE * 29, GlobalRepo.TILE * 8, boss),
	down = new BossEye.Down(GlobalRepo.TILE * 19, GlobalRepo.TILE * 15, boss);
	private final List<BossEye> bossEyeList = new ArrayList<BossEye>(Arrays.asList(left, right, down));

	public ChallengeBoss(Stage stage, List<Wave> waves) {
		super(stage, waves);
	}

	protected void startChallenge(){
		super.startChallenge();
		left.toggleOpen();
		down.toggleOpen();
		for (BossEye bossEye: bossEyeList) MapHandler.addEntity(bossEye);
	}

	public void update(){
		super.update();
		if (DowntiltEngine.getDeltaTime() % 240 == 0){
			for (BossEye bossEye: bossEyeList) if (bossEye.isOpen()) bossEye.fire();
		}
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
