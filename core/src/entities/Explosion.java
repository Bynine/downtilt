package entities;

import java.util.Arrays;
import java.util.List;

import timers.DurationTimer;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Hittable.HitstunType;
import main.MapHandler;
import main.SFX;
import moves.ActionCircleGroup;
import moves.ExplosionHitbox;
import moves.Hitbox;

public abstract class Explosion extends ImmobileEntity{

	Hitbox ac;
	Fighter owner;
	float velX, velY = 0;
	public final Timer life = new DurationTimer(1);
	boolean trans = false;
	private TextureRegion texture;

	public Explosion(float posX, float posY, Fighter owner) {
		super(posX, posY);
		this.owner = owner;
		timerList.add(life);
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		if (life.timeUp()) setRemove();
		setupRectangles(rectangleList, entityList);
		checkWalls();
		checkFloor();
		if (deltaTime > 1) handleTouch(entityList);

		updateTimers();
		updatePosition();
		updateImagePosition(deltaTime);
	}
	
	public void dispose(){
		super.dispose();
		texture.getTexture().dispose();
	}

	/** texture string, lifetime, velocity x, velocity y **/
	void setup(String texString, int lifeTime, float velX, float velY){
		texture = new TextureRegion(new Texture(Gdx.files.internal(texString)));
		setImage(texture);
		if (owner.direct() == -1) flip();
		life.setEndTime(lifeTime);
		life.reset();
		this.velX = owner.direct() * velX;
		this.velY = velY;
	}

	public Fighter getUser() {
		return owner;
	}

	protected static class RocketExplosion extends Explosion{

		private final int lifeTime = 30;

		public RocketExplosion(Fighter owner, Hurlable rocket, int ownerDirect) {
			super(0, 0, owner);
			setup("sprites/entities/explosion.png", lifeTime, 0, 0);
			new SFX.Explode().play();
			position.x = rocket.position.x;
			position.y = rocket.position.y;
			ac = 		 new ExplosionHitbox(owner,10.0f, 3.5f, 20, Hitbox.SAMURAI, 0, 0, 40, new SFX.HeavyHit(), this, lifeTime);
			Hitbox ac2 = new ExplosionHitbox(owner, 8.0f, 3.0f, 16, Hitbox.SAMURAI, 0, 0, 60, new SFX.MidHit(), this, lifeTime);
			Hitbox ac3 = new ExplosionHitbox(null,  4.0f,    0,  0, Hitbox.SAMURAI, 0, 0, 80, new SFX.LightHit(), this, lifeTime);
			ac.setRefresh(2);
			ac2.setRefresh(4);
			ac.setHitstunType(HitstunType.SUPER);
			new ActionCircleGroup(Arrays.asList(ac, ac2, ac3));
			MapHandler.addActionCircle(ac);
			MapHandler.addActionCircle(ac2);
			MapHandler.addActionCircle(ac3);
		}

	}

	protected static class GrenadeExplosion extends Explosion{

		private final int lifeTime = 12;
		private final int displacement = 20;

		public GrenadeExplosion(Fighter owner, Hurlable grenade) {
			super(0, 0, owner);
			new SFX.Explode().play();
			position.x = grenade.position.x - displacement;
			position.y = grenade.position.y - displacement;
			setup("sprites/entities/grenadeexplosion.png", lifeTime, 0, 0);
			ac =  
					new ExplosionHitbox(owner, 6.5f, 5.0f, 14, 81, 0, 0, 36, new SFX.HeavyHit(), this, lifeTime);
			Hitbox ac2 = 
					new ExplosionHitbox(owner, 5.0f, 4.0f,  8, 76, 0, 0, 52, new SFX.MidHit(), this, lifeTime);
			ac.setRefresh(2);
			ac2.setRefresh(2);
			new ActionCircleGroup(Arrays.asList(ac, ac2));
			MapHandler.addActionCircle(ac);
			MapHandler.addActionCircle(ac2);
		}
	}

	protected static class BombExplosion extends Explosion{

		private final int lifeTime = 30;
		private final int displacement = 40;

		public BombExplosion(Fighter owner, int ownerDirect) {
			super(0, 0, owner);
			setup("sprites/entities/explosion.png", lifeTime, 0, 0);
			new SFX.Explode().play();
			int facingOffset = 40 * ownerDirect;
			position.x = owner.position.x - displacement;
			position.y = owner.position.y - displacement;
			ac = 		 new ExplosionHitbox(null,  7.0f, 2.5f, 20, Hitbox.SAMURAI, 0, 0, 30, new SFX.HeavyHit(), this, lifeTime);
			Hitbox ac2 = new ExplosionHitbox(null,  6.0f, 2.0f, 12, Hitbox.SAMURAI, facingOffset, 0, 50, new SFX.MidHit(), this, lifeTime);
			ac.setRefresh(2);
			ac2.setRefresh(4);
			ac.setHitstunType(HitstunType.SUPER);
			new ActionCircleGroup(Arrays.asList(ac, ac2));
			MapHandler.addActionCircle(ac);
			MapHandler.addActionCircle(ac2);
		}
	}

}
