package entities;

import java.util.List;

import timers.DurationTimer;
import timers.Timer;
import inputs.InputHandlerCPU;
import inputs.Brain.MookBrain;
import main.GlobalRepo;
import main.MapHandler;
import movelists.M_Basic;

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
	private Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/basic/tumble.png", 4, 1, 8, PlayMode.LOOP_REVERSED);
	private Animation hitstunImage = GlobalRepo.makeAnimation("sprites/fighters/basic/hitstun.png", 2, 1, 8, PlayMode.LOOP);
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/basic/fall.png")));
	private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/basic/fallen.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/basic/dash.png")));

	public Basic(float posX, float posY, int team) {
		super(posX, posY, team);
		setInputHandler(new InputHandlerCPU(this, MookBrain.class));
		image = new Sprite(standImage.getKeyFrame(0));
		//gravity = -0.3f;
		gravity = -0.45f;
		baseWeight = 90;
		jumpAcc = 0.49f;
		airSpeed = 1.8f;
		walkSpeed = 1.7f;
		runSpeed = 2.5f;
		friction = 0.9f;
		wallJumpStrengthX = 2f;
		wallJumpStrengthY = 7.2f;
		doubleJumpStrength = 7.5f;
		moveList = new M_Basic(this);
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
	TextureRegion getGrabFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getFallFrame(float deltaTime) { return fallImage; }
	TextureRegion getAscendFrame(float deltaTime) { return jumpImage.getKeyFrame(deltaTime); }
	TextureRegion getCrouchFrame(float deltaTime) { return crouchImage.getKeyFrame(deltaTime); }
	TextureRegion getDashFrame(float deltaTime) { return dashImage; }
	TextureRegion getDodgeFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getJumpSquatFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getTumbleFrame(float deltaTime) { return helplessImage.getKeyFrame(deltaTime); }
	TextureRegion getHitstunFrame(float deltaTime) { return hitstunImage.getKeyFrame(deltaTime); }
	TextureRegion getFallenFrame(float deltaTime) { return fallenImage; }

	public static class Bad extends Basic {

		private final static ShaderProgram norm = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/palettes/basic/bad.glsl"));

		public Bad(float posX, float posY, int team) {
			super(posX, posY, team);
			setPalette(norm);
			gravity = -0.4f;
			airSpeed = 0.5f;
			walkSpeed = 1.2f;
			runSpeed = 1.8f;
			basePower = 0.5f;
			baseWeight = 50;
		}


	}

	public static class Beefy extends Basic {

		private final static ShaderProgram norm = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/palettes/basic/beefy.glsl"));

		public Beefy(float posX, float posY, int team) {
			super(posX, posY, team);
			setPalette(norm);
			basePower = 1.6f;
			baseWeight = 180;
			walkSpeed = 2.1f;
			runSpeed = 3f;
		}


	}

	public static class Bomb extends Basic {

		private final Timer kaboom = new DurationTimer(120);
		private final static ShaderProgram norm = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/palettes/basic/bomb.glsl"));
		private final static ShaderProgram flash = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/bombflash.glsl"));

		public Bomb(float posX, float posY, int team) {
			super(posX, posY, team);
			setPalette(norm);
			friction = 0.94f;
			airSpeed = 0.5f;
			walkSpeed = 1.2f;
			runSpeed = 1.8f;
			gravity = -0.6f;
			basePower = 1.2f;
			baseWeight = 110;
		}

		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			int flashPoint = 10;
			boolean shouldFlash = (deltaTime % flashPoint < 1) || (kaboom.getCounter() > kaboom.getEndTime()/2 && deltaTime % flashPoint/2 < 1);
			if (kaboom.getCounter() > 1 && shouldFlash) setPalette(flash);
			else setPalette(norm);
			if (kaboom.timeUp()) explode();
		}

		private void explode(){
			MapHandler.addEntity(new Projectile.BombExplosion(this, direct()));
			setRemove();
		}

		protected void takeKnockback(Vector2 knockback, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
			if (!timerList.contains(kaboom) && hitstun > (30 + Math.random() * 30)) timerList.add(kaboom);
			super.takeKnockback(knockback, hitstun, shouldChangeKnockback, ht);
		}

		public void setBomb(int time){
			kaboom.setEndTime(time);
			timerList.add(kaboom);
		}

	}

	public static class Bonkers extends Basic {

		private final static ShaderProgram norm = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/palettes/basic/bonkers.glsl"));

		public Bonkers(float posX, float posY, int team) {
			super(posX, posY, team);
			setPalette(norm);
			gravity = -0.35f;
			airSpeed = 2.2f;
			walkAcc = 1.8f;
			walkSpeed = 2.2f;
			runAcc = 2.5f;
			runSpeed = 3.5f;
			friction = 0.7f;
			airFriction = 0.85f;
			armor = 1;
			baseHitstun = 0.5f;
			basePower = 2.1f;
			baseWeight = 350;
			powerTimer.reset();
		}


	}
	
	public static class Baffle extends Bonkers {

		private final static ShaderProgram norm = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/palettes/basic/baffle.glsl"));

		public Baffle(float posX, float posY, int team) {
			super(posX, posY, team);
			setPalette(norm);
			armor = 0;
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			super.update(rectangleList, entityList, deltaTime);
			super.update(rectangleList, entityList, deltaTime);
		}


	}

}
