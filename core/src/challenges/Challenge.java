package challenges;

import java.util.ArrayList;
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

	protected final List<Wave> currWaves, initWaves;
	protected Wave activeWave = null;
	protected final Stage stage;
	protected long score = 0;
	private int longestCombo = 0, timeSpent = 0;
	protected final Vector2 centerPosition = new Vector2(0, 0);
	protected final Vector2 startPosition = new Vector2(0, 0);
	protected boolean finished = false, started = false;
	int soloLives = 5;
	int coopLives = 3;

	Challenge(Stage stage, List<Wave> waves){
		this.currWaves = new ArrayList<Wave>();
		initWaves = new ArrayList<Wave>();
		initWaves.addAll(waves);
		this.stage = stage;
	}

	/**
	 * Called when a challenge begins.
	 */
	protected void startChallenge(){
		for (Fighter player: (DowntiltEngine.getPlayers() ) ){
			if (DowntiltEngine.getPlayers().size() == 1) player.setLives(soloLives);
			else player.setLives(coopLives);
		}
		
		currWaves.clear();
		for (Wave wave: initWaves) wave.restart();
		currWaves.addAll(initWaves);
		setActiveWave();
		DowntiltEngine.changeRoom(stage);
		centerPosition.set(stage.getCenterPosition());
		startPosition.set(stage.getStartPosition());
		
		if (!DowntiltEngine.debugOn()){
			int waitBetween = 60;
			TransitionGraphicsHandler.readyGo();
			DowntiltEngine.wait(waitBetween);
		}
		
		for (Fighter player: DowntiltEngine.getPlayers()) {
			player.refresh();
		}
		started = true;
	}
	
	protected void setActiveWave(){
		activeWave = currWaves.remove(0);
	}

	public void update(){
		activeWave.update(DowntiltEngine.getDeltaTime());
		nextWaveChecker();
		if (inFailState()) failChallenge();
	}
	
	protected void nextWaveChecker(){
		if (activeWave.getNumEnemies() == 0 && !activeWave.isEndless()) {
			if (currWaves.size() > 0) nextWave();
			else if (inSuccessState()) succeedChallenge();
		}
	}
	
	protected boolean inSuccessState(){
		return currWaves.size() == 0 && activeWave.getNumEnemies() == 0;
	}
	
	protected boolean inFailState(){
		boolean shouldRestart = true;
		for (Fighter player: (DowntiltEngine.getPlayers() ) ){
			if (player.getLives() > 0) shouldRestart = false;
		}
		return shouldRestart;
	}

	protected void nextWave(){
		MapHandler.addEntity(new TreasureChest(startPosition.x, startPosition.y + GlobalRepo.TILE));
		setActiveWave();
	}

	/**
	 * Called if all players die
	 */
	public void failChallenge(){
		new SFX.Error().play();
		startChallenge();
	}
	
	public void succeedChallenge(){
		timeSpent = DowntiltEngine.getDeltaTime();
		finished = true;
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
	
	public String getWaveCounter() {
		return "WAVES LEFT: " + (currWaves.size() + 1);
	}
	
	public final String getTime(){
//		int minutes = getTimeMinSec()[0];
		int seconds = getTimeMinSec()[1];
//		String secondsString = "" + seconds;
//		if (secondsString.length() == 1) secondsString = "0".concat(secondsString);
		return getTimeString() + GlobalRepo.getTimeString(seconds);
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

	public float getStartDispX() {
		return stage.getDispX();
	}
	
	public int getLongestCombo(){
		return longestCombo;
	}
	
	public int getSeconds(){
		return timeSpent * 60;
	}
	
	public void updateLongestCombo(int combo){
		if (combo > longestCombo) longestCombo = combo;
	}
	
	public void rotateEyes(){
		/* */
	}
	
	public void resolveCombo(float mod){
		/* */
	}

}
