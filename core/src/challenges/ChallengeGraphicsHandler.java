package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;
import timers.Timer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class ChallengeGraphicsHandler {
		
	private static SpriteBatch batch = null;
	private static final Timer readyGoTimer = new Timer(90);
	private static final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(readyGoTimer));
	private static Animation readyGo = GlobalRepo.makeAnimation("sprites/graphics/readygo.png", 2, 1, 60, PlayMode.LOOP);
	
	public static void update(){
		if (batch == null) batch = new SpriteBatch();
		for (Timer t: timerList) t.countUp();
		
		batch.begin();
		if (!readyGoTimer.timeUp()){
			batch.draw(readyGo.getKeyFrame(readyGoTimer.getCounter()), 600, 400);
		}
		batch.end();
	}
	
	static void readyGo(){
		readyGoTimer.start();
	}
	
	static void finish(){
		
	}
	
}
