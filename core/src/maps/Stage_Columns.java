package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Columns extends Stage {

	public Stage_Columns(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/glow.mp3"));
		setup();
		name = "STANDARD";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/columns.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(21 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}

}
