package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timers.DurationTimer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import main.MapHandler;
import main.SFX;
import moves.ActionCircle;
import moves.ActionCircleGroup;
import moves.HazardHitbox;
import moves.Hitbox;
import moves.Hitbox.Property;

public abstract class Hazard extends Entity {

	public Hazard(float posX, float posY) {
		super(posX, posY);
	}

	abstract List<ActionCircle> getActionCircles();

	void handleTouchHelper(Entity e){
		if (e instanceof Hittable) {
			for (ActionCircle ac: getActionCircles()){
				if (ac.didHitTarget((Hittable) e)) ac.hitTarget((Hittable) e);
			}
		}
	}

	/**
	 * Ceiling mounted spikes that knock targets straight downward.
	 */
	public static class Spikes extends Hazard{

		Hitbox h1;

		public Spikes(float posX, float posY) { 
			super(posX, posY); 
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/spikes.png"))));
			h1 = new HazardHitbox(this, 2.5f, 3.0f, 20, 270, 0, 0, 21, new SFX.SharpHit());
			h1.setRefresh(20);
			for (ActionCircle ac: getActionCircles()) MapHandler.addActionCircle(ac);
			gravity = 0;
		}

		@Override
		List<ActionCircle> getActionCircles() {
			return new ArrayList<ActionCircle>(Arrays.asList( h1 ));
		}

		void updatePosition(){
			/* doesn't move */
		}

	}

	/**
	 * A lightning bolt that strikes the rooftop stage periodically.
	 */
	public static class Lightning extends Hazard{

		Hitbox h1, h2, h3, h4, h5, h6, h7, h8;
		List<Hitbox> hitboxes = new ArrayList<Hitbox>(Arrays.asList(h1, h2, h3, h4, h5, h6, h7, h8));
		List<ActionCircle> finale =  new ArrayList<ActionCircle>();
		DurationTimer life = new DurationTimer(25);
		ActionCircleGroup acg = new ActionCircleGroup();

		public Lightning(float posX, float posY) {
			super(posX, posY);
			new SFX.Lightning().play();
			timerList.add(life);
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/lightning.png"))));
			
			int inc = 64;
			for (Hitbox h: hitboxes){
				h = new HazardHitbox(this, 4.5f, 3.0f, 20, 45, 0, inc, 32, new SFX.SharpHit());
				h.setProperty(Property.STUN);
				inc -= 64;
				finale.add(h);
				MapHandler.addActionCircle(h);
				acg.addActionCircle(h);
			}
		}

		@Override
		List<ActionCircle> getActionCircles() {
			return finale;
		}

		void updatePosition(){
			if (life.timeUp()) {
				for (ActionCircle ac: acg.getConnectedCircles()) ac.remove();
				setRemove();
			}
		}

	}
	
	/**
	 * Front of the truck, hits forward and up
	 */
	public static class TruckHead extends Hazard{

		Hitbox h1;

		public TruckHead(float posX, float posY) { 
			super(posX, posY); 
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/bounce.png"))));
			h1 = new HazardHitbox(this, 5.5f, 5.0f, 20, 55, 0, 0, 24, new SFX.HeavyHit());
			h1.setRefresh(20);
			for (ActionCircle ac: getActionCircles()) MapHandler.addActionCircle(ac);
			gravity = 0;
		}

		@Override
		List<ActionCircle> getActionCircles() {
			return new ArrayList<ActionCircle>(Arrays.asList( h1 ));
		}

		void updatePosition(){
			/* doesn't move */
		}

	}

}
