package movelists;

import main.SFX;
import moves.Action;
import moves.Hitbox;
import moves.Move;
import moves.Windbox;
import entities.Fighter;
import entities.Graphic;

public class M_Fly extends MoveList {

	public M_Fly(Fighter user) {
		super(user);
	}

	@Override
	public Move nWeak() {
		Move m = new Move(user, 10);
		return m;
	}

	@Override
	public Move slide() {
		Move m = new Move(user, 10);
		return m;
	}

	@Override
	public Move nAir() {
		int end = 60;
		int startSwoop = 26;
		int iter = 8;
		int endSwoop = startSwoop + (iter * 4);

		Move m = new Move(user, end);
		m.setAerial();
		m.setAnimation("sprites/fighters/fly/nair.png", 3, 20);
		Hitbox hit = new Hitbox(user, 4.2f, 2.8f, 16, 80, 8, -8, 18, new SFX.MidHit());
		hit.setMovesAheadMod(1);
		m.setContinueOnLanding();
		m.eventList.addConstantVelocity(user, 0, startSwoop, -1, 1);
		m.eventList.addConstantVelocity(user, startSwoop, startSwoop + iter, 7, -7);
		m.eventList.addConstantVelocity(user, startSwoop + iter, startSwoop + iter * 2, 7, -4);
		m.eventList.addConstantVelocity(user, startSwoop + iter * 2, startSwoop + iter * 3, 6, 0);
		m.eventList.addConstantVelocity(user, startSwoop + iter * 3, startSwoop + iter * 4, 2, 3);
		m.eventList.addActionCircle(hit, startSwoop, endSwoop);
		return m;
	}

	@Override
	public Move nCharge() {
		int frames = 4;
		int frame = 30;
		int interframe = frame/3;

		Move m = new Move(user, frames * frame);
		m.setAerial();
		m.setAnimation("sprites/fighters/fly/ncharge.png", frames, frame);
		Hitbox hit = new Hitbox.AngleHitbox(user, 4.2f, 3.8f, 24, 8, -8, 18, new SFX.HeavyHit());
		hit.setMovesAheadMod(1);
		m.setContinueOnLanding();
		m.eventList.addTremble(m, 0, frame * 1);
		m.eventList.addConstantVelocity(user, 0, frame, 0, 0);
		m.eventList.addConstantVelocity(user, frame + (interframe * 0), frame + (interframe * 1), 7,-7);
		m.eventList.addConstantVelocity(user, frame + (interframe * 1), frame + (interframe * 2), 7, 7);
		m.eventList.addConstantVelocity(user, frame + (interframe * 2), frame + (interframe * 3),-7, 7);
		m.eventList.addConstantVelocity(user, frame + (interframe * 3), frame + (interframe * 5),-6, -6);
		m.eventList.addConstantVelocity(user, frame + (interframe * 5), frame + (interframe * 6), 7, 0);
		m.eventList.addActionCircle(hit, frame * 1, frame * 3);
		return m;
	}

	@Override
	public Move nSpecial() {
		return uSpecial();
	}

	@Override
	public Move uSpecial() {
		Move m = new Move(user, 20);
		m.setHelpless();
		m.setAnimation("sprites/fighters/fly/jump.png", 1, 1);
		m.eventList.addVelocityChange(user, 5, Action.ChangeVelocity.noChange, 7);
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

	private final float getUpYSpeed = 3.4f;
	private Move roll(){
		Move m = new Move(user, 48);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/fly/jump.png", 2, 16);
		m.eventList.addConstantVelocity(user, 2, 20, -4, getUpYSpeed);
		return m;
	}

	@Override
	public Move dodge() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/fly/jump.png", 1, 1);
		m.eventList.addConstantVelocity(user, 2, 20, 0, getUpYSpeed);
		return m;
	}

	@Override
	public Move land() {
		Move m = new Move(user, 6);
		m.setAnimation("sprites/fighters/fly/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move skid() {
		Move m = new Move(user, 6);
		m.setAnimation("sprites/fighters/fly/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move taunt() {
		int frame = 20;
		int frames = 2;

		Move m = new Move(user, frame * 6);
		m.setAnimation("sprites/fighters/fly/nspecial.png", frames, frame);
		Windbox wind1 = new Windbox(user, 0.9f, 0.3f, 60, 0, 30);
		Windbox wind3 = new Windbox(user, 1.4f, 0.5f, 30, 0, 20);
		Windbox wind2 = new Windbox(user, 0.7f, 0.2f, 40, 0, 40);
		wind1.setRefresh(4);
		wind2.setRefresh(4);
		m.eventList.addConstantVelocity(user, 0, frame * 1, 0, 0);
		m.eventList.addConstantVelocity(user, frame * 1, frame * 5, -1, 0);
		m.eventList.addGraphic(user, frame * 1,  frame * 5, new Graphic.Gust(user, frame * 4));
		m.eventList.addActionCircle(wind1, frame * 1, frame * 3);
		m.eventList.addActionCircle(wind3, frame * 1, frame * 3);
		m.eventList.addActionCircle(wind2, frame * 3, frame * 5);
		return m;
	}

}
