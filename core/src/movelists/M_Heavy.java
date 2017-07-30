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
		int frame = 50;
		int subFrame = frame/5;
		float bkb = 6.8f;
		float kbg = 3.4f;
		int damage = 20;

		Move m = new Move(user, frames * frame);
		m.setHurtBox(80, 80, 0, Move.HURTBOXNOTSET);
		m.setAnimation("sprites/fighters/heavy/nweak.png", frames, frame);
		Hitbox early1 = new Hitbox(user, bkb, kbg, damage, Hitbox.SAMURAI, 48, -24, 24, new SFX.HeavyHit());
		Hitbox early2 = new Hitbox(user, bkb, kbg, damage, Hitbox.SAMURAI, 42,  -2, 28, new SFX.HeavyHit());
		Hitbox early3 = new Hitbox(user, bkb, kbg, damage, Hitbox.SAMURAI, 32,   8, 24, new SFX.HeavyHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, early3));
		m.eventList.addArmor(m, frame * 0, frame * 1, 6);
		m.eventList.addActionCircle(early1, frame + (subFrame * 0), frame + (subFrame * 1));
		m.eventList.addActionCircle(early2, frame + (subFrame * 0), frame + (subFrame * 1));
		m.eventList.addActionCircle(early3, frame + (subFrame * 0), frame + (subFrame * 1));
		m.eventList.addArmor(m, frame * 1, frame * 3, -Heavy.HEAVY_ARMOR * 2);
		return m;
	}

	@Override
	public Move slide() {
		return nWeak();
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
		int frame = 27;
		float pushY = 19.5f;

		Move m = new Move(user, 20 * frame);
		m.setHelpless();
		m.setAerial();
		m.setAnimation("sprites/fighters/heavy/uspecial.png", 1, 1);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox rise = new Hitbox(user, 6.2f, 2.1f, 20,  80, 0, 0, 30, new SFX.MidHit(), c);
		Hitbox crash  = new Hitbox(user, 7.5f, 3.5f, 40, 270, 0, -20, 30, new SFX.MeatyHit(), c);
		new ActionCircleGroup(Arrays.asList(rise, crash));
		m.eventList.addCharge(user, c);
		m.eventList.addConstantVelocity(user, 0, frame * 2, 0, 0);
		m.eventList.addArmor(m, frame * 2, frame * 3, 4);
		m.eventList.addConstantVelocity(user, frame * 2, frame * 2 + 2, Action.ChangeVelocity.noChange, pushY);
		m.eventList.addActionCircle(rise, frame * 2, frame * 3 );
		m.eventList.addActionCircle(crash,  frame * 3, frame * 20 );
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
