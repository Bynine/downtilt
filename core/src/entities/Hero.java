package entities;

import main.DowntiltEngine;
import main.GlobalRepo;
import movelists.M_Hero;
import movelists.MoveList;
import movelists.MoveList_Advanced;
import moves.IDMove;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Hero extends Fighter {

	private static Animation standImage = GlobalRepo.makeAnimation("sprites/fighters/bomber/idle.png", 2, 1, 20, PlayMode.LOOP);
	private static Animation walkImage = GlobalRepo.makeAnimation("sprites/fighters/bomber/walk.png", 4, 1, 12, PlayMode.LOOP);
	private static Animation runImage = GlobalRepo.makeAnimation("sprites/fighters/bomber/run.png", 6, 1, 6, PlayMode.LOOP);
	private static Animation tumbleImage = GlobalRepo.makeAnimation("sprites/fighters/bomber/tumble.png", 4, 1, 7, PlayMode.LOOP);
	private static Animation fallenImage = GlobalRepo.makeAnimation("sprites/fighters/bomber/fallen.png", 2, 1, 24, PlayMode.LOOP);
	private static TextureRegion fJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/fjump.png")));
	private static TextureRegion nJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/njump.png")));
	private static TextureRegion bJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/bjump.png")));
	private static TextureRegion dJumpImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/doublejump.png")));
	private static TextureRegion fallImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/fall.png")));
	private static TextureRegion ascendImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/fall.png")));
	private static TextureRegion crouchImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/crouch.png")));
	private static TextureRegion dashImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/dash.png")));
	private static TextureRegion dodgeImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/dodgebegin.png")));
	private static TextureRegion jumpSquatImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/land.png")));
	private static TextureRegion slideImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/slide.png")));
	private static TextureRegion helplessImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/helpless.png")));
	private static TextureRegion holdImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/hold.png")));
	private static TextureRegion airHoldImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/bomber/airhold.png")));

	public Hero(float posX, float posY, int team) {
		super(posX, posY, team);
		baseWeight = 95;
		runAcc = 1.8f;
		runSpeed = 5.2f;
		walkAcc = 0.8f;
		walkSpeed = 3.1f;
		airSpeed = 3.2f;
		airAcc = 0.24f;
		friction = 0.83f;
		gravity = -0.52f;
		jumpAcc = 0.92f;
		dashStrength = 0.5f;
		doubleJumpStrength = 9.7f;
		wallJumpStrengthX = 7.5f;
		wallJumpStrengthY = 8.8f;
		jumpSquatTimer.setEndTime(3);
		footStoolDuration = 25;
		dashTimer.setEndTime(20);
		baseHitstun = GlobalRepo.HEROHITSTUNMOD;
		moveList = new M_Hero(this);
		defaultIcon = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/iconwasp.png")));
		updateImage(0);
	}

	@Override
	public boolean isPerfectGuarding() { 
		int perfectGuard = 7;
		return isGuarding() && guardTimer.getCounter() < perfectGuard; 
	}
	
	@Override
	public void parry(){
		endAttack();
		invincibleTimer.setEndTime(1);
		invincibleTimer.reset();
		startAttack(new IDMove(((MoveList_Advanced)moveList).parry(), MoveList.noStaleMove));
	}

	@Override
	public void perfectParry(){
		endAttack();
		invincibleTimer.reset();
		startAttack(new IDMove(((MoveList_Advanced)moveList).perfectParry(), MoveList.noStaleMove));
	}
	
	public Color getColor(){
		if (DowntiltEngine.getPlayers().size() == 2){
			if (this.equals(DowntiltEngine.getPlayers().get(1))) return new Color(0.4f, 1, 1, 1);
		}
		if (team == GlobalRepo.BADTEAM) return new Color(0, 0, 0, 1);
		return new Color(1, 1, 1, 1);
	}

	TextureRegion getJumpFrame(float deltaTime){
		if (!doubleJumpGraphicTimer.timeUp()) return dJumpImage;
		else if (Math.abs(velocity.x) > 1 && Math.signum(velocity.x) != direct()) return bJumpImage; 
		else if (Math.abs(velocity.x) > airSpeed) return fJumpImage;
		else return nJumpImage;
	}
	TextureRegion getStandFrame(float deltaTime) { return standImage.getKeyFrame(deltaTime); }
	TextureRegion getWalkFrame(float deltaTime) { return walkImage.getKeyFrame(deltaTime); }
	TextureRegion getRunFrame(float deltaTime) { return runImage.getKeyFrame(deltaTime); }
	TextureRegion getWallSlideFrame(float deltaTime) { return slideImage; }
	TextureRegion getHelplessFrame(float deltaTime) { return helplessImage; }
	TextureRegion getHoldFrame(float deltaTime) { 
		if (isGrounded()) return holdImage; 
		else return airHoldImage;
		}
	TextureRegion getFallFrame(float deltaTime) { return fallImage; }
	TextureRegion getAscendFrame(float deltaTime) { return ascendImage; }
	TextureRegion getCrouchFrame(float deltaTime) { return crouchImage; }
	TextureRegion getDashFrame(float deltaTime) { return dashImage; }
	TextureRegion getDodgeFrame(float deltaTime) { return dodgeImage; }
	TextureRegion getJumpSquatFrame(float deltaTime) { return jumpSquatImage; }
	TextureRegion getTumbleFrame(float deltaTime) { return tumbleImage.getKeyFrame(deltaTime); }
	TextureRegion getHitstunFrame(float deltaTime) { return helplessImage; }
	TextureRegion getFallenFrame(float deltaTime) { return fallenImage.getKeyFrame(deltaTime); }

}
