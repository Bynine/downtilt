package moves;

import com.badlogic.gdx.math.Vector2;

import entities.Basic;
import entities.Entity;
import entities.Fighter;
import entities.Explosion;
import main.DowntiltEngine;
import main.MapHandler;
import main.SFX;
import moves.Effect.Charge;

public abstract class Action {

	abstract void performAction();

	public static class MakeActionCircle extends Action{
		ActionCircle ac;
		
		MakeActionCircle(ActionCircle ac){
			this.ac = ac;
		}

		void performAction(){
			ac.checkGroup();
			if (ac.toRemove()) return;
			ac.updatePosition();
			MapHandler.addActionCircle(ac);
		}
	}
	
	public static class ChangeVelocity extends Action{
		float velX, velY;
		Fighter user;
		Charge charge = null;
		public static final float noChange = -999f;
		
		ChangeVelocity(Fighter user, float velX, float velY){
			this.user = user;
			this.velX = velX;
			this.velY = velY;
		}
		
		public ChangeVelocity(Fighter user, float velX, float velY, Charge c) {
			this(user, velX, velY);
			charge = c;
		}

		void performAction(){
			if (null != charge){
				if (velX != noChange) velX *= Math.pow(charge.getHeldCharge(), 2);
				if (velY != noChange) velY *= Math.pow(charge.getHeldCharge(), 2);
			}
			if (velX != noChange) user.getVelocity().x = velX * user.direct();
			if (velY != noChange) user.getVelocity().y = velY;
		}
	}
	
	public static class PlaySFX extends Action{
		final SFX sfx;
		
		PlaySFX(SFX sfx){
			this.sfx = sfx;
		}
		
		void performAction() {
			sfx.play();
		}
		
	}
	
	public static class MakeProjectile<T extends Explosion> extends Action{
		final Class<T> proj;
		final Fighter user;

		public MakeProjectile (Fighter user, Class<T> proj){
			this.proj = proj;
			this.user = user;
		}
		
		void performAction() {
			try { MapHandler.addEntity(proj.getConstructor(float.class, float.class, Fighter.class)
					.newInstance(user.getPosition().x, user.getPosition().y, user));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static class Invincible extends Action {
		final Fighter user;
		final int start, end;
		
		public Invincible(Fighter user, int start, int end){
			this.user = user;
			this.start = start;
			this.end = end;
		}
		
		void performAction() {
			user.setInvincible(end - start);
		}

	}
	
	public static class UseSpecial extends Action {
		final Fighter user;
		final float drain;
		
		public UseSpecial(Fighter user, float drain){
			this.user = user;
			this.drain = drain;
		}

		void performAction() {
			if (DowntiltEngine.getChallenge().getSpecialMeter() < -drain) user.interruptSpecialAttack();
			else DowntiltEngine.getChallenge().changeSpecial(drain);
		}
	}
	
	public static class Explode extends Action {
		final Fighter user;
		final int time;
		
		public Explode(Fighter user, int time){
			this.user = user;
			this.time = time;
		}

		void performAction() {
			((Basic.Bomb)user).setBomb(time);
		}
	}
	
	public static class AddEntity extends Action {
		final Entity en, user;
		final float velX, velY;
		float dispX, dispY;
		
		public AddEntity(Entity user, Entity en, float velX, float velY){
			this.user = user;
			this.en = en;
			this.velX = velX;
			this.velY = velY;
			dispX = 0;
			dispY = 0;
		}
		
		public AddEntity(Entity user, Entity en, float velX, float velY, float dispX, float dispY){
			this(user, en, velX, velY);
			this.dispX = dispX;
			this.dispY = dispY;
		}

		void performAction() {
			en.setPosition(user.getPosition().add(new Vector2(dispX, dispY)));
			en.getVelocity().x = velX;
			en.getVelocity().y = velY;
			MapHandler.addEntity(en);
		}
	}
	
	public static class ActivateGuard extends Action {
		final Fighter user;
		final int time;
		
		public ActivateGuard(Fighter user, int time){
			this.user = user;
			this.time = time;
		}

		void performAction() {
			user.setGuard(time);
		}
		
	}
	
	public static class Slow extends Action {
		final int time;
		
		public Slow( int time){
			this.time = time;
		}

		void performAction() {
			new SFX.Slow().play();
			DowntiltEngine.slow(time);
		}
		
	}

	
}
