package entities;

import main.SFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BreakableBlock extends Hittable {
	
	private TextureRegion tex = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/breakblock.png")));
	float health = 10;

	public BreakableBlock(float posX, float posY) {
		super(posX, posY);
		collision = Collision.SOLID;
	}
	
	protected void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		super.knockbackHelper(knockback, DAM, hitstun, shouldChangeKnockback, ht);
		health -= DAM;
		if (health <= 0) destroy();
	}
	
	private void destroy(){
		toRemove = true;
		new SFX.Break().play();
	}
	
	void updatePosition(){
		/* doesn't move */
	}
	
	void checkPushAway(Hittable hi){
		/* nope */
	}

	@Override
	TextureRegion getStandFrame(float deltaTime) {
		return tex;
	}

	@Override
	TextureRegion getTumbleFrame(float deltaTime) {
		return tex;
	}

}
