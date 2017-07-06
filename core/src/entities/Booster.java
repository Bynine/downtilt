package entities;

import timers.Timer;

public class Booster extends ImmobileEntity {

	private final Timer wait = new Timer(30);
	private final float boostX, boostY;
	
	public Booster(float posX, float posY, float boostX, float boostY) {
		super(posX, posY);
		timerList.add(wait);
		this.boostX = boostX;
		this.boostY = boostY;
	}
	
	void handleTouchHelper(Entity e){
		if (isTouching(e, 0) && wait.timeUp()){
			e.velocity.x += boostX;
			e.velocity.y += boostY;
			wait.reset();
		}
	}

}
