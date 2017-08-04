package challenges;

import java.util.List;

import entities.Fighter;
import main.DowntiltEngine;
import main.SFX;

public abstract class Mode {
	
	protected int activeChallengeIndex = 0;

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
			getActiveChallenge().getStage().getMusic().setVolume(DowntiltEngine.getVolume() / 8.0f);
			getActiveChallenge().getStage().getMusic().play();
		}
		if (!getActiveChallenge().started) getActiveChallenge().startChallenge();
		getActiveChallenge().update();
		if (getActiveChallenge().finished()) {
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

	abstract List<Challenge> getChallengeList();
	abstract Victory getVictory();
	
}
