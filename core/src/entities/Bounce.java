package entities;

import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Bounce extends ImmobileEntity {

	final float bounceStrength = 5.0f;
	float bounceX = 0f, bounceY = bounceStrength;

	public Bounce(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/bounce.png"))));
	}

	public static class BounceLeft extends Bounce{

		public BounceLeft(float posX, float posY){
			super(posX, posY);
			bounceX = -bounceStrength;
		}

	}

	public static class BounceRight extends Bounce{

		public BounceRight(float posX, float posY){
			super(posX, posY);
			bounceX = bounceStrength;
		}

	}

	public float getBounceX(){
		return bounceX;
	}

	public float getBounceY(){
		return bounceY;
	}

	public void bounce(Entity entity) {
		new SFX.FootStool().play();
		if (entity.getVelocity().y > 0) entity.getVelocity().y = 0;
		entity.getVelocity().x = -entity.getVelocity().x * 0.8f + getBounceX();
		entity.getVelocity().y = -entity.getVelocity().y * 0.8f + getBounceY();
	}
	
	public void dispose(){
		image.getTexture().dispose();
	}

}
