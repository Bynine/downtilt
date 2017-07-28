package entities;

import java.util.List;

import timers.DurationTimer;
import main.MapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Raindrop extends Entity {

	public Raindrop(float posX, float posY) {
		super(posX, posY);
		setImage(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/raindrop.png"))));
		gravity = -1.5f;
		airFrictionX = 0.999f;
		collision = Collision.CREATURE;
	}
	
	public boolean doesCollide(float x, float y){
		if (collision == Collision.GHOST) return false;
		for (Rectangle r : tempRectangleList){
			Rectangle thisR = getCollisionBox(x, y);
			boolean thinPlatform = r.getHeight() <= 1;
			if (!thinPlatform && Intersector.overlaps(thisR, r) && thisR != r && !toRemove) splish();
		}
		return false;
	}
	
	void handleWind(){
		velocity.x = MapHandler.getRoomWind() * 30;
	}
	
	private void splish(){
		int raindroplets = 3;
		for (int i = 0; i < raindroplets; ++i) MapHandler.addEntity(new Raindroplet(position.x, position.y + 0.1f));
		setRemove();
	}
	
	private class Raindroplet extends Entity{
		
		DurationTimer life = new DurationTimer(12);

		public Raindroplet(float posX, float posY) {
			super(posX, posY);
			setImage(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/raindroplet.png"))));
			airFrictionX = 0.996f;
			velocity.x = (float) ( 3 * (0.5 - Math.random()));
			velocity.y = (float) (Math.random() + 2);
		}
		
		public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
			handleMovement();
			limitingForces(rectangleList, entityList);
			updatePosition();
			life.countUp();
			if (life.timeUp()) setRemove();
		}
		
	}

}
