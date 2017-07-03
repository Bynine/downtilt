package entities;

import java.util.ArrayList;
import java.util.List;

import main.GlobalRepo;
import main.MapHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

public class TreasureChest extends Hurlable {

	private TextureRegion closed = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/chestclosed.png")));
	private TextureRegion open = new TextureRegion(new Texture(Gdx.files.internal("sprites/entities/chestopen.png")));
	private Animation spin = GlobalRepo.makeAnimation("sprites/entities/chestspin.png", 4, 1, 8, PlayMode.LOOP);
	boolean opened = false;

	public TreasureChest(float posX, float posY) {
		super(posX, posY);
		image = new Sprite(closed);
		staticPercent = 40;
	}

	protected void knockbackHelper(Vector2 knockback, float DAM, int hitstun, boolean shouldChangeKnockback, HitstunType ht){
		if (knockbackIntensity(knockback) > 0 && !opened) open();
		super.knockbackHelper(knockback, DAM, hitstun, shouldChangeKnockback, ht);
	}

	private void open(){
		int numberOfCollectibles = 2;
		int numberOfPowerups = 1;
		for (int i = 0; i < numberOfCollectibles; ++i) addCollectible(spawnCollectible());
		for (int i = 0; i < numberOfPowerups; ++i) addCollectible(spawnPowerUp());
		opened = true;
	}
	
	private void addCollectible(Collectible co){
		float collectibleSpeed = 7;
		float angleVariance = 30;
		co.velocity.set(collectibleSpeed, collectibleSpeed);
		co.velocity.setAngle((float) ( (90 + (angleVariance/2)) - (Math.random() * angleVariance)));
		MapHandler.addEntity(co);
	}

	private Collectible spawnCollectible(){
		List<Collectible> potentialCollectibles = new ArrayList<Collectible>();
		potentialCollectibles.add(new Collectible.Food(position.x, position.y));
		potentialCollectibles.add(new Collectible.Ammo(position.x, position.y));
		return GlobalRepo.getRandomElementFromList(potentialCollectibles);
	}
	
	private Collectible spawnPowerUp(){
		List<Collectible> potentialCollectibles = new ArrayList<Collectible>();
		potentialCollectibles.add(new Collectible.BoostPower(position.x, position.y));
		potentialCollectibles.add(new Collectible.BoostDefense(position.x, position.y));
		potentialCollectibles.add(new Collectible.BoostSpeed(position.x, position.y));
		potentialCollectibles.add(new Collectible.BoostAir(position.x, position.y));
		potentialCollectibles.add(new Collectible.BoostPower(position.x, position.y));
		potentialCollectibles.add(new Collectible.BoostDefense(position.x, position.y));
		potentialCollectibles.add(new Collectible.BoostSpeed(position.x, position.y));
		potentialCollectibles.add(new Collectible.BoostAir(position.x, position.y));
		potentialCollectibles.add(new Collectible.BoostAll(position.x, position.y));
		return GlobalRepo.getRandomElementFromList(potentialCollectibles);
	}

	TextureRegion getStandFrame(float deltaTime) {
		if (opened) return open;
		else return closed;
	}

	TextureRegion getTumbleFrame(float deltaTime) {
		return spin.getKeyFrame(deltaTime);
	}

}
