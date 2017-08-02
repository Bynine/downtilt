package inputs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import timers.DurationTimer;
import timers.Timer;
import entities.Entity.State;
import entities.InputPackage;
import main.GlobalRepo;

public abstract class Brain{
	protected final InputHandlerCPU body;
	protected final List<Timer> timerList = new ArrayList<Timer>();
	protected final Timer changeUpDown = new DurationTimer(90);
	protected final Timer waitToUseUpSpecial = new Timer(30);
	protected final Timer tryJump = new Timer(30);
	protected final Timer performJump = new Timer(20);
	protected final Timer changeDirection = new Timer(30);
	protected final Timer dodgeTimer = new Timer(6);
	protected final Timer attackTimer = new Timer(10);
	protected InputPackage pack;
	
	public Brain (InputHandlerCPU body){
		this.body = body;
		timerList.addAll(Arrays.asList(changeUpDown, waitToUseUpSpecial, tryJump, changeDirection, performJump, dodgeTimer, attackTimer) );
	}
	
	void update(InputPackage pack){
		this.pack = pack;
		float random = 0.9f;
		for (Timer t: timerList) if (Math.random() < random) t.countUp();
	}
	
	/* BEHAVIORS */

	float setInput(float value){
		return MathUtils.clamp(value, -1, 1);
	}
	
	boolean isCharging(){
		if (Math.random() < 0.95) return true;
		else return false;
	}

	void getUp(){
		if (Math.random() < 0.4)  body.handleCommand(InputHandler.commandStickDown);
		else {
			if (Math.random() < 0.5) body.handleCommand(InputHandler.commandStickLeft);
			else body.handleCommand(InputHandler.commandStickRight);
		}
	}

	void attemptRecovery(Timer waitToUseUpSpecial){
		body.yInput = 1;
		if (Math.abs(pack.distanceFromCenter) > GlobalRepo.TILE * 2) body.xInput = setInput(pack.distanceFromCenter);
		if (pack.isBelowStage){
			if (pack.state == State.WALLSLIDE) {
				if (Math.random() < 0.1) body.handleCommand(InputHandler.commandJump);
			}
			else if (!pack.outOfDoubleJumps) {
				body.handleCommand(InputHandler.commandJump);
				waitToUseUpSpecial.reset();
			}
			else if (waitToUseUpSpecial.timeUp()) body.handleCommand(InputHandler.commandSpecial);
		}
	}

	void jumpAtPlayer(Timer tryJump, Timer performJump){
		tryJump.reset();
		if (performJump.timeUp() && checkPerformJump()) {
			body.handleCommand(InputHandler.commandJump);
			performJump.reset();
		}
	}
	
	boolean checkPerformJump(){
		return Math.random() < (pack.distanceYFromPlayer * 0.02f);
	}

	void performJump(Timer performJump){
		body.handleJumpHeld();
		if (Math.random() < 0.5) performJump.countUp(); // modulates jump height
	}

	void attackPlayer(int command){
		facePlayer();
		attackTimer.reset();
		body.handleCommand(command);
	}

	void facePlayer(){
		body.xInput = Math.signum(pack.distanceXFromPlayer);
		if (pack.distanceYFromPlayer > 20 && Math.random() < 0.5) body.yInput = 1;
	}

	float runTendency = 0.01f;
	void headTowardPlayer(Timer changeDirection){
		body.xInput = setInput(pack.distanceXFromPlayer);
		if (Math.random() < runTendency && Math.abs(pack.distanceXFromPlayer) > 300) { // run toward
			if (pack.distanceXFromPlayer > 0) body.handleCommand(InputHandler.commandStickRight);
			else body.handleCommand(InputHandler.commandStickLeft);
		}
		else if (Math.random() < 0.1) body.xInput = 0;
		changeDirection.reset();
	}

	void changeUpDown(){
		double ud = (1 - (2 * Math.random()) );
		ud = Math.signum(ud) * Math.pow(Math.abs(ud), 0.7);
		body.yInput = (float) MathUtils.clamp(ud, -1, 1);
		changeUpDown.reset();
	}

	void crouchBeforeAttacking(){
		body.yInput = -1;
		body.xInput = 0;
	}
	
	protected float maxVerticalAttackRange = 90;
	boolean inVerticalAttackRange(){
		return Math.abs(pack.distanceYFromPlayer + 40) < maxVerticalAttackRange && !pack.isGrounded || Math.abs(pack.distanceYFromPlayer) < maxVerticalAttackRange;
	}

	boolean shouldAttack(double chance, int distance, boolean mustBeGrounded){
		boolean shouldAttack = Math.random() < chance && Math.abs(pack.distanceXFromPlayer) < distance;
		if (mustBeGrounded) shouldAttack = shouldAttack && pack.isGrounded;
		return shouldAttack;
	}
	
	boolean shouldAttack(double chance, int closeDistance, int farDistance, boolean mustBeGrounded){
		return shouldAttack(chance, farDistance, mustBeGrounded) && Math.abs(pack.distanceXFromPlayer) > closeDistance;
	}
	
	boolean shouldGetUp(double d){
		return pack.state == State.FALLEN && Math.random() < d;
	}
	
	void avoidBadThings(){
		for (Vector2 badThing: pack.shitToAvoid){
			if (badThing.dst(pack.position.add(pack.velocity.scl(5))) < 50 ){
				// TODO: avoid behavior, change vector2 to rectangle
			}
		}
	}

	/* BRAINS */

	public static class Braindead extends Brain{ // does nothing

		public Braindead(InputHandlerCPU body) {
			super(body);
		}

	}

