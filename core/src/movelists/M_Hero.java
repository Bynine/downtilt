package movelists;

import java.util.Arrays;

import main.SFX;
import moves.Action;
import moves.ActionCircleGroup;
import moves.Effect;
import moves.Effect.ConstantVelocity;
import moves.Grabbox;
import moves.Hitbox;
import moves.Hitbox.Property;
import moves.Move;
import moves.Effect.Charge;
import entities.Entity.Direction;
import entities.Fighter;
import entities.Graphic;
import entities.Hurlable;
import entities.Hittable.HitstunType;

public class M_Hero extends MoveList_Advanced{

	public M_Hero(Fighter user) {
		super(user);
	}

	/* WEAK ATTACKS */

	public Move nWeak() {
		Move m = new Move(user, 16);
		m.setHurtBox(25, 50, 0, Move.HURTBOXNOTSET);
		m.setAnimation("sprites/fighters/bomber/nweak.png", 1, 1);
		m.setStopsInAir();
		Hitbox h1 = new Hitbox(user, 3.2f, 0.6f, 6, 84, 24, 0, 14, new SFX.MidHit());
		Hitbox h2 = new Hitbox(user, 3.2f, 0.6f, 6, 84, 8, 4, 6, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(h1, h2));
		m.eventList.addActionCircle(h1, 2, 7);
		m.eventList.addActionCircle(h2, 2, 7);
		return m;
	}

	public Move uWeak() {
		int frame = 8;
		int frames = 3;

		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/uweak.png", frames, frame);
		m.setHurtBox(25, 45, -8, Move.HURTBOXNOTSET);
		m.setStopsInAir();
		Hitbox swing = new Hitbox(user, 4.2f, 0.6f, 5, 90, 9,  5, 16, new SFX.LightHit());
		Hitbox punch = new Hitbox(user, 5.4f, 0.6f, 6, 85, 0, 27, 14, new SFX.MidHit());
		m.eventList.addActionCircle(swing, frame, frame * 2);
		m.eventList.addActionCircle(punch, frame + 2, frame * 2 + 2);
		return m;
	}

	public Move dWeak() {
		int frame = 8;
		int frames = 3;

		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/dweak.png", frames, frame);
		m.setHurtBox(30, 40, -8, Move.HURTBOXNOTSET);
		m.setStopsInAir();
		Hitbox inner = new Hitbox(user, 5.2f, 0.7f, 6, 82,  2, -8, 12, new SFX.MidHit());
		Hitbox midd =  new Hitbox(user, 5.1f, 0.8f, 7, 88, 13,-11, 10, new SFX.MidHit());
		Hitbox outer = new Hitbox(user, 5.0f, 0.9f, 8, 96, 27,-11, 10, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(inner, midd, outer));
		m.eventList.addActionCircle(inner, frame * 1, frame * 2);
		m.eventList.addActionCircle(midd,  frame * 1, frame * 2);
		m.eventList.addActionCircle(outer, frame * 1, frame * 2);
		return m;
	}

	public Move sWeak() {
		int frame = 9;
		int frames = 3;

		Move m = new Move(user, 27);
		m.setAnimation("sprites/fighters/bomber/sweak.png", frames, frame);
		m.setHurtBox(30, 45, -8, Move.HURTBOXNOTSET);
		m.setStopsInAir();
		Hitbox inner =	new Hitbox(user, 2.5f, 2.8f,  9, 80,  3, 0, 10, new SFX.MidHit());
		Hitbox outer =	new Hitbox(user, 2.6f, 2.8f, 10, 80, 10, 0, 10, new SFX.MidHit());
		Hitbox fire =	new Hitbox(user, 3.2f, 1.0f,  8, 90, 22, 0, 13, new SFX.SharpHit());
		Hitbox inner2 = new Hitbox(user, 1.8f, 1.0f,  5, 60,  6, 0, 10, new SFX.LightHit());
		Hitbox outer2 = new Hitbox(user, 2.0f, 1.0f,  6, 60, 14, 0, 10, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(inner, outer, fire, inner2, outer2));
		m.eventList.addActionCircle(inner, frame, frame * 2);
		m.eventList.addActionCircle(outer, frame, frame * 2);
		m.eventList.addActionCircle(fire, frame, frame * 2);
		m.eventList.addActionCircle(inner2,  (frame * 2), (frame * 2) + 4);
		m.eventList.addActionCircle(outer2,  (frame * 2), (frame * 2) + 4);
		return m;
	}

