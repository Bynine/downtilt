package moves;

import main.MapHandler;
import main.SFX;
import main.DowntiltEngine;

import com.badlogic.gdx.math.Intersector;

import entities.Fighter;
import entities.Hittable;
import entities.Projectile;

public class ProjectileHitbox extends Hitbox {
	
	final Projectile proj;

	public ProjectileHitbox(Fighter user, float BKB, float KBG, float DAM,
			float ANG, float dispX, float dispY, int size, SFX sfx, Projectile proj, int dur) {
		super(user, BKB, KBG, DAM, ANG, dispX, dispY, size, sfx);
		this.proj = proj;
		if (null == user) area.set(getX(), getY(), size);
		else area.set(getX() + (user.direct() * dispX), getY(), size);
		duration.setEndTime(dur);
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
		if (proj.life.timeUp()) remove = true;
	}
	
	public boolean didHitTarget(Hittable target){ 
		boolean hitAnyFighter = 
				!remove &&
				!target.isInvincible() &&
				Intersector.overlaps(area, target.getHurtBox()) &&
				!hitTargetList.contains(target);
		if (null == user) return hitAnyFighter; 
		return 
				target != user &&
				user.getTeam() != target.getTeam() &&
				hitAnyFighter; 
	}
	
	void handlePerfectBlockingKnockback(){
		proj.reverse();
	}
	
	private final float reflectMultiplier = 1.6f;
	void touchOtherActionCircles(){
		for (ActionCircle ac: MapHandler.getActionCircles()){
			if (Intersector.overlaps(this.area, ac.area) && ac.doesReflect() && !ac.user.equals(user) ){
				proj.reflect((Fighter)ac.user);
				user = proj.getUser();
				DAM *= reflectMultiplier;
				KBG *= reflectMultiplier;
				BKB *= reflectMultiplier;
			}
		}
	}
	
	float getX(){ 
		if (null == proj) return 0;
		return proj.getPosition().x + proj.getImage().getWidth()/2  + dispX;
		}
	float getY(){ 
		if (null == proj) return 0;
		return proj.getPosition().y + proj.getImage().getHeight()/2 + dispY; 
	}
	
	void startHitlag(Fighter target){
		int hitlag = hitlagFormula(knockbackFormula(target)) - 2;
		if (hitlag > 1) DowntiltEngine.causeHitlag(hitlag);
	}

}
