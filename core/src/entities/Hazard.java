package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import main.MapHandler;
import main.SFX;
import moves.ActionCircle;
import moves.HazardHitbox;
import moves.Hitbox;

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
	
	public static class Spikes extends Hazard{
		
		Hitbox h1;

		public Spikes(float posX, float posY) { 
			super(posX, posY); 
			image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/spikes.png"))));
			h1 = new HazardHitbox(this, 6.0f, 3.0f, 20, 270, 0, 0, 24, new SFX.SharpHit());
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
