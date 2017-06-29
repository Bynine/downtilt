package challenges;

import inputs.Brain;
import entities.Fighter;

public class Enemy {
	public final Class<? extends Fighter> type;
	public final Class<? extends Brain> brain;

	Enemy (Class<? extends Fighter> type, Class<? extends Brain> brain){
		this.type = type;
		this.brain = brain;
	}
	
}
