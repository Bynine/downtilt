package entities;

import main.GlobalRepo;
import movelists.M_Shoot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Shoot extends Fighter {
	
	private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/shoot/stand.png", 1, 1, 1, PlayMode.LOOP);
	private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/shoot/walk.png", 2, 1, 16, PlayMode.LOOP);
	private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/shoot/run.png", 2, 1, 8, PlayMode.LOOP);
	private Animation jumpImage = GlobalRepo.makeAnimation("sprites/fighters/shoot/jump.png", 1, 1, 1, PlayMode.LOOP);
	private Animation crouchImage = GlobalRepo.makeAnimation("sprites/fighters/shoot/crouch.png", 1, 1, 1, PlayMode.LOOP);
	private Animation helplessImage = GlobalRepo.makeAnimation("sprites/fighters/shoot/tumble.png", 4, 1, 8, PlayMode.LOOP_REVERSED);
	private Animation hitstunImage = GlobalRepo.makeAnimation("sprites/fighters/shoot/hitstun.png", 2, 1, 8, PlayMode.LOOP);
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/shoot/fall.png")));
	private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/shoot/fallen.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/shoot/dash.png")));

	public Shoot(float posX, float posY, int team) {
		super(posX, posY, team);
		image = new Sprite(standImage.getKeyFrame(0));
		gravity = -0.41f;
		baseWeight = 90;
		jumpAcc = 0.49f;
		airSpeed = 1.8f;
		walkAcc = 0.5f;
		walkSpeed = 1.7f;
		runAcc = 1.2f;
		runSpeed = 2.5f;
		friction = 0.96f;
		doubleJumpStrength = 7.5f;
		moveList = new M_Shoot(this);
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

}
