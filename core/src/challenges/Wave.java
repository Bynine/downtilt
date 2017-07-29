package challenges;

import java.util.ArrayList;
import java.util.List;

public class Wave {

	private List<EnemySpawner> fSList = new ArrayList<EnemySpawner>();
	private int numEnemies = 0;
	private int baseKilled = 0;
	SpecialEffect specialEffect;

	public Wave(EnemySpawner es){
		fSList.add(es);
		setup();
	}

	public void update(float deltaTime){
		numEnemies = 0;
		for (EnemySpawner fs: fSList) {
			fs.update(deltaTime);
			numEnemies += fs.getAmount() + fs.getNumActiveEnemies();
		}
	}

	void restart(){
		setup();
	}

	private void setup(){
		for (EnemySpawner fs: fSList) fs.restart();
	}
	
	public int getNumEnemies(){
		return numEnemies;
	}
	
	public int getNumKilled(){
		int killed = baseKilled;
		for (EnemySpawner fs: fSList) killed += fs.getKilled();
		return killed;
	}
	
	enum SpecialEffect{
		NONE
	}

	public void setNumKilled(int numKilled) {
		baseKilled = numKilled;
	}

	public boolean isEndless() {
		return fSList.get(0).getAmount() == EnemySpawner.ENDLESS;
	}

}
