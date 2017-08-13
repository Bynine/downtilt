package maps;

import main.GlobalRepo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Truck extends Stage {
	
	boolean windPlay = false;

	public Stage_Truck(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/lock.mp3"));
		setup();
		scrolls = true;
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/truck.tmx");
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
	}

	public Vector2 getStartPosition() {
		return new Vector2(24 * GlobalRepo.TILE, 7 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(24 * GlobalRepo.TILE, 4 * GlobalRepo.TILE);
	}
	

	public float getWind(){
		return -0.08f;
	}
	
	private static final int num = 2;
	public static int getStaticNumber(){
		return num;
	}
	public int getNumber(){
		return num;
	}
	
	public static String getName(){
		return "High-speed Highway Hoedown";
	}

}
