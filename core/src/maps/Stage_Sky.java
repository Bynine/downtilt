package maps;

import main.DowntiltEngine;
import main.GlobalRepo;
import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Sky extends Stage {
	
	SFX gust = new SFX.Gust();

	public Stage_Sky(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/dance.mp3"));
		setup();
		name = "STANDARD";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/sky.tmx");
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
		rain();
	}


	public Vector2 getStartPosition() {
		return new Vector2(18 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(22 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}

	public float getWind(){
		float windStrength = 0.24f;
		int directionTiming = 1200;
		int windDuration = 240;
		int wind1 = 400;
		int wind2 = 1000;
		
		int gx = (DowntiltEngine.getDeltaTime() % directionTiming);
		//if (DowntiltEngine.getDeltaTime() == wind1 || DowntiltEngine.getDeltaTime() == wind2) gust.play();
		if (gx > wind1 && gx < wind1 + windDuration) return  windStrength;
		if (gx > wind2 && gx < wind2 + windDuration) return -windStrength;
		else return 0;
	}

}
