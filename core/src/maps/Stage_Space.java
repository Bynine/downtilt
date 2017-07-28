package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import main.GlobalRepo;

public class Stage_Space extends Stage {

	public Stage_Space(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/spook.mp3"));
		setup();
		name = "SPACE";
		dispX = GlobalRepo.TILE * 4;
		gravity = 0.7f;
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/space.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(22 * GlobalRepo.TILE, 7 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(22 * GlobalRepo.TILE, 7 * GlobalRepo.TILE);
	}

}
