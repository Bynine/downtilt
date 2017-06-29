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

	/* misc */
	public abstract Move land();
	public abstract Move skid();
	public abstract Move taunt();

	/* Move Selection */

	private static final int IDslide = 0;
	private static final int IDuweak = 1;
	private static final int IDdweak = 2;
	private static final int IDnweak = 3;
	private static final int IDsweak = 4;
	private static final int IDuspecial = 10;
	private static final int IDdspecial = 11;
	private static final int IDnspecial = 12;
	private static final int IDuthrow = 20;
	private static final int IDdthrow = 21;
	private static final int IDfthrow = 22;
	private static final int IDbthrow = 23;
	private static final int IDuairthrow = 24;
	private static final int IDdairthrow = 25;
	private static final int IDfairthrow = 26;
	private static final int IDbairthrow = 27;
	private static final int IDucharge = 40;
	private static final int IDdcharge = 41;
	private static final int IDncharge = 42;
	private static final int IDuair = 50;
	private static final int IDdair = 51;
	private static final int IDfair = 52;
	private static final int IDbair = 53;
	private static final int IDnair = 54;
	public static int[] specialRange = {10, 19};
	public static int[] chargeRange = {40, 49};

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

	public IDMove selectThrow(){
		if (user.isGrounded()){
			if (user.isHoldUp()) return new IDMove(uThrow(), IDuthrow);
			else if (user.isHoldDown()) return new IDMove(dThrow(), IDdthrow);
			else if (user.isHoldForward()) return new IDMove(fThrow(), IDfthrow);
			else if (user.isHoldBack()) return new IDMove(bThrow(), IDbthrow);
		}
		else{
			if (user.isHoldUp()) return new IDMove(uAirThrow(), IDuairthrow);
			else if (user.isHoldDown()) return new IDMove(dAirThrow(), IDdairthrow);
			else if (user.isHoldForward()) return new IDMove(fAirThrow(), IDfairthrow);
			else if (user.isHoldBack()) return new IDMove(bAirThrow(), IDbairthrow);
		}
		return null;
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

	public IDMove selectBlock() {
		if (!user.isGrounded() && (user.isHoldUp() || user.isHoldDown() || user.isHoldBack() || user.isHoldForward())) return new IDMove(airDodge(), noStaleMove);
		else return new IDMove(null, noMove);
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
