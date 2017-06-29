package entities;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public abstract class Throwable extends Hittable {
	
	float staticPercent = 50;

	public Throwable(float posX, float posY) {
		super(posX, posY);
		baseHitstun = 8;
		baseHitSpeed = -0.6f;
	}
	
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		updateImage(deltaTime);
		if (isGrounded() && !inGroundedState()) ground();
		else if (!isGrounded()) state = State.FALL;
	}
	
	public void ground(){
		super.ground();
		state = State.STAND;
		hitstunTimer.end();
	}
	
	public float getPercentage(){
		return staticPercent;
	}
}
