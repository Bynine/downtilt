package entities;

import main.DowntiltEngine;

public class Boss {

	private static final int MAXHEALTH = 250;
	private int health = MAXHEALTH;
	
	public void setHealth(int i){
		health += i;
		if (i < 0) DowntiltEngine.getChallenge().rotateEyes();
	}
	
	public int getHealth(){
		return health;
	}
}
