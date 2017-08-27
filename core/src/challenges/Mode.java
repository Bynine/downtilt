package challenges;

import java.util.ArrayList;
import java.util.List;

import challenges.Challenge.Difficulty;
import main.DowntiltEngine;
import main.DowntiltEngine.GameState;
import main.SFX;

public abstract class Mode {

	protected int activeChallengeIndex = 0;
	protected final List<Bonus> pendingBonuses = new ArrayList<Bonus>();
	protected final List<Bonus> bonuses = new ArrayList<Bonus>();
	protected boolean usedSpecial = false;

	/**
	 * Returns the current challenge being played.
	 */
	public Challenge getActiveChallenge(){
		if (activeChallengeIndex >= getChallengeList().size()) return getChallengeList().get(getChallengeList().size() - 1);
		return getChallengeList().get(activeChallengeIndex);
	}

	/**
	 * Starts a new challenge after finishing the old one.
	 */
	public void update(){
		if (DowntiltEngine.musicOn() && !getActiveChallenge().postBoss()) handleMusic();
		if (!DowntiltEngine.isWaiting() && getActiveChallenge().finished()) {
			if (!pendingBonuses.isEmpty()) addPendingBonuses();
			getActiveChallenge().getStage().getMusic().stop();
			activeChallengeIndex++;
		}
		boolean shouldStartChallenge = !DowntiltEngine.isWaiting() && DowntiltEngine.getGameState() != GameState.ROUNDEND && !getActiveChallenge().started;
		if (shouldStartChallenge) getActiveChallenge().startChallenge();
		if (activeChallengeIndex > getChallengeList().size()) win();
		getActiveChallenge().update();
	}
	
	protected void handleMusic(){
		float vol = (DowntiltEngine.getMusicVolume()) / 8.0f;
		getActiveChallenge().getStage().getMusic().setVolume(vol);
		getActiveChallenge().getStage().getMusic().play();
	}

	/**
	 * Activates when all challenges are completed.
	 */
	protected void win(){
		new SFX.Victory().play();
		boolean unstoppable = true;
		boolean immortal = true;
		for (Challenge c: getChallengeList()){
			if (c.everRetried) immortal = false;
			if (c.everFailed) unstoppable = false;
		}
		if (unstoppable) pendValidBonus(new Bonus.UnstoppableBonus());
		if (immortal) pendValidBonus(new Bonus.ImmortalBonus());
		if (!usedSpecial) pendValidBonus(new Bonus.NoSpecialBonus());
		if (Math.random() < 0.01) pendValidBonus(new Bonus.NoveltyBonus());
		addPendingBonuses();
		DowntiltEngine.startVictoryScreen(getVictory());
	}

	protected int getCombo(){
		int longestCombo = 0;
		for (Challenge c: getChallengeList()){
			if (longestCombo < c.getLongestCombo()) longestCombo = c.getLongestCombo();
		}
		return longestCombo;
	}

	public Difficulty getDifficulty(){
		return Difficulty.Standard;
	}
	
	public void setUsedSpecial(){
		usedSpecial = true;
	}
	
	public int getTime(){
		return 0;
	}
	
	public void pendValidBonus(Bonus newBonus){
		pendingBonuses.add(newBonus);
	}
	
	protected void addPendingBonuses(){
		for (Bonus newBonus: pendingBonuses){
			boolean shouldAdd = true;
			for (Bonus bonus: bonuses){
				if (bonus.getClass() == newBonus.getClass()) {
					if (newBonus instanceof Bonus.MultBonus) ((Bonus.MultBonus)bonus).increase();
					shouldAdd = false;
				}
			}
			if (shouldAdd) addBonus(newBonus);
		}
		pendingBonuses.clear();
	}
	
	private void addBonus(Bonus b){
		bonuses.add(b);
	}
	
	public void wipePendingBonuses(){
		pendingBonuses.clear();
	}

	abstract List<Challenge> getChallengeList();
	abstract Victory getVictory();

}
