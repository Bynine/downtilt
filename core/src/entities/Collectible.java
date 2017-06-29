package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import timers.*;
import main.GlobalRepo;
import main.SFX;
import main.DowntiltEngine;

public abstract class Collectible extends Entity {
	
	private final Timer noTouchie = new DurationTimer(40);
	protected int bonus = 0;

	public Collectible(float posX, float posY) {
		super(posX, posY);
		timerList.add(noTouchie);
		airFriction = 0.99f;
	}

	void handleTouchHelper(Entity e){
		if (e instanceof Fighter){
			Fighter fi = (Fighter) e;
			if (isTouching(fi, 0) && fi.getTeam() == GlobalRepo.GOODTEAM && noTouchie.timeUp()) collect(fi);
		}
	}
	
	protected void collect(Fighter fi){
		new SFX.Collect().play();
		DowntiltEngine.getChallenge().score(bonus);
		setRemove();
		collectHelper(fi);
	}

	void collectHelper(Fighter fi){
		/**/
	}
	
	public Color getColor(){
		if (noTouchie.timeUp()) return new Color(1, 1, 1, 1);
		else return new Color(1, 1, 1, 0.5f);
	}
	
	/* COLLECTIBLES */
	
	public static class Food extends Collectible {

		public Food(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/food.png"))));
			bonus = 30;
		}

		void collectHelper(Fighter fi) {
			fi.setPercentage(MathUtils.clamp(fi.getPercentage() - restoredHealth(), 0, 999));
		}
		
		private float restoredHealth(){
			return (float) ( 20 + (Math.random() * 15) );
		}

	}
	
	public static class Drink extends Collectible {

		public Drink(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ammo.png"))));
			bonus = 30;
		}

		void collectHelper(Fighter fi){
			fi.changeSpecial(4);
		}

	}
	
	public static class Treasure extends Collectible {

		public Treasure(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/treasure.png"))));
			bonus = 100;
		}

	}
	
	private abstract static class StatBooster extends Collectible {

		public StatBooster(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_attack.png"))));
			bonus = 20;
		}
		
		void collectHelper(Fighter fi){
			addStat(0.1f, fi);
		}
		
		abstract void addStat(float add, Fighter fi);

	}
	
	public static class BoostPower extends StatBooster {
		public BoostPower(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_attack.png"))));
		}
		void addStat(float add, Fighter fi){
			fi.addPower(add);
		}
	}
	public static class BoostDefense extends StatBooster {
		public BoostDefense(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_defend.png"))));
		}
		void addStat(float add, Fighter fi){
			fi.addDefense(add);
		}
	}
	public static class BoostSpeed extends StatBooster {
		public BoostSpeed(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_speed.png"))));
		}
		void addStat(float add, Fighter fi){
			fi.addSpeed(add);
		}
	}
	public static class BoostAir extends StatBooster {
		public BoostAir(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_air.png"))));
		}
		void addStat(float add, Fighter fi){
			fi.addAir(add);
		}
	}



}


