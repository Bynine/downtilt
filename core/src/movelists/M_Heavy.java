package movelists;

import java.util.Arrays;

import main.SFX;
import moves.Action;
import moves.ActionCircleGroup;
import moves.Effect;
import moves.Hitbox;
import moves.Move;
import moves.Effect.Charge;
import entities.Fighter;
import entities.Heavy;

public class M_Heavy extends MoveList {

	public M_Heavy(Fighter user) {
		super(user);
	}

	@Override
	public Move nWeak() {
		int frames = 3;
		int frame = 60;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/heavy/nweak.png", frames, frame);
		Hitbox h1 = new Hitbox(user, 5.8f, 3.2f, 20, Hitbox.SAMURAI, 38, -2, 32, new SFX.MidHit());
		m.eventList.addArmor(m, 0, frame * 2, 4);
		m.eventList.addActionCircle(h1, frame, frame + 4);
		m.eventList.addArmor(m, frame * 2, frame * 3, -Heavy.HEAVY_ARMOR);
		return m;
	}

	@Override
	public Move slide() {
		int frames = 4;
		int frame = 14;

		Move m = new Move(user, frames * frame);
		m.setContinueOnLanding();
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/heavy/slide.png", frames, frame);
		Hitbox early = new Hitbox(user, 3.1f, 2.6f, 11, 40, 16, -12, 24, new SFX.MidHit());
		Hitbox late  = new Hitbox(user, 2.0f, 2.0f, 7, 40, 16, -12, 24, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addConstantVelocity(user, 0, 14, 0, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 14, 5, Action.ChangeVelocity.noChange);
		m.eventList.addConstantVelocity(user, 16, 30, 3, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(early, 16, 20);
		m.eventList.addActionCircle(late, 21, 30);
		return m;
	}

	@Override
	public Move nAir() {
		int startFall = 60;
		int moveEnd = 180;

		Move m = new Move(user, moveEnd);
		m.setAerial();
		m.setAnimation("sprites/fighters/heavy/nair.png", 6, 4);
		Hitbox early = new Hitbox(user, 5.2f, 3.6f, 30, 300, 0, -20, 22, new SFX.MeatyHit());
		m.eventList.addConstantVelocity(user, 0, startFall/3, Action.ChangeVelocity.noChange, 0);
		m.eventList.addConstantVelocity(user, startFall/3, startFall, 0, 1);
		m.eventList.addActionCircle(early, startFall, moveEnd);
		m.eventList.addConstantVelocity(user, startFall, moveEnd, 0, -10);
		return m;
	}

	@Override
	public Move nCharge() {
		int frames = 5;
		int frame = 15;
		float pushX = 6.5f;
		float pushY = 0; //4.0f;

		Move m = new Move(user, frames * frame);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/heavy/ncharge.png", frames, frame);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox early = new Hitbox(user, 3.2f, 3.3f, 15, Hitbox.SAMURAI, 0, 0, 22, new SFX.HeavyHit(), c);
		Hitbox late  = new Hitbox(user, 2.5f, 2.6f, 11, Hitbox.SAMURAI, 0, 0, 22, new SFX.MidHit(), c);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addCharge(user, c);
		m.eventList.addConstantVelocity(user, frame * 2, frame * 4, pushX, pushY);
		m.eventList.addActionCircle(early, frame * 2, frame * 3 );
		m.eventList.addActionCircle(late,  frame * 3, frame * 4 );
		return m;
	}

	@Override
	public Move nSpecial() {
		int frames = 8;
		int frame = 22;

		Move m = new Move(user, frames * frame);
		return m;
	}

	@Override
	public Move uSpecial() {
		int frames = 5;
		int frame = 27;
		float pushY = 18.6f;

		Move m = new Move(user, frames * frame);
		m.setHelpless();
		m.setAnimation("sprites/fighters/heavy/uspecial.png", frames, frame);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox early = new Hitbox(user, 7.2f, 3.1f, 23,  80, 0, 0, 50, new SFX.HeavyHit(), c);
		Hitbox late  = new Hitbox(user, 7.5f, 2.5f, 19, 270, 0, 0, 50, new SFX.MeatyHit(), c);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addCharge(user, c);
		m.eventList.addConstantVelocity(user, 0, frame * 2, 0, 0);
		m.eventList.addConstantVelocity(user, frame*2, frame * 2 + 6, Action.ChangeVelocity.noChange, pushY);
		m.eventList.addActionCircle(early, frame * 2, frame * 3 );
		m.eventList.addActionCircle(late,  frame * 3, frame * 4 );
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
		m.setAnimation("sprites/fighters/heavy/sgetup.png", 2, 16);
		m.eventList.addConstantVelocity(user, 2, 20, -3, Action.ChangeVelocity.noChange);
		return m;
	}

	@Override
	public Move dodge() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/heavy/ngetup.png", 1, 1);
		return m;
	}

	@Override
	public Move land() {
		Move m = new Move(user, 30);
		if (null != user.getPrevMove())
			if (user.getPrevMove().id == MoveList.aerial) {
				m = new Move(user, 60);
//				Hitbox h1 = new Hitbox(user, 5.8f, 3.2f, 20, Hitbox.SAMURAI, 0, -20, 32, new SFX.MidHit());
//				m.eventList.addActionCircle(h1, 0, 4);
			}
		m.setAnimation("sprites/fighters/heavy/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move skid() {
		Move m = new Move(user, 12);
		m.setAnimation("sprites/fighters/heavy/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move taunt() {
		return new Move(user, 60);
	}

}
