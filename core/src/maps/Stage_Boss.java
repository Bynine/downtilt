package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Boss extends Stage {

	public Stage_Boss(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/lock.mp3"));
		setup();
		name = "BOSS";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/boss.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(21 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(24 * GlobalRepo.TILE, 8 * GlobalRepo.TILE);
	}

}
