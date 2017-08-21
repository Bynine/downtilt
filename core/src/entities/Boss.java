package entities;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
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

	private Animation floatImage = GlobalRepo.makeAnimation("sprites/fighters/boss/float.png", 2, 1, 30, PlayMode.LOOP);
	private Animation hurtImage = GlobalRepo.makeAnimation("sprites/fighters/boss/hurt.png", 1, 1, 10, PlayMode.LOOP);
	private Animation castImage = GlobalRepo.makeAnimation("sprites/fighters/boss/cast.png", 2, 1, 10, PlayMode.LOOP);
	private int health = 1;
	private int modulo = 180;
	private int lightning = 120;
	private double chance = 0.5;
	private float laserSpeedX = 6;
	private float laserSpeedY = -9;
	private int teleportRange = GlobalRepo.TILE * 12;
	private final Timer hurtTimer = new Timer(20), castTimer = new Timer(40);

	public Boss(float posX, float posY) {
		super(posX, posY);
		gravity = 0;
		grabbable = false;
		timerList.addAll(Arrays.asList(hurtTimer, castTimer));
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		boolean playerIsClose = false;
		if (playerIsClose || hurtTimer.timeJustUp()) teleport();
		if (deltaTime % modulo == 0 && Math.random() < chance && !isHurt()) {
			new SFX.PrepAttack().play();
			castTimer.reset();
		}
		if (castTimer.timeJustUp()) castSpell();
	}

	private void teleport(){
		MapHandler.addEntity(new Graphic.Teleport(position.x, position.y));
		position.x += teleportRange;
		teleportRange *= -1;
		flip();
		new SFX.Teleport().play();
	}

	private void castSpell(){
		new SFX.CastSpell().play();

		double random = Math.random();
		if (random < 0.25/3.0) {
			teleport();
		}
		else if (random < (2.0/3.0)){
			makeLaser(0);
			makeLaser(10);
			makeLaser(20);
		}
		else if (random < (2.5/3.0)){
			MapHandler.addEntity(new Graphic.LightningSpell(position.x, position.y));
			MapHandler.addLightningHandler(new LightningHandler(360, 60, lightning, 1));
		}
		else{
			if (Math.random() < 0.5) {
				MapHandler.addEntity(new Graphic.LowGravity(position.x, position.y));
				MapHandler.setLowGravity();
			}
			else {
				MapHandler.setHighGravity();
				MapHandler.addEntity(new Graphic.HighGravity(position.x, position.y));
			}
		}
	}

	private void makeLaser(int x){
		float posX = position.x - 32;
		if (direction == Direction.RIGHT) posX = position.x + image.getWidth() + 32;
		float posY = position.y - 32;
		Hurlable.Laser laser = new Hurlable.Laser(GlobalRepo.GOODTEAM, posX, posY);
		laser.getVelocity().x = laserSpeedX * direct();
		laser.getVelocity().y = (float) (Math.random() * laserSpeedY);
		MapHandler.addTimedEntity(laser, x);
	}

	public void set(Difficulty difficulty){
		switch(difficulty){
		case Beginner: {
			health = 3;
			modulo = 240;
			chance = 0.4;
			castTimer.setEndTime(60);
		} break;
		case Standard: {
			health = 10;
			modulo = 150;
			chance = 0.6;
		} break;
		case Advanced: {
			health = 15;
			modulo = 120;
			chance = 0.8;
		} break;
		case Nightmare: {
			health = 20;
			modulo = 70;
			chance = 1.1;
			lightning = 80;
			laserSpeedX = 8;
			laserSpeedY = -12;
		} break;
		}

	}

	public int getHealth(){
		return health;
	}

	@Override
	protected void takeKnockIntoKnockback(Hittable hurtler, Vector2 knockback, float DAM, int hitstun){
		if (DAM > 0 && !isHurt()) {
			DowntiltEngine.causeHitlag(8);
			health--;
			hurtTimer.reset();
			castTimer.end();
			if (health == 1) {
				castTimer.setEndTime(castTimer.getEndTime()/2);
				modulo = modulo/2;
			}
			hurtler.setRemove();
		}
	}

	protected void takeKnockback(Vector2 knockback, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		/**/
	}

	void updatePosition(){
		/* doesn't move */
	}

	private boolean isHurt(){
		return !hurtTimer.timeUp();
	}

	private boolean isCasting(){
		return !castTimer.timeUp();
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
		if (isHurt()) return hurtImage.getKeyFrame(deltaTime);
		else if (isCasting()) return castImage.getKeyFrame(deltaTime);
		return floatImage.getKeyFrame(deltaTime);
	}

	@Override
	TextureRegion getTumbleFrame(float deltaTime) {
		return getStandFrame(deltaTime);
	}

}
