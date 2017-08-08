package entities;

import main.GlobalRepo;
import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;

import timers.DurationTimer;

public abstract class Graphic extends Entity{


	final DurationTimer duration;
	int dur;

	Graphic(float posX, float posY, int dur){
		super(posX, posY);
		this.dur = dur;
		duration = new DurationTimer(dur);
		duration.reset();
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
	
	private static abstract class HitGraphic extends Graphic{
		private final int frame = 6;
		private final int frames = 3;
		private final int startFrame;
		private Animation anim = GlobalRepo.makeAnimation("sprites/graphics/hitanimation.png", frames, 1, frame, PlayMode.NORMAL);

		public HitGraphic(float posX, float posY, int dur){
			super(posX, posY, dur);
			startFrame = MathUtils.clamp((frame * frames) - duration.getEndTime() - 4, 0, frame * frames);
		}
		
		protected void setAnimation(Animation anim){
			this.anim = anim;
		}
		
		protected void setup(){
			setImage(anim.getKeyFrame(startFrame));
			position.x -= image.getWidth()/2;
			position.y -= image.getHeight()/2;
		}

		void updatePosition(){
			//System.out.println("Graphic is on frame " + (startFrame + duration.getCounter()) );
			setImage(anim.getKeyFrame(startFrame + duration.getCounter()));
			if (duration.timeUp()) setRemove();
		}
		
	}

	public static class HitGoodGraphic extends HitGraphic{

		public HitGoodGraphic(float posX, float posY, int dur){
			super(posX, posY, dur);
			setup();
		}

	}
	
	public static class HitBadGraphic extends HitGraphic{

		public HitBadGraphic(float posX, float posY, int dur){
			super(posX, posY, dur);
			setup();
		}

	}
	
	public static class HitGuardGraphic extends HitGraphic{
//		private TextureRegion fullSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/hitguard.png")));
//		private TextureRegion halfSize = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/hitguardsmall.png")));

		public HitGuardGraphic(float posX, float posY, int dur){
			super(posX, posY, dur);
			setAnimation(GlobalRepo.makeAnimation("sprites/graphics/hitguardanimation.png", 3, 1, 8, PlayMode.LOOP));
			setup();
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
	

	public static class Sparks extends Graphic {
		private static TextureRegion tex = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/sparks.png")));
		public Sparks(float posX, float posY, int dur) {
			super(posX, posY, dur);
			new SFX.Sparks().play();
			image = new Sprite(tex);
			updatePosition();
		}
		
		void updatePosition(){
			if (duration.timeUp()) setRemove();
		}

	}
	
	public static class Quake extends Graphic {
		private static TextureRegion tex = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/sparks.png")));
		public Quake(float posX, float posY, int dur) {
			super(posX, posY, dur);
			image = new Sprite(tex);
			updatePosition();
		}
		
		void updatePosition(){
			if (duration.timeUp()) setRemove();
		}

	}
	
	public static class Fire extends Graphic {
		private static final TextureRegion tex = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/fire.png")));
		private final Hittable user;
		private final static int dispX = -24;
		public Fire(Hittable user, int dur) {
			super(user.getPosition().x + dispX, user.getPosition().y, dur);
			this.user = user;
			image = new Sprite(tex);
			updatePosition();
			layer = Layer.MIDDLEBACK;
		}
		
		void updatePosition(){
			position.set(user.getPosition());
			position.x += dispX;
			if (duration.timeUp()) setRemove();
		}

	}
	
	public static class Steam extends Graphic {
		private static final TextureRegion tex = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/steam.png")));
		private final Hittable user;
		private final static int dispY = -32;
		public Steam(Hittable user, int dur) {
			super(user.getPosition().x, user.getPosition().y + dispY, dur);
			this.user = user;
			image = new Sprite(tex);
			updatePosition();
			layer = Layer.MIDDLEBACK;
		}
		
		void updatePosition(){
			position.set(user.getPosition());
			position.y += dispY;
			if (duration.timeUp()) setRemove();
		}

	}
	
	public static class Gust extends Graphic {
		private static final TextureRegion tex = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/gust.png")));
		private final Fighter user;
		private int dispX = 36;
		private final int dispY = -12;
		public Gust(Fighter user, int dur) {
			super(user.getPosition().x, user.getPosition().y, dur);
			this.user = user;
			image = new Sprite(tex);
			if (user.getDirection() == Direction.LEFT){
				dispX = (int) (-6 - user.getImage().getWidth());
			}
			else dispX += 6;
			updatePosition();
		}
		
		void updatePosition(){
			position.set(user.getPosition());
			position.x += dispX;
			position.y += dispY;
			image.setFlip(user.getImage().isFlipX(), false);
			if (duration.timeUp() || user.inHitstun()) setRemove();
		}

	}
	
	public static class BoostMessage extends Graphic {
		final Fighter user;
		float dispY = 0;
		public BoostMessage(Fighter user, TextureRegion tex){
			super(0, 0, 30);
			this.user = user;
			image = new Sprite(tex);
			updatePosition();
		}
		void updatePosition(){
			dispY += 1;
			position.set(user.getCenter().x - image.getWidth()/2, user.getPosition().y + user.getImage().getHeight() + dispY);
			if (duration.timeUp()) setRemove();
		}
	}

}
