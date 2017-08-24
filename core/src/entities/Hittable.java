package entities;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import moves.Combo;
import moves.Equipment;
import moves.Hitbox;
import timers.Timer;

public abstract class Hittable extends Entity {

	protected TextureRegion defaultTexture = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/dummy.png")));
	protected float percentage = 0;
	protected boolean tumbling = false, slowed = true, grabbable = true;
	protected final Timer caughtTimer = new Timer(0), knockIntoTimer = new Timer(20), stunTimer = new Timer(0), guardTimer = new Timer(0), hitstopTimer = new Timer(0);
	public static final int BOOSTTIMERDEFAULT = 1800, BOOSTTIMERRUSH = 600;
	protected final Timer powerTimer = new Timer(BOOSTTIMERDEFAULT), speedTimer = new Timer(BOOSTTIMERDEFAULT), 
			defenseTimer = new Timer(BOOSTTIMERDEFAULT), airTimer = new Timer(BOOSTTIMERDEFAULT);
	boolean permaPower, permaSpeed, permaDefense, permaAir;
	protected HitstunType hitstunType = HitstunType.NORMAL;
	protected int team = GlobalRepo.BADTEAM;
	protected SFX onHitSFX = null;

	protected float baseHurtleBK = 4.0f, baseKBG = 2.0f, baseBKB = 1.0f;
	protected float baseHitSpeed = -0.8f;
	protected float baseHitstun = 1, basePower = 1, baseKnockIntoDamage = 2.2f, baseArmor = 0, baseWeight = 100;
	protected float walkSpeed = 2f, runSpeed = 4f, airSpeed = 3f;
	protected float jumpStrength = 5f, doubleJumpStrength = 8.5f, dashStrength = 0f;
	protected float walkAcc = 0.5f, runAcc = 0.75f, airAcc = 0.25f, jumpAcc = 0.54f;
	protected float wallJumpStrengthX = 8f, wallJumpStrengthY = 7.2f;
	protected float wallSlideSpeed = -1f;

	protected int hitstunDealtBonus = 0;
	private Equipment equipment = new Equipment.Default();

