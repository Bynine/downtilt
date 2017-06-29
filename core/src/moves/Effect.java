package moves;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import main.MapHandler;
import entities.Fighter;
import entities.Graphic;

public abstract class Effect extends Action{

	protected int start, end;
	
	Effect(int start, int end){
		this.start = start;
		this.end = end;
	}
	
	public int getStart(){ return start; }
	public int getEnd() { return end; }
	public void moveForward(){
		start += 1;
		end += 1;
	}
	public boolean isActive(int position){
		return position > start && position < end;
	}
	abstract void finish();
	
	public static class ConstantVelocity extends Effect{
		float velX, velY;
		Fighter user;
		public static final float noChange = -999f;	
		ConstantVelocity(Fighter user, float velX, float velY, int start, int end){
			super(start, end);
			this.user = user;
			this.velX = velX;
			this.velY = velY;
		}
		void performAction(){
			if (velX != noChange) user.getVelocity().x = velX * user.direct();
			if (user.doesCollide(user.getPosition().x, user.getPosition().y + velY)) {
				velY = noChange; 
				return;
			}
			if (velY != noChange) user.getVelocity().y = velY;
		}
		void finish() { /* */ }
	}
	
	public static class ConstantVelocityAngled extends Effect {
		final Fighter user;
		final float velocity;
		boolean firstTime = true;
		Vector2 newVelocity, angle;
		
		ConstantVelocityAngled(Fighter user, int start, int end, float velocity){
			super(start, end);
			this.user = user;
			this.velocity = velocity;
		}
		void performAction() {
			if (firstTime){
				newVelocity = new Vector2(velocity, velocity);
				angle = new Vector2(user.getStickX(), -user.getStickY());
				newVelocity.setAngle(angle.angle());
			}
			user.getVelocity().x = newVelocity.x;
			user.getVelocity().y = newVelocity.y;
			if (firstTime) firstTime = false;
		}
		void finish() { /* */ }
	}
	
	public static class Charge extends Effect{
		private float heldCharge = 1;
		private final float chargeSpeed;
		private final Move move;
		private final Fighter user;
		private final int maxTime;
		public Charge(int start, int maxTime, float chargeSpeed, Fighter user, Move move) {
			super(start, start+1);
			this.chargeSpeed = chargeSpeed;
			this.move = move;
			this.user = user;
			this.maxTime = maxTime;
		}		
		void performAction() {
			if (user.isCharging() && move.duration.getCounter() <= maxTime) hold();
			heldCharge += chargeSpeed;
		}		
		private void hold(){
			this.end += 1;
			List<Integer> actionStartTimes = move.eventList.actionStartTimes;
			List<Effect> effectList = move.eventList.effectList;
			for (int i = 0; i < actionStartTimes.size(); ++i) actionStartTimes.set(i, actionStartTimes.get(i) + 1);
			for (Effect eff: effectList){
				if (eff != this) eff.moveForward();
			}
			move.addFrame();
			user.countDownAttackTimer();
		}	
		float getHeldCharge(){
			return heldCharge;
		}
		void finish() { /* */ }
	}
	
	public static class Armor extends Effect {
		private final Move move;
		private final float armor;
		Armor(Move move, int start, int end, float armor) {
			super(start, end);
			this.move = move;
			this.armor = armor;
		}
		void performAction() {
			move.setArmor(armor);
		}
		void finish() {
			move.setArmor(0);
		}
	}
	
	public static class GraphicEffect extends Effect {
		final Graphic g;
		boolean made = false;
		GraphicEffect(Fighter user, int start, int end, Graphic g) {
			super(start, end);
			this.g = g;
			g.setDuration(end - start);
		}
		void finish() { /* */ }
		void performAction() {
			if (!made) {
				MapHandler.addEntity(g);
				made = true;
			}
		}	
	}
	
}