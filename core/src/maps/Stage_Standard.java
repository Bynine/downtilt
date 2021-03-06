package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Standard extends Stage {

	public Stage_Standard(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/stroll.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/standard.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(21.5f * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(22 * GlobalRepo.TILE, 4 * GlobalRepo.TILE);
	}
	
	private static final int num = 0;
	public static int getStaticNumber(){
		return num;
	}
	public int getNumber(){
		return num;
	}
	
	public static String getName(){
		return "Ruins Rumble";
	}

}
