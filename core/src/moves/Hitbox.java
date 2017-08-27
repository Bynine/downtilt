package moves;

import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import main.DowntiltEngine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import entities.Entity.State;
import entities.Fighter;
import entities.Graphic;
import entities.Hittable;

public class Hitbox extends ActionCircle{
	public static final int SAMURAI = 361;
	public static final int REVERSE = -487;

	protected float BKB, KBG;
	protected float DAM;
	protected float ANG;
	protected float heldCharge = 1;
	protected final Effect.Charge charge;
	protected SFX sfx;

	/**
	 * @param User (this)
	 * @param BaseKnockback
	 * @param KnockbackGrowth
	 * @param Damage
	 * @param Angle
	 * @param HDisplacement
	 * @param VDisplacement
	 * @param Size
	 */
	public Hitbox(Hittable user, float BKB, float KBG, float DAM, float ANG, float dispX, float dispY, int size, SFX sfx){
		super(user, dispX, dispY, size);
		this.BKB = BKB;
		this.KBG = KBG;
		this.DAM = DAM;
		this.ANG = ANG;
		this.sfx = sfx;
		charge = null;
	}

	/**
	 * @param User (this)
	 * @param BaseKnockback
	 * @param KnockbackGrowth
	 * @param Damage
	 * @param Angle
	 * @param HDisplacement
	 * @param VDisplacement
	 * @param Size
	 * @param Charge
	 */
	public Hitbox(Hittable user, float BKB, float KBG, float DAM, float ANG, float dispX, float dispY, int size, SFX sfx, Effect.Charge charge){
		super(user, dispX, dispY, size);
		this.BKB = BKB;
		this.KBG = KBG;
		this.DAM = DAM;
		this.ANG = ANG;
		this.sfx = sfx;
		this.charge = charge;
	}

	private boolean guarding = false, perfectGuarding = false, ignoreArmor = false;

	public void hitTarget(Hittable target){
		final int meteorAngleSize = 50;
		final int downAngle = 270;
		final float meteorHitstunMod = 1.25f;
		final float meteorGroundMod = -0.75f;

		if (!didHitTarget(target)) return;
		refreshTimer.reset();
		guarding = target.isGuarding();
		perfectGuarding = target.isPerfectGuarding();

		// Apply user modifications.
		float staleness = 1;
		if (null != user) {
			if (user instanceof Fighter) {
				Fighter fi = (Fighter)user;
				staleness = getStaleness(fi);
				if (isGuarding()) {
					fi.setStun(10);
					if (perfectGuarding)	target.perfectParry();
					else if (guarding)		target.parry();
				}
			}
			DAM *= user.getPower();
			KBG *= user.getPower();
		}

		Vector2 knockback = new Vector2();
		if (null != charge) heldCharge = charge.getHeldCharge();

		knockback.set(knockbackFormula(target) * staleness, knockbackFormula(target) * staleness);
		if (ANG == SAMURAI) setSamuraiAngle(target, knockback);
		else 	if (ANG == REVERSE) setReverseAngle(target, knockback);
		else 						knockback = setAngle(knockback, target);
		if (isGuarding() || staleness == overuseStaleness) knockback.set(0, 0);
		knockback.x *= applyReverseHitbox(target);
		if (knockbackFormula(target) > 8 && null != user) user.takeRecoil(recoilFormula(knockback, target));
		int hitstun = hitstunFormula( target, knockbackFormula(target) * staleness );

		// Checks if the move hits downward and the target is on the ground.
		boolean groundedMeteor = target.isGrounded() && ((downAngle + meteorAngleSize) > knockback.angle() && knockback.angle() > (downAngle - meteorAngleSize));
		if (groundedMeteor){
			knockback.y *= meteorGroundMod;
			hitstun *= meteorHitstunMod;
		}

		float finalDamage = heldCharge * DAM * staleness;
		if (perfectGuarding) finalDamage = 0;
		else if (guarding) finalDamage *= 0.5f;
		target.takeDamagingKnockback(knockback, finalDamage, hitstun, hitstunType, user);
		if (property == Property.STUN) {
			int stunTime = (int) (finalDamage * 6);
			if (DowntiltEngine.entityIsPlayer(target)) stunTime /= 2;
			target.stun(stunTime);
		}
		
		if (guarding || ( knockback.x == 0 && knockback.y == 0)) {
			sfx = new SFX.EmptyHit();
			MapHandler.addEntity(new Graphic.HitGuardGraphic(area.x + area.radius/2, area.y + area.radius/2, hitlagFormula(knockbackFormula(target) * 3)));
		}
		else{
			startHitlag(target);
			if (property == Property.FIRE){
				target.ignite();
			}
			if (target.getTeam() == GlobalRepo.BADTEAM) 
				MapHandler.addEntity(new Graphic.HitGoodGraphic(area.x + area.radius/2, area.y + area.radius/2, graphicLengthFormula(knockbackFormula(target))));
			if (target.getTeam() == GlobalRepo.GOODTEAM) 
				MapHandler.addEntity(new Graphic.HitBadGraphic(area.x + area.radius/2, area.y + area.radius/2, graphicLengthFormula(knockbackFormula(target))));
		}
		sfx.play();
		hitTargetList.add(target);
	}
	
