package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import inputs.InputHandlerCPU;
import inputs.Brain.MookBrain;
import main.DowntiltEngine;
import main.GlobalRepo;
import main.SFX;
import movelists.M_Heavy;

public class Heavy extends Fighter {

	private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/stand.png", 1, 1, 1, PlayMode.LOOP);
	private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/walk.png", 2, 1, 16, PlayMode.LOOP);
	private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/run.png", 2, 1, 8, PlayMode.LOOP);
	private Animation jumpImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/jump.png", 1, 1, 1, PlayMode.LOOP);
	private Animation crouchImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/crouch.png", 1, 1, 1, PlayMode.LOOP);
	private Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/tumble.png", 4, 1, 8, PlayMode.LOOP_REVERSED);
	private Animation hitstunImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/hitstun.png", 1, 1, 8, PlayMode.LOOP);
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/heavy/fall.png")));
	private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/heavy/fallen.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/heavy/dash.png")));
	
	public static final float HEAVY_ARMOR = 0.5f;

	public Heavy(float posX, float posY, int team) {
		super(posX, posY, team);
		setInputHandler(new InputHandlerCPU(this, MookBrain.class));
		image = new Sprite(standImage.getKeyFrame(0));
		gravity = -0.57f;
		baseWeight = 200;
		baseArmor = HEAVY_ARMOR;
		jumpAcc = 0.64f;
		airAcc = 1.2f;
		airSpeed = 1.8f;
		walkAcc = 1.2f;
		walkSpeed = 1.4f;
		runAcc = 1.2f;
		runSpeed = 1.4f;
		airFrictionX = 0.8f;
		friction = 0.5f;
		doubleJumpMax = 0;
		doubleJumps = doubleJumpMax;
		moveList = new M_Heavy(this);
		jumpSquatTimer.setEndTime(20);
		baseHitstun = GlobalRepo.ENEMYHITSTUNMOD;
	}

	protected boolean isWallSliding(){
		return false;
	}

	protected void hitGround(){
		super.hitGround();
		new SFX.Break().play();
		DowntiltEngine.causeHitlag(2);
	}
	
	protected void getFootStooled(){
		if (getArmor() <= HEAVY_ARMOR) super.getFootStooled();
	}

//	public float getArmor() { 
//		if (inHitstun()) return 0;
//		else return super.getArmor(); 
//	}

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

}
