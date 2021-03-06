package moves;

import com.badlogic.gdx.graphics.Color;

import entities.Entity.Direction;
import entities.Hittable;

public class Windbox extends ActionCircle {
	
	private final float strX, strY;

	public Windbox(Hittable user, float strX, float strY, float dispX, float dispY, int size) {
		super(user, dispX, dispY, size);
		this.strX = strX;
		this.strY = strY;
	}

	@Override
	public void hitTarget(Hittable en) {
		if (didHitTarget(en) && en != user) {
			final int windLimiter = 10;
			float pushX = strX;
			if (user.getDirection() == Direction.LEFT) pushX *= -1;
			if (!(Math.signum(en.getVelocity().x) == pushX && Math.abs(en.getVelocity().x) > Math.abs(pushX) * windLimiter)) en.getVelocity().add(pushX, 0);
			if (!(Math.signum(en.getVelocity().y) == strY  && Math.abs(en.getVelocity().y) > Math.abs(strY) * windLimiter))  en.getVelocity().add(0, strY);
		}
	}
	
	@Override
	public boolean teamCheck(Hittable target){
		return true;
	}

	@Override
	public Color getColor() {
		return new Color(0.0f, 1.0f, 1.0f, 1.0f);
	}

}
