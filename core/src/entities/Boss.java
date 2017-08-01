package entities;

import main.DowntiltEngine;

public class Boss {

	public static final int MAXHEALTH = 250;
	private int health = MAXHEALTH;
	
	public void addHealth(int i){
		health += i;
		if (i < 0) DowntiltEngine.getChallenge().rotateEyes();
	}
	
	public void setHealth(int i){
		health = i;
	}
	
	public int getHealth(){
		return health;
	}
}
