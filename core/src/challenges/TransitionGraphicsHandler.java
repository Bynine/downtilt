package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.GlobalRepo;
import main.SFX;
import timers.Timer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class TransitionGraphicsHandler {
		
	private static SpriteBatch batch = null;
	private static final Timer readyGoTimer = new Timer(90), finishTimer = new Timer(90);
	private static final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(readyGoTimer, finishTimer));
	private static Animation readyGo = GlobalRepo.makeAnimation("sprites/graphics/readygo.png", 2, 1, 60, PlayMode.LOOP);
	private static Animation finish = GlobalRepo.makeAnimation("sprites/graphics/finish.png", 1, 1, 60, PlayMode.LOOP);
	
	public static void update(){
		if (batch == null) batch = new SpriteBatch();
		for (Timer t: timerList) t.countUp();
		
		batch.begin();
		if (!finishTimer.timeUp()) batch.draw(finish.getKeyFrame(finishTimer.getCounter()), 320, 500);
		else if (!readyGoTimer.timeUp()) batch.draw(readyGo.getKeyFrame(readyGoTimer.getCounter()), 400, 500);
		batch.end();
	}
	
	static void readyGo(){
		new SFX.ReadySetGo().play();
		readyGoTimer.reset();
	}
	
	static void finish(){
		new SFX.Finish().play();
		finishTimer.reset();
	}
	
}
