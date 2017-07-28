package maps;

import timers.Timer;
import main.DowntiltEngine;
import main.GlobalRepo;
import main.MapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import entities.Graphic;
import entities.Hazard;

public class Stage_Rooftop extends Stage {
	
	private final LightningHandler lh1 = new LightningHandler();

	public Stage_Rooftop(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/rave.mp3"));
		dispX = GlobalRepo.TILE * 4;
		setup();
		name = "ROOFTOP";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/flat.tmx");
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
		lh1.update();
		rain();
	}

	public Vector2 getStartPosition() {
		return new Vector2(22 * GlobalRepo.TILE, 5 * GlobalRepo.TILE);
	}
	
	private class LightningHandler{
		Timer lightningTimer = new Timer(100);
		Vector2 lightningPos = new Vector2(0, 0);
		
		void update(){
			lightningTimer.countUp();
			if (lightningTimer.timeUp() && DowntiltEngine.getDeltaTime() % 30 == 0 && Math.random() < 0.13){
				lightningTimer.reset();
				lightningPos = makeNewLightningPos();
				MapHandler.addEntity(new Graphic.Sparks(lightningPos.x, lightningPos.y, lightningTimer.getEndTime() ));
			}
			if (lightningTimer.timeJustUp()){
				MapHandler.addEntity(new Hazard.Lightning(lightningPos.x, lightningPos.y));
			}
		}
		
		private Vector2 makeNewLightningPos(){
			Vector2 newLightningPos = getStartPosition();
			newLightningPos.x += (0.5 - Math.random()) * (10 * GlobalRepo.TILE);
			return newLightningPos;
		}
		
	}

}
