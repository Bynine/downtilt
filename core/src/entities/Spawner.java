package entities;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

import main.GlobalRepo;
import timers.DurationTimer;

public class Spawner extends ImmobileEntity {
	
	public static final int SPAWNTIME = 30;
	private final DurationTimer life;;
	private final Animation anim;

	public Spawner(float posX, float posY, int freq) {
		super(posX, posY);
		anim = GlobalRepo.makeAnimation("sprites/entities/spawner.png", 4, 1, (freq/4)+1, PlayMode.LOOP);
		image = new Sprite(anim.getKeyFrame(0));
		collision = Collision.GHOST;
		life = new DurationTimer(freq);
		timerList.add(life);
		layer = Layer.BACKGROUND;
	}
	
	@Override
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		if (life.timeUp()) setRemove();
		setImage(anim.getKeyFrame(life.getCounter()));
	}

}
