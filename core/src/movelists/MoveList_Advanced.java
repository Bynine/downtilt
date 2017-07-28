package movelists;

import moves.IDMove;
import moves.Move;
import entities.Entity.Direction;
import entities.Fighter;

public abstract class MoveList_Advanced extends MoveList{

	MoveList_Advanced(Fighter user) {
		super(user);
	}

	/* grounded normals */
	public abstract Move nWeak();
	public abstract Move uWeak();
	public abstract Move dWeak();
	public abstract Move sWeak();
	public abstract Move slide();

	/* charge attacks */
	public abstract Move nCharge();
	public Move fCharge(){
		return setSideCharge(1);
	}
	public Move bCharge(){
		return setSideCharge(-1);
	}
	private Move setSideCharge(int dir){
		Move m = nCharge();
		if (user.isGrounded() && user.direct() != dir) user.flip();
		return m;
	}
	public abstract Move uCharge();
	public abstract Move dCharge();

	/* aerial normals */
	public abstract Move nAir();
	public abstract Move fAir();
	public abstract Move bAir();
	public abstract Move uAir();
	public abstract Move dAir();

	/* specials */
	public abstract Move nSpecial();
	public abstract Move uSpecial();
	public abstract Move dSpecial();

	/* throws */
	public abstract Move fThrow();
	public abstract Move bThrow();
	public abstract Move uThrow();
	public abstract Move dThrow();
	public abstract Move fAirThrow();
	public abstract Move bAirThrow();
	public abstract Move uAirThrow();
	public abstract Move dAirThrow();

	/* grabs */
	public abstract Move grab();
	public abstract Move dashGrab();
	public abstract Move airGrab();
	public Move airBackGrab(){
		user.flip();
		return airGrab();
	}

	/* guard */
	public abstract Move rollForward();
	public abstract Move rollBack();
	public abstract Move airDodge();
	public abstract Move getUpAttack();
	public abstract Move dodge();
	public abstract Move block();

	/* misc */
	public abstract Move land();
	public abstract Move skid();
	public abstract Move taunt();
	public abstract Move parry();
	public abstract Move perfectParry();

	/* Move Selection */

	static final int IDslide = 0;
	static final int IDuweak = 1;
	static final int IDdweak = 2;
	static final int IDnweak = 3;
	static final int IDsweak = 4;
	static final int IDuspecial = 10;
	static final int IDdspecial = 11;
	static final int IDnspecial = 12;
	static final int IDuthrow = 20;
	static final int IDdthrow = 21;
	static final int IDfthrow = 22;
	static final int IDbthrow = 23;
	static final int IDuairthrow = 24;
	static final int IDdairthrow = 25;
	static final int IDfairthrow = 26;
	static final int IDbairthrow = 27;
	static final int IDucharge = 40;
	static final int IDdcharge = 41;
	static final int IDncharge = 42;
	static final int IDuair = 50;
	static final int IDdair = 51;
	static final int IDfair = 52;
	static final int IDbair = 53;
	static final int IDnair = 54;
	public static int[] specialRange = {10, 19};
	public static int[] chargeRange = {40, 49};
	public static int[] throwRange = {20, 29};

	public IDMove selectNormalMove(){
		if (user.isGrounded()) {
			if (user.isRunning()) return new IDMove(slide(), IDslide);
			else if (user.isHoldUp()) return new IDMove(uWeak(), IDuweak);
			else if (user.isHoldDown()) return new IDMove(dWeak(), IDdweak);
			else if (user.isHoldForward()) return new IDMove(sWeak(), IDsweak);
			else return new IDMove(nWeak(), IDnweak);
		}
		else return selectAerial();
	}

	public IDMove selectSpecialMove(){
		if (user.isHoldUp()) return new IDMove(uSpecial(), IDuspecial);
		else if (user.isHoldDown()) return new IDMove(dSpecial(), IDdspecial);
		else if (user.isHoldBack()) {
			user.flip();
			return new IDMove(nSpecial(), IDnspecial);
		}
		else return new IDMove(nSpecial(), IDnspecial);
	}

