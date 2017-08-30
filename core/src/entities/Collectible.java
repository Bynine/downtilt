package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import timers.*;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;
import main.DowntiltEngine;

public abstract class Collectible extends Entity {
	
	private final Timer noTouchie = new DurationTimer(40);
	protected int bonus = 0;

	public Collectible(float posX, float posY) {
		super(posX, posY);
		timerList.add(noTouchie);
		airFrictionX = 0.94f;
	}

	void handleTouchHelper(Entity e){
		if (e instanceof Fighter){
			Fighter fi = (Fighter) e;
			if (isTouching(fi, 0) && fi.getTeam() == GlobalRepo.GOODTEAM && noTouchie.timeUp()) collect(fi);
		}
	}
	
	protected void collect(Fighter fi){
		new SFX.Collect().play();
		DowntiltEngine.getChallenge().addScore(bonus);
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
	
	public void dispose(){
		image.getTexture().dispose();
	}
	
	/* COLLECTIBLES */
	
//	public static class Food extends Collectible {
//
//		public Food(float posX, float posY) {
//			super(posX, posY);
//			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/food.png"))));
//			bonus = 30;
//		}
//
//		void collectHelper(Fighter fi) {
//			fi.setPercentage(MathUtils.clamp(fi.getPercentage() - restoredHealth(), 0, 999));
//		}
//		
//		private float restoredHealth(){
//			return (float) ( 20 + (Math.random() * 15) );
//		}
//
//	}
//	
//	public static class Ammo extends Collectible {
//
//		public Ammo(float posX, float posY) {
//			super(posX, posY);
//			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/ammo.png"))));
//			bonus = 30;
//		}
//
//		void collectHelper(Fighter fi){
//			fi.changeSpecial(4);
//		}
//
//	}
//	
//	public static class Treasure extends Collectible {
//
//		public Treasure(float posX, float posY) {
//			super(posX, posY);
//			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/treasure.png"))));
//			bonus = 100;
//		}
//
//	}
	
	private abstract static class StatBooster extends Collectible {

		public StatBooster(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_attack.png"))));
			bonus = 20;
		}
		
		void collectHelper(Fighter fi){
			addStat(fi);
		}
		
		abstract void addStat(Fighter fi);

	}
	
	public static class BoostPower extends StatBooster {
		public BoostPower(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_attack.png"))));
		}
		void addStat(Fighter fi){
			fi.addPower(Hittable.BOOSTTIMERDEFAULT);
			MapHandler.addEntity(new Graphic.BoostMessage(fi, (new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/upatk.png"))))));
		}
	}
	public static class BoostDefense extends StatBooster {
		public BoostDefense(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_defend.png"))));
		}
		void addStat(Fighter fi){
			fi.addDefense(Hittable.BOOSTTIMERDEFAULT);
			MapHandler.addEntity(new Graphic.BoostMessage(fi, (new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/updef.png"))))));
		}
	}
	public static class BoostSpeed extends StatBooster {
		public BoostSpeed(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_speed.png"))));
		}
		void addStat(Fighter fi){
			fi.addSpeed(Hittable.BOOSTTIMERDEFAULT);
			MapHandler.addEntity(new Graphic.BoostMessage(fi, (new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/upspeed.png"))))));
		}
	}
	public static class BoostAir extends StatBooster {
		public BoostAir(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_air.png"))));
		}
		void addStat(Fighter fi){
			fi.addAir(Hittable.BOOSTTIMERDEFAULT);
			MapHandler.addEntity(new Graphic.BoostMessage(fi, (new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/upair.png"))))));
		}
	}
	public static class BoostAll extends StatBooster {
		public BoostAll(float posX, float posY) {
			super(posX, posY);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/statboost_all.png"))));
		}
		void addStat(Fighter fi){
			fi.addAll(Hittable.BOOSTTIMERRUSH);
			MapHandler.addEntity(new Graphic.BoostMessage(fi, (new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/upall.png"))))));
		}
	}



}


