package entities;

import java.util.List;

import timers.DurationTimer;
import main.DowntiltEngine;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Hurlable extends Hittable {

	protected TextureRegion normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/nut.png")));
	protected Animation tumbleImage = GlobalRepo.makeAnimation("sprites/entities/nutspin.png", 4, 1, 8, PlayMode.LOOP);
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

	private static abstract class Explosive extends Hurlable {

		private boolean exploded = false;

		public Explosive(float posX, float posY) {
			super(posX, posY);
			baseHurtleBK = 0;
		}

		protected abstract Explosion getExplosion();

		protected boolean shouldExplode(){
			return !exploded;
		}

		protected void explode(){
			if (!shouldExplode()) return;
			exploded = true;
			MapHandler.addEntity(getExplosion());
			setRemove();
		}
		
		@Override
		public boolean inHitstun(){
			return true;
		}
		
		@Override
		protected boolean teamCheck(Hittable hi){
			for (Fighter player: DowntiltEngine.getPlayers()){
				if (hi.equals(player)) return false;
			}
			return true;
		}

	}

	public static class TrashCan extends Hurlable {

		public TrashCan(float posX, float posY) {
			super(posX, posY);
			normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/trashcan.png")));
			tumbleImage = GlobalRepo.makeAnimation("sprites/entities/trashcanspin.png", 4, 1, 8, PlayMode.LOOP);
			image = new Sprite(normImage);
			baseKnockIntoDamage = 3f;
			hitstunDealtBonus = 12;
			airFrictionX = 0.986f;
			baseWeight = 130;
		}

	}

	private static abstract class Breakable extends Hurlable {

		protected final DurationTimer life = new DurationTimer(400);
		protected float frailtyMod = 0;

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

		public void takeDamage(float DAM){
			life.moveCounterForward((int) (DAM * frailtyMod) );
		}

		public Color getColor(){
			if ( (DowntiltEngine.getDeltaTime() % 20 < 10) && life.getCounter() > life.getEndTime() * (5.0/6.0)) return new Color(1, 1, 1, 0.5f);
			else return super.getColor();
		}
		
		@Override
		public void knockInto(){
			shatter();
		}

	}

	public static class Nut extends Breakable {

		public Nut(float posX, float posY) {
			super(posX, posY);
			normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/nut.png")));
			tumbleImage = GlobalRepo.makeAnimation("sprites/entities/nutspin.png", 4, 1, 6, PlayMode.LOOP);
			image = new Sprite(normImage);
			baseKnockIntoDamage = 3f;
			hitstunDealtBonus = 12;
			airFrictionX = 0.992f;
			baseWeight = 80;
		}

	}

	public static class ShootBall extends Breakable {
		
		protected TextureRegion stillBadTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ballbad.png")));
		protected TextureRegion stillGoodTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ballgood.png")));
		protected Animation moveBadAnim = GlobalRepo.makeAnimation("sprites/entities/ballbadspin.png", 4, 1, 6, PlayMode.LOOP);
		protected Animation moveGoodAnim = GlobalRepo.makeAnimation("sprites/entities/ballgoodspin.png", 4, 1, 6, PlayMode.LOOP);

		protected float minSpeedForHit = 2.2f;
		public ShootBall(Fighter user, int team, float posX, float posY) {
			super(posX, posY);
			if (user.getDirection() == Direction.LEFT) posX += user.getImage().getWidth()/4;
			else posX += 3 * user.getImage().getWidth()/4;
			setup(team);
		}
		
		public ShootBall(int team, float posX, float posY) {
			super(posX, posY);
			setup(team);
		}
		
		private void setup(int team){
			this.team = team;
			setImages(team);
			image = new Sprite(normImage);
			baseKnockIntoDamage = 3f;
			hitstunDealtBonus = 1;
			airFrictionX = 0.992f;
			friction = 0.94f;
			baseWeight = 90;
			baseHurtleBK = minSpeedForHit;
			baseKBG = 3.5f;
			touchRadius = 16;
		}
		
		protected void setImages(int team){
			if (team == GlobalRepo.BADTEAM) {
				normImage = stillGoodTexture;
				tumbleImage = moveGoodAnim;
			}
			else if (team == GlobalRepo.GOODTEAM) {
				normImage = stillBadTexture;
				tumbleImage = moveBadAnim;
			}
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
				life.reset();
			}
			else if (user.getTeam() == GlobalRepo.BADTEAM) {
				team = GlobalRepo.GOODTEAM;
				life.reset();
			}
			setImages(team);
		}

		protected float bounceMod = -1.1f;
		@Override
		public void ground(){ 
			if (velocity.y < -1 && !inGroundedState()){
				velocity.y *= bounceMod;
			}
			super.ground();
		}

		@Override
		public boolean inHitstun(){
			if (life.getCounter() < 3) return false;
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
	
	public static class Boulder extends ShootBall{

		public Boulder(Fighter user, int team, float posX, float posY) {
			super(user, team, posX, posY);
			baseKnockIntoDamage = 7.0f;
			hitstunDealtBonus = 9;
			baseWeight = 160;
			baseKBG = 8.0f;
			baseBKB = 4.0f;
			gravity = -1.2f;
			airFrictionY = 0.9f;
			minSpeedForHit = 1.2f;
			friction = 0.98f;
			stillBadTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/boulderbad.png")));
			stillGoodTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/bouldergood.png")));
			moveBadAnim = GlobalRepo.makeAnimation("sprites/entities/boulderbadspin.png", 4, 1, 6, PlayMode.LOOP);
			moveGoodAnim = GlobalRepo.makeAnimation("sprites/entities/bouldergoodspin.png", 4, 1, 6, PlayMode.LOOP);
			setImages(team);
			bounceMod = -0.2f;
			baseHitSpeed = -0.1f;
		}
		
	}
	
	public static class Meteor extends ShootBall{

		public Meteor(int team, float posX, float posY) {
			super(team, posX, posY);
			new SFX.Explode().play();
			baseKnockIntoDamage = 7.0f;
			stillBadTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/boulderbad.png")));
			stillGoodTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/bouldergood.png")));
			moveBadAnim = GlobalRepo.makeAnimation("sprites/entities/boulderbadspin.png", 4, 1, 6, PlayMode.LOOP);
			moveGoodAnim = GlobalRepo.makeAnimation("sprites/entities/bouldergoodspin.png", 4, 1, 6, PlayMode.LOOP);
			setImages(team);
		}
	}
	
	public static class Laser extends ShootBall{

		public Laser(int team, float posX, float posY) {
			super(team, posX, posY);
			new SFX.LaserFire().play();
			hitstunDealtBonus = 10;
			moveBadAnim = GlobalRepo.makeAnimation("sprites/entities/laserbad.png", 1, 1, 6, PlayMode.LOOP);
			moveGoodAnim = GlobalRepo.makeAnimation("sprites/entities/lasergood.png", 1, 1, 6, PlayMode.LOOP);
			gravity = 0;
			airFrictionX = 1;
			airFrictionY = 1;
			setImages(team);
			trails = true;
		}
		
		@Override
		public boolean inHitstun(){
			return true;
		}
	}

	public static class Rocket extends Explosive {

		private final DurationTimer flightTime = new DurationTimer(48);
		private int dir;
		private final Fighter user;

		public Rocket(Fighter user, float posX, float posY) {
			super(posX, posY);
			this.user = user;
			dir = user.direct();
			normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/rocket.png")));
			tumbleImage = GlobalRepo.makeAnimation("sprites/entities/rocket.png", 2, 1, 8, PlayMode.LOOP);
			timerList.add(flightTime);
			airFrictionX = 0.9f;
			gravity = -0.5f;
			updateImage(0);
			team = GlobalRepo.BADTEAM;
		}

		@Override
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			if (!flightTime.timeUp()) {
				if (Math.signum(velocity.x) != 0 && Math.signum(velocity.x) != dir) dir *= -1;
				velocity.x += dir * 0.28;
			}
			else explode();
			super.update(rectangleList, entityList, deltaTime);
		}

		@Override
		public float getGravity() {
			if (!flightTime.timeUp()) return 0;
			else return gravity; 
		}
		
		@Override
		public void takeDamagingKnockback(Vector2 knockback, float DAM, int hitstun, HitstunType hitboxhitstunType, Hittable user) {
			if (DAM > 0) {
				explode();
			}
		}

		@Override
		public void knockInto(){
			explode();
		}

		@Override
		public boolean doesCollide(float x, float y){
			for (Rectangle r : tempRectangleList){
				Rectangle thisR = getCollisionBox(x, y);
				if (Intersector.overlaps(thisR, r) && thisR != r) {
					explode();
					return true;
				}
			}
			return false;
		}

		protected Explosion getExplosion(){
			return new Explosion.RocketExplosion(user, this, dir);
		}

	}

	public static class Grenade extends Explosive {

		private final Fighter user;
		private final DurationTimer duration = new DurationTimer(120);

		public Grenade(Fighter user, float posX, float posY) {
			super(posX, posY);
			this.user = user;
			user.direct();
			normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/grenade.png")));
			tumbleImage = GlobalRepo.makeAnimation("sprites/entities/grenade.png", 1, 1, 1, PlayMode.LOOP);
			timerList.add(duration);
			team = GlobalRepo.BADTEAM;
			gravity = -0.5f;
			friction = 0.96f;
			baseKBG = 0;
			image = new Sprite(normImage);
		}

		@Override
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			if (duration.timeUp()) explode();
			super.update(rectangleList, entityList, deltaTime);
		}

		@Override
		public void ground(){ 
			if (velocity.y < -1 && !inGroundedState()){
				velocity.y *= -1.1;
			}
			super.ground();
		}

		@Override
		public void knockInto(){
			cook();
		}
		
		@Override
		protected Explosion getExplosion(){
			return new Explosion.GrenadeExplosion(user, this);
		}
		
		private void cook(){
			final int cookTime = 1;
			velocity.x *= 0.25f;
			if (duration.getEndTime() - duration.getCounter() > cookTime) {
				duration.setEndTime(cookTime);
				duration.reset();
			}
		}

	}
}