	public Move slide() { 
		Move m = new Move(user, 32);
		if (user.speedActive()) m = new Move(user, 20);
		m.dontTurn();
		m.setHurtBox(40, 25, -8, Move.HURTBOXNOTSET);
		m.setAnimation("sprites/fighters/bomber/slideattack.png", 2, 16);

		Hitbox early1 = new Hitbox(user, 6.6f, 1.7f, 10,  75, -4, -4, 16, new SFX.MidHit());
		Hitbox early2 = new Hitbox(user, 6.6f, 1.7f, 10,  75, 24, -6, 12, new SFX.MidHit());
		Hitbox late1  = new Hitbox(user, 5.0f, 1.0f,  7, 100, -4, -8, 12, new SFX.LightHit());
		Hitbox late2  = new Hitbox(user, 5.0f, 1.0f,  7, 100, 24, -4,  8, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		m.eventList.addConstantVelocity(user, 4, 16, user.getSpeedMod() * 9, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(early1, 3, 10);
		m.eventList.addActionCircle(early2, 3, 10);
		m.eventList.addActionCircle(late1, 11, 20);
		m.eventList.addActionCircle(late2, 11, 20);
		return m;
	}

	/* CHARGE ATTACKS */

	public Move nCharge() {
		int frame = 9;
		int frames = 6;

		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/scharge.png", frames, frame);
		m.setHurtBox(25, 50, -8, Move.HURTBOXNOTSET);
		m.setStopsInAir();
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		Hitbox early = new Hitbox(user, 4.0f, 3.3f, 18, Hitbox.SAMURAI, 18, 2, 22, new SFX.MeatyHit(), c);
		Hitbox late  = new Hitbox(user, 3.0f, 2.0f, 12, Hitbox.SAMURAI, 19, 2, 20, new SFX.MidHit(), c);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addCharge(user, c);
		m.eventList.addTremble(m, 0, 4);
		m.eventList.addConstantVelocity(user, frame * 2, frame * 3, user.getSpeedMod() * 8, 0);
		m.eventList.addActionCircle(early, frame * 2, frame * 3);
		m.eventList.addActionCircle(late, frame * 3, frame * 4);
		return m;
	}

	public Move uCharge() {
		int frame = 15;
		int frames = 3;

		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/ucharge.png", frames, frame);
		m.setHurtBox(25, 50, 0, Move.HURTBOXNOTSET);
		m.setStopsInAir();
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 5.5f, 3.8f, 17, 95, 18,-22, 16, new SFX.MeatyHit(), c);
		Hitbox h2 = new Hitbox(user, 4.5f, 3.2f, 15, 90, 18, 21, 16, new SFX.HeavyHit(), c);
		Hitbox h3 = new Hitbox(user, 4.0f, 3.0f, 13, 85,  0, 30, 16, new SFX.HeavyHit(), c);
		Hitbox h4 = new Hitbox(user, 3.4f, 2.0f, 12, 70,-18, 21, 16, new SFX.MidHit(), 	 c);
		Hitbox h5 = new Hitbox(user, 2.5f, 1.5f, 10, 30,-18,-16, 16, new SFX.MidHit(),	 c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4, h5));
		m.eventList.addCharge(user, c);
		m.eventList.addTremble(m, 0, 4);
		m.eventList.addActionCircle(h1, frame + 0, frame + 2);
		m.eventList.addActionCircle(h2, frame + 2, frame + 4);
		m.eventList.addActionCircle(h3, frame + 4, frame + 6);
		m.eventList.addActionCircle(h4, frame + 6, frame + 8);
		m.eventList.addActionCircle(h5, frame + 8, frame +10);
		return m;
	}

