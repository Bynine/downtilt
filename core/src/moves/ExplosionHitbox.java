package moves;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import entities.Explosion;
import entities.Hittable;
import main.SFX;

public class ExplosionHitbox extends Hitbox {
	
	private final Explosion explosion;

	public ExplosionHitbox(Hittable user, float BKB, float KBG, float DAM, float ANG, float dispX, float dispY,
			int size, SFX sfx, Explosion explosion, int dur) {
		super(user, BKB, KBG, DAM, ANG, dispX, dispY, size, sfx);
		this.explosion = explosion;
		if (null == user) area.set(getX(), getY(), size);
		else area.set(getX() + (user.direct() * dispX), getY(), size);
		duration.setEndTime(dur);
	}
	
	@Override
	public void update(int deltaTime){
		super.update(deltaTime);
		if (explosion.life.timeUp()) remove = true;
	}
	
	@Override
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
	
	@Override
	float getX(){ 
		if (null == explosion) return 0;
		else return explosion.getPosition().x + explosion.getImage().getWidth()/2  + dispX;
		}
	
	@Override
	float getY(){ 
		if (null == explosion) return 0;
		else return explosion.getPosition().y + explosion.getImage().getHeight()/2 + dispY; 
	}
	
	@Override
	protected Vector2 recoilFormula(Vector2 knockback, Hittable target) {
		return new Vector2(0, 0);
	}
	

}
