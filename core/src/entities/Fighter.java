package entities;

import inputs.InputHandler;
import inputs.InputHandlerKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.DowntiltEngine;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import movelists.*;
import moves.IDMove;
import moves.Move;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class Fighter extends Hittable{

	protected final float unregisteredInputMax = 0.2f;
	protected boolean blockHeld;
	protected int doubleJumpMax = 1;
	int doubleJumps = doubleJumpMax;
	public int queuedCommand = InputHandler.commandNone;
	protected final Timer inputQueueTimer = new Timer(8), wallJumpTimer = new Timer(10), attackTimer = new Timer(0), grabbingTimer = new Timer(0), 
			dashTimer = new Timer(20), invincibleTimer = new Timer(0), guardHoldTimer = new Timer(1), footStoolTimer = new Timer(20), slowedTimer = new Timer(0),
			doubleJumpGraphicTimer = new Timer (12), doubleJumpUseTimer = new Timer (20), respawnTimer = new Timer(3);
	protected float prevStickX = 0, stickX = 0, stickY = 0;

	private ShaderProgram palette = null;
	private final Vector2 spawnPoint;
	private int lives = 1;

	public static final int SPECIALMETERMAX = 8;
	private float specialMeter = SPECIALMETERMAX;

	protected Vector2 footStoolKB = new Vector2(0, 0);
	protected int footStoolDuration = 30;
	protected int footStoolDamage = 0;

	protected TextureRegion defaultIcon = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/iconalien.png")));

	private Hittable caughtTarget = null;
	private IDMove activeMove = null, prevMove = null; 

	private InputHandler inputHandler = new InputHandlerKeyboard(this);
	private final int randomAnimationDisplacement;
	protected MoveList moveList = new M_Basic(this);

	private final List<IDMove> staleMoveQueue = new ArrayList<IDMove>();
	public static final int staleMoveQueueSize = 5;

	public Fighter(float posX, float posY, int team) {
		super(posX, posY);
		this.team = team;
		spawnPoint = new Vector2(posX, posY);
		this.setInputHandler(inputHandler);
		timerList.addAll(Arrays.asList(inputQueueTimer, wallJumpTimer, attackTimer, grabbingTimer, dashTimer, invincibleTimer,
				guardHoldTimer, footStoolTimer, slowedTimer, doubleJumpGraphicTimer, doubleJumpUseTimer, guardTimer, respawnTimer));
		state = State.STAND;
		randomAnimationDisplacement = (int) (8 * Math.random());
		baseHurtleBK = 8;
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		int slowMod = 2;
		if (!slowedTimer.timeUp() && deltaTime % slowMod != 0) return;
		stickX = getInputHandler().getXInput();
		stickY = getInputHandler().getYInput();

		if (grabbingTimer.timeJustUp()) dropTarget();
		else if (!grabbingTimer.timeUp()) handleThrow();
		if (null != getActiveMove()) {
			if (getActiveMove().id != MoveList_Advanced.noStaleMove && 
					getActiveMove().move.connected() && 
					!staleMoveQueue.contains(getActiveMove())){
				staleMoveQueue.add(getActiveMove());
				if (staleMoveQueue.size() > staleMoveQueueSize) staleMoveQueue.remove(0);
			}
			handleMove();
		}

		super.update(rectangleList, entityList, deltaTime);
		prevStickX = stickX;
	}

	private void handleThrow(){
		if (null != getActiveMove()) {
			if (!activeMoveIsThrow()) return;
			startAttack(getActiveMove());
			beginThrow();
		}
	}

	private void beginThrow(){
		grabbingTimer.end();
		releaseTarget();
	}

	private void dropTarget(){
		if (null == caughtTarget) return;
		new SFX.LightHit().play();
		release(this, -1);
		releaseTarget();
	}

	private void releaseTarget(){
		release(caughtTarget, 1);
		caughtTarget.caughtTimer.end();
		caughtTarget = null;
	}

	private void release(Hittable fi, int direction){
		fi.velocity.x = 3 * direction * direct();
		fi.velocity.y = 2;
		fi.hitstunTimer.reset();
		fi.hitstunTimer.setEndTime(10);
	}
	
	protected boolean shouldPushAway(int pushDistance, Hittable hi){
		return super.shouldPushAway(pushDistance, hi) && (null == getActiveMove());
	}

	private void handleMove(){
		getActiveMove().move.update();
		if (null == getActiveMove()) return;
		if (getActiveMove().move.doesStopInAir() && (!isGrounded() || !inGroundedState()) ) getActiveMove().move.setDone();
		if (getActiveMove().move.done()) {
			if (getActiveMove().move.causesHelpless()) state = State.HELPLESS;
			endAttack();
		}
	}

	protected void handleBounce(Entity e){
		super.handleBounce(e);
		endAttack();
	}

	void updateState(){
		if (canMove() && jumpSquatTimer.timeUp() && state == State.JUMPSQUAT) {
			state = State.JUMP;
			jump();
		}
		if (isGrounded()) updateGroundedState();
		else updateAerialState();
		prevState = state;
	}

	private void updateGroundedState(){
		float minCrouchHold = 0.9f;
		float minRunHold = 0.5f;
		float minRunFromAirHold = 0.9f;
		if (!(jumpSquatTimer.timeUp() && state == State.JUMPSQUAT) && !inGroundedState()) ground();

		if (state == State.FALLEN) state = State.FALLEN;
		else if (!jumpSquatTimer.timeUp()) {
			if (state != State.JUMPSQUAT) preJumpSquatState = state;
			state = State.JUMPSQUAT;
		}
		//else if ((state == State.GUARD && !guardHoldTimer.timeUp() || blockHeld) && attackTimer.timeUp()) state = State.GUARD;
		else if (Math.abs(stickX) < minRunHold && stickY > minCrouchHold) state = State.CROUCH;
		else if (Math.abs(stickX) > minRunHold){
			boolean fallToDash = !inGroundedState(prevState) && Math.abs(stickX) > minRunFromAirHold;
			if (fallToDash) state = State.DASH;
			else if (isRun()) state = State.RUN;
			else if (state == State.DASH && !dashTimer.timeUp()) state = State.DASH;
			else state = State.WALK;
			boolean noWalls = (!doesCollide(position.x - 2, position.y) && direction == Direction.LEFT)
					|| (!doesCollide(position.x + 2, position.y) && direction == Direction.RIGHT);
			if (canAct() && noWalls && Math.signum(velocity.x) != direct()) flip();
		}
		else state = State.STAND;
		if (canAttack() && prevState == State.RUN && state != State.RUN) startAttack(new IDMove(moveList.skid(), MoveList_Advanced.noStaleMove));
	}

	protected void updateAerialState(){
		if (state == State.HELPLESS) return;
		if (isWallSliding()) state = State.WALLSLIDE;
		else if (!jumpTimer.timeUp()) state = State.JUMP;
		else state = State.FALL;
	}

	private boolean isRun(){
		boolean turningAround = (Math.signum(velocity.x) != Math.signum(stickX));
		boolean enterRun = (state == State.DASH && dashTimer.timeUp());
		return (state == State.RUN || enterRun) && !turningAround;
	}

	private boolean tryDash(){
		if (!isGrounded() || !inGroundedState() || isRun()) return false;
		dashTimer.reset();
		velocity.x = Math.signum(stickX) * getDashStrength();
		state = State.DASH;
		MapHandler.addEntity(new Graphic.DustCloud(this, position.x, position.y));
		return true;
	}

	public boolean tryJump(){
		if (isGrounded()) {
			return true;
		}
		else if (isWallSliding()) {
			wallJump(); return true;
		}
		else if (getEnemyAbove() != null && footStoolTimer.timeUp()){
			footStool(getEnemyAbove()); return true;
		}
		else if (doubleJumps > 0 && doubleJumpUseTimer.timeUp() && state != State.JUMP){
			doubleJump(); return true;
		}
		return false;
	}

	public boolean tryNormal(){
		if (state == State.FALLEN){
			state = State.STAND;
			startAttack(new IDMove(moveList.getUpAttack(), MoveList.noStaleMove));
		}
		else if (canAttack()) {
			startAttack(moveList.selectNormalMove());
			return true;
		}
		return false;
	}

	public boolean trySpecial(){
		startAttack(moveList.selectSpecialMove());
		return true;
	}

	public boolean tryBlock(){
		startAttack(moveList.selectBlock());
		return true;
	}

	public boolean tryStickRight(){
		if (isGuarding()) {
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollBack(), MoveList_Advanced.noStaleMove));
			else startAttack(new IDMove(moveList.rollForward(), MoveList_Advanced.noStaleMove));
		}
		else if (isGrabbing()){
			MoveList_Advanced advMoveList = (MoveList_Advanced) moveList; // fighters that can grab must have an advanced movelist
			setActiveMove(advMoveList.selectForwardThrow());
		}
		else if (state == State.FALLEN){
			state = State.STAND;
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollBack(), MoveList_Advanced.noStaleMove));
			else startAttack(new IDMove(moveList.rollForward(), MoveList_Advanced.noStaleMove));
		}
		else if (canAttack()) tryDash();
		return true;
	}

	public boolean tryStickLeft(){ 
		if (isGuarding()) {
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollForward(), MoveList_Advanced.noStaleMove));
			else startAttack(new IDMove(moveList.rollBack(), MoveList_Advanced.noStaleMove));
		}
		else if (isGrabbing()){
			MoveList_Advanced advMoveList = (MoveList_Advanced) moveList; // fighters that can grab must have an advanced movelist
			setActiveMove(advMoveList.selectBackThrow());
		}
		else if (state == State.FALLEN){
			state = State.STAND;
			if (direction == Direction.LEFT) startAttack(new IDMove(moveList.rollForward(), MoveList_Advanced.noStaleMove));
			else startAttack(new IDMove(moveList.rollBack(), MoveList_Advanced.noStaleMove));
		}
		else if (canAttack()) tryDash();
		return true;
	}

	public boolean tryStickUp(){
		if (isGrabbing()){
			MoveList_Advanced advMoveList = (MoveList_Advanced) moveList; // fighters that can grab must have an advanced movelist
			setActiveMove(advMoveList.selectUpThrow());
		}
		else tryStickVertical();
		return true; 
	}

	public boolean tryStickDown(){
		if (isGrabbing()){
			MoveList_Advanced advMoveList = (MoveList_Advanced) moveList; // fighters that can grab must have an advanced movelist
			setActiveMove(advMoveList.selectDownThrow());
		}
		else if (isGrounded() && canAct()) fallThroughThinFloor();
		else tryStickVertical();
		return true; 
	}

	private void fallThroughThinFloor(){
		for (Rectangle r: tempRectangleList){
			Rectangle checker1 = new Rectangle(position.x, position.y - 1, 1, 1);
			Rectangle checker2 = new Rectangle(position.x + image.getWidth(), position.y - 1, 1, 1);
			if ((checker1.overlaps(r) || checker2.overlaps(r)) && r.getHeight() <= 1) {
				endAttack();
				position.y -= 2;
				float faller = -2.0f;
				if (velocity.y >= faller) velocity.y = faller;
			}
		}
	}

	private void tryStickVertical(){
		if (state == State.FALLEN){
			state = State.STAND;
			spotDodge();
		}
	}

	private void spotDodge(){
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
		MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
		startAttack(new IDMove(moveList.dodge(), MoveList_Advanced.noStaleMove));
	}

	public boolean tryCStickUp(){
		startAttack(moveList.selectCStickUp()); 
		return true; 
	}

	public boolean tryCStickDown(){
		startAttack(moveList.selectCStickDown()); 
		return true; 
	}

	public boolean tryCStickForward(){
		startAttack(moveList.selectCStickForward()); 
		return true; 
	}

	public boolean tryCStickBack(){
		startAttack(moveList.selectCStickBack()); 
		return true; 
	}

	public boolean tryGrab(){
		startAttack(moveList.selectGrab()); 
		return true; 
	}

	public boolean tryCharge(){
		startAttack(moveList.selectCharge()); 
		return true; 
	}

	public boolean tryTaunt() {
		if (!isGrounded() || !canAct()) return false;
		startAttack(moveList.selectTaunt()); 
		return true;
	}

	protected void startAttack(IDMove im){
		if (im.id == MoveList_Advanced.noMove) return;
		Move m = im.move;
		setActiveMove(im);
		attackTimer.setEndTime(m.getDuration() + 1);
		attackTimer.reset();
		tumbling = false;
		if (isGrounded()) state = State.WALK;
	}

	public void endAttack(){
		setActiveMove(null);
		guardTimer.end();
		attackTimer.end();
	}

	public void endSpecialAttack(){
		endAttack();
		new SFX.Error().play();
	}

	public void handleJumpHeld(boolean button) {
		if (isGrounded() && button && state != State.JUMPSQUAT && jumpSquatTimer.timeUp() && canAct()) jumpSquatTimer.reset();
		if (state == State.JUMP && !button) jumpTimer.end();
	}

	public void handleBlockHeld(boolean block) {
		if (!blockHeld && block) guardTimer.reset();
		blockHeld = block;
	}

	private void jump(){
		jumpTimer.reset();
		endAttack();
		if (stickX < 0) MapHandler.addEntity(new Graphic.SmokeTrail(position.x + image.getWidth(), position.y + 8));
		else MapHandler.addEntity(new Graphic.SmokeTrail(position.x, position.y + 8));
		if (velocity.y < 0) velocity.y = 0;
		velocity.x += 2 * stickX;
		getVelocity().y += getJumpStrength();
	}

	protected final int wallSlideDistance = 1;
	protected boolean isWallSliding() {
		if (isGrounded() || velocity.y > 4 || !canAct()) return false;
		boolean canWS = false;
		if (prevStickX < -unregisteredInputMax) {
			canWS = doesCollide(position.x - wallSlideDistance, position.y) && doesCollide(position.x - wallSlideDistance, position.y + image.getHeight()/2);
			if (direction == Direction.RIGHT && canWS) flip();
		}
		if (prevStickX > unregisteredInputMax) {
			canWS = doesCollide(position.x + wallSlideDistance, position.y) && doesCollide(position.x + wallSlideDistance, position.y + image.getHeight()/2);
			if (direction == Direction.LEFT && canWS) flip();
		}
		return canWS;
	}

	private void wallJump(){
		wallJumpTimer.reset();
		flip();
		velocity.x = getWallJumpStrengthX() * direct();
		velocity.y = getWallJumpStrengthY();
		tumbling = false;
	}

	protected void doubleJump(){
		getVelocity().y = getDoubleJumpStrength();
		MapHandler.addEntity(new Graphic.DoubleJumpRing(position.x, getCenter().y));
		if (prevStickX < -unregisteredInputMax && velocity.x > 0) velocity.x = 0; 
		if (prevStickX >  unregisteredInputMax && velocity.x < 0) velocity.x = 0; 
		velocity.x += 1 * stickX;
		decrementDoubleJump();
		doubleJumpUseTimer.reset();
		tumbling = false;
	}

	protected void decrementDoubleJump(){
		doubleJumps--;
		doubleJumpGraphicTimer.reset();
	}

	private void footStool(Fighter footStoolee){
		doubleJump();
		doubleJumps ++;
		doubleJumpGraphicTimer.end();
		new SFX.FootStool().play();
		footStoolee.velocity.set(footStoolKB);
		footStoolee.tumbling = true;
		footStoolee.percentage += footStoolDamage;
		if (!footStoolee.inHitstun()){
			footStoolee.hitstunTimer.setEndTime((int) (footStoolDuration * footStoolee.getHitstun() * (100.0/footStoolee.getWeight() )));
			footStoolee.hitstunTimer.reset();
		}
		footStoolTimer.reset();
	}

	private Fighter getEnemyAbove(){
		if (team != GlobalRepo.GOODTEAM) return null;
		for (Entity en: MapHandler.getEntities()) {
			if (en instanceof Fighter){
				Fighter fi = (Fighter) en;
				if (fi != this && isAbove(fi) && fi.getTeam() != GlobalRepo.GOODTEAM) return fi;
			}
		}
		return null;
	}

	private boolean isAbove(Fighter en){
		boolean xCorrect = Math.abs(en.getCenter().x - getCenter().x) < en.getImage().getWidth()/1.5f;
		boolean yCorrect = Math.abs(en.getCenter().y - position.y) < en.getImage().getHeight()/1.5f;
		return xCorrect && yCorrect;
	}

	public Rectangle getHurtBox(){
		Rectangle r = getNormalHurtBox();
		if (null != activeMove) r = activeMove.move.getMoveHurtBox(this, r);
		return r;
	}

	public Rectangle getNormalHurtBox(){
		return super.getHurtBox();
	}

	private void selectImage(float deltaTime){
		switch(state){
		case STAND: setImage(getStandFrame(deltaTime)); break;
		case WALK: setImage(getWalkFrame(deltaTime)); break;
		case RUN: setImage(getRunFrame(deltaTime)); break;
		case JUMP: setImage(getJumpFrame(deltaTime)); break;
		case FALL: {
			if (tumbling) setImage(getTumbleFrame(deltaTime));
			else if (velocity.y > 1) setImage(getJumpFrame(deltaTime));
			else if (velocity.y > 0) setImage(getAscendFrame(deltaTime));
			else setImage(getFallFrame(deltaTime)); 
			break;
		}
		case WALLSLIDE: setImage(getWallSlideFrame(deltaTime)); break;
		case CROUCH: setImage(getCrouchFrame(deltaTime)); break;
		case HELPLESS: setImage(getHelplessFrame(deltaTime)); break;
		case DASH: setImage(getDashFrame(deltaTime)); break;
		case JUMPSQUAT: setImage(getJumpSquatFrame(deltaTime)); break;
		case FALLEN: setImage(getFallenFrame(deltaTime)); break;
		default: break;
		}
	}

	private float prevX = 0, currX = 0, prevY = 0, currY = 0;
	void updateImage(float deltaTime){
		prevX = image.getWidth();
		prevY = image.getHeight();
		TextureRegion prevImage = image;
		selectImage(deltaTime);
		if (!jumpSquatTimer.timeUp()) setImage(getJumpSquatFrame(deltaTime));
		if (!attackTimer.timeUp() && null != getActiveMove() && null != getActiveMove().move.getAnimation()){
			setImage(getActiveMove().move.getAnimation().getKeyFrame(getActiveMove().move.getFrame()));
		}
		if (!caughtTimer.timeUp() || !stunTimer.timeUp()) setImage(getHitstunFrame(0));
		if (inHitstun()) {
			if (hitstunType == HitstunType.SUPER || hitstunType == HitstunType.ULTRA) setImage(getTumbleFrame(deltaTime));
			else setImage(getHitstunFrame(deltaTime));
		}
		if (!grabbingTimer.timeUp()) setImage(getGrabFrame(deltaTime));

		currX = image.getWidth();
		currY = image.getHeight();
		if (!prevImage.equals(image)) adjustImage(deltaTime, prevImage);
	}

	private void adjustImage(float deltaTime, TextureRegion prevImage){
		if (prevImage != getWallSlideFrame(deltaTime) && state == State.WALLSLIDE && direction == Direction.RIGHT) wallCling();
		float adjustedPosX = (image.getWidth() - prevImage.getRegionWidth())/2;
		if (!doesCollide(position.x - adjustedPosX, position.y) && state != State.WALLSLIDE) position.x -= adjustedPosX;
		float dispX = prevX - currX;
		float dispY = prevY - currY;

		if (doesCollide(position.x, position.y) && !doesCollide(position.x - dispX, position.y)) position.x -= dispX;
		if (doesCollide(position.x, position.y) && !doesCollide(position.x + dispX, position.y)) position.x += dispX;
		if (doesCollide(position.x, position.y) && !doesCollide(position.x, position.y - dispY)) position.y -= dispY;
		if (doesCollide(position.x, position.y) && !doesCollide(position.x, position.y + dispY)) position.y += dispY;

		if (doesCollide(position.x, position.y)) {
			System.out.println("Had to reset image of " + this);
			position.y += 8;
			setImage(prevImage);
		}
	}

	private int maxAdjust = 50;
	void wallCling(){
		int adjust = 0;
		while (!doesCollide(position.x + 1, position.y)){
			position.x += 1;
			adjust++;
			if (adjust > maxAdjust) break;
		}
	}

	void resetStance(){
		int adjust = 0;
		while (doesCollide(position.x, position.y)){
			position.x -= 1;
			adjust++;
			if (adjust > maxAdjust) break;
		}
	}

	public boolean doesCollide(float x, float y){
		if (collision == Collision.GHOST) return false;
		for (Rectangle r : tempRectangleList){
			Rectangle thisR = getCollisionBox(x, y);
			boolean fallThrough = stickY > 0.95f && null == getActiveMove() && !inGroundedState() && !inHitstun();
			boolean upThroughThinPlatform = r.getHeight() <= 1 && (r.getY() - this.getPosition().y > 0 || fallThrough);
			if (!upThroughThinPlatform && Intersector.overlaps(thisR, r) && thisR != r) return true;
		}
		return false;
	}

	void bounceOff(){
		if (inputHandler.isTeching()) tech();
		else super.bounceOff();
	}

	private void tech(){
		setInvincible(30);
		new SFX.Tech().play();
		hitstunTimer.end();
		tumbling = false;
		velocity.x = 0;
		velocity.y = 0;
	}

	public void ground(){
		if (hitstunTimer.getCounter() > 1 && velocity.y < 0){	
			if (tumbling) {
				if (inputHandler.isTeching()) tech();
				else {
					SFX.proportionalHit(knockbackIntensity(velocity)).play();
					state = State.FALLEN;
				}
			}
			hitstunTimer.end();
		}
		tumbling = false;
		super.ground();
		
		boolean upThroughThinPlatform = velocity.y >= 0;
		if (null != getActiveMove() && !getActiveMove().move.continuesOnLanding() && !upThroughThinPlatform) endAttack();
		if (velocity.y < 0 && getActiveMove() == null && state != State.FALLEN){
			startAttack(new IDMove(moveList.land(), MoveList_Advanced.noStaleMove));
		}
		refreshDoubleJump();
	}

	private void refreshDoubleJump(){
		doubleJumps = doubleJumpMax;
	}

	void handleGravity(){
		if (state == State.WALLSLIDE) velocity.y = getWallSlideSpeed() * MapHandler.getRoomGravity();
		else super.handleGravity();
	}

	void handleDirection(){
		float minTurn = 0.2f;
		int minFrameBReverse = 6;
		if (Math.abs(stickX) < unregisteredInputMax || !canMove() || cantTurnStates.contains(state)) return;
		if (null != getActiveMove()){
			boolean bReverse = 
					!attackTimer.timeUp() && attackTimer.getCounter() < minFrameBReverse && !getActiveMove().move.isNoTurn()
					&& getActiveMove().id >= MoveList_Advanced.specialRange[0] && getActiveMove().id <= MoveList_Advanced.specialRange[1];
					if (!bReverse) return;
		}
		else if (!canReverseInAir()) return;
		boolean turnLeft = stickX < -minTurn && (prevStickX > -minTurn) && getDirection() == Direction.RIGHT;
		boolean turnRight = stickX > minTurn && (prevStickX < minTurn)  && getDirection() == Direction.LEFT;
		if (turnLeft || turnRight) flip();
	}
	private final List<State> cantTurnStates = new ArrayList<State>(Arrays.asList(State.CROUCH, State.JUMPSQUAT, State.FALLEN));

	protected boolean canReverseInAir(){
		return isGrounded();
	}

	protected boolean activeMoveIsSpecial(){
		return activeMoveIsWhatever(MoveList_Advanced.specialRange);
	}

	protected boolean activeMoveIsCharge(){
		return activeMoveIsWhatever(MoveList_Advanced.chargeRange);
	}
	
	protected boolean activeMoveIsThrow(){
		return activeMoveIsWhatever(MoveList_Advanced.throwRange);
	}

	private boolean activeMoveIsWhatever(int[] range){
		if (null == getActiveMove()) return false;
		else return getActiveMove().id >= range[0] && getActiveMove().id <= range[1];
	}

	void handleMovement(){
		boolean groundedAttacking = !attackTimer.timeUp() && isGrounded();
		if (groundedAttacking || !canMove() || inHitstun()) return;
		switch (state){
		case WALK: addSpeed(getWalkSpeed(), getWalkAcc()); break;
		case DASH:
		case RUN: addSpeed(getRunSpeed(), getRunAcc()); break;
		case JUMP: velocity.y += getJumpAcc(); break;
		default: break;
		}
		if (!isGrounded() && wallJumpTimer.timeUp() && Math.abs(stickX) > unregisteredInputMax) {
			if (Math.abs(velocity.x) > getAirSpeed()) addSpeed(getRunSpeed(), getAirAcc());
			else addSpeed(getAirSpeed(), getAirAcc());
		}
	}

	private void addSpeed(float speed, float acc){ 
		if (Math.abs(velocity.x) < Math.abs(speed)) velocity.x += Math.signum(stickX) * acc; 
	}

	protected float directionalInfluenceAngle(Vector2 knockback){
		float diStrength = 8;
		if (team == GlobalRepo.GOODTEAM) diStrength = 40;
		if (getInputHandler().getXInput() < unregisteredInputMax && getInputHandler().getXInput() < unregisteredInputMax) return knockback.angle();
		Vector2 di = new Vector2(getInputHandler().getXInput(), getInputHandler().getYInput());
		float parallelAngle = Math.round(knockback.angle() - di.angle());
		double sin = Math.sin(parallelAngle * Math.PI/180);
		int signMod = 1;
		if (knockback.angle() > 90 && knockback.angle() <= 270) signMod *= -1;
		if (di.x < 0) signMod *= -1;
		double diModifier = signMod * Math.signum(180 - di.angle()) * diStrength * Math.pow(sin, 2);
		knockback.setAngle((float) (knockback.angle() + diModifier));
		return knockback.angle();
	}

	protected void takeKnockback(Vector2 knockback, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		super.takeKnockback(knockback, hitstun, shouldChangeKnockback, ht);
		if (null != caughtTarget) beginThrow();
	}

	public void isNowGrabbing(Hittable target, int caughtTime){
		caughtTarget = target;
		grabbingTimer.setEndTime(caughtTime);
		grabbingTimer.reset();
	}

	public void parry(){
		endAttack();
		invincibleTimer.setEndTime(1);
		invincibleTimer.reset();
		startAttack(new IDMove(moveList.parry(), MoveList.noStaleMove));
	}

	public void respawn() {
		lives -= 1;
		re();
	}

	public void refresh(){
		re();
		inputHandler.refresh();
	}


	public void reset() {
		setPosition(spawnPoint);
	}

	private void re(){
		position.set(spawnPoint);
		changeSpecial(SPECIALMETERMAX);
		percentage = 0;
		velocity.x = 0;
		velocity.y = 0;
		state = State.FALL;
		refreshDoubleJump();
		tumbling = false;
		if (direction == Direction.LEFT) flip();
		for (Timer t: timerList) t.end();
		endAttack();
		staleMoveQueue.clear();
		if (team == GlobalRepo.GOODTEAM) setInvincible(120);
	}

	private final float minDirect = 0.85f;
	public boolean isHoldUp() 		{ return -stickY > minDirect; }
	public boolean isHoldDown()		{ return stickY > minDirect; }
	public boolean isHoldForward() 	{ return Math.signum(stickX) == direct() && Math.abs(stickX) > minDirect; }
	public boolean isHoldBack() 	{ return Math.signum(stickX) != direct() && Math.abs(stickX) > minDirect; }

	public boolean canAttack() { return canAttackDodge(); }
	public boolean canAttackDodge() { return canAct() && state != State.WALLSLIDE; }
	public boolean canAct() { return !inHitstun() && attackTimer.timeUp() && state != State.FALLEN && state != State.HELPLESS && canMove(); }
	public boolean canMove(){ return super.canMove() && grabbingTimer.timeUp(); } 
	public boolean isRunning() { return state == State.RUN || state == State.DASH; }
	public boolean isInvincible(){ return hitstunTimer.getCounter() == 0 || !invincibleTimer.timeUp(); }
	public boolean inputQueueTimeUp(){ return inputQueueTimer.timeUp(); }
	public boolean attackTimeUp(){ return attackTimer.timeUp(); }
	public boolean isGuarding() { return isGrounded() && !guardTimer.timeUp(); }
	public boolean isCharging() {
		if (null == getInputHandler()) return false;
		else return activeMoveIsCharge() && getInputHandler().isCharging();
	}
	public boolean isGrabbing(){
		return !grabbingTimer.timeUp();
	}
	public boolean groundBelow(){
		Rectangle r = groundBelowRect();
		for (Rectangle tr: tempRectangleList) {
			if (Intersector.overlaps(r, tr)) return true;
		}
		return false;
	}
	public Rectangle groundBelowRect(){
		int rectHeight = GlobalRepo.TILE * 32;
		return new Rectangle (getCenter().x, position.y - rectHeight, 12, rectHeight);
	}

	public void setRespawnPoint(Vector2 startPosition) { spawnPoint.set(startPosition); }
	public void restartInputQueue() { inputQueueTimer.reset(); }
	public void countDownAttackTimer(){ attackTimer.countDown(); }
	public void setLives(int i) { lives = i; }
	public void setArmor(float armor) { this.armor = armor; }
	public void setActiveMove(IDMove activeMove) { 
		prevMove = this.activeMove;
		this.activeMove = activeMove; 
	}
	public void setToFall() { state = State.FALL; }
	public void setInputHandler(InputHandler inputHandler) { this.inputHandler = inputHandler; }
	public void setPalette(ShaderProgram pal) { palette = pal; }
	public void changeSpecial(float change) { specialMeter = MathUtils.clamp(specialMeter + change, 0, SPECIALMETERMAX); }
	public void setRespawnTimer() { respawnTimer.reset(); }
	public void setInvincible(int i) { setTimer(invincibleTimer, i); }
	public void setGuard(int i) { setTimer(guardTimer, i);}
	public void setHitstun(int i) { 
		velocity.x = -direct();
		velocity.y = -1;
		endAttack();
		setTimer(hitstunTimer, i); 
	}
	public void setStun(int i) { 
		setTimer(stunTimer, i); 
	}
	private void setTimer(Timer t, int i){
		t.setEndTime(i);
		t.reset();
	}
	public void setHitstunImage() { setImage(getHitstunFrame(DowntiltEngine.getDeltaTime())); }

	public float getStickX() { return stickX; }
	public float getStickY() { return stickY; }
	public float getSpecialMeter() { return specialMeter; }
	public float getArmor() { 
		float addedMoveArmor = 0;
		if (null != getActiveMove()) {
			addedMoveArmor = getActiveMove().move.getArmor();
		}
		return super.getArmor() + addedMoveArmor; 
	}
	public int getTeam() { return team; }
	public int getLives() { return lives; }
	public InputHandler getInputHandler() { return inputHandler; }
	public IDMove getActiveMove() { return activeMove; }
	public IDMove getPrevMove() { return prevMove; }
	public State getState() { return state; } 
	public List<IDMove> getMoveQueue() { return staleMoveQueue; }
	public InputPackage getInputPackage() { return new InputPackage(this); }
	public ShaderProgram getPalette() { return palette; }
	public TextureRegion getIcon(){ return defaultIcon; }
	protected TextureRegion getFrame(Animation anim, float deltaTime) { return anim.getKeyFrame(deltaTime + randomAnimationDisplacement); }
	public Timer getRespawnTimer(){ return respawnTimer; }

	abstract TextureRegion getWalkFrame(float deltaTime);
	abstract TextureRegion getRunFrame(float deltaTime);
	abstract TextureRegion getJumpFrame(float deltaTime);
	abstract TextureRegion getWallSlideFrame(float deltaTime);
	abstract TextureRegion getHelplessFrame(float deltaTime);
	abstract TextureRegion getGrabFrame(float deltaTime);
	abstract TextureRegion getFallFrame(float deltaTime);
	abstract TextureRegion getAscendFrame(float deltaTime);
	abstract TextureRegion getCrouchFrame(float deltaTime);
	abstract TextureRegion getDashFrame(float deltaTime);
	abstract TextureRegion getDodgeFrame(float deltaTime);
	abstract TextureRegion getJumpSquatFrame(float deltaTime);
	abstract TextureRegion getHitstunFrame(float deltaTime);
	abstract TextureRegion getFallenFrame(float deltaTime);

}
