package maps;

import main.GlobalRepo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Sky extends Stage {
	
	boolean windPlay = false;

	public Stage_Sky(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/dance.mp3"));
		setup();
		name = "Crazy Cloud Castle";
		dispX = GlobalRepo.TILE * 2;
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/sky.tmx");
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
	}
	
	public Vector2 getStartPosition() {
		return new Vector2(21 * GlobalRepo.TILE, 7 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(22 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
//
//	public float getWind(){
//		float windStrength = -0.24f;
//		int directionTiming = 1500;
//		int windDuration = 210;
//		int windLeftTiming = 500;
//		int windRightTiming = 1250;
//		
//		int gx = (DowntiltEngine.getDeltaTime() % directionTiming);
//		if (gx == windLeftTiming + 1 || gx == windRightTiming + 1) windPlay = false;
//		else if ( (gx == windLeftTiming || gx == windRightTiming) && !windPlay) {
//			windPlay = true;
//			new SFX.Gust().play();
//		}
//		if (gx > windLeftTiming && gx < windLeftTiming + windDuration) return windStrength;
//		if (gx > windRightTiming && gx < windRightTiming + windDuration) return -windStrength;
//		else return 0;
//	}
	
	public int getNumber(){
		return 6;
	}

}
