package maps;

import main.GlobalRepo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Rooftop extends Stage {

	public Stage_Rooftop(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/spook.mp3"));
		dispX = GlobalRepo.TILE * 4;
		setup();
		activeLightningHandler = new LightningHandler();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/flat.tmx");
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
		rain();
	}

	public Vector2 getStartPosition() {
		return new Vector2(22 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
	
	public Vector2 getCenterPosition(){
		return new Vector2(22 * GlobalRepo.TILE, 3 * GlobalRepo.TILE);
	}
	
	private static final int num = 1;
	public static int getStaticNumber(){
		return num;
	}
	public int getNumber(){
		return num;
	}
	
	public static String getName(){
		return "Monster Mash Manor";
	}

}
