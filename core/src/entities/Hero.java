package entities;

import main.GlobalRepo;
import movelists.M_Hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

public class Hero extends Fighter {

	private Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/bomber/stand.png", 1, 1, 1, PlayMode.LOOP);
	private Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/bomber/walk.png", 2, 1, 16, PlayMode.LOOP);
	private Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/bomber/run.png", 1, 1, 1, PlayMode.LOOP);
	private Animation tumbleImage = GlobalRepo.makeAnimation("sprites/fighters/bomber/tumble.png", 4, 1, 8, PlayMode.LOOP);
	private TextureRegion fJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/fjump.png")));
	private TextureRegion nJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/njump.png")));
	private TextureRegion bJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/bjump.png")));
	private TextureRegion dJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/doublejump.png")));
	private TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/fall.png")));
	private TextureRegion ascendImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/ascend.png")));
	private TextureRegion crouchImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/crouch.png")));
	private TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/dash.png")));
	private TextureRegion dodgeImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/dodgebegin.png")));
	private TextureRegion jumpSquatImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/crouch.png")));
	private TextureRegion slideImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/slide.png")));
	private TextureRegion helplessImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/helpless.png")));
	private TextureRegion grabImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/grab.png")));
	private TextureRegion fallenImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/fallen.png")));

	public Hero(float posX, float posY, int team) {
		super(posX, posY, team);
		baseWeight = 100;
		runAcc = 2.0f;
		runSpeed = 5.8f;
		walkAcc = 0.8f;
		walkSpeed = 3.1f;
		airSpeed = 3.2f;
		airAcc = 0.24f;
		friction = 0.85f;
		gravity = -0.52f;
		jumpAcc = 0.92f;
		dashStrength = 0f;
		doubleJumpStrength = 9.7f;
		wallJumpStrengthX = 6.5f;
		wallJumpStrengthY = 8.4f;
		fallSpeed = -6.7f;
		jumpSquatTimer.setEndTime(3);
		footStoolDuration = 25;
		dashTimer.setEndTime(20);
		baseHitstun = GlobalRepo.HEROHITSTUNMOD;
		moveList = new M_Hero(this);
		defaultIcon = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/iconwasp.png")));
	}
	
	public Rectangle getNormalHurtBox(){
		return GlobalRepo.makeHurtBoxDynamic(this, 0.8f, 0.9f);
	}
	
	TextureRegion getJumpFrame(float deltaTime){
		if (!doubleJumpTimer.timeUp()) return dJumpImage;
		else if (Math.abs(velocity.x) > 1 && Math.signum(velocity.x) != direct()) return bJumpImage; 
		else if (Math.abs(velocity.x) > airSpeed) return fJumpImage;
		else return nJumpImage;
	}
	TextureRegion getStandFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getWalkFrame(float deltaTime) { return walkImage.getKeyFrame(deltaTime); }
	TextureRegion getRunFrame(float deltaTime) { return runImage.getKeyFrame(deltaTime); }
	TextureRegion getWallSlideFrame(float deltaTime) { return slideImage; }
	TextureRegion getHelplessFrame(float deltaTime) { return helplessImage; }
	TextureRegion getGrabFrame(float deltaTime) { return grabImage; }
	TextureRegion getFallFrame(float deltaTime) { return fallImage; }
	TextureRegion getAscendFrame(float deltaTime) { return ascendImage; }
	TextureRegion getCrouchFrame(float deltaTime) { return crouchImage; }
	TextureRegion getDashFrame(float deltaTime) { return dashImage; }
	TextureRegion getDodgeFrame(float deltaTime) { return dodgeImage; }
	TextureRegion getJumpSquatFrame(float deltaTime) { return jumpSquatImage; }
	TextureRegion getTumbleFrame(float deltaTime) { return tumbleImage.getKeyFrame(deltaTime); }
	TextureRegion getHitstunFrame(float deltaTime) { return helplessImage; }
	TextureRegion getFallenFrame(float deltaTime) { return fallenImage; }

}
