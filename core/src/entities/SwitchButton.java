package entities;

import main.GlobalRepo;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SwitchButton extends Hittable {

	static Timer cantBeHit = new Timer(90);
	private static TextureRegion green = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/switchblockgreen.png")));
	private static TextureRegion red = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/switchblockred.png")));
	private static TextureRegion blue = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/switchblockblue.png")));
	private static TextureRegion grey = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/switchblockgrey.png")));

	public SwitchButton(float posX, float posY) {
		super(posX, posY);
		timerList.add(cantBeHit);
		touchRadius = 0;
		grabbable = false;
		layer = Layer.MIDDLEFRONT;
	}

	void updatePosition(){
		/* doesn't move */
	}
	
	protected void setKnockIntoVelocity(Hittable hurtler){
		/* */
	}

	protected void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		if (cantBeHit.timeUp()) changeBlocks();
	}

	private void changeBlocks(){
		cantBeHit.reset();
		GlobalRepo.rotateBlocks();
	}
	
	@Override
	protected void checkHitByHurtlingObject(Hittable hi){
		if (!cantBeHit.timeUp()) return;
		else super.checkHitByHurtlingObject(hi);
	}

	@Override
	TextureRegion getStandFrame(float deltaTime) {
		return getBlockTexture();
	}

	@Override
	TextureRegion getTumbleFrame(float deltaTime) {
		return getBlockTexture();
	}

	private TextureRegion getBlockTexture(){
		if (!cantBeHit.timeUp()) return grey;
		switch(GlobalRepo.getBlockColor()){
		case R: return red;
		case B: return blue;
		case G: return green; 
		}
		return red;
	}

}