	public IDMove selectForwardThrow(){
		if (user.getDirection() == Direction.RIGHT) return selectThrow(new IDMove(fThrow(), IDfthrow), new IDMove(fAirThrow(), IDfairthrow));
		else return selectThrow(new IDMove(bThrow(), IDbthrow), new IDMove(bAirThrow(), IDbairthrow));
	}

	public IDMove selectBackThrow(){
		if (user.getDirection() == Direction.LEFT) return selectThrow(new IDMove(fThrow(), IDfthrow), new IDMove(fAirThrow(), IDfairthrow));
		else return selectThrow(new IDMove(bThrow(), IDbthrow), new IDMove(bAirThrow(), IDbairthrow));
	}

	public IDMove selectUpThrow(){
		return selectThrow(new IDMove(uThrow(), IDuthrow), new IDMove(uAirThrow(), IDuairthrow));
	}

	public IDMove selectDownThrow(){
		return selectThrow(new IDMove(dThrow(), IDdthrow), new IDMove(dAirThrow(), IDdairthrow));
	}
	
	private IDMove selectThrow(IDMove groundThrow, IDMove airThrow){
		if (user.isGrounded()) return groundThrow;
		else return airThrow;
	}

	public IDMove selectGrab(){
		if (!user.isGrounded()){
			if (user.isHoldBack()) return new IDMove(airBackGrab(), noStaleMove);
			else return new IDMove(airGrab(), noStaleMove);
		}
		else if (user.isRunning()) return new IDMove(dashGrab(), noStaleMove);
		return new IDMove(grab(), noStaleMove);
	}

	public IDMove selectCharge() {
		if (user.isGrounded()){
			if (user.isHoldUp()) return new IDMove(uCharge(), IDucharge);
			else if (user.isHoldDown()) return new IDMove(dCharge(), IDdcharge);
			else if (user.isHoldBack()) {
				user.flip();
				return new IDMove(nCharge(), IDncharge);
			}
			else return new IDMove(nCharge(), IDncharge);
		}
		else return selectAerial();
	}

	public IDMove selectCStickUp() {
		return selectCStickMove(new IDMove(uAir(), IDuair), new IDMove(uCharge(), IDucharge));
	}

	public IDMove selectCStickDown() {
		return selectCStickMove(new IDMove(dAir(), IDdair), new IDMove(dCharge(), IDdcharge));
	}

	public IDMove selectCStickForward() {
		if (user.isGrounded()) return cStickCharge(user.getDirection() == Direction.LEFT);
		else{
			if (user.getDirection() == Direction.LEFT) return new IDMove(bAir(), IDbair);
			else return new IDMove(fAir(), IDfair);
		}
	}

	public IDMove selectCStickBack() {
		if (user.isGrounded()) return cStickCharge(user.getDirection() == Direction.RIGHT);
		else{
			if (user.getDirection() == Direction.LEFT) return new IDMove(fAir(), IDfair);
			else return new IDMove(bAir(), IDbair);
		}
	}

	protected IDMove cStickCharge(boolean dir){
		IDMove im = new IDMove(nCharge(), IDncharge);
		if (dir) user.flip();
		return im;
	}

	@Override
	public IDMove selectBlock() {
		if (!user.isGrounded()) return new IDMove(airDodge(), noStaleMove);
		else return new IDMove(block(), noStaleMove);
	}

	public IDMove selectTaunt() {
		return new IDMove(taunt(), noStaleMove);
	}

	protected IDMove selectAerial(){
		if (user.isHoldUp()) return new IDMove(uAir(), IDuair);
		else if (user.isHoldDown()) return new IDMove(dAir(), IDdair);
		else if (user.isHoldForward()) return new IDMove(fAir(), IDfair);
		else if (user.isHoldBack()) return new IDMove(bAir(), IDbair);
		else return new IDMove(nAir(), IDnair);
	}

	protected IDMove selectCStickMove(IDMove aerial, IDMove charge){
		if (user.isGrounded()) return charge;
		else return aerial;
	}

}
