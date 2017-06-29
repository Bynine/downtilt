package challenges;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import entities.CombatStarter;
import entities.Fighter;
import entities.TreasureChest;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import main.DowntiltEngine;
import maps.*;

public abstract class Challenge {

	protected final List<Stage> stageList = new ArrayList<Stage>();
	protected final List<Wave> prevWaves = new ArrayList<Wave>();
	protected Wave activeWave = null;
	protected CombatStarter activeCombatStarter = null;
	protected int place = 0;
	protected long score = 0;
	protected int numLives = 5;
	protected Mode mode = Mode.ADVENTURE;
	protected final Vector2 combatPosition = new Vector2(0, 0);
	private boolean combatNotEnded = false;
	final int difficulty;

	Challenge(int difficulty){
		this.difficulty = difficulty;
	}

	int waitBetween = 60;
	protected void begin(){
		for (Fighter player: (DowntiltEngine.getPlayers() ) ){
			player.setLives(numLives);
		}
		startStage();
	}

	public void update(){
		if (isInCombat()) activeWave.update(DowntiltEngine.getDeltaTime());
		if (activeWave.getNumEnemies() == 0 && combatNotEnded) endCombat();
		boolean shouldRestart = true;
		for (Fighter player: (DowntiltEngine.getPlayers() ) ){
			if (player.getLives() > 0) shouldRestart = false;
		}
		if (shouldRestart) restart();
	}

	public void goToNextStage(){
//		place++;
//		if (place >= stageList.size()) win();
//		else startStage();
	}

	protected void startStage(){
		DowntiltEngine.changeRoom(stageList.get(place));
		activeWave = WaveGenerator.generate(difficulty);
		if (!DowntiltEngine.debugToggle){
			ChallengeGraphicsHandler.readyGo();
			DowntiltEngine.wait(waitBetween);
		}
		for (Fighter player: DowntiltEngine.getPlayers()) player.refresh();
		MapHandler.addEntity(new TreasureChest(combatPosition.x, combatPosition.y + GlobalRepo.TILE));
	}

	void win(){
		new SFX.Victory().play();
		DowntiltEngine.returnToMenu();
		restart();
	}

	public Wave getActiveCombat(){
		return activeWave;
	}

	public boolean isInCombat(){
		return mode == Mode.COMBAT;
	}

	public Vector2 getCombatPosition(){
		return combatPosition;
	}

	public void startCombat(CombatStarter cs, Vector2 position) {
		startCombatHelper(cs, position);
		Vector2 spawnPoint = new Vector2(position.x, position.y + GlobalRepo.TILE * 2);
		for (Fighter player: DowntiltEngine.getPlayers()) player.setRespawnPoint(spawnPoint);
		if (DowntiltEngine.getPlayers().size() > 1){
			for (Fighter player: DowntiltEngine.getPlayers().subList(1,  DowntiltEngine.getPlayers().size())){
				player.refresh();
			}
		}
		if (cs instanceof CombatStarter.EndCombatStarter){
			activeWave = WaveGenerator.generate(difficulty + 1);
		}
		else activeWave = WaveGenerator.generate(difficulty);
		prevWaves.add(activeWave);
	}

	protected void startCombatHelper(CombatStarter cs, Vector2 position){
		activeCombatStarter = cs;
		combatPosition.set(position);
		mode = Mode.COMBAT;
		combatNotEnded = true;
	}

	int inc = 0;
	private void endCombat(){
		MapHandler.addEntity(new TreasureChest(combatPosition.x, combatPosition.y + GlobalRepo.TILE));
		if (difficulty != WaveGenerator.DIFF_ZEN){
			inc++;
			activeWave = WaveGenerator.generate(difficulty + inc);
			prevWaves.add(activeWave);
		}
		else if (difficulty == WaveGenerator.DIFF_ZEN){
			activeWave = WaveGenerator.generate(difficulty);
			prevWaves.add(activeWave);
		}
		//combatNotEnded = false;
	}

	public void restart(){
		DowntiltEngine.returnToMenu();
//		prevWaves.clear();
//		for (Fighter player: DowntiltEngine.getPlayers()) player.refresh();
//		place = 0;
//		MapHandler.resetRoom();
//		begin();
	}

	protected Stage getRoomByRound(int position){
		return new Stage_Standard();
	}

	public long getScore(){
		return score;
	}

	protected static enum Mode{
		COMBAT, ADVENTURE
	}

	public void score(int i) {
		score += i;
	}
	
	public abstract String getEnemyCounter();

}
