package inputs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.Fighter;

public abstract class InputHandler {

	public static final int commandNone			=-1;
	public static final int commandAttack		= 0;
	public static final int commandSpecial		= 1;
	public static final int commandJump			= 2;
	public static final int commandCharge		= 3; 
	public static final int commandBlock		= 4;
	public static final int commandGrab 		= 5;
	public static final int commandSelect 		= 6;
	public static final int commandPause 		= 7;
	public static final int commandStickUp 		=20;
	public static final int commandStickLeft	=21;
	public static final int commandStickRight	=22;
	public static final int commandStickDown	=23;
	public static final int commandCStickUp 	=30;
	public static final int commandCStickLeft	=31;
	public static final int commandCStickRight	=32;
	public static final int commandCStickDown	=33;
	public static final int commandTaunt 		=40;

	Fighter fighter;
	public InputHandler(Fighter fighter){
		this.fighter = fighter;
	}

	public void update(){
		if (fighter.inputQueueTimeUp()) fighter.queuedCommand = commandNone;
		if (!fighter.inputQueueTimeUp()) handleCommand(fighter.queuedCommand);
	}

	public abstract float getXInput();
	public abstract float getYInput();
	public abstract boolean isCharging(); 
	public abstract boolean isTeching(); 

	protected void handleCommand(int command){
		boolean wasCommandAccepted = false;

		if (fighter.canMove()) wasCommandAccepted = handleCanMoveActions(command);
		if (fighter.canAct()) wasCommandAccepted = handleCanActActions(command);
		if (fighter.canAttack()) wasCommandAccepted = handleCanAttackActions(command);

		boolean shouldAddToInputQueue = !wasCommandAccepted && fighter.inputQueueTimeUp()
				&& !stickCommands.contains(command) && !(command == commandJump && fighter.isGrounded());
		if (shouldAddToInputQueue) {
			fighter.queuedCommand = command;
			fighter.restartInputQueue();
		}
		else if (wasCommandAccepted) {
			fighter.queuedCommand = commandNone;
		}
	}
	
	public void refresh(){
		/* */
	}

	private boolean handleCanMoveActions(int command){
		switch (command){
		case commandAttack:			return fighter.tryNormal();
		case commandStickRight:		return fighter.tryStickForward();
		case commandStickLeft:		return fighter.tryStickBack();
		case commandStickUp:		return fighter.tryStickUp();
		case commandStickDown:		return fighter.tryStickDown();
		}
		return true;
	}

	private boolean handleCanActActions(int command){
		if (command == commandJump) return fighter.tryJump();
		return true;
	}

	private boolean handleCanAttackActions(int command){
		switch (command){
		case commandBlock:			return fighter.tryBlock();
		case commandAttack:			return fighter.tryNormal();
		case commandSpecial:		return fighter.trySpecial();
		case commandGrab:	 		return fighter.tryGrab();
		case commandCharge: 		return fighter.tryCharge();
		case commandTaunt:			return fighter.tryTaunt();
		case commandCStickRight:	return fighter.tryCStickForward();
		case commandCStickLeft:		return fighter.tryCStickBack();
		case commandCStickUp:		return fighter.tryCStickUp();
		case commandCStickDown:		return fighter.tryCStickDown();
		}
		return false;
	}

	private final List<Integer> stickCommands = new ArrayList<Integer>(Arrays.asList(commandStickUp, commandStickRight, commandStickLeft, commandStickDown));
	
	public String getControllerName(){
		return "No controller found";
	}

}
