package challenges;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import inputs.InputHandlerCPU;
import main.GlobalRepo;
import main.MapHandler;
import main.DowntiltEngine;
import entities.Fighter;
import entities.Spawner;

public class EnemySpawner {
	private final List<EnemyType> enemyList = new ArrayList<EnemyType>();
	private int initAmount, amount, capacity, killed;
	private float frequency;
	private final List<Fighter> spawnedEntities = new ArrayList<Fighter>();
	private final Vector2 spawnPoint = new Vector2();

	public static final int ENDLESS = -5;

	EnemySpawner (List<EnemyType> enemyList, int amount, int capacity, float frequency){
		this.enemyList.addAll(enemyList);
		this.amount = amount;
		initAmount = amount;
		this.capacity = capacity;
		this.frequency = frequency;
	}

	EnemySpawner(EnemySpawner es){
		this.enemyList.addAll(es.enemyList);
		this.amount = es.amount;
		initAmount = es.amount;
		this.capacity = es.capacity;
		this.frequency = es.frequency;
	}

	void update(float deltaTime){
		if (amount > 0 && spawnedEntities.size() < capacity) {
			if (deltaTime % frequency == 1) createNewSpawner((int)frequency);
			if (deltaTime % frequency == frequency - 1) spawnNewEnemy();
		}
		else if (amount == ENDLESS && spawnedEntities.size() < capacity){
			if (deltaTime % frequency == 0) spawnNewEnemy();
		}

		Iterator<Fighter> spawnIter = spawnedEntities.iterator();
		while (spawnIter.hasNext()){
			Fighter spawn = spawnIter.next();
			if (spawn.getLives() == 0) {
				spawnIter.remove();
				killed++;
			}
		}
	}
	
	private void createNewSpawner(int frequency){
		setSpawnPoint();
		MapHandler.addEntity(new Spawner(spawnPoint.x, spawnPoint.y, (int) (frequency * 1.25) ));
	}
	
	private void setSpawnPoint(){
		spawnPoint.set(DowntiltEngine.getChallenge().getStartPosition());
		float dispX = DowntiltEngine.getChallenge().getStartDispX();
		float dispY = GlobalRepo.TILE * 3;
		if (Math.random() < 0.5f) spawnPoint.set(spawnPoint.x - dispX, spawnPoint.y + dispY);
		else spawnPoint.set(spawnPoint.x + dispX, spawnPoint.y + dispY);
	}

	private void spawnNewEnemy(){
		if (spawnPoint.x == 0) setSpawnPoint();
		Fighter enemy = null;
		EnemyType enemyType = getEnemy();
		try {
			enemy = enemyType.type.getDeclaredConstructor(float.class, float.class, int.class).newInstance(spawnPoint.x, spawnPoint.y, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		enemy.setInputHandler(new InputHandlerCPU(enemy, enemyType.brain));
		switch (enemyType.powerUp){
		case POWER: enemy.setPermaPower(); break;
		case DEFENSE: enemy.setPermaDefense(); break;
		case SPEED: enemy.setPermaSpeed(); break;
		case AIR: enemy.setPermaAir(); break;
		case ALL: {
			enemy.setPermaPower();
			enemy.setPermaDefense();
			enemy.setPermaSpeed();
			enemy.setPermaAir();
		} break;
		case NONE: break;
		}
		MapHandler.addEntity(enemy);
		spawnedEntities.add(enemy);
		if (amount != ENDLESS) amount--;
	}

	EnemyType getEnemy(){
		EnemyType en = enemyList.get(0);
		rotate(enemyList);
		return en;
	}

	public <T> List<T> rotate(List<T> lst) {  // remove last element, add it to front of the ArrayList
		if (lst.size() == 0) return lst;
		T element = null;
		element = lst.remove( lst.size() - 1 );
		lst.add(0, element);
		return lst;
	}

	public void restart() {
		amount = initAmount;
		spawnedEntities.clear();
	}

	public int getNumActiveEnemies(){
		return spawnedEntities.size();
	}

	public void setToEndless(){
		amount = ENDLESS;
		initAmount = ENDLESS;
	}

	public int getAmount(){
		return amount;
	}

	public int getCapacity(){
		return capacity;
	}

	public int getKilled(){
		return killed;
	}

	public class RandomSpawner extends EnemySpawner{

		RandomSpawner(List<EnemyType> enemyList, int amount, int capacity, float frequency) {
			super(enemyList, amount, capacity, frequency);
		}

		EnemyType getEnemy(){
			return GlobalRepo.getRandomElementFromList(enemyList);
		}

	}
}
