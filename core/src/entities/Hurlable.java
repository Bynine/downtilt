package entities;

import java.util.List;

import timers.DurationTimer;
import main.DowntiltEngine;
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
	
	private static abstract class Breakable extends Hurlable {
		
		protected final DurationTimer life = new DurationTimer(400);
		protected float frailtyMod = 3;
		
		public Breakable(float posX, float posY){
			super(posX, posY);
			timerList.add(life);
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			if (life.timeUp()) shatter();
		}
		
		private void shatter(){
			new SFX.Break().play();
			setRemove();
		}
		
		protected void takeDamage(float DAM){
			life.moveCounterForward((int) (DAM * frailtyMod) );
		}
		
		public Color getColor(){
			if ( (DowntiltEngine.getDeltaTime() % 20 < 10) && life.getCounter() > life.getEndTime() * (5.0/6.0)) return new Color(1, 1, 1, 0.5f);
			else return super.getColor();
		}
		
	}
	
	public static class Nut extends Breakable {

		public Nut(float posX, float posY) {
			super(posX, posY);
			normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/nut.png")));
			tumbleImage = GlobalRepo.makeAnimation("sprites/entities/nutspin.png", 2, 1, 8, PlayMode.LOOP);
			image = new Sprite(normImage);
			baseKnockIntoDamage = 3f;
			hitstunDealtBonus = 12;
			airFriction = 0.992f;
			baseWeight = 80;
		}

	}
	
	public static class ShootBall extends Breakable {

		private final float minSpeedForHit = 2.2f;
		public ShootBall(Fighter user, int team, float posX, float posY) {
			super(posX, posY);
			if (user.getDirection() == Direction.LEFT) posX += user.getImage().getWidth()/4;
			else posX += 3 * user.getImage().getWidth()/4;
			this.team = team;
			normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ballbad.png")));
			tumbleImage = GlobalRepo.makeAnimation("sprites/entities/ballbadspin.png", 4, 1, 6, PlayMode.LOOP);
			image = new Sprite(normImage);
			baseKnockIntoDamage = 4f;
			hitstunDealtBonus = 8;
			airFriction = 0.992f;
			friction = 0.97f;
			baseWeight = 90;
			baseHurtleBK = minSpeedForHit;
			baseKBG = 3.5f;
			touchRadius = 16;
			life.setEndTime(240);
			frailtyMod = 1.5f;
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
			if (user.getTeam() == GlobalRepo.GOODTEAM) {
				team = GlobalRepo.BADTEAM;
				normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ballgood.png")));
				tumbleImage = GlobalRepo.makeAnimation("sprites/entities/ballgoodspin.png", 4, 1, 6, PlayMode.LOOP);
			}
			else if (user.getTeam() == GlobalRepo.BADTEAM) {
				team = GlobalRepo.GOODTEAM;
				normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ballbad.png")));
				tumbleImage = GlobalRepo.makeAnimation("sprites/entities/ballbadspin.png", 4, 1, 6, PlayMode.LOOP);
			}
		}
		
		@Override
		public void ground(){ 
			if (velocity.y < -1 && !inGroundedState()){
				velocity.y *= -1.1;
			}
			super.ground();
		}
		
		@Override
		public boolean inHitstun(){
			if (life.getCounter() < 6) return false;
			return knockbackIntensity(velocity) > minSpeedForHit;
		}
		
		@Override
		public Rectangle getBodyHitBox(){
			Rectangle r = getHurtBox();
			float mod = 0.75f;
			int inc = 4; 
			r.setWidth(r.getWidth() * mod);
			r.setHeight(r.getHeight() * mod);
			r.setX(r.getX() + inc);
			r.setY(r.getY() + inc);
			return r;
		}
		
	}
	
	public static class Rocket extends Hurlable {

		public Rocket(float posX, float posY) {
			super(posX, posY);
		}
		
	}
	
	public static class Grenade extends Hurlable {

		public Grenade(float posX, float posY) {
			super(posX, posY);
		}
		
	}
}
