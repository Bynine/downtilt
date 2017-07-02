package maps;

import main.GlobalRepo;
import main.MapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import entities.Hurlable;

public class Stage_Mushroom extends Stage {

	public Stage_Mushroom(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/heartbeat.mp3"));
		setup();
		name = "STANDARD";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/mushroom.tmx");
	}
	
	public void update(int deltaTime){
		super.update(deltaTime);
		if (Math.random() < 0.05 && deltaTime % 15 == 0){
			if (Math.random() < 0.5) MapHandler.addEntity(new Hurlable.Nut(12 * GlobalRepo.TILE, 14 * GlobalRepo.TILE));
			else 					 MapHandler.addEntity(new Hurlable.Nut(30 * GlobalRepo.TILE, 14 * GlobalRepo.TILE));
		}
	}

	public Vector2 getStartPosition() {
		return new Vector2(22 * GlobalRepo.TILE, 5 * GlobalRepo.TILE);
	}

}
