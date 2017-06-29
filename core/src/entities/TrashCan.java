package entities;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class TrashCan extends Throwable {
	
	private TextureRegion normImage = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/trashcan.png")));
	private Animation tumbleImage = GlobalRepo.makeAnimation("sprites/entities/trashcanspin.png", 4, 1, 8, PlayMode.LOOP);

	public TrashCan(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/trashcan.png"))));
		baseKnockIntoDamage = 3f;
		hitstunDealtBonus = 12;
		airFriction = 0.986f;
		baseWeight = 130;
	}

	TextureRegion getStandFrame(float deltaTime) {
		return normImage;
	}

	TextureRegion getTumbleFrame(float deltaTime) {
		return tumbleImage.getKeyFrame(deltaTime);
	}

}
