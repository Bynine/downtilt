package challenges;

import java.util.ArrayList;
import java.util.List;

import challenges.Challenge.Difficulty;
import entities.Fighter;
import main.DowntiltEngine;
import main.SFX;

public abstract class Mode {
	
	protected int activeChallengeIndex = 0;
	protected final List<Bonus> bonuses = new ArrayList<Bonus>();

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
			getActiveChallenge().getStage().getMusic().setVolume(DowntiltEngine.getMusicVolume() / 8.0f);
			getActiveChallenge().getStage().getMusic().play();
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
	
	public void addBonus(Bonus b){
		bonuses.add(b);
	}

	abstract List<Challenge> getChallengeList();
	abstract Victory getVictory();
	
}
