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
		if (!didHitTarget(target) || !target.isGrabbable()) return;
		
		target.getGrabbed((Fighter)user, target, caughtTimeFormula(target));
		remove = true;
	}

	private int caughtTimeFormula(Hittable target){
		int minGrabTime = 15;
		int maxGrabTime = 60;
		float weightMod = 100.0f/target.getWeight();
		int grabTime = (int) ((minGrabTime + target.getPercentage()/3) * weightMod);
		if (grabTime > maxGrabTime) grabTime = maxGrabTime;
		return grabTime;
	}

	@Override
	public Color getColor() {
		return new Color(1, 0, 0.8f, 0.75f);
	}

}