	public Move dCharge() {
		int frame = 12;
		int frames = 4;

		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/dcharge.png", frames, frame);
		m.setHurtBox(20, 50, 0, Move.HURTBOXNOTSET);
		m.setStopsInAir();
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		m.eventList.addCharge(user, c);
		m.eventList.addTremble(m, 0, 4);

		int x = 18;
		int y = -14;
		int size = 14;
		int earlyAngle = 273;
		int lateAngle = 110;
		Hitbox earlyL = new Hitbox(user, 5.0f, 3.8f, 15, earlyAngle,  x,  y, size,		new SFX.MeatyHit(), c);
		Hitbox earlyR = new Hitbox(user, 5.0f, 3.8f, 15, earlyAngle, -x,  y, size,		new SFX.MeatyHit(), c);
		Hitbox lateL =  new Hitbox(user, 3.5f, 2.0f, 12, lateAngle,   x,  y, size - 1,	new SFX.MidHit(), c);
		Hitbox lateR =  new Hitbox(user, 3.5f, 2.0f, 12, lateAngle,  -x,  y, size - 1,	new SFX.MidHit(), c);
		new ActionCircleGroup(Arrays.asList(earlyL, lateL, earlyR, lateR));
		m.eventList.addSound(new SFX.Break(), frame * 2);
		m.eventList.addActionCircle(earlyL, frame*2, frame*3);
		m.eventList.addActionCircle(earlyR, frame*2, frame*3);
		m.eventList.addActionCircle(lateL, frame*3, frame*4);
		m.eventList.addActionCircle(lateR, frame*3, frame*4);
		return m;
	}

	/* AIR ATTACKS */

	public Move nAir() {
		Move m = new Move(user, 27);
		m.setAnimation("sprites/fighters/bomber/nair.png", 1, 14);
		Hitbox earlyBody = new Hitbox(user, 4.0f, 1.3f, 10, 75, -10, 0, 17, new SFX.MidHit());
		Hitbox earlyFoot = new Hitbox(user, 4.0f, 1.2f, 11, 90, 20, -6, 15, new SFX.MidHit());
		Hitbox lateBody = new Hitbox(user,  2.0f, 1.0f, 7, 80, -10, 0, 13, new SFX.LightHit());
		Hitbox lateFoot = new Hitbox(user,  2.0f, 1.0f, 8, 90, 20, -6, 11, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(earlyBody, earlyFoot, lateBody, lateFoot));
		m.eventList.addActionCircle(earlyBody, 3, 8);
		m.eventList.addActionCircle(earlyFoot, 3, 8);
		m.eventList.addActionCircle(lateBody, 9, 24);
		m.eventList.addActionCircle(lateFoot, 9, 24);
		return m;
	}

	public Move uAir() {
		Move m = new Move(user, 27);
		m.setAnimation("sprites/fighters/bomber/uair.png", 3, 8);
		Hitbox h1top =	new Hitbox(user, 0.5f, 0.0f, 3,270, 0, 20, 15, new SFX.LightHit());
		Hitbox h1bott =	new Hitbox(user, 2.8f, 0.0f, 3, 90, 0,  0, 16, new SFX.LightHit());
		Hitbox h2top = 	new Hitbox(user, 2.2f, 4.1f, 8, 90, 0, 24, 18, new SFX.MidHit());
		Hitbox h2bott =	new Hitbox(user, 2.2f, 4.1f, 8, 90, 0,  4, 20, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(h1top, h1bott));
		new ActionCircleGroup(Arrays.asList(h2top, h2bott));
		m.eventList.addActionCircle(h1top,  8, 13);
		m.eventList.addActionCircle(h1bott, 8, 13);
		m.eventList.addActionCircle(h2top,  15, 19);
		m.eventList.addActionCircle(h2bott, 15, 19);
		return m;
	}

	public Move dAir() {
		int frames = 5;
		int frame = 6;
		int refresh = 4;
		int damage = 2;
		float kbg = 0.3f;

		Move m = new Move(user, frames * frame);
		m.setHurtBox(25, 50, 0, Move.HURTBOXNOTSET);
		m.setAnimation("sprites/fighters/bomber/dair.png", frames, frame);
		Hitbox upper = new Hitbox(user, 0.8f, kbg, damage, 85, 0,	0, 20, new SFX.LightHit());
		Hitbox lower = new Hitbox(user, 2.6f, kbg, damage, 95,  6,-12, 20, new SFX.LightHit());
		upper.setRefresh(refresh);
		lower.setRefresh(refresh);
		int end = (frame * frames) - 2;
		m.eventList.addActionCircle(upper, frame, end);
		m.eventList.addActionCircle(lower, frame, end);
		return m;
	}

