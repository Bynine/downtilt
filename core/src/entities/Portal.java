package entities;

import maps.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Portal extends ImmobileEntity {
	private Stage room;
	protected Vector2 destination = new Vector2();

	public Portal(float x, float y, Stage room, float desX, float desY){
		super(x, y);
		image = new Sprite(new Texture(Gdx.files.internal("sprites/portal.png")));
		this.room = room;
		destination.x = desX;
		destination.y = desY;
		updateImagePosition(0);
	}
	
	public Stage getRoom(){ 
		return room; 
	}

	public Vector2 getDestination(Fighter p){ 
		return destination; 
	}

}
