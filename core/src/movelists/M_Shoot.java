package movelists;

import entities.Fighter;
import entities.Hurlable;
import main.GlobalRepo;
import moves.Action;
import moves.Move;

public class M_Shoot extends MoveList {

	public M_Shoot(Fighter user) {
		super(user);
	}
	private float xGenericSpeed = 5.5f;
	private float yGenericSpeed = 3.5f;
	private int genericFrameLength = 35;

	@Override
	public Move nWeak() {
		int frames = 2;
		int frame = genericFrameLength;

		Move m = new Move(user, frames * frame);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/shoot/nspecial.png", frames, frame);
		m.eventList.addNewEntity(frame,
				user, (new Hurlable.ShootBall(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * xGenericSpeed, yGenericSpeed
				);
		return m;
	}

	@Override
	public Move slide() {
		return nWeak();
	}

	@Override
	public Move nAir() {
		int frames = 2;
		int frame = 30;

		Move m = new Move(user, (frames * frame) + 10);
		m.setAnimation("sprites/fighters/shoot/nair.png", frames, frame);
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 3);
		m.eventList.addVelocityChange(user, frame, -4, 4);
		m.eventList.addNewEntity(frame, user, (new Hurlable.ShootBall(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 3, -6
				);
		return m;
	}

	@Override
	/**
	 * Fires a ball super fast
	 */
	public Move nCharge() {
		int frames = 4;
		int frame = 40;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/shoot/ncharge.png", frames, frame);
		m.setStopsInAir();
		m.eventList.addTremble(m, 0, frame * 2);
		m.eventList.addNewEntity(frame * 2,
				user, (new Hurlable.ShootBall(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 11.5f, 3.6f
				);
		return m;
	}

	@Override
	/**
	 * Fires a ball upward
	 */
	public Move nSpecial() {
		int frames = 2;
		int frame = 40;

		Move m = new Move(user, frames * frame);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/shoot/up.png", frames, frame);
		m.eventList.addNewEntity(frame,
				user, (new Hurlable.ShootBall(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 3, 10
				);
		return m;
	}

	@Override
	public Move uSpecial() {
		Move m = new Move(user, 120);
		m.setHelpless();
		m.setAnimation("sprites/fighters/shoot/uspecial.png", 2, 16);
		m.eventList.addConstantVelocity(user, 0, 120, Action.ChangeVelocity.noChange, 2.6f);
		return m;
	}

	@Override
	public Move rollForward() {
		user.flip();
		return roll();
	}

	@Override
	public Move rollBack() {
		return roll();
	}

	private Move roll(){
		Move m = new Move(user, 48);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/shoot/sgetup.png", 2, 16);
		m.eventList.addConstantVelocity(user, 2, 30, -2, Action.ChangeVelocity.noChange);
		return m;
	}

	@Override
	public Move dodge() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/shoot/ngetup.png", 1, 1);
		return m;
	}

	@Override
	public Move land() {
		Move m = new Move(user, 8);
		m.setAnimation("sprites/fighters/shoot/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move skid() {
		Move m = new Move(user, 16);
		m.setAnimation("sprites/fighters/shoot/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move taunt() {
		return new Move(user, 60);
	}

}
