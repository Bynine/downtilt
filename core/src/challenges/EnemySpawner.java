package challenges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import inputs.InputHandlerCPU;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import main.DowntiltEngine;
import entities.Fighter;
import entities.Spawner;

public class EnemySpawner {
	private final List<EnemyType> enemyList = new ArrayList<EnemyType>();
	private int initAmount, amount, capacity, killed;
	private float frequency;
	private final List<Fighter> spawnedEntities = new ArrayList<Fighter>();
	private final Vector2 spawnPointA = new Vector2(), spawnPointB = new Vector2();
	boolean setup = false;

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
		if (!setup) setSpawnPoints();
		if (amount > 0 && spawnedEntities.size() < capacity) {
			if (deltaTime % frequency == frequency - 1) spawnNewEnemy();
		}
		else if (amount == ENDLESS && spawnedEntities.size() < capacity){
			if (deltaTime % frequency == 0) spawnNewEnemy();
		}

		Iterator<Fighter> spawnIter = spawnedEntities.iterator();
		while (spawnIter.hasNext()){
			Fighter spawn = spawnIter.next();
			if (spawn.isDead()) {
				spawnIter.remove();
				killed++;
			}
		}
	}
	
	private void setSpawnPoints(){
		setup = true;
		spawnPointA.set(DowntiltEngine.getChallenge().getStartPosition());
		spawnPointB.set(DowntiltEngine.getChallenge().getStartPosition());
		float dispX = DowntiltEngine.getChallenge().getStartDispX();
		float dispY = GlobalRepo.TILE * 2;
		spawnPointA.set(spawnPointA.x - dispX, spawnPointA.y + dispY);
		spawnPointB.set(spawnPointB.x + dispX, spawnPointB.y + dispY);
		MapHandler.addEntity(new Spawner(spawnPointA.x, spawnPointA.y));
		MapHandler.addEntity(new Spawner(spawnPointB.x, spawnPointB.y));
	}

	private void spawnNewEnemy(){
		Fighter enemy = null;
		EnemyType enemyType = getEnemy();
		try {
			enemy = enemyType.type.getDeclaredConstructor(float.class, float.class, int.class).newInstance(getSpawnPoint().x, getSpawnPoint().y, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		enemy.setInputHandler(new InputHandlerCPU(enemy, enemyType.brain));
		switch (enemyType.powerUp){
		case POWER: enemy.setPermaPower(); break;
		case DEFENSE: enemy.setPermaDefense(); break;
		case SPEED: enemy.setPermaSpeed(); break;
		case AIR: enemy.setPermaAir(); break;
		case WEAK: enemy.setPercentage(enemy.getWeight() * (0.65f)); break;
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
		new SFX.Spawn().play();
		if (amount != ENDLESS) amount--;
	}

	EnemyType getEnemy(){
		EnemyType en = enemyList.get(0);
		Collections.rotate(enemyList, -1);
		return en;
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
	
	private Vector2 getSpawnPoint(){
		if (Math.random() < 0.5) return spawnPointA;
		else return spawnPointB;
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
