package challenges;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import entities.Basic;
import entities.Boss;
import entities.Entity.Direction;
import entities.Graphic;
import entities.Hittable.HitstunType;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import maps.Stage;

public class ChallengeBoss extends ChallengeEndless {

	private final Boss boss = new Boss(GlobalRepo.TILE * 15, GlobalRepo.TILE * 11);
	private final Basic.PostBoss postBoss = new Basic.PostBoss(0, 0, GlobalRepo.BADTEAM);
	private final Difficulty difficulty;
	private boolean postBossAdded = false;

	public ChallengeBoss(Stage stage, List<Wave> waves, Difficulty difficulty) {
		super(stage, waves);
		for (Wave w: waves) w.setEndless();
		this.difficulty = difficulty;
		lifeSetting = 4;
	}

	protected void startChallenge(){
		super.startChallenge();
		boss.set(difficulty);
		MapHandler.addEntity(boss);
	}
	
	public String getWaveCounter() {
		return "HEALTH: " + boss.getHealth();
	}
	
	@Override
	protected void nextWaveChecker(){
		if (inSuccessState() && !postBossAdded) setPostBossMode();
		else if (postBossAdded && MapHandler.entityNotAlive(postBoss)) succeedChallenge();
	}
	
	private void setPostBossMode(){
		int xMod = 30;
		postBoss.setPosition(new Vector2(boss.getPosition().x + xMod, boss.getPosition().y));
		activeWave.stop();
		boolean bossFacingLeft = boss.getDirection() == Direction.LEFT;
		MapHandler.removeAllNonPlayerEntities();
		Graphic bossSheet = new Graphic.BossSheet(postBoss.getPosition().x - xMod, postBoss.getPosition().y);
		if (bossFacingLeft) bossSheet.flip();
		MapHandler.addEntity(bossSheet);
		MapHandler.addEntity(postBoss);
		new SFX.Fall().play();
		postBossAdded = true;
		getStage().getMusic().stop();
		
		final int center = 500;
		final float speedX = 1;
		final float speedY = -7;
		final int hitstunTime = 50;
		if (boss.getPosition().x < center) postBoss.takeKnockback(new Vector2(speedX, speedY), hitstunTime, true, HitstunType.SUPER);
		else postBoss.takeKnockback(new Vector2(-speedX, speedY), hitstunTime, true, HitstunType.SUPER);
	}
	
	@Override
	protected boolean inSuccessState(){
		return boss.getHealth() <= 0;
	}

	@Override
	public void failChallenge(){
		new SFX.Error().play();
		startChallenge();
	}
	
	@Override
	public void resolveCombo(float mod){
		/**/
	}

	@Override
	public boolean bossHurt(){
		return boss.isHurt() && !MapHandler.entityNotAlive(boss);
	}
	
	@Override
	public boolean postBoss(){
		return postBossAdded;
	}
	
	
}