	public Move fAir() {
		int frame = 8;
		int frames = 4;

		Move m = new Move(user, frame * frames);
		m.setHurtBox(35, 35, -10, Move.HURTBOXNOTSET);
		m.setAnimation("sprites/fighters/bomber/fair.png", frames, frame);
		Hitbox early1 = new Hitbox(user, 4.1f, 2.8f, 14,  48,  16,  -4, 14, new SFX.MeatyHit());
		Hitbox early2 = new Hitbox(user, 4.1f, 2.8f, 14,  58,   3,  13, 12, new SFX.MeatyHit());
		Hitbox spike = new Hitbox(user,  4.1f, 2.8f, 20, 290,  12, -22, 14, new SFX.HeavyHit());
		Hitbox late =  new Hitbox(user,  3.1f, 1.0f,  8,  75,  18, -10, 15, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, spike, late));
		spike.setProperty(Property.ELECTRIC);
		m.eventList.addActionCircle(early1, frame*2, frame*3);
		m.eventList.addActionCircle(early2, frame*2, frame*3);
		m.eventList.addActionCircle(spike, frame*2, frame*2 + 4);
		m.eventList.addActionCircle(late, frame*3, frame*4);
		return m;
	}

	public Move bAir() {
		int frame = 9;
		int frames = 3;

		Move m = new Move(user, frame * frames);
		m.setHurtBox(25, 50, 10, Move.HURTBOXNOTSET);
		m.setAnimation("sprites/fighters/bomber/bair.png", frames, frame);
		Hitbox fist1 = new Hitbox(user, 3.1f, 2.6f,  13, 62, -20, 10, 18, new SFX.HeavyHit());
		Hitbox body1 = new Hitbox(user, 2.5f, 1.5f,  11, 78,  -4,  4, 20, new SFX.MidHit());
		Hitbox fist2 = new Hitbox(user, 2.0f, 0.5f, 10,  30, -18, 11, 12, new SFX.MidHit());
		Hitbox body2 = new Hitbox(user, 1.5f, 0.5f,  9,  40,  -2,  7, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(fist1, body1, fist2, body2));
		m.eventList.addActionCircle(fist1, frame, frame*2);
		m.eventList.addActionCircle(body1, frame, frame*2);
		m.eventList.addActionCircle(fist2, frame*2, frame*3);
		m.eventList.addActionCircle(body2, frame*2, frame*3);
		return m;
	}

	/* SPECIAL ATTACKS */

	public Move uSpecial() {
		int start = 20;
		int end = start + 16;
		Move m = new Move(user, end + 1);
		m.setAnimation("sprites/fighters/bomber/uspecialu.png", 1, 1);
		m.setHeroUSpecial();

		Hitbox push = new Hitbox.AngleHitbox(user, 7.6f, 0.0f, 2, 0, 0, 24, new SFX.LightHit());
		push.setMovesAheadMod(1);
		push.setRefresh(2);
		push.setNoReverse();

		Hitbox fini = new Hitbox.AngleHitbox(user, 3.0f, 3.2f, 10, 0, 0, 28, new SFX.MidHit());
		fini.setMovesAheadMod(1);
		fini.setNoReverse();
		fini.setProperty(Property.ELECTRIC);

		m.eventList.addConstantVelocity(user, 0, start, ConstantVelocity.noChange, 0);
		m.eventList.addGenerateGraphic(start, end, 4, user, Graphic.SmokeTrail.class);
		m.eventList.addUseSpecial(user, start - 1, -1);
		m.eventList.addConstantAngledVelocity(user, start, end, 11 * user.getAirMod());
		m.eventList.addActionCircle(push, start, end - 4);
		m.eventList.addActionCircle(fini, end - 4, end - 2);
		m.eventList.addVelocityChange(user, end, 0, 0);
		return m;
	}

	public Move dSpecial() {
		int frames = 2;
		int frame = 20;

		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/bomber/dspecial.png", frames, frame);
		m.setContinueOnLanding();
		m.eventList.addConstantVelocity(user, 0, frame * 2, 0, -user.getGravity());
		m.eventList.addUseSpecial(user, frame - 1, -6);
		m.eventList.addSlow(frame, 300);
		return m;
	}

	public Move nSpecial() {
		int frame = 10;
		int frames = 3;

		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/fairthrow.png", frames, frame);
		m.setContinueOnLanding();
		m.eventList.addUseSpecial(user, frame - 1, -2);
		m.eventList.addNewEntity(frame, user, 
				(new Hurlable.Grenade(user, user.getPosition().x, user.getPosition().y)), user.direct() * 6, 4, user.direct()*16, 16);
		return m;
	}

	/* THROWS */

	int throwSize = 20;
	public Move fThrow(){
		int frame = 8;
		int frames = 3;

		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/fthrow.png", frames, frame);
		m.dontTurn();
		Hitbox thro  =  new Hitbox(user, 4.2f, 0.0f,  1, 60,  8, 0, throwSize, new SFX.None());
		Hitbox swing1 = new Hitbox(user, 5.4f, 1.0f, 14, 30,  8, 0, 20, new SFX.MeatyHit());
		Hitbox swing2 = new Hitbox(user, 5.4f, 1.0f, 14, 30, 28, 0, 20, new SFX.MeatyHit());
		thro.setNoReverse();
		thro.setProperty(Property.STUN);
		swing1.setNoReverse();
		swing2.setNoReverse();
		swing1.setHitstunType(Fighter.HitstunType.SUPER);
		swing2.setHitstunType(Fighter.HitstunType.SUPER);
		new ActionCircleGroup(Arrays.asList(swing1, swing2));
		m.eventList.addActionCircle(thro, 0, 1);
		m.eventList.addActionCircle(swing1, 12, 18);
		m.eventList.addActionCircle(swing2, 12, 18);
		return m;
	}

	public Move bThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/bomber/bthrow.png", 3, 8);
		m.dontTurn();
		Hitbox thro = new Hitbox(user, 5.5f, 0.0f,  2, 160, 8, 0, throwSize, new SFX.LightHit());
		Hitbox kick = new Hitbox(user, 4.1f, 1.1f, 12, 150, -24, 0, throwSize, new SFX.MeatyHit());
		thro.setNoReverse();
		kick.setNoReverse();
		kick.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(thro, 0, 4);
		m.eventList.addActionCircle(kick, 8, 16);
		return m;
	}

	public Move uThrow(){
		Move m = new Move(user, 14);
		m.dontTurn();
		m.setAnimation("sprites/fighters/bomber/uthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 6.1f, 1.8f, 12, 86, 8, 0, throwSize, new SFX.MeatyHit());
		h1.setNoReverse();
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 8);
		return m;
	}

	public Move dThrow(){
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/bomber/dthrow.png", 1, 1);
		m.dontTurn();
		Hitbox up = 	new Hitbox(user, 4.8f, 0.0f, 8,  82, 8, -8, throwSize, new SFX.MeatyHit());
		up.setNoReverse();
		m.eventList.addActionCircle(up, 0, 8);
		return m;
	}

	public Move fAirThrow(){
		int frame = 10;
		int frames = 3;

		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/fairthrow.png", frames, frame);
		Hitbox toss= new Hitbox(user, 2.0f, 0.0f,  0, 50, 6, 0, throwSize, new SFX.None());
		Hitbox hit = new Hitbox(user, 4.4f, 1.4f, 14,  0, 14, 0, throwSize, new SFX.MeatyHit());
		hit.setNoReverse();
		hit.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addConstantVelocity(user, 0, frame, Action.ChangeVelocity.noChange, 0);
		m.eventList.addActionCircle(toss, 0,  frame);
		m.eventList.addActionCircle(hit,  frame, frame*2);
		return m;
	}

	public Move bAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/bomber/fjump.png", 1, 1);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 4.2f, 1.4f, 14, 150, 24, -12, throwSize, new SFX.HeavyHit());
		h1.setNoReverse();
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addVelocityChange(user, 5, 4, 4);
		m.eventList.addActionCircle(h1, 2, 8);
		return m;
	}

	public Move uAirThrow(){
		Move m = new Move(user, 8);
		m.setAnimation("sprites/fighters/bomber/uairthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 8.0f, 0.4f, 10, 90, 8, 0, throwSize, new SFX.MidHit());
		h1.setNoReverse();
		m.eventList.addActionCircle(h1, 0, 8);
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 7);
		return m;
	}

	public Move dAirThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/bomber/dthrow.png", 1, 1);
		Hitbox down = new Hitbox(user, 3.5f, 1.5f, 14, 270, 8, 0, throwSize, new SFX.MeatyHit());
		down.setNoReverse();
		down.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(down, 0, 8);
		m.eventList.addVelocityChange(user, 4, -4.0f, 5.0f);
		return m;
	}

	/* GRABS */

	public Move grab() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/bomber/grab.png", 1, 1);
		m.setHurtBox(25, 50, -8, Move.HURTBOXNOTSET);
		m.setStopsInAir();
		Grabbox g1 = new Grabbox(user, 22, 4, 13);
		Grabbox g2 = new Grabbox(user, 8, 8, 4);
		new ActionCircleGroup(Arrays.asList(g1, g2));
		m.eventList.addActionCircle(g1, 4, 7);
		m.eventList.addActionCircle(g2, 4, 7);
		return m;
	}

	public Move dashGrab() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/bomber/dashgrab.png", 1, 1);
		m.eventList.addVelocityChange(user, 4, 6, Action.ChangeVelocity.noChange);
		m.setHurtBox(25, 50, -8, Move.HURTBOXNOTSET);
		Grabbox g1 = new Grabbox(user, 18, 0, 13);
		m.eventList.addActionCircle(g1, 4, 7);
		return m;
	}

	public Move airGrab() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/bomber/airgrab.png", 1, 1);
		m.setHurtBox(25, 50, -8, Move.HURTBOXNOTSET);
		Grabbox g1 = new Grabbox(user, 22, 4, 13);
		Grabbox g2 = new Grabbox(user, 8, 8, 4);
		new ActionCircleGroup(Arrays.asList(g1, g2));
		m.eventList.addActionCircle(g1, 4, 7);
		m.eventList.addActionCircle(g2, 4, 7);
		return m;
	}

	/* DODGES */

	protected float boost = 10.6f;
	public Move airDodge(){
		int endBoost = 8;
		int end = 20;

		Move m = new Move(user, end);
		m.setHelpless();
		m.dontTurn();
		boolean airDodgeBack = (user.getStickX() < 0 && user.getDirection() == Direction.RIGHT) || (user.getStickX() < 0 && user.getDirection() == Direction.RIGHT);
		if (airDodgeBack) m.setAnimation("sprites/fighters/bomber/airdodgeb.png", 1, 1);
		else m.setAnimation("sprites/fighters/bomber/airdodgef.png", 1, 1);
		m.eventList.addInvincible(user, 2, 18);
		m.eventList.addConstantVelocity(user, 0, endBoost, user.direct() * user.getStickX() * boost, -user.getStickY() * boost);
		m.eventList.addConstantVelocity(user, endBoost, end, 0, 0);
		return m;
	}

	public Move dodge(){
		Move m = new Move(user, 24);
		m.dontTurn();
		m.setAnimation("sprites/fighters/bomber/dodge.png", 1, 1);
		m.eventList.addInvincible(user, 1, 20);
		return m;
	}

	public Move getUpAttack() {
		return dodge();
//		Move m = new Move(user, 33);
//		m.setAnimation("sprites/fighters/bomber/getupattack.png", 3, 11);
//		m.setHurtBox(25, 35, 0, Move.HURTBOXNOTSET);
//		m.eventList.addInvincible(user, 0, 10);
//		Hitbox front1 = new Hitbox(user, 5.0f, 0.5f, 12, Hitbox.SAMURAI,  20, -8, 16, new SFX.LightHit());
//		Hitbox front2 = new Hitbox(user, 5.0f, 0.5f, 14, Hitbox.SAMURAI,  44, -8, 12, new SFX.LightHit());
//		Hitbox back1 =  new Hitbox(user, 5.0f, 0.5f, 12, Hitbox.SAMURAI, -20, -8, 16, new SFX.LightHit());
//		Hitbox back2 =  new Hitbox(user, 5.0f, 0.5f, 14, Hitbox.SAMURAI, -44, -8, 12, new SFX.LightHit());
//		m.eventList.addActionCircle(front1, 11, 16);
//		m.eventList.addActionCircle(front2, 11, 16);
//		m.eventList.addActionCircle(back1, 22, 27);
//		m.eventList.addActionCircle(back2, 22, 27);
//		return m;
	}

	private final int rollLength = 20;
	private final int rollInvinc = 15;
	private final float rollHeight = 3.1f;
	private final float rollSpeed = -7.4f;
	public Move rollForward(){
		Move m = new Move(user, rollLength);
		m.dontTurn();
		user.flip();
		m.setAnimation("sprites/fighters/bomber/airdodgeb.png", 1, 1);
		m.eventList.addConstantVelocity(user, 1, 27, rollSpeed, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, rollHeight);
		m.eventList.addInvincible(user, 1, rollInvinc);
		return m;
	}

	public Move rollBack(){
		Move m = new Move(user, rollLength);
		m.dontTurn();
		m.setAnimation("sprites/fighters/bomber/airdodgeb.png", 1, 1);
		m.eventList.addConstantVelocity(user, 1, 27, rollSpeed, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, rollHeight);
		m.eventList.addInvincible(user, 1, rollInvinc);
		return m;
	}

	/* MISC */

	public Move land(){
		Move m = new Move(user, 4);
		if (null != user.getPrevMove() && !user.airActive()){
			switch(user.getPrevMove().id){
			case MoveList_Advanced.IDnair: m = new Move(user, 6); break;
			case MoveList_Advanced.IDfair: m = new Move(user, 12); break;
			case MoveList_Advanced.IDbair: m = new Move(user, 8); break;
			case MoveList_Advanced.IDuair: m = new Move(user, 8); break;
			default: break;
			}
		}
		m.setAnimation("sprites/fighters/bomber/land.png", 1, 1);
		if (null != user.getPrevMove() && user.getPrevMove().id == MoveList_Advanced.IDdair){
			m = new Move(user, 12); 
			if (user.airActive()) m = new Move(user, 3); 
			m.setAnimation("sprites/fighters/bomber/landdair.png", 1, 1);
			Hitbox finish = new Hitbox(user, 4.0f, 1.3f, 4, 85, 0, 0, 30, new SFX.MidHit());
			m.eventList.addActionCircle(finish, 0, 4);
		}
		m.dontTurn();
		return m;
	}

	public Move skid(){
		if (user.speedActive()) return new Move(user, 0);
		Move m = new Move(user, 15);
		m.dontTurn();
		m.setAnimation("sprites/fighters/bomber/skid.png", 1, 1);
		m.setStopsInAir();
		return m;
	}

	public Move taunt(){
		Move m = new Move(user, 56);
		m.setAnimation("sprites/fighters/bomber/taunt.png", 2, 12);
		m.setStopsInAir();
		return m;
	}

	public Move block(){
		Move m = new Move(user, 48);
		m.setAnimation("sprites/fighters/bomber/dodgebegin.png", 1, 1);
		m.setStopsInAir();
		m.eventList.addGuard(user, 0, 16);
		return m;
	}

	@Override
	public Move parry() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/bomber/parry.png", 1, 1);
		m.setHurtBox(25, 50, -4, Move.HURTBOXNOTSET);
		Hitbox h1 = new Hitbox.ParryHitbox(user, 5.2f, 1.6f, 20, 80, 30, 19, 0, 12, new SFX.MidHit());
		h1.setHitstunType(HitstunType.SUPER);
		h1.setIgnoreArmor();
		h1.setNoReverse();
		m.eventList.addActionCircle(h1, 10, 15);
		m.eventList.addInvincible(user, 0, 10);
		return m;
	}

	@Override
	public Move perfectParry() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/bomber/perfectparry.png", 1, 1);
		m.setHurtBox(25, 50, 0, Move.HURTBOXNOTSET);
		Hitbox h1 = new Hitbox.ParryHitbox(user, 6.0f, 3.5f, 24, 90, 20, 0, 0, 42, new SFX.HeavyHit());
		h1.setHitstunType(HitstunType.SUPER);
		h1.setIgnoreArmor();
		h1.setNoReverse();
		m.eventList.addSound(new SFX.Explode(), 0);
		m.eventList.addActionCircle(h1, 10, 20);
		m.eventList.addInvincible(user, 0, 30);
		return m;
	}

}
