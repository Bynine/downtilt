package challenges;

import java.util.List;

import entities.Fighter;
import main.DowntiltEngine;

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
		if (DowntiltEngine.musicOn()) getActiveChallenge().getStage().getMusic().play();
		if (!getActiveChallenge().started) getActiveChallenge().startChallenge();
		getActiveChallenge().update();
		if (getActiveChallenge().finished()) {
			for (Fighter player: DowntiltEngine.getPlayers()) player.refresh();
			getActiveChallenge().getStage().getMusic().stop();
			activeChallengeIndex++;
		}
	}

	abstract List<Challenge> getChallengeList();
	
}
