package movelists;

import moves.IDMove;
import moves.Move;
import entities.Fighter;

public abstract class MoveList {
	
	protected final Fighter user;
	public static final int noMove = -2;
	public static final int noStaleMove = -1;
	public static final int aerial = 1;

	MoveList(Fighter user){
		this.user = user;
	}
	
	public abstract Move nWeak();
	public abstract Move slide();
	public abstract Move nAir();
	public abstract Move nCharge();
	public abstract Move nSpecial();
	public abstract Move uSpecial();
	public abstract Move rollForward();
	public abstract Move rollBack();
	public abstract Move dodge();
	public abstract Move land();
	public abstract Move skid();
	public abstract Move taunt();
	public abstract Move block();
	public Move getUpAttack(){
		return dodge();
	}
	
	public IDMove selectNormalMove(){
		if (user.isRunning()) return new IDMove(slide(), noStaleMove);
		else if (user.isGrounded()) return new IDMove(nWeak(), noStaleMove);
		else return new IDMove(nAir(), aerial);
	}

	public IDMove selectSpecialMove(){
		if (user.isGrounded()) return new IDMove(nSpecial(), noStaleMove);
		else return new IDMove(uSpecial(), noStaleMove);
	}

	public IDMove selectThrow(){
		return new IDMove(null, noMove);
	}

	public IDMove selectGrab(){
		return new IDMove(null, noMove);
	}

	public IDMove selectCharge() {
		if (user.isGrounded()) return new IDMove(nCharge(), noStaleMove);
		else return new IDMove(nAir(), aerial);
	}

	public IDMove selectCStickUp() {
		return selectCharge();
	}

	public IDMove selectCStickDown() {
		return selectCharge();
	}

	public IDMove selectCStickForward() {
		return selectCharge();
	}

	public IDMove selectCStickBack() {
		return selectCharge();
	}

	public IDMove selectBlock() {
		return new IDMove(block(), noStaleMove);
	}

	public IDMove selectTaunt() {
		return new IDMove(taunt(), noStaleMove);
	}


}
