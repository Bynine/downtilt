package movelists;

import entities.Fighter;
import entities.Hurlable;
import main.GlobalRepo;
import main.SFX;
import moves.Action;
import moves.Hitbox;
import moves.Move;

public class M_Shoot extends MoveList {

	public M_Shoot(Fighter user) {
		super(user);
	}
	private float xGenericSpeed = 7;
	private float yGenericSpeed = 4;
	private int genericFrameLength = 30;

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
		int frames = 2;
		int frame = genericFrameLength;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/shoot/nspecial.png", frames, frame);
		m.setStopsInAir();
		m.eventList.addNewEntity(frame,
				user, (new Hurlable.ShootBall(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * xGenericSpeed, yGenericSpeed
				);
		return m;
	}

	@Override
	public Move nAir() {
		int frames = 2;
		int frame = 20;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/shoot/nair.png", frames, frame);
		m.eventList.addVelocityChange(user, frame, -4, 8);
		m.eventList.addNewEntity(frame, user, (new Hurlable.ShootBall(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 3, -6
				);
		return m;
	}

	@Override
	/**
	 * Fires multiple balls
	 */
	public Move nCharge() {
		int frames = 4;
		int frame = 40;
		int betweenShot = 24;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/shoot/ncharge.png", frames, frame);
		m.setStopsInAir();
		m.eventList.addNewEntity(frame * 2,
				user, (new Hurlable.ShootBall(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 2, 8
				);
		m.eventList.addNewEntity(frame * 2 + betweenShot,
				user, (new Hurlable.ShootBall(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * xGenericSpeed, yGenericSpeed
				);
		m.eventList.addNewEntity(frame * 2 + betweenShot * 2,
				user, (new Hurlable.ShootBall(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 6, 2
				);
		return m;
	}

	@Override
	/**
	 * Fires a ball upward
	 */
	public Move nSpecial() {
		int frames = 2;
		int frame = genericFrameLength;

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

	public Move block(){
		Move m = new Move(user, 70);
		m.setAnimation("sprites/fighters/shoot/block.png", 2, 40);
		m.setStopsInAir();
		m.eventList.addGuard(user, 5, 40);
		return m;
	}
	
	@Override
	public Move parry() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/shoot/parry.png", 1, 1);
		Hitbox parry = new Hitbox(user, 8.6f, 0.1f, 12, 67, 0, 0, 48, new SFX.Pop());
		m.eventList.addActionCircle(parry, 0, 10);
		return m;
	}

}
