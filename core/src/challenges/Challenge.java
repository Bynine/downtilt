package challenges;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import entities.Fighter;
import entities.TreasureChest;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import main.DowntiltEngine;
import maps.*;

public abstract class Challenge {

	protected final List<Wave> waves;
	protected Wave activeWave = null;
	protected final Stage stage;
	protected long score = 0;
	protected int numLives = 5;
	protected final Vector2 centerPosition = new Vector2(0, 0);
	protected final Vector2 startPosition = new Vector2(0, 0);
	protected boolean finished = false, started = false;

	Challenge(Stage stage, List<Wave> waves){
		this.waves = waves;
		this.stage = stage;
	}

	/**
	 * Called when a challenge begins.
	 */
	protected void begin(){
		for (Fighter player: (DowntiltEngine.getPlayers() ) ){
			player.setLives(numLives);
		}
		activeWave = waves.remove(0);
		DowntiltEngine.changeRoom(stage);
		centerPosition.set(stage.getCenterPosition());
		startPosition.set(stage.getStartPosition());
		
		if (!GlobalRepo.debugToggle){
			int waitBetween = 60;
			TransitionGraphicsHandler.readyGo();
			DowntiltEngine.wait(waitBetween);
		}
		
		for (Fighter player: DowntiltEngine.getPlayers()) player.refresh();
		started = true;
	}

	public void update(){
		activeWave.update(DowntiltEngine.getDeltaTime());
		if (activeWave.getNumEnemies() == 0) {
			if (waves.size() > 0) nextWave();
			else finished = true;
		}
		if (inFailState()) endChallenge();
	}
	
	protected boolean inFailState(){
		boolean shouldRestart = true;
		for (Fighter player: (DowntiltEngine.getPlayers() ) ){
			if (player.getLives() > 0) shouldRestart = false;
		}
		return shouldRestart;
	}

	private void nextWave(){
		MapHandler.addEntity(new TreasureChest(startPosition.x, startPosition.y + GlobalRepo.TILE));
		activeWave = waves.remove(0);
	}

	/**
	 * Called if all players die
	 */
	public void endChallenge(){
		new SFX.Error().play();
		DowntiltEngine.returnToMenu();
	}

	protected Stage getRoomByRound(int position){
		return new Stage_Standard();
	}

	public long getScore(){
		return score;
	}
	
	public boolean started() {
		return started;
	}
	
	public boolean finished() {
		return finished;
	}

	public void addScore(int i) {
		score += i;
	}
	
	public Wave getActiveWave(){
		return activeWave;
	}

	public Vector2 getCenterPosition(){
		return centerPosition;
	}
	
	public Vector2 getStartPosition(){
		return startPosition;
	}
	
	public String getEnemyCounter() {
		return "WAVES LEFT: " + (waves.size() + 1);
	}
	
	public final String getTime(){
		int minutes = getTimeMinSec()[0];
		int seconds = getTimeMinSec()[1];
		String secondsString = "" + seconds;
		if (secondsString.length() == 1) secondsString = "0".concat(secondsString);
		return getTimeString() + minutes + ":" + secondsString;
	}
	
	protected int[] getTimeMinSec(){
		int minutes = (DowntiltEngine.getDeltaTime() /3600);
		int seconds = (DowntiltEngine.getDeltaTime() /60) - (minutes * 60);
		return new int[]{minutes, seconds};
	}
	
	protected String getTimeString(){
		return "TIME: ";
	}

	public Stage getStage() {
		return stage;
	}

}
