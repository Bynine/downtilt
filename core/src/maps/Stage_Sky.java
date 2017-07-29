package maps;

import main.DowntiltEngine;
import main.GlobalRepo;
import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Sky extends Stage {
	
	boolean windPlay = false;

	public Stage_Sky(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/dance.mp3"));
		setup();
		name = "SKY CASTLE";
		dispX = GlobalRepo.TILE * 3;
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/sky.tmx");
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
		rain();
	}


	public Vector2 getStartPosition() {
		return new Vector2(19 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(22 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}

	public float getWind(){
		float windStrength = -0.24f;
		int directionTiming = 1000;
		int windDuration = 210;
		int windTiming = 500;
		
		int gx = (DowntiltEngine.getDeltaTime() % directionTiming);
		if (gx > windTiming) windPlay = false;
		else if (gx == windTiming && !windPlay) {
			windPlay = true;
			new SFX.Gust().play();
		}
		if (gx > windTiming && gx < windTiming + windDuration) return windStrength;
		else return 0;
	}

}
