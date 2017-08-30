package entities;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

import main.GlobalRepo;
import timers.Timer;

public class Spawner extends ImmobileEntity {
	
	public static final int SPAWNTIME = 30;
	private final Animation anim;
	private final Timer flashTimer = new Timer(30);
	private static final TextureRegion spawn = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/spawnerflash.png")));

	public Spawner(float posX, float posY) {
		super(posX, posY);
		anim = GlobalRepo.makeAnimation("sprites/entities/spawner.png", 4, 1, 18, PlayMode.LOOP);
		image = new Sprite(anim.getKeyFrame(0));
		collision = Collision.GHOST;
		layer = Layer.MIDDLEBACK;
		timerList.add(flashTimer);
	}
	
	@Override
	public void update(List<Rectangle> rectangleList, List<Entity> entityList, int deltaTime){
		super.update(rectangleList, entityList, deltaTime);
		if (!flashTimer.timeUp()) setImage(spawn);
		else setImage(anim.getKeyFrame(deltaTime));
	}

	public void flash() {
		flashTimer.reset();
	}

}
