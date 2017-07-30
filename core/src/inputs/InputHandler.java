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

	Fighter player;
	public InputHandler(Fighter fighter){
		this.player = fighter;
	}
	
	public void setPlayer(Fighter fighter){
		this.player = fighter;
	}

	public void update(){
		if (player.inputQueueTimeUp()) player.queuedCommand = commandNone;
		if (!player.inputQueueTimeUp()) handleCommand(player.queuedCommand);
	}

	public abstract float getXInput();
	public abstract float getYInput();
	public abstract boolean isCharging(); 
	public abstract boolean isTeching(); 

	protected void handleCommand(int command){
		boolean wasCommandAccepted = false;

		wasCommandAccepted = handleActions(command);
		if (player.canMove()) wasCommandAccepted = handleCanMoveActions(command);
		if (player.canAct()) wasCommandAccepted = handleCanActActions(command);
		if (player.canAttack()) wasCommandAccepted = handleCanAttackActions(command);
		

		boolean shouldAddToInputQueue = !wasCommandAccepted && player.inputQueueTimeUp()
				&& !stickCommands.contains(command) && !(command == commandJump && player.isGrounded());
		if (shouldAddToInputQueue) {
			player.queuedCommand = command;
			player.restartInputQueue();
		}
		else if (wasCommandAccepted) {
			player.queuedCommand = commandNone;
		}
	}
	
	public void refresh(){
		/* */
	}
	
	private boolean handleActions(int command){
		switch (command){
		case commandStickRight:		return player.tryStickRight();
		case commandStickLeft:		return player.tryStickLeft();
		case commandStickUp:		return player.tryStickUp();
		case commandStickDown:		return player.tryStickDown();
		}
		return true;
	}

	private boolean handleCanMoveActions(int command){
		switch (command){
		case commandAttack:			return player.tryNormal();
		}
		return true;
	}

	private boolean handleCanActActions(int command){
		if (command == commandJump) return player.tryJump();
		return true;
	}

	private boolean handleCanAttackActions(int command){
		switch (command){
		case commandBlock:			return player.tryBlock();
		case commandAttack:			return player.tryNormal();
		case commandSpecial:		return player.trySpecial();
		case commandGrab:	 		return player.tryGrab();
		case commandCharge: 		return player.tryCharge();
		case commandTaunt:			return player.tryTaunt();
		case commandCStickRight:	return player.tryCStickForward();
		case commandCStickLeft:		return player.tryCStickBack();
		case commandCStickUp:		return player.tryCStickUp();
		case commandCStickDown:		return player.tryCStickDown();
		}
		return false;
	}

	private final List<Integer> stickCommands = new ArrayList<Integer>(Arrays.asList(commandStickUp, commandStickRight, commandStickLeft, commandStickDown));
	
	public String getControllerName(){
		return "No controller found";
	}

}
