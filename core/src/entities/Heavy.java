package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.MathUtils;

import inputs.InputHandlerCPU;
import inputs.Brain.MookBrain;
import main.DowntiltEngine;
import main.GlobalRepo;
import main.SFX;
import movelists.M_Heavy;

public class Heavy extends Fighter {

	private static Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/stand.png", 1, 1, 1, PlayMode.LOOP);
	private static Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/walk.png", 2, 1, 16, PlayMode.LOOP);
	private static Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/run.png", 2, 1, 8, PlayMode.LOOP);
	private static Animation jumpImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/jump.png", 1, 1, 1, PlayMode.LOOP);
	private static Animation crouchImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/crouch.png", 1, 1, 1, PlayMode.LOOP);
	private static Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/tumble.png", 4, 1, 8, PlayMode.LOOP_REVERSED);
	private static Animation hitstunImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/hitstun.png", 1, 1, 8, PlayMode.LOOP);
	private static TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/heavy/fall.png")));
	private static Animation fallenImage = GlobalRepo.makeAnimation("sprites/fighters/heavy/fallen.png", 2, 1, 12, PlayMode.NORMAL);
	private static TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/heavy/dash.png")));
	
	public static final float HEAVY_ARMOR = 0.5f;

	public Heavy(float posX, float posY, int team) {
		super(posX, posY, team);
		setInputHandler(new InputHandlerCPU(this, MookBrain.class));
		image = new Sprite(standImage.getKeyFrame(0));
		gravity = -0.55f;
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
		int hitlag = (int) MathUtils.clamp(-velocity.y, 0, 3);
		if (hitlag > 0) new SFX.Break().play();
		DowntiltEngine.causeHitlag(hitlag);
	}
	
	protected void getFootStooled(){
		if (getArmor() <= HEAVY_ARMOR) super.getFootStooled();
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
	TextureRegion getFallenFrame(float deltaTime) { return fallenImage.getKeyFrame(fallenTimer.getCounter()); }

}
