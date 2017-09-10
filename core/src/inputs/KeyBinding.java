package inputs;

public class KeyBinding {

	final int commandAttack;
	final int commandSpecial;
	final int commandJump;
	final int commandCharge; 
	final int commandBlock;
	final int commandGrab;
	final int commandSelect;
	final int commandPause;
	final int commandTaunt;
	
	/**
	 * Creates a default KeyBinding
	 */
	public KeyBinding(){
		commandAttack	= 0;
		commandSpecial	= 1;
		commandJump		= 2;
		commandCharge	= 3; 
		commandBlock	= 4;
		commandGrab 	= 5;
		commandSelect 	= 6;
		commandPause 	= 7;
		commandTaunt	=40;
	}
	
	/**
	 * Creates a custom KeyBinding
	 */
	public KeyBinding(int atk, int spe, int jum, int cha, int blo, int gra, int sel, int pau, int tau){
		commandAttack	= atk;
		commandSpecial	= spe;
		commandJump		= jum;
		commandCharge	= cha;
		commandBlock	= blo;
		commandGrab 	= gra;
		commandSelect 	= sel;
		commandPause 	= pau;
		commandTaunt	= tau;
	}

}
