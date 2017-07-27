package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import main.GlobalRepo;

public class Stage_Colosseum extends Stage {

	public Stage_Colosseum(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/sand.mp3"));
		setup();
		name = "COLOSSEUM";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/colosseum.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(22 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(19 * GlobalRepo.TILE, 4 * GlobalRepo.TILE);
	}

}
