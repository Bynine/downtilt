package challenges;

import java.util.ArrayList;
import java.util.List;

import challenges.Challenge.Difficulty;
import entities.Fighter;
import main.DowntiltEngine;
import main.SFX;

public abstract class Mode {

	protected int activeChallengeIndex = 0;
	protected final List<Bonus> upcomingBonuses = new ArrayList<Bonus>(), bonuses = new ArrayList<Bonus>();
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
		if (DowntiltEngine.musicOn()) {
			float vol = (DowntiltEngine.getMusicVolume()) / 8.0f;
			getActiveChallenge().getStage().getMusic().setVolume(vol);
			getActiveChallenge().getStage().getMusic().play();
			if (DowntiltEngine.isWaiting()) getActiveChallenge().getStage().getMusic().stop();
		}
		if (!getActiveChallenge().started) getActiveChallenge().startChallenge();
		getActiveChallenge().update();
		if (!DowntiltEngine.isWaiting()  && getActiveChallenge().finished()) {
			for (Fighter player: DowntiltEngine.getPlayers()) player.refresh();
			getActiveChallenge().getStage().getMusic().stop();
			activeChallengeIndex++;
		}
		if (activeChallengeIndex > getChallengeList().size()) win();
	}

	/**
	 * Activates when all challenges are completed.
	 */
	void win(){
		new SFX.Victory().play();
		boolean unstoppable = true;
		boolean immortal = true;
		for (Challenge c: getChallengeList()){
			if (c.everRetried) immortal = false;
			if (c.everFailed) unstoppable = false;
		}
		if (unstoppable) addBonus(new Bonus.UnstoppableBonus());
		if (immortal) addBonus(new Bonus.ImmortalBonus());
		if (!usedSpecial) addBonus(new Bonus.NoSpecialBonus());
		DowntiltEngine.startVictoryScreen(getVictory());
	}

	int getCombo(){
		int longestCombo = 0;
		for (Challenge c: getChallengeList()){
			if (longestCombo < c.getLongestCombo()) longestCombo = c.getLongestCombo();
		}
		return longestCombo;
	}

	public Difficulty getDifficulty(){
		return Difficulty.Standard;
	}

	public void addBonus(Bonus newBonus){
		boolean shouldAdd = true;
		for (Bonus bonus: bonuses){
			if (bonus.getClass() == newBonus.getClass()) {
				if (newBonus instanceof Bonus.MultBonus) ((Bonus.MultBonus)bonus).increase();
				shouldAdd = false;
			}
		}
		if (shouldAdd) addValidBonus(newBonus);
	}
	
	protected void addValidBonus(Bonus newBonus){
		bonuses.add(newBonus);
	}
	
	public void setUsedSpecial(){
		usedSpecial = true;
	}
	
	public int getTime(){
		return 0;
	}

	abstract List<Challenge> getChallengeList();
	abstract Victory getVictory();

}
