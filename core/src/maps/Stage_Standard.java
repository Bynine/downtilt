package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Standard extends Stage {

	public Stage_Standard(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/stroll.mp3"));
		setup();
		name = "Ruins Rumble";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/standard.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(22 * GlobalRepo.TILE, 7 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(22 * GlobalRepo.TILE, 4 * GlobalRepo.TILE);
	}

}
