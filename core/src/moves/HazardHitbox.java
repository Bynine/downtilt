package moves;

import com.badlogic.gdx.math.Intersector;

import main.GlobalRepo;
import main.SFX;
import entities.Hazard;
import entities.Hittable;

public class HazardHitbox extends Hitbox {
	
	Hazard haz = null;

	public HazardHitbox(Hazard haz, float BKB, float KBG, float DAM,
			float ANG, float dispX, float dispY, int size, SFX sfx) {
		super(GlobalRepo.getGenericHittable(), BKB, KBG, DAM, ANG, dispX, dispY, size, sfx);
		this.haz = haz;
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
		duration.countDown();
	}
	
	public boolean didHitTarget(Hittable target){ 
		boolean hitAnyFighter = 
				!remove &&
				!target.isInvincible() &&
				Intersector.overlaps(area, target.getHurtBox()) &&
				!hitFighterList.contains(target);
		return hitAnyFighter; 
	}
	
	float getX(){ 
		if (haz == null) return 0;
		return haz.getPosition().x + haz.getImage().getWidth()/2 + (haz.direct() * dispX  + (movesAheadMod * haz.getVelocity().x)); 
	}
	
	float getY(){
		if (haz == null) return 0;
		return haz.getPosition().y + haz.getImage().getHeight()/2 + dispY  + (movesAheadMod * haz.getVelocity().y);
	}

}
