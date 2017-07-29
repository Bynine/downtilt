package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import main.DowntiltEngine;
import main.GlobalRepo;
import main.MapHandler;
import main.SFX;

public abstract class BossEye extends Hittable {

	private boolean open = true;
	private final Boss boss;

	public BossEye(float posX, float posY, Boss boss) {
		super(posX, posY);
		this.boss = boss;
		grabbable = false;
	}

	public void toggleOpen(){
		open = !open;
	}

	public boolean isOpen(){
		return open;
	}

	public void fire(){
		for (int i = 0; i < 1; ++ i){
			MapHandler.addEntity(changeFireTrajectory(getFire()));
		}
	}

	private Hurlable changeFireTrajectory(Hurlable fire){
		float angle = fire.getVelocity().angle();
		angle += (0.5 - Math.random()) * 30;
		fire.getVelocity().setAngle(angle);
		return fire;
	}

	private void die(){
		new SFX.Die().play();
		new SFX.Die().play();
		MapHandler.addEntity(new Graphic.Die(position.x - 32, position.y - 32));
		MapHandler.addEntity(new Graphic.Die(position.x + 64, position.y + 128));
		setRemove();
		DowntiltEngine.getChallenge().succeedChallenge();
	}

	@Override
	protected void takeKnockIntoKnockback(Vector2 knockback, float DAM, int hitstun){
		if (!open) return;
		boss.setHealth((int)-DAM);
		hitstunTimer.setEndTime(1);
		hitstunTimer.reset();
	}

	@Override
	protected void setKnockIntoVelocity(Hittable hurtler){
		hurtler.setRemove();
	}

	@Override
	void updatePosition(){
		if (boss.getHealth() < 0) die();
	}

	TextureRegion getStandFrame(float deltaTime) { 
		if (open) return getOpen();
		else return getClosed();
	}
	TextureRegion getTumbleFrame(float deltaTime) { return getStandFrame(deltaTime); }

	abstract TextureRegion getOpen();
	abstract TextureRegion getClosed();
	abstract Hurlable getFire();
	abstract void bounce(Entity e);

	protected final float velForward = 11;
	protected final float velUp = 4;

	public static class Left extends BossEye{
		private TextureRegion idle, closed;
		public Left(float posX, float posY, Boss boss) {
			super(posX, posY, boss);
			idle = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/boss/left/idle.png")));
			closed = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/boss/left/closed.png")));
			image = new Sprite(idle);
		}
		TextureRegion getOpen() { return idle; }
		TextureRegion getClosed() { return closed; }
		Hurlable getFire(){
			Hurlable fire = new Hurlable.ShootBall(GlobalRepo.GOODTEAM, getPosition().x + image.getWidth(), getCenter().y);
			fire.getVelocity().set(velForward, velUp);
			return fire;
		}
		void bounce(Entity e){
			e.getVelocity().set(velForward, velUp);
		}
	}

	public static class Right extends BossEye{
		private TextureRegion idle, closed;
		public Right(float posX, float posY, Boss boss) {
			super(posX, posY, boss);
			idle = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/boss/right/idle.png")));
			closed = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/boss/right/closed.png")));
			image = new Sprite(idle);
		}
		TextureRegion getOpen() { return idle; }
		TextureRegion getClosed() { return closed; }
		Hurlable getFire(){
			Hurlable fire = new Hurlable.ShootBall(GlobalRepo.GOODTEAM, getPosition().x, getCenter().y);
			fire.getVelocity().set(-velForward, velUp);
			return fire;
		}
		void bounce(Entity e){
			e.getVelocity().set(-velForward, velUp);
		}
	}

	public static class Down extends BossEye{
		private TextureRegion idle, closed;
		public Down(float posX, float posY, Boss boss) {
			super(posX, posY, boss);
			idle = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/boss/down/idle.png")));
			closed = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/boss/down/closed.png")));
			image = new Sprite(idle);
		}
		TextureRegion getOpen() { return idle; }
		TextureRegion getClosed() { return closed; }
		Hurlable getFire(){
			Hurlable fire = new Hurlable.ShootBall(GlobalRepo.GOODTEAM, getCenter().x, getPosition().y);
			fire.getVelocity().set(0, -velForward);
			return fire;
		}
		void bounce(Entity e){
			e.getVelocity().set(0, -velForward);
		}
	}

}
