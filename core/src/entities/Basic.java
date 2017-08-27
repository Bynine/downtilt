package entities;

import java.util.List;

import timers.DurationTimer;
import timers.Timer;
import inputs.InputHandlerCPU;
import inputs.Brain.MookBrain;
import inputs.Brain.PostBossBrain;
import main.GlobalRepo;
import main.MapHandler;
import movelists.M_Basic;
import movelists.M_PostBoss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Basic extends Fighter {

	private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/basic/stand.png", 1, 1, 1, PlayMode.LOOP);
	private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/basic/walk.png", 2, 1, 16, PlayMode.LOOP);
	private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/basic/run.png", 2, 1, 8, PlayMode.LOOP);
	private Animation jumpImage = GlobalRepo.makeAnimation("sprites/fighters/basic/jump.png", 1, 1, 1, PlayMode.LOOP);
	private Animation crouchImage = GlobalRepo.makeAnimation("sprites/fighters/basic/crouch.png", 1, 1, 1, PlayMode.LOOP);
	private Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/basic/tumble.png", 4, 1, 6, PlayMode.LOOP_REVERSED);
	private Animation hitstunImage = GlobalRepo.makeAnimation("sprites/fighters/basic/hitstun.png", 2, 1, 8, PlayMode.LOOP);
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/basic/fall.png")));
	private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/basic/fallen.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/basic/dash.png")));

	public Basic(float posX, float posY, int team) {
		super(posX, posY, team);
		setInputHandler(new InputHandlerCPU(this, MookBrain.class));
		image = new Sprite(standImage.getKeyFrame(0));
		gravity = -0.46f;
		baseWeight = 90;
		jumpAcc = 0.49f;
		airAcc = 0.5f;
		airSpeed = 2.1f;
		walkSpeed = 1.7f;
		runSpeed = 2.5f;
		friction = 0.9f;
		wallJumpStrengthX = 2f;
		wallJumpStrengthY = 7.2f;
		doubleJumpStrength = 7.5f;
		moveList = new M_Basic(this, "basic");
		jumpSquatTimer.setEndTime(5);
		baseHitstun = GlobalRepo.ENEMYHITSTUNMOD;
	}

	protected boolean isWallSliding(){
		return false;
	}

	TextureRegion getJumpFrame(float deltaTime) { return jumpImage.getKeyFrame(deltaTime); }
	TextureRegion getStandFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getWalkFrame(float deltaTime) { return walkImage.getKeyFrame(deltaTime); }
	TextureRegion getRunFrame(float deltaTime) { return runImage.getKeyFrame(deltaTime); }
	TextureRegion getWallSlideFrame(float deltaTime) { return fallImage; }
	TextureRegion getHelplessFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
	TextureRegion getHoldFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getFallFrame(float deltaTime) { return fallImage; }
	TextureRegion getAscendFrame(float deltaTime) { return jumpImage.getKeyFrame(deltaTime); }
	TextureRegion getCrouchFrame(float deltaTime) { return crouchImage.getKeyFrame(deltaTime); }
	TextureRegion getDashFrame(float deltaTime) { return dashImage; }
	TextureRegion getDodgeFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getJumpSquatFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getTumbleFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
	TextureRegion getHitstunFrame(float deltaTime) { return hitstunImage.getKeyFrame(deltaTime); }
	TextureRegion getFallenFrame(float deltaTime) { return fallenImage; }

	public static class Bomb extends Basic {
		
		private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/bomb/stand.png", 1, 1, 1, PlayMode.LOOP);
		private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/bomb/walk.png", 2, 1, 16, PlayMode.LOOP);
		private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/bomb/run.png", 2, 1, 8, PlayMode.LOOP);
		private Animation jumpImage = GlobalRepo.makeAnimation("sprites/fighters/bomb/jump.png", 1, 1, 1, PlayMode.LOOP);
		private Animation crouchImage = GlobalRepo.makeAnimation("sprites/fighters/bomb/crouch.png", 1, 1, 1, PlayMode.LOOP);
		private Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/bomb/tumble.png", 4, 1, 6, PlayMode.LOOP_REVERSED);
		private Animation hitstunImage = GlobalRepo.makeAnimation("sprites/fighters/bomb/hitstun.png", 2, 1, 8, PlayMode.LOOP);
		private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomb/fall.png")));
		private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomb/fallen.png")));
		private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomb/dash.png")));

		private final Timer kaboom = new DurationTimer(120);
		private boolean exploded = false;
		private final int minKnockbackToCook = 45;
		private final static ShaderProgram flash = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/palettes/basic/bombflash.glsl"));

		public Bomb(float posX, float posY, int team) {
			super(posX, posY, team);
			friction = 0.94f;
			airSpeed = 0.5f;
			walkSpeed = 1.2f;
			runSpeed = 1.8f;
			gravity = -0.6f;
			jumpAcc = 0.58f;
			basePower = 1.2f;
			baseWeight = 110;
			moveList = new M_Basic(this, "bomb");
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			int flashPoint = 10;
			boolean shouldFlash = (deltaTime % flashPoint < 1) || (kaboom.getCounter() > kaboom.getEndTime()/2 && deltaTime % flashPoint/2 < 1);
			if (kaboom.getCounter() > 1 && shouldFlash) setPalette(flash);
			else setPalette(null);
			if (kaboom.timeUp()) explode();
		}

		private void explode(){
			if (exploded) return;
			exploded = true;
			MapHandler.addEntity(new Explosion.BombExplosion(this, direct()));
			resolveCombo();
			setRemove();
		}

		public void takeKnockback(Vector2 knockback, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
			if (!timerList.contains(kaboom) && hitstun > minKnockbackToCook) setOff();
			super.takeKnockback(knockback, hitstun, shouldChangeKnockback, ht);
		}

		public void setBomb(int time){
			kaboom.setEndTime(time);
			timerList.add(kaboom);
		}
		
		public boolean isExploded(){
			return exploded;
		}
		
		private void setOff(){
			timerList.add(kaboom);
		}
		
		public void ignite() {
			setOff();
		}
		
		TextureRegion getJumpFrame(float deltaTime) { return jumpImage.getKeyFrame(deltaTime); }
		TextureRegion getStandFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getWalkFrame(float deltaTime) { return walkImage.getKeyFrame(deltaTime); }
		TextureRegion getRunFrame(float deltaTime) { return runImage.getKeyFrame(deltaTime); }
		TextureRegion getWallSlideFrame(float deltaTime) { return fallImage; }
		TextureRegion getHelplessFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
		TextureRegion getHoldFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getFallFrame(float deltaTime) { return fallImage; }
		TextureRegion getAscendFrame(float deltaTime) { return jumpImage.getKeyFrame(deltaTime); }
		TextureRegion getCrouchFrame(float deltaTime) { return crouchImage.getKeyFrame(deltaTime); }
		TextureRegion getDashFrame(float deltaTime) { return dashImage; }
		TextureRegion getDodgeFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getJumpSquatFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getTumbleFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
		TextureRegion getHitstunFrame(float deltaTime) { return hitstunImage.getKeyFrame(deltaTime); }
		TextureRegion getFallenFrame(float deltaTime) { return fallenImage; }

	}
	
	public static class PostBoss extends Basic {
		private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/postboss/stand.png", 1, 1, 1, PlayMode.LOOP);
		private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/postboss/walk.png", 2, 1, 16, PlayMode.LOOP);
		private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/basic/walk.png", 2, 1, 8, PlayMode.LOOP);
		private Animation crouchImage = GlobalRepo.makeAnimation("sprites/fighters/postboss/crouch.png", 1, 1, 1, PlayMode.LOOP);
		private Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/postboss/tumble.png", 4, 1, 6, PlayMode.LOOP_REVERSED);
		private Animation hitstunImage = GlobalRepo.makeAnimation("sprites/fighters/postboss/hitstun.png", 2, 1, 8, PlayMode.LOOP);
		private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/postboss/fall.png")));
		private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/postboss/fallen.png")));
		private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/postboss/dash.png")));

		public PostBoss(float posX, float posY, int team) {
			super(posX, posY, team);
			setInputHandler(new InputHandlerCPU(this, PostBossBrain.class));
			tumbling = true;
			image = new Sprite(standImage.getKeyFrame(0));
			hitstopTimer.reset(25);
			moveList = new M_PostBoss(this);
			walkAcc = 1.2f;
			runAcc = walkAcc;
			walkSpeed = 2.0f;
			runSpeed = walkSpeed;
			friction = 0.94f;
			baseHitstun = GlobalRepo.ENEMYHITSTUNMOD * 1.5f;
		}

		TextureRegion getJumpFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getStandFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getWalkFrame(float deltaTime) { return walkImage.getKeyFrame(deltaTime); }
		TextureRegion getRunFrame(float deltaTime) { return runImage.getKeyFrame(deltaTime); }
		TextureRegion getWallSlideFrame(float deltaTime) { return fallImage; }
		TextureRegion getHelplessFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
		TextureRegion getHoldFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getFallFrame(float deltaTime) { return fallImage; }
		TextureRegion getAscendFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getCrouchFrame(float deltaTime) { return crouchImage.getKeyFrame(deltaTime); }
		TextureRegion getDashFrame(float deltaTime) { return dashImage; }
		TextureRegion getDodgeFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getJumpSquatFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
		TextureRegion getTumbleFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
		TextureRegion getHitstunFrame(float deltaTime) { return hitstunImage.getKeyFrame(deltaTime); }
		TextureRegion getFallenFrame(float deltaTime) { return fallenImage; }
	}

}
