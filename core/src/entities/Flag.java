package entities;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

import main.GlobalRepo;

public class Flag extends ImmobileEntity {
	
	Animation anim = GlobalRepo.makeAnimation("sprites/entities/flag.png", 2, 1, 15, PlayMode.LOOP);

	public Flag(float posX, float posY) {
		super(posX, posY);
		setImage(anim.getKeyFrame(0));
		layer = Layer.MIDDLEBACK;
	}
	
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		setImage(anim.getKeyFrame(deltaTime));
	}

}
