package moves;

import com.badlogic.gdx.graphics.Color;

import entities.Fighter;
import entities.Hittable;

public class Grabbox extends ActionCircle {

	public Grabbox(Fighter user, float dispX, float dispY, int size) {
		super(user, dispX, dispY, size);
	}

	@Override
	public void hitTarget(Hittable target) {
		if (!didHitTarget(target)) return;
		
		target.getGrabbed((Fighter)user, target, caughtTimeFormula(target));
		remove = true;
	}
	
	private final int minGrabTime = 20;
	private int caughtTimeFormula(Hittable target){
		return (int) (minGrabTime + target.getPercentage());
	}

	@Override
	public Color getColor() {
		return new Color(1, 0, 0.8f, 0.75f);
	}

}
