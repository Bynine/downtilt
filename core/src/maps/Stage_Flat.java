package maps;

import main.GlobalRepo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Flat extends Stage {

	public Stage_Flat(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/rave.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/flat.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(24 * GlobalRepo.TILE, 5 * GlobalRepo.TILE);
	}

}
