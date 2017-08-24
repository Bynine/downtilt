package movelists;

import java.util.Arrays;

import main.GlobalRepo;
import main.SFX;
import moves.Action;
import moves.ActionCircleGroup;
import moves.Effect;
import moves.Hitbox;
import moves.Move;
import moves.Effect.Charge;
import entities.Fighter;
import entities.Graphic;
import entities.Heavy;
import entities.Hurlable;

public class M_Heavy extends MoveList {
	
	private final float chargeArmor = 7;

	public M_Heavy(Fighter user) {
		super(user);
	}

	@Override
	public Move nWeak() {
		int frames = 3;
		int frame = 40;
		int subFrame = frame/5;
		float bkb = 6.8f;
		float kbg = 2.4f;
		int damage = 20;

		Move m = new Move(user, frames * frame);
		m.setHurtBox(80, 80, 0, Move.HURTBOXNOTSET);
		m.setAnimation("sprites/fighters/heavy/nweak.png", frames, frame);
		Hitbox early1 = new Hitbox(user, bkb, kbg, damage, Hitbox.SAMURAI, 48, -24, 24, new SFX.HeavyHit());
		Hitbox early2 = new Hitbox(user, bkb, kbg, damage, Hitbox.SAMURAI, 42,  -2, 28, new SFX.HeavyHit());
		Hitbox early3 = new Hitbox(user, bkb, kbg, damage, Hitbox.SAMURAI, 32,   8, 24, new SFX.HeavyHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, early3));
		m.eventList.addActionCircle(early1, frame + (subFrame * 0), frame + (subFrame * 1));
		m.eventList.addActionCircle(early2, frame + (subFrame * 0), frame + (subFrame * 1));
		m.eventList.addActionCircle(early3, frame + (subFrame * 0), frame + (subFrame * 1));
		m.eventList.addArmor(m, frame * 1, frame * 3, -Heavy.HEAVY_ARMOR);
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
	public Move nCharge() { // jesus this move is huge
		int frames = 3;
		int frame = 60;
		final int dispX = 42;
		final int dispY = -30;
		final int hitboxSize = 29;
		final int hitboxDur = 10;
		final float bkb = 7.2f;
		final float kbg = 5.2f;
		final int damage = 30;
		final int angle = 280;
		
		final int groundSize = 16;
		final float groundbkb = 8.8f;
		final float groundDispY = 40;
		final int groundAngle = 300;
		
		final int swingSize = 20;
		final float swingDispX = 28;
		final float swingDispY = 2;
		final int swingAngle = 280;

		Move m = new Move(user, frames * frame);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/heavy/ncharge.png", frames, frame);
		m.setHurtBox(80, 100, 0, Move.HURTBOXNOTSET);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox leftPound =  new Hitbox(user, bkb, kbg, damage, angle, -dispX, dispY, hitboxSize, new SFX.MeatyHit(), c);
		Hitbox rightPound = new Hitbox(user, bkb, kbg, damage, angle,  dispX, dispY, hitboxSize, new SFX.MeatyHit(), c);
		Hitbox leftSwing =  new Hitbox(user, bkb, kbg, damage, swingAngle, -swingDispX, swingDispY, swingSize, new SFX.HeavyHit(), c);
		Hitbox rightSwing = new Hitbox(user, bkb, kbg, damage, swingAngle,  swingDispX, swingDispY, swingSize, new SFX.HeavyHit(), c);
		Hitbox rightGround1 = new Hitbox.QuakeHitbox(user, groundbkb, 0, damage/2, groundAngle, dispX*2, -groundDispY, groundSize, new SFX.HeavyHit());
		Hitbox rightGround2 = new Hitbox.QuakeHitbox(user, groundbkb, 0, damage/2, groundAngle, dispX*3, -groundDispY, groundSize, new SFX.HeavyHit());
		Hitbox leftGround1 = new Hitbox.QuakeHitbox(user, groundbkb, 0, damage/2, groundAngle, -dispX*2, -groundDispY, groundSize, new SFX.HeavyHit());
		Hitbox leftGround2 = new Hitbox.QuakeHitbox(user, groundbkb, 0, damage/2, groundAngle, -dispX*3, -groundDispY, groundSize, new SFX.HeavyHit());
		new ActionCircleGroup(Arrays.asList(leftPound, leftGround1, leftGround2, leftSwing));
		new ActionCircleGroup(Arrays.asList(rightPound, rightGround1, rightGround2, rightSwing));
		m.eventList.addCharge(user, c);
		m.eventList.addTremble(m, frame * 0, frame * 1);
		m.eventList.addArmor(m, frame/4, frame * 1, chargeArmor);
		m.eventList.addSound(new SFX.Break(), frame * 1);
		
		m.eventList.addGraphic(user, frame * 1, frame * 1 + hitboxDur/2, 
				new Graphic.Quake(user.getCenter().x - dispX * 2, user.getCenter().y -groundDispY, hitboxDur/2));
		m.eventList.addGraphic(user, frame * 1, frame * 1 + hitboxDur/2,
				new Graphic.Quake(user.getCenter().x - dispX * 3, user.getCenter().y -groundDispY, hitboxDur/2));
		m.eventList.addActionCircle(leftSwing, frame * 1, frame * 1 + hitboxDur);
		m.eventList.addActionCircle(leftGround1, frame * 1, frame * 1 + hitboxDur/2);
		m.eventList.addActionCircle(leftGround2, frame * 1, frame * 1 + hitboxDur/2);
		m.eventList.addActionCircle(leftPound, frame * 1, frame * 1 + hitboxDur);
		
		m.eventList.addGraphic(user, frame * 1, frame * 1 + hitboxDur/2, 
				new Graphic.Quake(user.getCenter().x + dispX * 2, user.getCenter().y -groundDispY, hitboxDur/2));
		m.eventList.addGraphic(user, frame * 1, frame * 1 + hitboxDur/2,
				new Graphic.Quake(user.getCenter().x + dispX * 3, user.getCenter().y -groundDispY, hitboxDur/2));
		m.eventList.addActionCircle(rightSwing, frame * 1, frame * 1 + hitboxDur);
		m.eventList.addActionCircle(rightGround1, frame * 1, frame * 1 + hitboxDur/2);
		m.eventList.addActionCircle(rightGround2, frame * 1, frame * 1 + hitboxDur/2);
		m.eventList.addActionCircle(rightPound, frame * 1, frame * 1 + hitboxDur);
		m.eventList.addArmor(m, frame * 1, frame * 3, -Heavy.HEAVY_ARMOR * 2);
		return m;
	}

