package entities;

import java.util.List;

import main.DowntiltEngine;
import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class ChangeBlock extends ImmobileEntity {

	Color color = Color.A;
	protected TextureRegion empty;
	protected TextureRegion full ;

	public ChangeBlock(float posX, float posY) {
		super(posX, posY);
	}

	public enum Color{
		A, B, C
	}

	public void update(List<Entity> entityList){
		int updateCounter = 180;
		if (DowntiltEngine.getDeltaTime() % updateCounter == (updateCounter-1) ) rotate(entityList);
	}
	
	private void rotate(List<Entity> entityList){
		switch(color){
		case A: color = Color.B; break;
		case B: color = Color.C; break;
		case C: color = Color.A; break;
		}
		if (isFull()){
			setImage(full);
			collision = Collision.SOLID;
			for (Entity en: entityList){
				if (en instanceof Fighter && isTouching(en, 0)) {
					new SFX.Tech().play();
					((Fighter)en).reset();
				}
			}
		}
		else{
			setImage(empty);
			collision = Collision.GHOST;
		}
	}

	boolean isFull() {
		return color == correctColor();
	}

	abstract Color correctColor();

	public static class BlockA extends ChangeBlock{

		public BlockA(float posX, float posY) {
			super(posX, posY);
			empty = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/blockaempty.png")));
			full  = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/blockafull.png")));
			setImage(empty);
		}

		Color correctColor() {
			return Color.A;
		}

	}

	public static class BlockB extends ChangeBlock{

		public BlockB(float posX, float posY) {
			super(posX, posY);
			empty = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/blockbempty.png")));
			full  = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/blockbfull.png")));
			setImage(empty);
		}

		Color correctColor() {
			return Color.B;
		}

	}

	public static class BlockC extends ChangeBlock{

		public BlockC(float posX, float posY) {
			super(posX, posY);
			empty = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/blockcempty.png")));
			full  = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/blockcfull.png")));
			setImage(empty);
		}

		Color correctColor() {
			return Color.C;
		}

	}

}
