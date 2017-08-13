package maps;

import main.GlobalRepo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Sky extends Stage {
	
	boolean windPlay = false;

	public Stage_Sky(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/dance.mp3"));
		setup();
		dispX = GlobalRepo.TILE * 2;
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/sky.tmx");
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
	}
	
	public Vector2 getStartPosition() {
		return new Vector2(21 * GlobalRepo.TILE, 7 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(22 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
	
	private static final int num = 6;
	public static int getStaticNumber(){
		return num;
	}
	public int getNumber(){
		return num;
	}
	
	public static String getName(){
		return "Crazy Cloud Castle";
	}

}
