package entities;

import java.util.List;

import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Randall extends Entity {
	
	Timer changeDirection = new Timer(285);

	public Randall(float posX, float posY) {
		super(posX, posY);
		setImage(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/randall.png"))));
		collision = Collision.SOLID;
		timerList.add(changeDirection);
		velocity.x = -2;
	}
	
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		if (changeDirection.timeUp()){
			velocity.x *= -1;
			changeDirection.reset();
		}
		updateTimers();
		updatePosition();
		updateImagePosition(deltaTime);
	}
	
	Rectangle getCollisionBox(float x, float y){
		Rectangle r = image.getBoundingRectangle();
		r.setX(x); r.setY(y + r.getHeight() - 1); r.setHeight(1);
		return r;
	}

}