	public static class Recover extends Brain{ // Recovers if thrown offstage, mixes up vertical DI

		public Recover(InputHandlerCPU body) {
			super(body);
			timerList.addAll(Arrays.asList(waitToUseUpSpecial));
		}

		void update(InputPackage pack){
			super.update(pack);
			body.xInput = 0;
			if (pack.isOffStage) attemptRecovery(waitToUseUpSpecial);
		}

	}

	public static class MookBrain extends Brain{

		public MookBrain(InputHandlerCPU body) {
			super(body);
		}

		void update(InputPackage pack){
			super.update(pack);
			body.yInput = 0;
			if (changeDirection.timeUp() && Math.abs(pack.distanceXFromPlayer) > 25 ) headTowardPlayer(changeDirection);
			if (!performJump.timeUp()) performJump(performJump);
			if (shouldGetUp(0.02)) getUp();
			else if (pack.distanceYFromPlayer > 20 && tryJump.timeUp()) jumpAtPlayer(tryJump, performJump);
			else if (inVerticalAttackRange()) chooseAttack();
			else if (pack.isOffStage) attemptRecovery(waitToUseUpSpecial);
		}
		
		void chooseAttack(){
			if (!attackTimer.timeUp()) return;
			if		(shouldAttack(0.02, 70,  true))								performJump(performJump);
			else if (shouldAttack(0.03, 40,  false))							attackPlayer(InputHandler.commandAttack);
			else if (shouldAttack(0.07, 50,  true))								attackPlayer(InputHandler.commandAttack);
			else if (shouldAttack(0.08, 30,  true))								attackPlayer(InputHandler.commandSpecial);
			else if (shouldAttack(0.04, 40, 70, false)) 						attackPlayer(InputHandler.commandCharge);
		}

	}
	
	public static class FlyBrain extends Brain{
		
		private final Timer dodgeTimer = new Timer(60);

		public FlyBrain(InputHandlerCPU body) {
			super(body);
			timerList.add(dodgeTimer);
		}

		void update(InputPackage pack){
			super.update(pack);
			body.yInput = 0;
			if (changeDirection.timeUp() && Math.abs(pack.distanceXFromPlayer) > 25 ) headTowardPlayer(changeDirection);
			if (!performJump.timeUp()) performJump(performJump);
			if (shouldGetUp(0.03)) getUp();
			else if (pack.distanceYFromPlayer > -60 && tryJump.timeUp() || pack.isGrounded) jumpAtPlayer(tryJump, performJump);
			else if (inVerticalAttackRange()) chooseAttack();
			if (Math.random() < 0.03 && pack.distanceYFromPlayer > -60) body.handleCommand(InputHandler.commandJump);
			else if (pack.isOffStage) attemptRecovery(waitToUseUpSpecial);
		}
		
		void chooseAttack(){
			if (!attackTimer.timeUp()) return;
			if		(shouldAttack(0.04, 70,  true))		performJump(performJump);
			else if (shouldAttack(0.18, 25,  false) && !pack.isGrounded)			attackPlayer(InputHandler.commandAttack);
			else if (shouldAttack(0.08, 25,  false) && !pack.isGrounded)			attackPlayer(InputHandler.commandCharge);
			else if (shouldAttack(0.08, 30,  false) && !pack.isGrounded)			attackPlayer(InputHandler.commandTaunt);
		}
		
		boolean checkPerformJump(){
			return Math.random() < 0.05;
		}

	}
	
	public static class ShootBrain extends Brain{

		public ShootBrain(InputHandlerCPU body) {
			super(body);
			maxVerticalAttackRange = 400;
		}

		void update(InputPackage pack){
			super.update(pack);
			body.yInput = 0;
			if (!performJump.timeUp()) performJump(performJump);
			if (shouldGetUp(0.02)) getUp();
			if (pack.isOffStage) attemptRecovery(waitToUseUpSpecial);
			else {
				chooseAttack();
				if (changeDirection.timeUp()) {
					//facePlayer();
					headTowardPlayer(changeDirection);
				}
			}
		}
		
		void chooseAttack(){
			if (!attackTimer.timeUp()) return;
			if		(shouldAttack(0.02, 120,  true))	performJump(performJump);
			if 		(shouldAttack(0.06, 350, false))	attackPlayer(InputHandler.commandAttack);
			else 
				if (shouldAttack(0.03, 400, true)
					&& pack.distanceYFromPlayer > 20)	attackPlayer(InputHandler.commandSpecial);
			else 
				if (shouldAttack(0.03, 400, false)) 	attackPlayer(InputHandler.commandCharge);
		}

	}
	
	
	public static class HeavyBrain extends Brain{

		public HeavyBrain(InputHandlerCPU body) {
			super(body);
		}

		void update(InputPackage pack){
			super.update(pack);
			body.yInput = 0;
			if (changeDirection.timeUp() && Math.abs(pack.distanceXFromPlayer) > 80 ) headTowardPlayer(changeDirection);
			if (shouldGetUp(0.02)) getUp();
			else if (inVerticalAttackRange()) chooseAttack();
			if (pack.isOffStage || pack.distanceYFromPlayer < -48) attemptRecovery(waitToUseUpSpecial);
		}
		
		void chooseAttack(){
			if (!attackTimer.timeUp()) return;
			if (shouldAttack(0.16, 80, true))	attackPlayer(InputHandler.commandAttack);
			if (shouldAttack(0.02, 160, true))	attackPlayer(InputHandler.commandCharge);
			if (shouldAttack(0.01, 300, true))	attackPlayer(InputHandler.commandSpecial);
		}

	}
}
