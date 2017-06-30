package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Mushroom extends Stage {

	public Stage_Mushroom(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/rave.mp3"));
		setup();
		name = "STANDARD";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/mushroom.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(22 * GlobalRepo.TILE, 5 * GlobalRepo.TILE);
	}

}
