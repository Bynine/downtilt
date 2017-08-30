package entities;

import main.GlobalRepo;
import main.MapHandler;
import movelists.M_Fly;
import moves.IDMove;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

public class Fly extends Fighter {

	private static Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/fly/stand.png", 1, 1, 1, PlayMode.LOOP);
	private static Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/fly/walk.png", 2, 1, 16, PlayMode.LOOP);
	private static Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/fly/walk.png", 2, 1, 10, PlayMode.LOOP);
	private static Animation jumpImage = GlobalRepo.makeAnimation("sprites/fighters/fly/jump.png", 1, 1, 1, PlayMode.LOOP);
	private static Animation crouchImage = GlobalRepo.makeAnimation("sprites/fighters/fly/crouch.png", 1, 1, 1, PlayMode.LOOP);
	private static Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/fly/tumble.png", 4, 1, 8, PlayMode.LOOP_REVERSED);
	private static Animation hitstunImage = GlobalRepo.makeAnimation("sprites/fighters/fly/hitstun.png", 4, 1, 6, PlayMode.LOOP);
	private static TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/fly/fall.png")));
	private static TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/fly/fallen.png")));
	private static TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/fly/jump.png")));

	public Fly(float posX, float posY, int team) {
		super(posX, posY, team);
		baseWeight = 45;
		baseArmor = -1;
		runAcc = 0.2f;
		runSpeed = 1f;
		walkAcc = 0.2f;
		walkSpeed = 1f;
		airSpeed = 4.2f;
		airAcc = 0.24f;
		friction = 0.98f;
		gravity = -0.18f;
		jumpStrength = 3.2f;
		jumpAcc = 0.23f;
		dashStrength = 0f;
		doubleJumpStrength = 5.2f;
		airFrictionY = 0.96f;
		jumpSquatTimer.setEndTime(8);
		moveList = new M_Fly(this);
		baseHitstun = GlobalRepo.ENEMYHITSTUNMOD + 0.6f;
		doubleJumpMax = 3;
		doubleJumps = doubleJumpMax;
	}
	
	public float getGravity() { 
		if (!hitstunTimer.timeUp() || state == State.HELPLESS) return -0.35f;
		else return gravity; 
		}
	
	public boolean tryBlock(){
		if (isGrounded()) return false;
		else startAttack(moveList.selectBlock());
		return true;
	}
	
	public boolean tryCharge(){
		startAttack(new IDMove(moveList.nCharge(), 0));
		return true;
	}
	
	public boolean tryTaunt(){
		startAttack(moveList.selectTaunt()); 
		return true;
	}
	
	protected boolean isWallSliding(){
		return false;
	}
	
	protected boolean canReverseInAir(){
		return true;
	}
	
	protected boolean upThroughThinPlatform(Rectangle r){
		return false;
	}
	
	protected void hitGround(){
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
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
