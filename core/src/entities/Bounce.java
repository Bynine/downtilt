package entities;

import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Bounce extends ImmobileEntity {
	
	final float bounceStrength = 4.0f;
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

	public void bounce(Hittable hittable) {
		if (isTouching(hittable, 0)){
			new SFX.FootStool().play();
			hittable.getVelocity().x = -hittable.getVelocity().x + getBounceX();
			hittable.getVelocity().y = -hittable.getVelocity().y + getBounceY();
		}
	}

}
