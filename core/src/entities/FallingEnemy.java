package entities;

import java.util.List;

import main.GlobalRepo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

public class FallingEnemy extends Entity {
	
	private static Animation anim = GlobalRepo.makeAnimation("sprites/graphics/enemyfall.png", 4, 1, 6, PlayMode.LOOP);

	public FallingEnemy(float posX, float posY) {
		super(posX, posY);
		layer = Layer.PARALLAXFAST;
		collision = Collision.GHOST;
		velocity.y = (float) (-3.5 + (Math.random() * 1.5));
	}
	
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		setImage(anim.getKeyFrame(deltaTime));
		updatePosition();
	}
	
	void updatePosition(){
		position.x += velocity.x;
		position.y += velocity.y;
	}

}
