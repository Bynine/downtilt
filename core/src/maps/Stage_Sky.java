package maps;

import main.DowntiltEngine;
import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Sky extends Stage {

	public Stage_Sky(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/glow.mp3"));
		setup();
		name = "STANDARD";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/sky.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(18 * GlobalRepo.TILE, 5 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(22 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}

	public float getWind(){
		float windStrength = 0.12f;
		int directionTiming = 800;
		int windDuration = 120;
		int wind1 = 300;
		int wind2 = 700;
		
		int gx = (DowntiltEngine.getDeltaTime() % directionTiming);
		if (gx > wind1 && gx < wind1 + windDuration) return  windStrength;
		if (gx > wind2 && gx < wind2 + windDuration) return -windStrength;
		else return 0;
	}

}
