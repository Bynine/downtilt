package entities;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import challenges.Challenge.Difficulty;
import main.DowntiltEngine;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import maps.Stage.LightningHandler;
import timers.Timer;

public class Boss extends Hittable {

	private static final int fireTiming = 20;
	private static Animation floatImage = GlobalRepo.makeAnimation("sprites/fighters/boss/float.png", 2, 1, 30, PlayMode.LOOP);
	private static Animation hurtImage = GlobalRepo.makeAnimation("sprites/fighters/boss/hurt.png", 1, 1, 10, PlayMode.LOOP);
	private static Animation castImage = GlobalRepo.makeAnimation("sprites/fighters/boss/cast.png", 2, 1, 10, PlayMode.LOOP);
	private static Animation fireImage = GlobalRepo.makeAnimation("sprites/fighters/boss/fire.png", 2, 1, fireTiming, PlayMode.LOOP);
	private int health = 1;
	private int spellModulo = 180;
	private double spellChance = 0.5;
	private float laserSpeed = 6;
	private float laserSpeedX = 6;
	private float laserSpeedY = -9;
	private int teleportRange = GlobalRepo.TILE * 12;
	private final Timer hurtTimer = new Timer(20), castTimer = new Timer(60), fireTimer = new Timer(40);

	public Boss(float posX, float posY) {
		super(posX, posY);
		gravity = 0;
		grabbable = false;
		timerList.addAll(Arrays.asList(hurtTimer, castTimer, fireTimer));
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		boolean playerIsClose = false;
		if (playerIsClose || hurtTimer.timeJustUp()) teleport();
		if (deltaTime % spellModulo == 0 && Math.random() < spellChance && !isHurt()) {
			if (Math.random() < 0.33){
				new SFX.PrepAttack().play();
				castTimer.reset();
			}
			else{
				fireTimer.reset();
			}
		}
		if (castTimer.timeJustUp()) castSpell();
		if (fireTimer.getCounter() == fireTiming) makeLaser(0);	
	}

	private void teleport(){
		MapHandler.addEntity(new Graphic.Teleport(position.x, position.y));
		position.x += teleportRange;
		teleportRange *= -1;
		flip();
		new SFX.Teleport().play();
	}

	private void castSpell(){
		double random = Math.random();
		
		if (random < 0.4/3.0) {
			teleport();
		}
		
		else if (random < (1.8/3.0)){
			new SFX.CastSpellLightning().play();
			MapHandler.addEntity(new Graphic.LightningSpell(position.x, position.y));
			MapHandler.addLightningHandler(new LightningHandler(270));
		}
		
		else{
			if (Math.random() < 0.5) {
				new SFX.CastSpell().play();
				MapHandler.addEntity(new Graphic.LowGravity(position.x, position.y));
				MapHandler.setLowGravity();
			}
			else {
				new SFX.CastSpellDown().play();
				MapHandler.setHighGravity();
				MapHandler.addEntity(new Graphic.HighGravity(position.x, position.y));
			}
		}
	}

	private void makeLaser(int x){
		float posX = position.x - 32;
		if (direction == Direction.RIGHT) posX = position.x + image.getWidth() + 32;
		float posY = position.y - 32;
		Hurlable.Laser laser = new Hurlable.Laser(GlobalRepo.GOODTEAM, posX, posY, laserSpeed);
		laser.getVelocity().x = laserSpeedX * direct();
		laser.getVelocity().y = (float) (Math.random() * laserSpeedY);
		MapHandler.addTimedEntity(laser, x);
	}

	int easySpellModulo = 120;
	public void set(Difficulty difficulty){
		switch(difficulty){
		case Beginner: {
			health = 3;
			spellModulo = easySpellModulo;
			spellChance = 0.4;
			laserSpeed = 3;
		} break;
		case Standard: {
			health = 6;
			spellModulo = 75;
			spellChance = 0.6;
			laserSpeed = 5;
		} break;
		case Advanced: {
			health = 10;
			spellModulo = 60;
			spellChance = 0.8;
		} break;
		case Nightmare: {
			health = 15;
			spellModulo = 50;
			spellChance = 0.9;
			laserSpeed = 9;
			castTimer.setEndTime(40);
			fireTimer.setEndTime(24);
		} break;
		}

	}

	public int getHealth(){
		return health;
	}

	@Override
	protected void takeKnockIntoKnockback(Hittable hurtler, Vector2 knockback, float DAM, int hitstun){
		if (DAM > 0 && !isHurt()) {
			DowntiltEngine.causeHitlag(10);
			health--;
			hurtTimer.reset();
			castTimer.end();
			if (health == 1 && spellModulo != easySpellModulo) {
				spellModulo = MathUtils.clamp(spellModulo/2, 41, 999);
			}
			hurtler.setRemove();
		}
		if (health == 0) DowntiltEngine.causeHitlag(16);
	}

	protected void takeKnockback(Vector2 knockback, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		/**/
	}

	private float t = 60;
	@Override
	void updatePosition(){
		if (isHurt() || isCasting()) return;
		t++;
		final float circleMod = 120;
		final float circleSpeed = 0.5f;
		Vector2 move = new Vector2(circleSpeed, circleSpeed);
		float angle = (float) (((t % circleMod)/circleMod) * 360);
		move.setAngle(angle);
		position.add(move);
	}

	public boolean isHurt(){
		return !hurtTimer.timeUp();
	}

	private boolean isCasting(){
		return !castTimer.timeUp();
	}
	
	private boolean isFiring(){
		return !fireTimer.timeUp();
	}

	@Override
	void handleTouchHelper(Entity en){
		if (en instanceof Hittable){
			checkHitByHurtlingObject((Hittable) en);
		}
		if (DowntiltEngine.entityIsPlayer(en) && isTouching(en, -80)) {
			teleport();
		}
	}

	@Override
	TextureRegion getStandFrame(float deltaTime) {
		if (isHurt()) return hurtImage.getKeyFrame(hurtTimer.getCounter());
		else if (isCasting()) return castImage.getKeyFrame(castTimer.getCounter());
		else if (isFiring()) return fireImage.getKeyFrame(fireTimer.getCounter());
		return floatImage.getKeyFrame(deltaTime);
	}

	@Override
	TextureRegion getTumbleFrame(float deltaTime) {
		return getStandFrame(deltaTime);
	}

}