	@Override
	public Move nSpecial() {
		int frames = 3;
		int frame = 60;

		Move m = new Move(user, frames * frame);
		m.setHurtBox(80, 80, 0, Move.HURTBOXNOTSET);
		m.setAnimation("sprites/fighters/heavy/nspecial.png", frames, frame);
		m.eventList.addTremble(m, frame * 0, frame * 1);
		m.eventList.addArmor(m, frame/4, frame * 1, chargeArmor);
		m.eventList.addNewEntity(frame,
				user, (new Hurlable.Boulder(user, GlobalRepo.GOODTEAM, user.getPosition().x, user.getPosition().y)),
				user.direct() * 8, 5
				);
		m.eventList.addArmor(m, frame * 1, frame * 3, -Heavy.HEAVY_ARMOR * 2);
		return m;
	}

	@Override
	public Move uSpecial() {
		int frame = 27;
		int effectivelyNoEnd = 20;
		float pushY = 19.5f;

		Move m = new Move(user, effectivelyNoEnd * frame);
		m.setHelpless();
		m.setAerial();
		m.setAnimationNoLoop("sprites/fighters/heavy/uspecial.png", 2, frame * 3);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox rise = new Hitbox(user, 6.2f, 2.1f, 20,  80, 0, 0, 30, new SFX.MidHit(), c);
		Hitbox crash  = new Hitbox(user, 7.5f, 3.5f, 40, 270, 0, -20, 30, new SFX.MeatyHit(), c);
		new ActionCircleGroup(Arrays.asList(rise, crash));
		m.eventList.addCharge(user, c);
		m.eventList.addTremble(m, frame * 0, frame * 2);
		m.eventList.addConstantVelocity(user, 0, frame * 2, 0, 0);
		m.eventList.addArmor(m, frame * 2, frame * 3, chargeArmor);
		m.eventList.addConstantVelocity(user, frame * 2, frame * 2 + 2, Action.ChangeVelocity.noChange, pushY);
		m.eventList.addActionCircle(rise, frame * 2, frame * 3 );
		m.eventList.addActionCircle(crash,  frame * 3, frame * 20 );
		m.eventList.addArmor(m, frame * 3, frame * 20, -Heavy.HEAVY_ARMOR * 2);
		return m;
	}

	@Override
	public Move rollForward() {
		user.flip();
		return roll();
	}

	@Override
	public Move rollBack() {
		return dodge();
	}

	private Move roll(){
		return dodge();
	}

	@Override
	public Move dodge() {
		Move m = new Move(user, 50);
		m.setAnimation("sprites/fighters/heavy/ngetup.png", 1, 1);
		return m;
	}

	@Override
	public Move land() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/heavy/crouch.png", 1, 1);
		if (null != user.getActiveMove())
			if (user.getActiveMove().id == MoveList.uspecial) {
				m = new Move(user, 60);
				m.setAnimation("sprites/fighters/heavy/landhead.png", 1, 1);
			}
		
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
