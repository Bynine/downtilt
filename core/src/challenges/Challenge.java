package challenges;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
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
	private int longestCombo = 0;
	protected int timeSpent = 0;
	protected float specialMeter = 0;
	public static final int SPECIALMETERMAX = 12, SPECIALMETERBEGINNING = 2, INFINITELIVES = 999;
	protected int specialBeginning = SPECIALMETERBEGINNING;
	protected int lives = 0;
	protected int lifeSetting = 4;

	protected final Vector2 centerPosition = new Vector2(0, 0);
	protected final Vector2 startPosition = new Vector2(0, 0);
	protected boolean finished = false, failed = false, started = false, lostATry = false, lostAllTries = false;

	Challenge(Stage stage, List<Wave> waves){
		this.currWaves = new ArrayList<Wave>();
		initWaves = new ArrayList<Wave>();
		initWaves.addAll(waves);
		this.stage = stage;
	}

	protected boolean shouldRunTransitionGraphics(){
		return (!DowntiltEngine.debugOn() && !(this instanceof ChallengeTutorial) );
	}

	/**
	 * Called when a challenge begins.
	 */
	protected void startChallenge(){
		failed = false;
		DowntiltEngine.resetDeltaTime();
		lives = lifeSetting;
		specialMeter = specialBeginning;

		currWaves.clear();
		for (Wave wave: initWaves) wave.restart();
		currWaves.addAll(initWaves);
		setActiveWave();
		DowntiltEngine.changeRoom(stage);
		centerPosition.set(stage.getCenterPosition());
		startPosition.set(stage.getStartPosition());

		if (shouldRunTransitionGraphics()) TransitionGraphicsHandler.readyGo();

		float mod = 16;
		for (Fighter player: DowntiltEngine.getPlayers()) {
			player.refresh();
			if (DowntiltEngine.getPlayers().size() > 1) player.getPosition().x += mod;
			mod *= -1f;
		}
		started = true;
	}

	protected void setActiveWave(){
		activeWave = currWaves.remove(0);
	}

	public void update(){
		if (failed) startChallenge();
		if (null == activeWave) return;
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
		return lives <= 0;
	}

	protected void nextWave(){
		MapHandler.addEntity(new TreasureChest(startPosition.x, startPosition.y + GlobalRepo.TILE));
		setActiveWave();
	}

	/**
	 * Called if all players die
	 */
	public void failChallenge(){
		failChallengeHelper();
	}
	
	protected void failChallengeHelper(){
		if (failed) return;
		new SFX.Error().play();
		timeSpent += DowntiltEngine.getDeltaTime();
		DowntiltEngine.getMode().wipePendingBonuses();
		if (shouldRunTransitionGraphics()) TransitionGraphicsHandler.failure();
		failed = true;
		lostAllTries = true;
	}

	public void succeedChallenge(){
		if (finished) return;
		timeSpent += DowntiltEngine.getDeltaTime();
		DowntiltEngine.resetDeltaTime();
		if (shouldRunTransitionGraphics()) TransitionGraphicsHandler.finish();
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
		return getTimeString() + GlobalRepo.getTimeString(getTimeNum());
	}

	protected int getTimeNum(){
		return DowntiltEngine.getDeltaTime()/60;
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

	public int getLives(){
		return lives;
	}

	public void removeLife(){
		if (lives >= INFINITELIVES) return;
		lostATry = true;
		lives--;
	}

	public float getSpecialMeter(){
		return specialMeter;
	}

	public void changeSpecial(float f){
		specialMeter = MathUtils.clamp(specialMeter + f, 0, SPECIALMETERMAX);
	}

	public void updateLongestCombo(int combo){
		if (combo > longestCombo) longestCombo = combo;
	}

	public void resolveCombo(float mod){
		/* */
	}

	public boolean isFailed(){
		return failed;
	}

	public enum Difficulty{
		Beginner, Standard, Advanced, Nightmare
	}

	public boolean bossHurt(){
		return false;
	}

	public boolean postBoss(){
		return false;
	}

}
