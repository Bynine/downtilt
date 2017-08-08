package inputs;

import main.DowntiltEngine;
import timers.Timer;
import challenges.ChallengeTutorial;
import challenges.ChallengeTutorial.Ping;
import challenges.ChallengeTutorial.ToolTip;
import entities.Fighter;

public abstract class InputHandlerPlayer extends InputHandler {

	private final Timer techTimer = new Timer(20);

	public InputHandlerPlayer(Fighter player) {
		super(player);
	}

	public void update(){
		if (null == player) return;
		super.update();
		inputToCommand(attack(), commandAttack);
		inputToCommand(special(), commandSpecial);
		inputToCommand(charge(), commandCharge);
		inputToCommand(jump(), commandJump);
		inputToCommand(grab(), commandGrab);
		inputToCommand(dodge(), commandBlock);
		inputToCommand(taunt(), commandTaunt);
		inputToCommand(flickLeft(), commandStickLeft);
		inputToCommand(flickRight(), commandStickRight);
		inputToCommand(flickUp(), commandStickUp);
		inputToCommand(flickDown(), commandStickDown);
		inputToCommand(flickCLeft(), commandCStickLeft);
		inputToCommand(flickCRight(), commandCStickRight);
		inputToCommand(flickCUp(), commandCStickUp);
		inputToCommand(flickCDown(), commandCStickDown);
		if (pause()) DowntiltEngine.pauseGame(); 
		if (DowntiltEngine.getChallenge() instanceof ChallengeTutorial){
			ChallengeTutorial challengeTutorial = (ChallengeTutorial) DowntiltEngine.getChallenge();
			if (select()) challengeTutorial.advanceToolTip();
			ToolTip tt = challengeTutorial.getToolTip();
			if (flickLeft() || flickRight() || flickUp() || flickDown()) tt.checkPing(Ping.MOVE);
			if (inputNormal()) tt.checkPing(Ping.NORMAL);
			if (inputSpecial()) tt.checkPing(Ping.SPECIAL);
			if (inputCharge()) tt.checkPing(Ping.CHARGE);
			if (inputJump()) tt.checkPing(Ping.JUMP);
			if (inputGrab()) tt.checkPing(Ping.GRAB);
			if (inputGuard()) tt.checkPing(Ping.GUARD);
		}
		if (chargeHold() && DowntiltEngine.isPaused()) DowntiltEngine.startGameMenu();
		if (!inputNormal()) isAdvanceHeld = false;
		if (!inputSpecial()) isBackHeld = false;

		player.handleJumpHeld(jumpHold());
		player.handleBlockHeld(blockHold());

		if (dodge() && techTimer.timeUp()) techTimer.reset();
		techTimer.countUp();
	}

	private void inputToCommand(boolean input, int command){
		if (input) {
			handleCommand(command);
		}
	}

	public boolean isCharging(){
		return chargeHold();
	}

	public boolean isTeching(){
		return !techTimer.timeUp();
	}

	public abstract boolean attack();
	public abstract boolean special();
	public abstract boolean charge();
	public abstract boolean jump();
	public abstract boolean grab();
	public abstract boolean dodge();
	public abstract boolean taunt();
	public abstract boolean flickLeft();
	public abstract boolean flickRight();
	public abstract boolean flickUp();
	public abstract boolean flickDown();
	public abstract boolean flickCLeft();
	public abstract boolean flickCRight();
	public abstract boolean flickCUp();
	public abstract boolean flickCDown();
	public abstract boolean pause();
	public abstract boolean select();

	private boolean isAdvanceHeld = false;
	public boolean menuAdvance(){
		if (inputNormal() && !isAdvanceHeld){
			isAdvanceHeld = true;
			return true;
		}
		else return false;
	}
	
	private boolean isBackHeld = false;
	public boolean menuBack(){
		if (inputSpecial() && !isBackHeld){
			isBackHeld = true;
			return true;
		}
		else return false;
	}
	
	public abstract boolean inputNormal();
	public abstract boolean inputSpecial();
	public abstract boolean inputCharge();
	public abstract boolean inputJump();
	public abstract boolean inputGrab();
	public abstract boolean inputGuard();

	public abstract boolean chargeHold();
	public abstract boolean jumpHold();
	public abstract boolean blockHold();
	public abstract boolean attackHold();

}
