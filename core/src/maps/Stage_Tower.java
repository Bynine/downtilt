package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Tower extends Stage {

	public Stage_Tower(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/rave.mp3"));
		setup();
		name = "STANDARD";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/tower.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(23 * GlobalRepo.TILE, 9 * GlobalRepo.TILE);
	}
	
	public Vector2 getCamPosition(){
		return new Vector2(23 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}

}
