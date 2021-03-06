package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Blocks extends Stage {

	public Stage_Blocks(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/leaf.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/blocks.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(22 * GlobalRepo.TILE, 9 * GlobalRepo.TILE);
	}
	
	private static final int num = 3;
	public static int getStaticNumber(){
		return num;
	}
	public int getNumber(){
		return num;
	}
	
	public static String getName(){
		return "Puzzle Palace";
	}

}