	public Hittable(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(defaultTexture);
		timerList.addAll(Arrays.asList(
				caughtTimer, knockIntoTimer, stunTimer, hitstopTimer,
				powerTimer, speedTimer, defenseTimer, airTimer));
	}

	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		updateImage(deltaTime);
		if (getPercentage() > getWeight() * 0.5 && deltaTime % 10 == 0 && Math.random() < getPercentage()/400.0){
			Graphic g = new Graphic.SmokeTrail(this, 16);
			float mod = 32;
			g.position.x += (0.5f - Math.random()) * mod;
			g.position.y += (0.5f - Math.random()) * mod;
			MapHandler.addEntity(g);
		}
	}

	void updateImage(float deltaTime){
		if (inHitstun()) setImage(getTumbleFrame(deltaTime));
		else setImage(getStandFrame(deltaTime));
		if (doesCollide(position.x, position.y)) position.set(500, 600);
	}

	void updatePosition(){
		if (canMove()) super.updatePosition();
	}

	void handleTouchHelper(Entity en){
		super.handleTouchHelper(en);
		if (en instanceof Hittable){
			checkHitByHurtlingObject((Hittable) en);
			checkPushAway((Hittable) en);
		}
	}

	void checkPushAway(Hittable hi){
//		int pushDistance = 8 + 2 * ((int) image.getWidth() - defaultTexture.getRegionWidth());
//		boolean toPush = shouldPushAway(pushDistance, hi);
//		if (getTeam() == hi.getTeam()) 
		boolean toPush = shouldPushAway(6, hi);
		if (toPush) pushAway(hi);
	}

	protected boolean shouldPushAway(int pushDistance, Hittable hi){
		return isTouching(hi, pushDistance) && Math.abs(hi.velocity.x) < 1 && Math.abs(this.velocity.x) < 1;
	}

	protected void pushAway(Entity e){
		float pushForce = 0.03f;
		float dirPush = Math.signum(e.position.x - this.position.x);
		if (dirPush == 0) dirPush = (float) Math.random();
		velocity.x -= dirPush * pushForce;
		e.velocity.x += dirPush * pushForce;
	}

	protected int touchRadius = 8;
	protected void checkHitByHurtlingObject(Hittable hurtler){
		boolean fighterGoingFastEnough = true;
//				knockbackIntensity(hurtler.velocity) > hurtler.baseHurtleBK;
//		if (hurtler.hitstunType != HitstunType.NORMAL) fighterGoingFastEnough = true;
		boolean higherVelocity = true;
		// knockbackIntensity(hurtler.velocity) > knockbackIntensity(velocity)
		boolean correctTeam = teamCheck(hurtler);
		boolean angleFarEnough = (Math.abs(hurtler.getVelocity().angle() - getVelocity().angle()) > 15) || knockbackIntensity(velocity) == 0;
		boolean knockInto = knockIntoTimer.timeUp() && fighterGoingFastEnough && higherVelocity && correctTeam && hurtler.inHitstun();
		if (angleFarEnough && knockInto && isTouching(hurtler, touchRadius) && !isInvincible()) {
			if (isGuarding()) blockHurtlingObject(hurtler);
			else getHitByHurtlingObject(hurtler);
		}
	}

	protected boolean teamCheck(Hittable hi){
		return getTeam() == hi.getTeam();
	}

	private void blockHurtlingObject(Hittable hurtler){
		if (isPerfectGuarding()) perfectParry();
		else parry();
	}

	public void getHitByHurtlingObject(Hittable hurtler){
		Vector2 knockIntoVector = new Vector2(hurtler.velocity.x, hurtler.velocity.y);
		float weightMod = (float) Math.pow(hurtler.getWeight()/getWeight(), 0.6);
		float bkb = baseBKB * knockbackIntensity(knockIntoVector) * weightMod;
		float dam = knockbackIntensity(knockIntoVector) * hurtler.baseKnockIntoDamage;
		
		Hitbox h;
		if (hurtler.hitstunType == HitstunType.SUPER){
			h = new Hitbox(hurtler, bkb * .7f, baseKBG, dam * 2, knockIntoVector.angle(), 0, 0, 0, null);
			knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
			float newAngle = h.getAngle();
			knockIntoVector.setAngle(newAngle);
		}
		else {
			h = new Hitbox(hurtler, bkb * .37f, baseKBG, dam * 1.2f, knockIntoVector.angle(), 0, 0, 0, null);
			knockIntoVector.set(h.knockbackFormula(this), h.knockbackFormula(this));
			knockIntoVector.setAngle( (h.getAngle() + 90) / 2);
		}
		
		hurtler.playImpactSound(h.getDamage());
		takeKnockIntoKnockback(hurtler, knockIntoVector, h.getDamage() / 2, (int) h.getDamage() + hitstunDealtBonus );
		handleCollision(hurtler);
		MapHandler.addEntity(new Graphic.HitGoodGraphic( (getCenter().x + hurtler.getCenter().x)/2, (getCenter().y + hurtler.getCenter().y)/2, (int)dam/2));
	}
	
	protected void playImpactSound(float DAM){
		SFX.proportionalHit(DAM).play();
	}
	
	private void handleCollision(Hittable hurtler){
		hurtler.knockIntoTimer.reset();
		knockIntoTimer.reset();
		setKnockIntoVelocity(hurtler);
		hurtler.knockInto();
		addToCombo(Combo.knockIntoID);
	}

	protected void setKnockIntoVelocity(Hittable hurtler){
		float weightMod = 1.1f * (float) Math.pow(getWeight()/hurtler.getWeight(), 0.5);
		final int spike = 270;
		final int range = 30;
		if (hurtler.velocity.angle() > (spike - range) && hurtler.velocity.angle() < (spike + range) && isGrounded()) weightMod = 2.15f;
		hurtler.velocity.add(weightMod * hurtler.velocity.x * hurtler.baseHitSpeed, weightMod * hurtler.velocity.y * hurtler.baseHitSpeed);
	}

	@Override
	protected void handleWindHelper(){
		float weightMod = (float) Math.pow(100/getWeight(), 0.420); // nice
		velocity.x += weightMod * MapHandler.getRoomWind();
	}

	protected void knockInto(){
		/* */
	}

	protected void addToCombo(int id){
		/* */
	}

	protected void takeKnockIntoKnockback(Hittable hurtler, Vector2 knockback, float DAM, int hitstun){
		knockbackHelper(knockback, DAM, hitstun, knockbackIntensity(velocity) < knockbackIntensity(knockback), HitstunType.NORMAL);
	}

	public void takeDamagingKnockback(Vector2 knockback, float DAM, int hitstun, HitstunType hitboxhitstunType, Hittable user) {
		if (null != user && this instanceof Fighter) user.dealDamage(DAM);
		knockbackHelper(knockback, DAM, hitstun, true, hitboxhitstunType);
	}

	public void takeRecoil(Vector2 knockback){
		knockback.setAngle(directionalInfluenceAngle(knockback));
		velocity.add(knockback);
	}

	protected void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		takeDamage(DAM);
		if (!isGrounded()) {
			prevAerialHitAngle = knockback.angle();
		}
		else{
			prevAerialHitAngle = 0;
		}
		if (knockbackIntensity(knockback) > 0) takeKnockback(knockback, hitstun, shouldChangeKnockback, ht);
		if (knockbackIntensity(knockback) > tumbleBK) tumbling = true;
	}

	public void takeDamage(float DAM){
		percentage += DAM;
		if (percentage < 0) percentage = 0;
	}

	protected void dealDamage(float DAM){
		/* */
	}

	protected void takeKnockback(Vector2 knockback, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		knockback.setAngle(directionalInfluenceAngle(knockback));
		if (shouldChangeKnockback) velocity.set(knockback);
		if (state == State.HELPLESS) state = State.FALL;
		if (this instanceof Fighter && team != GlobalRepo.GOODTEAM){
			hitstopTimer.setEndTime(hitstopDuration(hitstun));
			hitstopTimer.reset();
			hitstun += hitstopDuration(hitstun);
		}
		hitstunTimer.setEndTime(hitstun);
		hitstunTimer.reset();
		hitstunType = ht;
		guardTimer.end();
		disrupt();
	}

	protected int hitstopDuration(int hitstun){
		return MathUtils.clamp(hitstun / 21, 0, 5);
	}

	protected float directionalInfluenceAngle(Vector2 knockback){
		return knockback.angle();
	}

	public void getGrabbed(Fighter user, Hittable target, int caughtTime) {
		user.isNowGrabbing(target, caughtTime);
		caughtTimer.setEndTime(caughtTime);
		caughtTimer.reset();
		
		float newPosX = 0;
		float dispX = 6;
		if (user.getDirection() == Direction.LEFT) newPosX = user.getCenter().x - (dispX + (target.getImage().getWidth() * 0.8f));
		if (user.getDirection() == Direction.RIGHT) newPosX = user.getCenter().x + dispX;
		float newPosY = user.position.y + image.getHeight()/4;
		
		if (!doesCollide(newPosX, newPosY)) position.set(newPosX, newPosY);
		else if (!doesCollide(position.x, newPosY)) position.set(position.x, newPosY);
		else if (!doesCollide(newPosX, position.y)) position.set(newPosX, position.y);
		disrupt();
	}

	@Override
	protected boolean upThroughThinPlatform(Rectangle r){
		return super.upThroughThinPlatform(r);
	}

	protected void disrupt(){
		stunTimer.end();
		endAttack();
	}

	public void stun(int duration) {
		stunTimer.setEndTime(duration);
		stunTimer.reset();
	}

	public void endAttack(){
		/**/
	}

	private float checkTimerForBonus(Timer t, boolean b){
		if (!t.timeUp() || b) return 1.4f;
		else return 1f;
	}

	@Override
	void bounceOff(){
		super.bounceOff();
		hitstopTimer.reset(5);
		float mod = -3f;
		MapHandler.addEntity(new Graphic.HitGoodGraphic(getCenter().x + velocity.x * mod, getCenter().y + velocity.y * mod, (int)(knockbackIntensity(velocity) / 2)));
		velocity.x *= 1.1f;
	}

	public float getPower(){ return basePower * checkTimerForBonus(powerTimer, permaPower) * equipment.getPowerMod(); }
	public float getWeight() { return baseWeight * checkTimerForBonus(defenseTimer, permaDefense) * equipment.getWeightMod(); }
	public float getArmor() { return baseArmor + 5 * (checkTimerForBonus(defenseTimer, permaDefense) - 1) + equipment.getArmorMod(); }
	public float getDashStrength() { return dashStrength * getSpeedMod(); }
	public float getWalkSpeed() { return walkSpeed * getSpeedMod() * equipment.getSpeedMod() * equipment.getWalkSpeedMod(); }
	public float getWalkAcc() { return walkAcc * getSpeedMod() * equipment.getSpeedMod() * equipment.getWalkAccMod(); }
	public float getRunSpeed() { return runSpeed * getSpeedMod() * equipment.getSpeedMod() * equipment.getRunSpeedMod(); }
	public float getRunAcc() { return runAcc * getSpeedMod() * equipment.getSpeedMod() * equipment.getRunAccMod(); }
	public float getAirSpeed() { return airSpeed * checkTimerForBonus(airTimer, permaAir) * equipment.getAirSpeedMod(); }
	public float getAirAcc() { return airAcc * checkTimerForBonus(airTimer, permaAir) * equipment.getAirAccMod(); }
	public float getJumpStrength() { return jumpStrength; }
	public float getJumpAcc() { return jumpAcc * checkTimerForBonus(airTimer, permaAir) * equipment.getJumpAccMod(); }
	public float getGravity() { return gravity * equipment.getGravityMod(); }
	public float getFriction() { return (float) Math.pow(friction, equipment.getFrictionMod()); }
	public float getAirFrictionX() { return (float) Math.pow(airFrictionX, equipment.getAirFrictionMod()); }
	public float getDoubleJumpStrength() { return doubleJumpStrength; }
	public float getWallJumpStrengthX() { return wallJumpStrengthX; }
	public float getWallJumpStrengthY() { return wallJumpStrengthY; }
	public float getWallSlideSpeed() { return wallSlideSpeed * equipment.getWallSlideMod(); }

	public void addPower(int time){ 
		setTimer(powerTimer, time);
	}
	public void addDefense(int time){ 
		setTimer(defenseTimer, time);
	}
	public void addSpeed(int time){ 
		setTimer(speedTimer, time);
	}
	public void addAir(int time){ 
		setTimer(airTimer, time);
	}
	public void addAll(int time){
		addPower(time);
		addDefense(time);
		addSpeed(time);
		addAir(time);
	}

	public float getSpeedMod(){ return checkTimerForBonus(speedTimer, permaSpeed) * equipment.getSpeedMod(); }
	public float getAirMod(){ return checkTimerForBonus(airTimer, permaAir); }

	public boolean isInvincible(){ return hitstunTimer.getCounter() == 0; }

	void handleWind(){
		velocity.x += MapHandler.getRoomWind() * (baseWeight/100);
	}

	public void setPercentage(float perc){
		percentage = perc;
	}

	public float getPercentage() { 
		return percentage; 
	}

	public float getHitstun(){
		return baseHitstun;
	}

	public int getTeam() {
		return team; 
	}

	public boolean canMove(){
		return !inHitStop() && stunTimer.timeUp() && caughtTimer.timeUp();
	}

	public boolean inHitStop(){
		return !hitstopTimer.timeUp();
	}

	public boolean isGuarding() { return false; }
	public boolean isPerfectGuarding() { return false; }

	public void setEquipment(Equipment e){
		equipment = e;
	}

	public boolean isGrabbable(){
		return grabbable;
	}

	public void parry(){
		/* */
	}

	public void perfectParry(){
		/* */
	}

	protected void setTimer(Timer t, int i){
		t.setEndTime(i);
		t.reset();
	}

	public void setPermaPower() { permaPower = true; }
	public void setPermaDefense() { permaDefense = true; }
	public void setPermaSpeed() { permaSpeed = true; }
	public void setPermaAir() { permaAir = true; }
	public boolean powerActive() { return !powerTimer.timeUp() || permaPower; }
	public boolean defenseActive() { return !defenseTimer.timeUp() || permaDefense; }
	public boolean speedActive() { return !speedTimer.timeUp() || permaSpeed; }
	public boolean airActive() { return !airTimer.timeUp() || permaAir; }
	public boolean isCaught() { return !caughtTimer.timeUp(); }

	abstract TextureRegion getStandFrame(float deltaTime);
	abstract TextureRegion getTumbleFrame(float deltaTime);

	public enum HitstunType{ NORMAL, SUPER }

}
