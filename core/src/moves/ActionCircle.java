package moves;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.DowntiltEngine;
import moves.Hitbox.Property;
import timers.DurationTimer;
import timers.Timer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import entities.Fighter;
import entities.Hittable;
import entities.Hittable.HitstunType;

public abstract class ActionCircle {

	Hittable user;
	final float dispX, dispY;
	final Circle area;
	final Timer duration = new DurationTimer(5);
	final Timer refreshTimer = new DurationTimer(6);
	boolean initialHit = false, remove = false, doesRefresh = false, reflects = false, reverse = true;
	float movesAheadMod = 1;
	final List<Hittable> hitFighterList = new ArrayList<Hittable>();
	private Set<Hittable> s;
	Property property = Property.NORMAL;
	Fighter.HitstunType hitstunType = HitstunType.NORMAL;
	ActionCircleGroup group = null;

	public ActionCircle(Hittable user, float dispX, float dispY, int size){
		this.user = user;
		this.dispX = dispX;
		this.dispY = dispY;
		area = new Circle(getX(), getY(), size);
	}

	public abstract void hitTarget(Hittable en);
	public abstract Color getColor();

	public void checkGroup(){ 
		if (null != group) for (ActionCircle ac: group.connectedCircles) {
			hitFighterList.addAll(ac.hitFighterList);
			s = new HashSet<Hittable>(hitFighterList);
			hitFighterList.clear();
			hitFighterList.addAll(s);
			if (ac.isInitialHit() || ac.remove) remove = true;
		}
	}

	public void setDuration(int dur){
		duration.setEndTime(dur);
	}

	public void update(int deltaTime){
		checkGroup();
		duration.countUp();
		if (DowntiltEngine.outOfHitlag()){
			updatePosition();
			if (isInitialHit()) remove = true;
		}
		touchOtherActionCircles();
	}

	public void updatePosition() {
		area.setX(getX());
		area.setY(getY());
	}

	public void reset(){
		hitFighterList.clear();
		remove = false;
		setInitialHit(false);
	}
	
	void touchOtherActionCircles(){
		/* */
	}

	float getX(){ 
		return user.getPosition().x + user.getImage().getWidth()/2 + (user.direct() * dispX  + (movesAheadMod * user.getVelocity().x) ); 
	}
	
	float getY(){
		return user.getPosition().y + user.getImage().getHeight()/2 + dispY  + (movesAheadMod * user.getVelocity().y);
	}
	
	public Circle getArea(){ return area; }
	public boolean toRemove() { return duration.timeUp(); }
	public boolean didHitTarget(Hittable target){ 
		boolean teamCheck = true;
		boolean attackTimeUp = true;
		if (null != user) {
			teamCheck = user.getTeam() != target.getTeam();
			if (user instanceof Fighter) attackTimeUp = !((Fighter)user).attackTimeUp();
		}
		return 
					teamCheck
				&& !remove 
				&& attackTimeUp
				&& !target.isInvincible() 
				&& Intersector.overlaps(area, target.getHurtBox()) 
				&& !hitFighterList.contains(target); 
	}
	public boolean isInitialHit() { return initialHit; }
	public boolean hitAnybody() { return hitFighterList.size() >= 1; }
	public boolean doesReflect() { return reflects; }
	public void setRefresh(int time) { 
		refreshTimer.setEndTime(time);
		doesRefresh = true; 
	}
	public void setInitialHit(boolean initialHit) { this.initialHit = initialHit; }
	public void setReflects() { reflects = true; }
	public void setMovesAheadMod(float mod) { movesAheadMod = mod; }
	public void setNoReverse() { reverse = false; }

}
