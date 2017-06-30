package movelists;

import java.util.Arrays;

import main.GlobalRepo;
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
import entities.Projectile;

public class M_Hero extends MoveList_Advanced{
	
	public M_Hero(Fighter user) {
		super(user);
	}

	/* WEAK ATTACKS */

	public Move nWeak() {
		Move m = new Move(user, 13);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 20, 50));
		m.setAnimation("sprites/fighters/bomber/nweak.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 3.1f, 0.6f, 5, 84, 18, 0, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 2, 5);
		return m;
	}

	public Move uWeak() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/bomber/uweak.png", 3, 7);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 30, 60));
		Hitbox swing = new Hitbox(user, 3.0f, 1.0f, 4, 95, 6, 8, 22, new SFX.LightHit());
		Hitbox foot =  new Hitbox(user, 3.0f, 1.0f, 5, 85, 0, 34, 14, new SFX.MidHit());
		m.eventList.addActionCircle(swing, 6, 12);
		m.eventList.addActionCircle(foot, 8, 15);
		return m;
	}

	public Move dWeak() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/bomber/dweak.png", 2, 10);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 24, 30));
		Hitbox inner = new Hitbox(user, 4, 0.7f, 6, 82, -4, -8, 12, new SFX.MidHit());
		Hitbox midd =  new Hitbox(user, 4, 0.8f, 7, 88, 10,-11, 10, new SFX.MidHit());
		Hitbox outer = new Hitbox(user, 4, 0.9f, 8, 96, 27,-11, 10, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(inner, midd, outer));
		m.eventList.addActionCircle(inner, 10, 19);
		m.eventList.addActionCircle(midd,  10, 19);
		m.eventList.addActionCircle(outer, 10, 19);
		return m;
	}
	
	public Move sWeak() {
		int frame = 9;
		int frames = 3;
		
		Move m = new Move(user, 27);
		m.setAnimation("sprites/fighters/bomber/sweak.png", frames, frame);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 18, 50));
		Hitbox inner =	new Hitbox(user, 2.0f, 2.7f,  9, 60,  8, 0, 12, new SFX.MidHit());
		Hitbox outer =	new Hitbox(user, 2.2f, 2.7f, 10, 60, 18, 0, 12, new SFX.MidHit());
		Hitbox inner2 = new Hitbox(user, 1.8f, 1.0f,  5, 60,  6, 0, 10, new SFX.LightHit());
		Hitbox outer2 = new Hitbox(user, 2.0f, 1.0f,  6, 60, 14, 0, 10, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(inner, outer, inner2, outer2));
		m.eventList.addActionCircle(inner, frame, frame * 2);
		m.eventList.addActionCircle(outer, frame, frame * 2);
		m.eventList.addActionCircle(inner2,  (frame * 2), (frame * 2) + 4);
		m.eventList.addActionCircle(outer2,  (frame * 2), (frame * 2) + 4);
		return m;
	}

	public Move slide() { 
		Move m = new Move(user, 32);
		m.dontTurn();
		m.setAnimation("sprites/fighters/bomber/slideattack.png", 2, 16);
		Hitbox early = new Hitbox(user, 4.0f, 2.4f, 10, 65, 12, -4, 12, new SFX.MidHit());
		Hitbox late  = new Hitbox(user, 3.0f, 1.0f, 07, 90, 16, -4,  8, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addConstantVelocity(user, 4, 12, 9, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(early, 3, 10);
		m.eventList.addActionCircle(late, 11, 20);
		return m;
	}

	/* CHARGE ATTACKS */

	public Move nCharge() {
		int frame = 10;
		int frames = 4;
		
		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/scharge.png", frames, frame);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 30, 60));
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 4.2f, 3.3f, 18, Hitbox.SAMURAI, 14, 0, 20, new SFX.MeatyHit(), c);
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(h1, frame * 2, frame * 3);
		return m;
	}

	public Move uCharge() {
		int frame = 13;
		int frames = 3;
		
		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/ucharge.png", frames, frame);
		m.setHurtBox(GlobalRepo.makeHurtBox(user, 30, 50));
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 5.5f, 4.2f, 17, 90, 18,-22, 20, new SFX.HeavyHit(), 	 c);
		Hitbox h2 = new Hitbox(user, 5.0f, 3.6f, 15, 87, 18, 21, 16, new SFX.HeavyHit(), 	 c);
		Hitbox h3 = new Hitbox(user, 4.5f, 3.0f, 13, 84,  0, 26, 16, new SFX.MidHit(), 	 c);
		Hitbox h4 = new Hitbox(user, 3.5f, 2.0f, 11, 70,-18, 21, 16, new SFX.MidHit(), 	 c);
		Hitbox h5 = new Hitbox(user, 2.0f, 1.0f,  9, 30,-18,-16, 16, new SFX.LightHit(),	 c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4, h5));
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(h1, frame + 0, frame + 2);
		m.eventList.addActionCircle(h2, frame + 2, frame + 4);
		m.eventList.addActionCircle(h3, frame + 4, frame + 6);
		m.eventList.addActionCircle(h4, frame + 6, frame + 8);
		m.eventList.addActionCircle(h5, frame + 8, frame +10);
		return m;
	}

	public Move dCharge() {
		int frame = 10;
		int frames = 4;
		
		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/dcharge.png", frames, frame);
		m.setHurtBox(GlobalRepo.makeHurtBox(user, 30, 30));
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		m.eventList.addCharge(user, c);
		Hitbox inner1 = new Hitbox(user, 5.0f, 1.0f, 12, 80,  16,  -7, 14, new SFX.MidHit(),		c);
		Hitbox inner2 = new Hitbox(user, 5.0f, 1.0f, 12, 80, -16,  -7, 14, new SFX.MidHit(),		c);
		Hitbox foot1  = new Hitbox(user, 4.0f, 2.4f, 14, 22,  36, -10, 10, new SFX.HeavyHit(),  	c);
		Hitbox foot2  = new Hitbox(user, 4.0f, 2.4f, 14, 22, -36, -10, 10, new SFX.HeavyHit(),  	c);
		new ActionCircleGroup(Arrays.asList(inner1, foot1));
		new ActionCircleGroup(Arrays.asList(inner2, foot2));
		m.eventList.addActionCircle(foot1, frame, frame*2);
		m.eventList.addActionCircle(inner1, frame, frame*2);
		m.eventList.addActionCircle(foot2, frame*3, frame*4);
		m.eventList.addActionCircle(inner2, frame*3, frame*4);
		return m;
	}

	/* AIR ATTACKS */

	public Move nAir() {
		Move m = new Move(user, 27);
		m.setAnimation("sprites/fighters/bomber/nair.png", 2, 14);
		Hitbox earlyBody = new Hitbox(user, 4.0f, 1.3f, 10, 75, -10, 0, 16, new SFX.MidHit());
		Hitbox earlyFoot = new Hitbox(user, 4.0f, 1.2f, 11, 90, 20, -6, 14, new SFX.MidHit());
		Hitbox lateBody = new Hitbox(user,  2.0f, 1.0f, 7, 80, -10, 0, 12, new SFX.LightHit());
		Hitbox lateFoot = new Hitbox(user,  2.0f, 1.0f, 8, 90, 20, -6, 10, new SFX.LightHit());
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
		Hitbox h1top =	new Hitbox(user, 2.5f, 0.0f, 3, 90, 0, 20, 15, new SFX.LightHit());
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
		int damage = 3;
		float bkb = 2.1f;
		float kbg = 0.4f;
		
		Move m = new Move(user, frames * frame);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 24, 60));
		m.setAnimation("sprites/fighters/bomber/dair.png", frames, frame);
		Hitbox h1 =	new Hitbox(user, bkb, kbg, damage, 80, 0,	0, 	 20, new SFX.LightHit());
		Hitbox h2 =	new Hitbox(user, bkb, kbg, damage, 80, 6, -12,   20, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(h1, h2));
		h1.setRefresh(refresh);
		h2.setRefresh(refresh);
		m.eventList.addActionCircle(h1, frame, frame * frames);
		m.eventList.addActionCircle(h2, frame, frame * frames);
		return m;
	}

	public Move fAir() {
		int frame = 8;
		int frames = 4;
		
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/bomber/fair.png", frames, frame);
		Hitbox early = new Hitbox(user, 4.1f, 2.8f, 14,  48,  4,  0, 20, new SFX.MeatyHit());
		Hitbox spike = new Hitbox(user, 4.1f, 3.5f, 16, 290, 10, -16,12, new SFX.HeavyHit());
		Hitbox late =  new Hitbox(user, 2.1f, 1.0f,  8,  75,  8, -6, 15, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early, spike, late));
		spike.setProperty(Property.ELECTRIC);
		m.eventList.addActionCircle(early, frame*2, frame*3);
		m.eventList.addActionCircle(spike, frame*2, frame*2 + 4);
		m.eventList.addActionCircle(late, frame*3, frame*4);
		return m;
	}

	public Move bAir() {
		int frame = 9;
		int frames = 3;
		
		Move m = new Move(user, frame * frames);
		m.setHurtBox(GlobalRepo.makeHurtBoxOuter(user, 10, 50));
		m.setAnimation("sprites/fighters/bomber/bair.png", frames, frame);
		Hitbox fist1 = new Hitbox(user, 3.1f, 2.6f,  13, 	62, -16,  1, 17, new SFX.MidHit());
		Hitbox body1 = new Hitbox(user, 2.5f, 1.5f,  11, 	78,   0, -6, 20, new SFX.MidHit());
		Hitbox fist2 =  new Hitbox(user, 2.0f, 1.5f, 10, 	30, -18,  0, 12, new SFX.LightHit());
		Hitbox body2 =  new Hitbox(user, 1.5f, 1.5f,  9, 	40,   0, -3, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(fist1, body1, fist2, body2));
		m.eventList.addActionCircle(fist1, frame, frame*2);
		m.eventList.addActionCircle(body1, frame, frame*2);
		m.eventList.addActionCircle(fist2, frame*2, frame*3);
		m.eventList.addActionCircle(body2, frame*2, frame*3);
		return m;
	}

	/* SPECIAL ATTACKS */

	int start = 24;
	int end = start + 14;
	public Move uSpecial() {
		Move m = new Move(user, end + 1);
		m.setAnimation("sprites/fighters/bomber/uspecial.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 5.0f, 2.0f, 10, 90, 0, 0, 24, new SFX.MidHit());
		h1.setMovesAheadMod(2);
		m.eventList.addConstantVelocity(user, 0, start, ConstantVelocity.noChange, 0);
		m.eventList.addUseSpecial(user, start - 1, -1);
		m.eventList.addConstantAngledVelocity(user, start, end, 11);
		m.eventList.addActionCircle(h1, start, end);
		m.eventList.addVelocityChange(user, end, 0, 0);
		m.setHelpless();
		return m;
	}

	public Move dSpecial() {
		int frames = 2;
		int frame = 15;
		
		Move m = new Move(user, frames * frame);
		m.setAnimation("sprites/fighters/bomber/dspecial.png", frames, frame);
		m.setContinueOnLanding();
		m.eventList.addArmor(m, 0, frames * frame, 4);
		m.eventList.addUseSpecial(user, frame - 1, -4);
		m.eventList.addVelocityChange(user, frame, -7, Action.ChangeVelocity.noChange);
		m.eventList.addProjectile(user, Projectile.Rocket.class, frame);
		return m;
	}

	public Move nSpecial() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/bomber/grab.png", 1, 1);
		m.setContinueOnLanding();
		m.eventList.addUseSpecial(user, 9, -2);
		m.eventList.addProjectile(user, Projectile.Grenade.class, 10);
		return m;
	}

	/* THROWS */

	int throwSize = 18;
	public Move fThrow(){
		int frame = 8;
		int frames = 3;
		
		Move m = new Move(user, frame * frames);
		m.setAnimation("sprites/fighters/bomber/fthrow.png", frames, frame);
		m.dontTurn();
		Hitbox thro  =  new Hitbox(user, 3.6f, 0.0f,  1, 70,  8, 0, throwSize, new SFX.None());
		Hitbox swing1 = new Hitbox(user, 4.0f, 2.4f, 14, 30,  8, 0, 20, new SFX.MeatyHit());
		Hitbox swing2 = new Hitbox(user, 4.0f, 2.4f, 14, 30, 28, 0, 20, new SFX.MeatyHit());
		thro.setNoReverse();
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
		Hitbox thro = new Hitbox(user, 5.5f, 0, 2, 160, 8, 0, throwSize, new SFX.LightHit());
		Hitbox kick = new Hitbox(user, 3, 2.4f, 12, 150, -24, 0, throwSize, new SFX.MeatyHit());
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
		Hitbox h1 = new Hitbox(user, 5, 1.6f, 12, 90, 8, 0, throwSize, new SFX.MidHit());
		h1.setNoReverse();
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move dThrow(){
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/bomber/dthrow.png", 1, 1);
		m.dontTurn();
		Hitbox down = 	new Hitbox(user, 3.0f, 0.0f, 4, 270, 8,  0, throwSize, new SFX.LightHit());
		Hitbox up = 	new Hitbox(user, 5.6f, 0.1f, 8,  82, 8, -8, throwSize, new SFX.MeatyHit());
		down.setNoReverse();
		up.setNoReverse();
		m.eventList.addActionCircle(down, 0, 1);
		m.eventList.addActionCircle(up, 4, 8);
		return m;
	}

	public Move fAirThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/bomber/fair.png", 4, 3);
		Hitbox h1 = new Hitbox(user, 3, 2.8f, 14, 30, 16, 0, throwSize, new SFX.MidHit());
		h1.setNoReverse();
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move bAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/bomber/fjump.png", 1, 1);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 3, 2.4f, 14, 150, 24, -12, throwSize, new SFX.MidHit());
		h1.setNoReverse();
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addVelocityChange(user, 5, 4, 4);
		m.eventList.addActionCircle(h1, 2, 5);
		return m;
	}

	public Move uAirThrow(){
		Move m = new Move(user, 8);
		m.setAnimation("sprites/fighters/bomber/uairthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 8, 0.1f, 10, 90, 8, 0, throwSize, new SFX.MidHit());
		h1.setNoReverse();
		m.eventList.addActionCircle(h1, 0, 4);
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 7);
		return m;
	}

	public Move dAirThrow(){
		Move m = new Move(user, 16);
		m.setAnimation("sprites/fighters/bomber/dthrow.png", 1, 1);
		Hitbox down = new Hitbox(user, 3.2f, 2.8f, 14, 270, 8, 0, throwSize, new SFX.HeavyHit());
		down.setNoReverse();
		m.eventList.addActionCircle(down, 0, 4);
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 6);
		return m;
	}

	/* GRABS */

	public Move grab() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/bomber/grab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 18, 10, 16);
		m.eventList.addActionCircle(g1, 4, 8);
		return m;
	}

	public Move dashGrab() {
		Move m = new Move(user, 28);
		m.setAnimation("sprites/fighters/bomber/dashgrab.png", 1, 1);
		m.eventList.addVelocityChange(user, 4, 6, Action.ChangeVelocity.noChange);
		Grabbox g1 = new Grabbox(user, 18, 10, 16);
		m.eventList.addActionCircle(g1, 4, 8);
		return m;
	}

	public Move airGrab() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/bomber/airgrab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 14, 0, 24);
		m.eventList.addActionCircle(g1, 4, 8);
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
		Move m = new Move(user, 33);
		m.setAnimation("sprites/fighters/bomber/getupattack.png", 3, 11);
		m.setHurtBox(GlobalRepo.makeHurtBox(user, 30, 30));
		m.eventList.addInvincible(user, 0, 10);
		Hitbox front1 = new Hitbox(user, 5.0f, 0.5f, 12, Hitbox.SAMURAI,  20, -8, 16, new SFX.LightHit());
		Hitbox front2 = new Hitbox(user, 5.0f, 0.5f, 14, Hitbox.SAMURAI,  44, -8, 12, new SFX.LightHit());
		Hitbox back1 =  new Hitbox(user, 5.0f, 0.5f, 12, Hitbox.SAMURAI, -20, -8, 16, new SFX.LightHit());
		Hitbox back2 =  new Hitbox(user, 5.0f, 0.5f, 14, Hitbox.SAMURAI, -44, -8, 12, new SFX.LightHit());
		m.eventList.addActionCircle(front1, 11, 16);
		m.eventList.addActionCircle(front2, 11, 16);
		m.eventList.addActionCircle(back1, 22, 27);
		m.eventList.addActionCircle(back2, 22, 27);
		return m;
	}
	
	private final int rollLength = 16;
	private final int rollInvinc = 12;
	private final float rollHeight = 2.8f;
	private final float rollSpeed = -7.0f;
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
		Move m = new Move(user, 3);
		m.dontTurn();
		m.setAnimation("sprites/fighters/bomber/land.png", 1, 1);
		return m;
	}
	
	public Move skid(){
		Move m = new Move(user, 10);
		m.dontTurn();
		m.setAnimation("sprites/fighters/bomber/skid.png", 1, 1);
		return m;
	}
	
	public Move taunt(){
		Move m = new Move(user, 56);
		m.setAnimation("sprites/fighters/bomber/taunt.png", 2, 12);
		return m;
	}

}
