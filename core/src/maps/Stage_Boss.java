package maps;

import main.GlobalRepo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Boss extends Stage {

	public Stage_Boss(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/glow.mp3"));
		setup();
		name = "BIG BAD BOSS BEATDOWN";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/boss.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(22 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
	
	@Override
	public Vector2 getCenterPosition(){
		return new Vector2(21.5f * GlobalRepo.TILE, 8 * GlobalRepo.TILE);
	}

}
