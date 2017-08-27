package movelists;

import entities.Fighter;
import moves.Action;
import moves.Move;

public class M_PostBoss extends M_Basic {

	public M_PostBoss(Fighter user) {
		super(user, "postboss");
	}

	@Override
	public Move nWeak() {
		return new Move(user, 0);
	}

	@Override
	public Move slide() {
		return nWeak();
	}

	@Override
	public Move nAir() {
		return nWeak();
	}

	@Override
	public Move nCharge() {
		return nWeak();
	}

	@Override
	public Move nSpecial() {
		return nWeak();
	}

	@Override
	public Move uSpecial() {
		int frames = 5;
		int frame = 18;
		float pushY = 9.6f;

		Move m = new Move(user, frames * frame);
		m.setHelpless();
		m.setAnimation("sprites/fighters/" + name + "/fall.png", 1, 1);
		m.eventList.addTremble(m, 0, frame * 2);
		m.eventList.addConstantVelocity(user, 2, frame * 2, 0, 0);
		m.eventList.addConstantVelocity(user, frame*2, frame * 2 + 6, Action.ChangeVelocity.noChange, pushY);
		return m;
	}
	
	@Override
	protected Move roll(){
		return dodge();
	}
	
}
