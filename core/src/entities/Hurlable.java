package entities;

import java.util.List;

import timers.DurationTimer;
import main.GlobalRepo;
import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Hurlable extends Hittable {

	protected TextureRegion normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/trashcan.png")));
	protected Animation tumbleImage = GlobalRepo.makeAnimation("sprites/entities/trashcanspin.png", 4, 1, 8, PlayMode.LOOP);
	float staticPercent = 50;

	public Hurlable(float posX, float posY) {
		super(posX, posY);
		baseHitstun = 8;
		baseHitSpeed = -0.6f;
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		updateImage(deltaTime);
		if (isGrounded() && !inGroundedState()) ground();
		else if (!isGrounded()) state = State.FALL;
	}

	public void ground(){
		super.ground();
		state = State.STAND;
		hitstunTimer.end();
	}

	public float getPercentage(){
		return staticPercent;
	}

	TextureRegion getStandFrame(float deltaTime) {
		return normImage;
	}

	TextureRegion getTumbleFrame(float deltaTime) {
		return tumbleImage.getKeyFrame(deltaTime);
	}


	public static class TrashCan extends Hurlable {

		public TrashCan(float posX, float posY) {
			super(posX, posY);
			normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/trashcan.png")));
			tumbleImage = GlobalRepo.makeAnimation("sprites/entities/trashcanspin.png", 4, 1, 8, PlayMode.LOOP);
			image = new Sprite(normImage);
			baseKnockIntoDamage = 3f;
			hitstunDealtBonus = 12;
			airFriction = 0.986f;
			baseWeight = 130;
		}

	}
	
	public static class Nut extends Hurlable {
		
		private final DurationTimer life = new DurationTimer(900);
		private int health = 60;

		public Nut(float posX, float posY) {
			super(posX, posY);
			normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/nut.png")));
			tumbleImage = GlobalRepo.makeAnimation("sprites/entities/nutspin.png", 2, 1, 8, PlayMode.LOOP);
			image = new Sprite(normImage);
			baseKnockIntoDamage = 3f;
			hitstunDealtBonus = 12;
			airFriction = 0.992f;
			baseWeight = 80;
			timerList.add(life);
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (life.timeUp() || health <= 0) shatter();
		}
		
		private void shatter(){
			new SFX.Break().play();
			setRemove();
		}
		
		protected void takeDamage(float DAM){
			health -= DAM;
		}
		
		public Color getColor(){
			if (life.getCounter() > life.getEndTime() * (5.0/6.0)) return new Color(1, 1, 1, 0.5f);
			else return super.getColor();
		}

	}
	
	public static class ShootBall extends Hurlable {

		public ShootBall(int team, float posX, float posY) {
			super(posX, posY);
			this.team = team;
			normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ball.png")));
			tumbleImage = GlobalRepo.makeAnimation("sprites/entities/ballspin.png", 4, 1, 6, PlayMode.LOOP);
			image = new Sprite(normImage);
			baseKnockIntoDamage = 4f;
			hitstunDealtBonus = 8;
			airFriction = 0.992f;
			friction = 0.97f;
			baseWeight = 90;
		}
		
		public void takeDamagingKnockback(Vector2 knockback, float DAM, int hitstun, HitstunType hitboxhitstunType, Hittable user) {
			super.takeDamagingKnockback(knockback, DAM, hitstun, hitboxhitstunType, user);
			if (null != user) switchTeam(user);
		}
		
		public void getGrabbed(Fighter user, Hittable target, int caughtTime) {
			super.getGrabbed(user, target, caughtTime);
			switchTeam(user);
		}
		
		private void switchTeam(Hittable user){
			if (user.getTeam() == GlobalRepo.GOODTEAM) team = GlobalRepo.BADTEAM;
			else if (user.getTeam() == GlobalRepo.BADTEAM) team = GlobalRepo.GOODTEAM;
		}
		
		public void ground(){ 
			if (velocity.y < -1 && !inGroundedState()){
				velocity.y *= -1.1;
			}
			super.ground();
		}
		
		public Color getColor(){
			if (team == GlobalRepo.GOODTEAM) return new Color(1, 0, 0, 1);
			if (team == GlobalRepo.BADTEAM) return new Color(0, 0, 1, 1);
			else return new Color(0, 0, 0, 1);
		}
		
		public boolean inHitstun(){
			return knockbackIntensity(getVelocity()) > 1;
		}
		
	}
}
