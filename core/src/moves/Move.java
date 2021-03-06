package moves;

import main.GlobalRepo;
import moves.Effect.Charge;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

import entities.Entity.Direction;
import entities.Fighter;
import entities.Hittable;
import timers.DurationTimer;
import timers.Timer;

public class Move {
	final Fighter user;
	final Timer duration;
	public final EventList eventList = new EventList();
	private boolean helpless = false, continueOnLanding = false, noTurn = false, connected = false, stopsInAir = false, isAerial = false, tremble = false,
			heroUSpecial = false;
	float armor = 0;
	private Animation animation = null;
	private int addedFrames = 0, id = -1;
	public static final float HURTBOXNOTSET = -1;
	private float hurtBoxWidth = HURTBOXNOTSET, hurtBoxHeight = HURTBOXNOTSET, hurtBoxX = HURTBOXNOTSET, hurtBoxY = HURTBOXNOTSET;
	private final List<Fighter> comboedTheseFighters = new ArrayList<Fighter>();

	public Move(Fighter user, int dur){
		this.user = user;
		duration = new DurationTimer(dur);
	}

	public void update(){
		duration.countUp();
		eventList.update(duration.getCounter(), user.inHitstun());
		for (ActionCircle ac: eventList.acList){
			if (ac.hitTargetList.size() > 0) connected = true;
			for (Hittable target: ac.getHitTargets()){
				if (target instanceof Fighter && !comboedTheseFighters.contains(target)){
					Fighter fi = (Fighter) target;
					fi.addToCombo(id);
					comboedTheseFighters.add(fi);
				}
			}
		}
	}

	public void setAnimation(String string, int cols, int frameLength) {
		String finalString = string.toLowerCase();
		animation = GlobalRepo.makeAnimation(finalString, cols, 1, frameLength, PlayMode.LOOP);
	}
	
	public void setAnimationNoLoop(String string, int cols, int frameLength) {
		String finalString = string.toLowerCase();
		animation = GlobalRepo.makeAnimation(finalString, cols, 1, frameLength, PlayMode.NORMAL);
	}

	public void addFrame() {
		duration.setEndTime(duration.getEndTime() + 1);
		addedFrames--;
	}

	public void setHelpless() { 
		helpless = true; 
	}

	public void setStopsInAir() { 
		stopsInAir = true; 
	}

	public void setDone() {
		duration.end();
		eventList.clearActionCircles();
	}

	public void dontTurn() { 
		noTurn = true;
	}

	public void setContinueOnLanding() { 
		continueOnLanding = true; 
	}

	public void setArmor(float armor) { 
		this.armor = armor; 
	}

	public void setHurtBox(float width, float height, float x, float y){
		hurtBoxWidth = width;
		hurtBoxHeight = height;
		hurtBoxX = x;
		hurtBoxY = y;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setAerial(){
		isAerial = true;
	}

	public void clearHitboxes() {
		eventList.clearActionCircles();
	}
	
	public void setTremble(boolean b){
		tremble = b;
	}
	
	public void setHeroUSpecial(){
		heroUSpecial = true;
	}
	
	/* GETTERS */
	
	public Rectangle getMoveHurtBox(Fighter user, Rectangle r){
		float origWidth = r.width;
		
		if (hurtBoxWidth != HURTBOXNOTSET) {
			r.width = hurtBoxWidth;
			r.x = r.x + (origWidth - hurtBoxWidth)/2;
		}
		if (hurtBoxHeight != HURTBOXNOTSET) r.height = hurtBoxHeight;
		if (hurtBoxX != HURTBOXNOTSET) {
			if (user.getDirection() == Direction.LEFT) r.x -= hurtBoxX;
			else r.x += hurtBoxX;
		}
		if (hurtBoxY != HURTBOXNOTSET) r.y += hurtBoxY;
		return r;
	}

	public int getDuration() { 
		return duration.getEndTime(); 
	}

	private static final Animation animUp = GlobalRepo.makeAnimation("sprites/fighters/bomber/uspecialu.png", 1, 1, 1, PlayMode.LOOP);
	private static final Animation animForward = GlobalRepo.makeAnimation("sprites/fighters/bomber/uspecialf.png", 1, 1, 1, PlayMode.LOOP);
	private static final Animation animBack = GlobalRepo.makeAnimation("sprites/fighters/bomber/uspecialb.png", 1, 1, 1, PlayMode.LOOP);
	private static final Animation animDown = GlobalRepo.makeAnimation("sprites/fighters/bomber/uspeciald.png", 1, 1, 1, PlayMode.LOOP);
	public Animation getAnimation() { 
		if (heroUSpecial && duration.getCounter() == 22){
			if (Math.abs(user.getVelocity().y) > Math.abs(user.getVelocity().x)){
				if (user.getVelocity().y < 0) animation = animDown;
				else animation = animUp;
			}
			else{
				if ((user.getVelocity().x > 0 && user.getDirection() == Direction.RIGHT) || (user.getVelocity().x < 0 && user.getDirection() == Direction.LEFT)) return animation = animForward;
				else return animation = animBack;
			}
		}
		return animation; 
	}

	public int getFrame() { 
		return duration.getCounter() + addedFrames; 
	}

	public float getArmor() { 
		return armor; 
	}

	/* BOOLEANS */

	public boolean isCharging() {
		for (Effect e: eventList.effectList){
			if (e instanceof Charge && e.isActive(duration.getCounter()))  return true;
		}
		return false;
	}

	public boolean connected() { 
		return connected;
	}

	public boolean causesHelpless() { 
		return helpless; 
	}

	public boolean doesStopInAir() { 
		return stopsInAir; 
	}

	public boolean continuesOnLanding() { 
		return continueOnLanding; 
	}

	public boolean isNoTurn() { 
		return noTurn; 
	}

	public boolean done(){ 
		return duration.timeUp(); 
	}
	
	public boolean isAerial(){
		return isAerial;
	}
	
	public boolean doesTremble(){
		return tremble;
	}

	public boolean hasAnimation() {
		return animation != null;
	}


}