	private int graphicLengthFormula(float knockback){
		return (int) (2 * hitlagFormula(knockback));
	}
	
	private boolean isGuarding(){
		return guarding || perfectGuarding;
	}
	
	protected Vector2 setAngle(Vector2 knockback, Hittable target){
		return knockback.setAngle(ANG);
	}

	private static final float overuseStaleness = 0.25f;
	private float getStaleness(Fighter user) {
		float staleMod = 0.94f;
		IDMove currMove = user.getActiveMove();
		if (null == currMove) return 1;
		float staleness = 1/staleMod;
		boolean spam = false;
		if (user.getMoveQueue().size() >= Fighter.staleMoveQueueSize) spam = true;
		for (IDMove im: user.getMoveQueue()){
			if (im.id == currMove.id) staleness *= staleMod;
			if (im.id != currMove.id) spam = false;
		}
		if (spam) {
			staleness = overuseStaleness;
			sfx = new SFX.EmptyHit();
		}
		return staleness;
	}

	protected Vector2 recoilFormula(Vector2 knockback, Hittable target) {
		float recoil = -knockback.x/4;
		recoil *= (target.getWeight()/100);
		Vector2 recoilVector = new Vector2(recoil, 0);
		return recoilVector;
	}

	protected void setSamuraiAngle(Hittable target, Vector2 knockback){
		float minSamuraiKnockback = 4;
		float samuraiKnockbackAngle = 45;
		if (knockbackFormula(target) < minSamuraiKnockback && target.isGrounded()) knockback.setAngle(0);
		else knockback.setAngle(samuraiKnockbackAngle);
	}

	protected void setReverseAngle(Hittable target, Vector2 knockback){
		knockback.setAngle(-target.getVelocity().angle());
	}

	void startHitlag(Hittable target){
		if (!DowntiltEngine.getPlayers().contains(target) && !DowntiltEngine.getPlayers().contains(user)) return;
		float hit = knockbackFormula(target);
		if (!ignoreArmor) hit += target.getArmor();
		int hitlag;
		if (perfectGuarding) hitlag = (int) (DAM / 2);
		else if (guarding) hitlag = (int) (DAM / 2);
		else hitlag = hitlagFormula(hit);
		DowntiltEngine.causeHitlag(hitlag);
	}

	protected int hitlagFormula(float knockback) {
		final int hitlagCap = 24;
		final float hitlagRatio = 0.6f;
		final float electricHitlagMultiplier = 1.5f;

		int hitlag = (int) (knockback * hitlagRatio);
		if (hitlag > hitlagCap) hitlag = hitlagCap;
		if (property == Property.ELECTRIC) hitlag *= electricHitlagMultiplier;
		return hitlag;
	}

