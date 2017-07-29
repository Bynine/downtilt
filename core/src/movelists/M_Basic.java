package movelists;

import java.util.Arrays;

import main.SFX;
import moves.Action;
import moves.ActionCircleGroup;
import moves.Effect;
import moves.Hitbox;
import moves.Move;
import moves.Effect.Charge;
import entities.Basic;
import entities.Fighter;
import entities.Graphic;

public class M_Basic extends MoveList {

	public M_Basic(Fighter user) {
		super(user);
	}

	@Override
	public Move nWeak() {
		int frames = 3;
		int frame = 22;

		Move m = new Move(user, frames * frame);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/basic/nweak.png", 3, frame);
		Hitbox h1 = new Hitbox(user, 2.2f, 2.2f, 12, Hitbox.SAMURAI, 20, 4, 16, new SFX.MidHit());
		m.eventList.addActionCircle(h1, frame, frame + 4);
		return m;
	}

	@Override
	public Move slide() {
		return nWeak();
//		int frames = 4;
//		int frame = 14;
//		int length = frames * frame;
//
//		Move m = new Move(user, length);
//		m.setContinueOnLanding();
//		m.setStopsInAir();
//		m.setAnimation("sprites/fighters/basic/slide.png", frames, frame);
//		Hitbox early = new Hitbox(user, 3.1f, 2.6f, 14, 40, 16, -12, 24, new SFX.MidHit());
//		Hitbox late  = new Hitbox(user, 2.0f, 2.0f, 10, 40, 16, -12, 24, new SFX.MidHit());
//		new ActionCircleGroup(Arrays.asList(early, late));
//		m.eventList.addConstantVelocity(user, 0, 14, 0, Action.ChangeVelocity.noChange);
//		m.eventList.addVelocityChange(user, 14, 5, Action.ChangeVelocity.noChange);
//		m.eventList.addConstantVelocity(user, 16, 30, 3, Action.ChangeVelocity.noChange);
//		m.eventList.addActionCircle(early, 16, 20);
//		m.eventList.addActionCircle(late, 21, 30);
//		return m;
	}

	@Override
	public Move nAir() {
		int frames = 5;
		int frame = 15;
		
		Move m = new Move(user, frames * frame);
		m.setAerial();
		m.setAnimation("sprites/fighters/basic/nair.png", frames, frame);
		Hitbox early = new Hitbox(user, 3.2f, 2.6f, 16, Hitbox.SAMURAI, 0, 0, 22, new SFX.MidHit());
		Hitbox late  = new Hitbox(user, 2.6f, 1.6f, 12, Hitbox.SAMURAI, 0, 0, 22, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addConstantVelocity(user, 2, frame * 2, 0, 0);
		m.eventList.addConstantVelocity(user, frame * 2, frame * 3, Action.ChangeVelocity.noChange, 1);
		m.eventList.addActionCircle(early, frame * 2, frame * 3);
		m.eventList.addActionCircle(late,  frame * 3, frame * 4);
		return m;
	}

	@Override
	public Move nCharge() {
		int frames = 9;
		int frame = 20;
		float pushX = 3.6f;
		float pushY = 0;

		Move m = new Move(user, frames * frame);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/basic/ncharge.png", frames, frame);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox early = new Hitbox(user, 3.2f, 3.3f, 16, Hitbox.SAMURAI, 0, 0, 22, new SFX.HeavyHit(), c);
		Hitbox late  = new Hitbox(user, 2.5f, 2.6f, 12, Hitbox.SAMURAI, 0, 0, 22, new SFX.MidHit(), c);
		m.eventList.addCharge(user, c);
		m.eventList.addTremble(m, 0, frame * 2);
		m.eventList.addConstantVelocity(user, frame * 2, frame * 4, pushX, pushY);
		m.eventList.addActionCircle(early, frame * 2, frame * 4 );
		m.eventList.addConstantVelocity(user, frame * 4, frame * 6, pushX/2, pushY);
		m.eventList.addActionCircle(late,  frame * 4, frame * 6 );
		return m;
	}

	@Override
	public Move nSpecial() {
		int frames = 8;
		int frame = 25;
		if (user instanceof Basic.Bomb) {
			frame = 30;
		}
		int length = frames * frame;
		int hitboxSize = 27;

		Move m = new Move(user, length);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/basic/nspecial.png", frames, frame);
		Hitbox steamL = new Hitbox(user, 2.6f, 3.8f, 15, 60, -24, 0, 18, new SFX.MidHit());
		Hitbox steamR = new Hitbox(user, 2.6f, 3.8f, 15, 60,  28, 0, 18, new SFX.MidHit());
		Hitbox early = new Hitbox(user, 3.6f, 4.8f, 25, Hitbox.SAMURAI, 4, 0, hitboxSize, new SFX.HeavyHit());
		Hitbox late  = new Hitbox(user, 2.3f, 2.8f, 15, Hitbox.SAMURAI, 4, 0, hitboxSize, new SFX.MidHit());
		m.eventList.addTremble(m, 0, frame * 2);
		if (user instanceof Basic.Bomb) {
			m.eventList.addExplosion((Basic.Bomb)user, frame * 2);
		}
		if (!(user instanceof Basic.Bomb)){
			m.eventList.addGraphic(user, frame * 2, frame * 3, new Graphic.Fire(user, frame * 2));
			m.eventList.addActionCircle(early, frame * 2, frame * 3);
			m.eventList.addActionCircle(steamL, frame * 2, frame * 3);
			m.eventList.addActionCircle(steamR, frame * 2, frame * 3);
			m.eventList.addActionCircle(late, frame * 3, frame * 4);
		}
		return m;
	}

	@Override
	public Move uSpecial() {
		int frames = 5;
		int frame = 16;
		float pushY = 14.6f;

		Move m = new Move(user, frames * frame);
		m.setHelpless();
		m.setAnimation("sprites/fighters/basic/uspecial.png", frames, frame);
		Hitbox early = new Hitbox(user, 4.2f, 3.1f, 15, 80, 0, 0, 10, new SFX.MidHit());
		Hitbox late  = new Hitbox(user, 2.5f, 2.5f, 10, Hitbox.SAMURAI, 0, 0, 10, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addTremble(m, 0, frame * 2);
		m.eventList.addConstantVelocity(user, 2, frame * 2, 0, 0);
		m.eventList.addConstantVelocity(user, frame*2, frame * 2 + 6, Action.ChangeVelocity.noChange, pushY);
		m.eventList.addGraphic(user, frame * 2, frame * 3, new Graphic.Steam(user, frame));
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
		m.setAnimation("sprites/fighters/basic/sgetup.png", 2, 16);
		m.eventList.addConstantVelocity(user, 2, 20, -3, Action.ChangeVelocity.noChange);
		return m;
	}

	@Override
	public Move dodge() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/basic/ngetup.png", 1, 1);
		return m;
	}

	@Override
	public Move land() {
		Move m = new Move(user, 8);
		if (null != user.getPrevMove())
			if (user.getPrevMove().id == MoveList.aerial) m = new Move(user, 16);
		m.setAnimation("sprites/fighters/basic/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move skid() {
		Move m = new Move(user, 12);
		m.setAnimation("sprites/fighters/basic/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move taunt() {
		return new Move(user, 60);
	}

}
