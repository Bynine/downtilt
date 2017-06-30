package challenges;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import entities.Fighter;
import entities.TreasureChest;
import main.GlobalRepo;
import main.MapHandler;
import main.DowntiltEngine;
import maps.*;

public abstract class Challenge {

	protected final List<Wave> waves;
	protected Wave activeWave = null;
	protected final Stage stage;
	protected long score = 0;
	protected int numLives = 5;
	protected final Vector2 centerPosition = new Vector2(0, 0);
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
		centerPosition.set(stage.getStartPosition());
		
		if (!DowntiltEngine.debugToggle){
			int waitBetween = 60;
			ChallengeGraphicsHandler.readyGo();
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
		checkIfAllPlayersDead();
	}
	
	private void checkIfAllPlayersDead(){
		boolean shouldRestart = true;
		for (Fighter player: (DowntiltEngine.getPlayers() ) ){
			if (player.getLives() > 0) shouldRestart = false;
		}
		if (shouldRestart) restart();
	}

	private void nextWave(){
		MapHandler.addEntity(new TreasureChest(centerPosition.x, centerPosition.y + GlobalRepo.TILE));
		activeWave = waves.remove(0);
	}

	/**
	 * Called if all players die
	 */
	public void restart(){
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
	
	public abstract String getEnemyCounter();

}
