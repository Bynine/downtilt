package entities;

import java.util.List;

import main.GlobalRepo;
import main.MapHandler;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class ChangeBlock extends ImmobileEntity {

	GlobalRepo.BlockColor color = GlobalRepo.getBlockColor();
	protected TextureRegion empty;
	protected TextureRegion full ;
	private static final Timer jesusChristChangeBlockJustDontFuckinKillAnymoreHowBoutThat = new Timer(2);

	public ChangeBlock(float posX, float posY) {
		super(posX, posY);
		timerList.add(jesusChristChangeBlockJustDontFuckinKillAnymoreHowBoutThat);
	}

	public void update(List<Entity> entityList){
		if (color != GlobalRepo.getBlockColor()) rotate(entityList);
	}

	protected void rotate(List<Entity> entityList){
		color = GlobalRepo.getBlockColor();
		if (isFull()){
			setImage(full);
			collision = Collision.SOLID;
			if (null != entityList){
				for (Entity en: entityList){
					if (en instanceof Fighter && isTouching(en, 0) && !((Fighter)en).noKill() &&
							jesusChristChangeBlockJustDontFuckinKillAnymoreHowBoutThat.timeUp()) {
						MapHandler.kill((Fighter)en);
						jesusChristChangeBlockJustDontFuckinKillAnymoreHowBoutThat.reset();
					}
				}
			}
		}
		else{
			setImage(empty);
			collision = Collision.GHOST;
		}
	}
	
	protected void setup(String empty, String full){
		this.empty = new TextureRegion(new Texture(Gdx.files.internal(empty)));
		this.full  = new TextureRegion(new Texture(Gdx.files.internal(full)));
		setImage(this.empty);
		rotate(null);
	}
	
	public void dispose(){
		empty.getTexture().dispose();
		full.getTexture().dispose();
	}

	boolean isFull() {
		return color == correctColor();
	}

	abstract GlobalRepo.BlockColor correctColor();

	public static class BlockR extends ChangeBlock{

		public BlockR(float posX, float posY) {
			super(posX, posY);
			setup("sprites/entities/blockaempty.png", "sprites/entities/blockafull.png");
		}

		GlobalRepo.BlockColor correctColor() {
			return GlobalRepo.BlockColor.R;
		}

	}

	public static class BlockG extends ChangeBlock{

		public BlockG(float posX, float posY) {
			super(posX, posY);
			setup("sprites/entities/blockbempty.png", "sprites/entities/blockbfull.png");
		}

		GlobalRepo.BlockColor correctColor() {
			return GlobalRepo.BlockColor.G;
		}

	}

	public static class BlockB extends ChangeBlock{

		public BlockB(float posX, float posY) {
			super(posX, posY);
			setup("sprites/entities/blockcempty.png", "sprites/entities/blockcfull.png");
		}

		GlobalRepo.BlockColor correctColor() {
			return GlobalRepo.BlockColor.B;
		}

	}

}
