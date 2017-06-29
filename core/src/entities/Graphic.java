package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import timers.DurationTimer;

public abstract class Graphic extends Entity{

	final DurationTimer duration;
	int dur;

	Graphic(float posX, float posY, int dur){
		super(posX, posY);
		this.dur = dur;
		duration = new DurationTimer(dur);
		duration.restart();
		timerList.add(duration);
		collision = Collision.GHOST;
	}

	public void setDuration(int endTime){
		duration.setEndTime(endTime);
	}

	void setSmall(TextureRegion smaller){
		position.x += image.getWidth()/2;
		position.y += image.getHeight()/2;
		setImage(smaller);
		position.x -= image.getWidth()/2;
		position.y -= image.getHeight()/2;
	}

	public static class HitGraphic extends Graphic{
		private TextureRegion fullSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/hit.png")));
		private TextureRegion halfSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/hitsmall.png")));

		public HitGraphic(float posX, float posY, int dur){
			super(posX, posY, dur);
			image = new Sprite(fullSize);
			position.x -= image.getWidth()/2;
			position.y -= image.getHeight()/2;
		}

		void updatePosition(){
			if (duration.getCounter() > dur/2) setSmall(halfSize);
			if (duration.timeUp()) setRemove();
		}

	}

	public static class SmokeTrail extends Graphic{
		private TextureRegion fullSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/poff.png")));
		private TextureRegion halfSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/poffsmall.png")));

		public SmokeTrail(float posX, float posY){
			super(posX, posY, 6);
			image = new Sprite(fullSize);
			position.x -= image.getWidth()/2;
			position.y -= image.getHeight()/2;
		}

		/** lag-behind smoke trail **/
		public SmokeTrail(Entity e, int dur){
			this(e.position.x - e.velocity.x + e.image.getWidth()/2, e.position.y - e.velocity.y + e.image.getHeight()/2);
			if (dur > 12) dur = 12;
			duration.setEndTime(dur);
		}

		void updatePosition(){
			if (duration.getCounter() > dur/2) setSmall(halfSize);
			if (duration.timeUp()) setRemove();
		}

	}

	public static class DustCloud extends Graphic{
		private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/dustcloud.png")));
		public DustCloud(Entity e, float posX, float posY){
			super(posX, posY, 4);
			image = new Sprite(texture);
			position.x -= image.getWidth();
			if (e.direction == Direction.LEFT){
				image.flip(true, false);
				position.x += e.getImage().getWidth() * 1.5f;
			}
		}
		void updatePosition(){
			if (duration.timeUp()) setRemove();
		}
	}

	public static class UFO extends Graphic {
		private TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/ufo.png")));
		private final Fighter user;
		public UFO(Fighter user, float posX, float posY){
			super(posX, posY, 4);
			this.user = user;
			image = new Sprite(texture);
			updatePosition();
		}
		void updatePosition(){
			position.x = user.getPosition().x;
			position.y = user.getPosition().y - 12;
			if (duration.timeUp() || user.isGrounded()) setRemove();
		}
	}

	public static class LaserCharge extends Graphic {
		private TextureRegion small = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/laserchargesmall.png")));
		private TextureRegion mid = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/laserchargemid.png")));
		private TextureRegion big = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/laserchargebig.png")));
		private final Fighter user;
		public LaserCharge(Fighter user, float posX, float posY){
			super(posX, posY, 40);
			this.user = user;
			image = new Sprite(big);
			updatePosition();
		}
		void updatePosition(){
			position.x = user.getPosition().x - image.getWidth()/2;
			if (user.getDirection() == Direction.RIGHT) position.x += user.getImage().getWidth();
			position.y = user.getPosition().y + 24 - image.getHeight()/2;
			if (duration.getCounter() > dur/3) setSmall(mid);
			if (duration.getCounter() > 2*dur/3) setSmall(small);
			if (duration.timeUp()) setRemove();
		}
	}
	
	public static class DoubleJumpRing extends Graphic {
		private TextureRegion tex = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/doublejump.png")));
		private TextureRegion small = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/doublejumpsmall.png")));
		public DoubleJumpRing(float posX, float posY){
			super(posX, posY, 12);
			image = new Sprite(tex);
			updatePosition();
		}
		void updatePosition(){
			if (duration.timeUp()) setRemove();
			if (duration.getCounter() > dur/2) setSmall(small);
		}
	}
	
	public static class Die extends Graphic {
		private static TextureRegion tex = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/die.png")));
		private TextureRegion small = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/diesmall.png")));
		public Die(float posX, float posY){
			super(posX - tex.getRegionWidth()/2, posY - tex.getRegionHeight()/2, 16);
			image = new Sprite(tex);
			updatePosition();
		}
		void updatePosition(){
			if (duration.timeUp()) setRemove();
			if (duration.getCounter() > dur/2) setSmall(small);
		}
	}

}