	public int hitstunFormula(Hittable target, float knockback){
		final float hitstunRatio = 5f;
		if (BKB + KBG == 0 || perfectGuarding) return 0;
		int hitstun = 2 + (int) (knockback * hitstunRatio * target.getHitstun());
		if (guarding) hitstun = (int) (hitstun * 0.35);
		return hitstun;
	}

	public float knockbackFormula(Hittable target){
		final float crouchCancelMod = .65f;
		final float kbgMod = 0.023f;
		final float weightMod = 0.01f;
		final float minKnockback = 0.2f;

		if (BKB + KBG == 0 || perfectGuarding) return 0;
		float knockback = heldCharge * (BKB + ( (KBG * target.getPercentage() * kbgMod) / (target.getWeight() * weightMod) ));
		if (!ignoreArmor) knockback -= target.getArmor();
		if (target instanceof Fighter){
			if (( (Fighter) target ).getState() == State.CROUCH) knockback *= crouchCancelMod;
		}
		if (knockback < minKnockback) return 0;
		else return knockback;
	}

	public void update(int deltaTime){
		super.update(deltaTime);
		refreshTimer.countUp();
		if (doesRefresh && refreshTimer.timeUp()){
			reset();
			if (group != null) for (ActionCircle ac: group.connectedCircles) ac.reset();
			refreshTimer.reset();
		}
	}

	protected float applyReverseHitbox(Hittable target){
		if (null == user) {
			if (area.x - area.radius/2 > target.getPosition().x) return -1;
			else return 1;
		}
		else if (reverse){
			if (user.getPosition().x > target.getPosition().x) return -1;
			else return 1;
		}
		else return user.direct();
	}

	public enum Property { NORMAL, ELECTRIC, STUN, FIRE  }
	public float getDamage() { return DAM; }
	public float getAngle() { return ANG; }
	public void setProperty(Property property) { this.property = property; }
	public void setHitstunType(Fighter.HitstunType ht) { hitstunType = ht; }
	public void setIgnoreArmor() { ignoreArmor = true; }
	public Color getColor() {
		return new Color(1, 0.2f, 0.2f, 0.75f);
	}
	
	public static class AngleHitbox extends Hitbox{

		public AngleHitbox(Hittable user, float BKB, float KBG, float DAM, float dispX, float dispY, int size, SFX sfx) {
			super(user, BKB, KBG, DAM, 0, dispX, dispY, size, sfx);
		}
		
		@Override
		protected Vector2 setAngle(Vector2 knockback, Hittable target){
			Vector2 vel = user.getVelocity();
			vel.set(vel.x * user.direct(), vel.y);
			return knockback.setAngle(vel.angle());
		}
		
	}
	
	public static class QuakeHitbox extends Hitbox{

		public QuakeHitbox(Hittable user, float BKB, float KBG, float DAM, float ANG, float dispX, float dispY,
				int size, SFX sfx) {
			super(user, BKB, KBG, DAM, ANG, dispX, dispY, size, sfx);
		}
		
		public boolean didHitTarget(Hittable target){ 
			return target.isGrounded() && user.getPosition().y >= (target.getPosition().y - 4) && super.didHitTarget(target);
		}
	}
	
	public static class ParryHitbox extends Hitbox{
		final float parryANG;
		
		public ParryHitbox(Hittable user, float BKB, float KBG, float DAM, float ANG, float parryANG, float dispX, float dispY, int size, SFX sfx) {
			super(user, BKB, KBG, DAM, ANG, dispX, dispY, size, sfx);
			this.parryANG = parryANG;
		}
		
		@Override
		protected Vector2 setAngle(Vector2 knockback, Hittable target){
			if (target instanceof Fighter) return knockback.setAngle(ANG);
			else return knockback.setAngle(parryANG);
		}
		
	}

}
