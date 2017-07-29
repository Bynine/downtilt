package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import main.DowntiltEngine;
import main.MapHandler;
import main.SFX;

public class Boss extends Hittable {

	private TextureRegion idle = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/boss/idle.png")));
	private int health = 333;

	public Boss(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(idle);
		grabbable = false;
	}

	public int getHealth(){
		return health;
	}

	private void die(){
		new SFX.Die().play();
		new SFX.Die().play();
		MapHandler.addEntity(new Graphic.Die(position.x - 32, position.y - 32));
		MapHandler.addEntity(new Graphic.Die(position.x + 64, position.y + 128));
		DowntiltEngine.getChallenge().succeedChallenge();
		setRemove();
	}

	@Override
	protected void takeKnockIntoKnockback(Vector2 knockback, float DAM, int hitstun){
		health -= DAM;
		hitstunTimer.setEndTime(1);
		hitstunTimer.reset();
	}

	@Override
	protected void setKnockIntoVelocity(Hittable hurtler){
		hurtler.getHitstunTimer().setEndTime(100);
		hurtler.getHitstunTimer().reset();
		hurtler.velocity.set(-20, 4);
	}

	@Override
	void updatePosition(){
		if (health < 0) die();
	}

	@Override
	TextureRegion getStandFrame(float deltaTime) {
		return idle;
	}

	@Override
	TextureRegion getTumbleFrame(float deltaTime) {
		return idle;
	}

}
