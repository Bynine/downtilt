package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Walledin extends Stage {
	
	public Stage_Walledin(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/storm.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/wall.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(18 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
}
