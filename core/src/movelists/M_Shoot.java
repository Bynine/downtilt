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

	@Override
	public Move nWeak() {
		int frames = 1;
		int frame = 60;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/shoot/nspecial.png", frames, frame);
		m.eventList.addNewEntity(30,
				(new Hurlable.ShootBall(GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 2, 12
				);
		return m;
	}

	@Override
	public Move slide() {
		int frames = 1;
		int frame = 60;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/shoot/nspecial.png", frames, frame);
		m.eventList.addNewEntity(30,
				(new Hurlable.ShootBall(GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 8, 3
				);
		return m;
	}

	@Override
	public Move nAir() {
		int frames = 1;
		int frame = 60;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/shoot/nspecial.png", frames, frame);
		m.eventList.addNewEntity(30,
				(new Hurlable.ShootBall(GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 4, 6
				);
		return m;
	}

	@Override
	public Move nCharge() {
		int frames = 1;
		int frame = 60;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/shoot/nspecial.png", frames, frame);
		m.eventList.addNewEntity(30,
				(new Hurlable.ShootBall(GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 4, 9
				);
		return m;
	}

	@Override
	public Move nSpecial() {
		int frames = 1;
		int frame = 60;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/shoot/nspecial.png", frames, frame);
		m.eventList.addNewEntity(30,
				(new Hurlable.ShootBall(GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 4, 6
				);
		return m;
	}

	@Override
	public Move uSpecial() {
		Move m = new Move(user, 120);
		m.setHelpless();
		m.setAnimation("sprites/fighters/shoot/uspecial.png", 1, 1);
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
		m.eventList.addConstantVelocity(user, 2, 20, -3, Action.ChangeVelocity.noChange);
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
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/shoot/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move skid() {
		Move m = new Move(user, 12);
		m.setAnimation("sprites/fighters/shoot/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move taunt() {
		return new Move(user, 60);
	}

	public Move block(){
		Move m = new Move(user, 0);
		return m;
	}

}
