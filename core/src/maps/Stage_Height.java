package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Height extends Stage {

	public Stage_Height() {
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/storm.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/height.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(14 * GlobalRepo.TILE, 5 * GlobalRepo.TILE);
	}

}
