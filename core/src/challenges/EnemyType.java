package challenges;

import inputs.Brain;
import entities.Fighter;

public class EnemyType {
	public final Class<? extends Fighter> type;
	public final Class<? extends Brain> brain;
	public final PowerUp powerUp;

	EnemyType (Class<? extends Fighter> type, Class<? extends Brain> brain){
		this.type = type;
		this.brain = brain;
		powerUp = PowerUp.NONE;
	}
	
	EnemyType (Class<? extends Fighter> type, Class<? extends Brain> brain, PowerUp powerUp){
		this.type = type;
		this.brain = brain;
		this.powerUp = powerUp;
	}
	
	public static enum PowerUp{
		NONE, POWER, DEFENSE, SPEED, AIR, ALL
	}
	
}
