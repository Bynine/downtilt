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

public class M_Basic extends MoveList {

	public M_Basic(Fighter user) {
		super(user);
	}

	@Override
	public Move nWeak() {
		int length = 36;
		int frame = length/3;

		Move m = new Move(user, length);
		m.setAnimation("sprites/fighters/basic/nweak.png", 3, frame);
		Hitbox h1 = new Hitbox(user, 1.6f, 1.4f, 6, Hitbox.SAMURAI, 20, 4, 16, new SFX.LightHit());
		m.eventList.addActionCircle(h1, frame, frame + 4);
		return m;
	}

	@Override
	public Move slide() {
		int frames = 4;
		int frame = 15;
		int length = frames * frame;

		Move m = new Move(user, length);
		m.setContinueOnLanding();
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/basic/slide.png", frames, frame);
		Hitbox early = new Hitbox(user, 2.8f, 2.6f, 11, 40, 16, -12, 24, new SFX.MidHit());
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
		int end = 32;
		
		Move m = new Move(user, end);
		m.setAnimation("sprites/fighters/basic/nair.png", 6, 4);
		Hitbox early = new Hitbox(user, 2.2f, 1.6f, 9, Hitbox.SAMURAI, 0, 0, 22, new SFX.MidHit());
		Hitbox late  = new Hitbox(user, 1.6f, 0.6f, 6, Hitbox.SAMURAI, 0, 0, 22, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addVelocityChange(user, 8, Action.ChangeVelocity.noChange, 1);
		m.eventList.addActionCircle(early, 8, 13);
		m.eventList.addActionCircle(late, 14, 22);
		return m;
	}

	@Override
	public Move nCharge() {
		int frames = 5;
		int frame = 15;
		float pushX = 6.5f;
		float pushY = 4.0f;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/basic/ncharge.png", frames, frame);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox early = new Hitbox(user, 3.2f, 3.1f, 13, Hitbox.SAMURAI, 0, 0, 22, new SFX.HeavyHit(), c);
		Hitbox late  = new Hitbox(user, 2.5f, 2.5f,  9, Hitbox.SAMURAI, 0, 0, 22, new SFX.MidHit(), c);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addCharge(user, c);
		m.eventList.addConstantVelocity(user, frame * 2, (frame * 2) + 2, pushX, pushY);
		m.eventList.addActionCircle(early, frame * 2, frame * 3 );
		m.eventList.addActionCircle(late,  frame * 3, frame * 4 );
		return m;
	}

	@Override
	public Move nSpecial() {
		int frames = 8;
		int frame = 22;
		if (user instanceof Basic.Bomb) {
			frame = 30;
		}
		int length = frames * frame;
		int hitboxSize = 26;

		Move m = new Move(user, length);
		m.setAnimation("sprites/fighters/basic/nspecial.png", frames, frame);
		Hitbox early = new Hitbox(user, 2.6f, 4.2f, 16, Hitbox.SAMURAI, 4, 0, hitboxSize, new SFX.HeavyHit());
		Hitbox late  = new Hitbox(user, 2.3f, 2.8f, 12, Hitbox.SAMURAI, 4, 0, hitboxSize, new SFX.MidHit());
		if (user instanceof Basic.Bomb) {
			m.eventList.addExplosion((Basic.Bomb)user, frame * 2);
		}
		if (!(user instanceof Basic.Bomb)){
			m.eventList.addActionCircle(early, frame * 2, frame * 3);
			m.eventList.addActionCircle(late, frame * 3, frame * 4);
		}
		return m;
	}

	@Override
	public Move uSpecial() {
		Move m = new Move(user, 60);
		m.setHelpless();
		m.setAnimation("sprites/fighters/basic/uspecial.png", 1, 1);
		m.eventList.addConstantVelocity(user, 6, 70, Action.ChangeVelocity.noChange, 4);
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
	
	public Move block(){
		Move m = new Move(user, 60);
		m.setAnimation("sprites/fighters/basic/block.png", 1, 1);
		m.eventList.addGuard(user, 5, 55);
		return m;
	}

}
