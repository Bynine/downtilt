package entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import main.DowntiltEngine;
import moves.Move;
import entities.Entity.State;

public class InputPackage {

	public final State state;
	public final boolean isOffStage, isBelowStage, outOfDoubleJumps, isGrounded, isRunning, awayFromWall, playerAttacking;
	public final float distanceFromCenter, distanceXFromPlayer, distanceYFromPlayer;
	public final int direct;
	public final Move activeMove;
	public final List<Vector2> shitToAvoid = new ArrayList<Vector2>();
	public final Vector2 position, velocity;
	
	/**
	 * 
	 * distance X: left of player is positive, right is neg
	 * distance Y: below player is positive, above is neg
	 */
	InputPackage(Fighter fighter){
		Fighter target = getTarget(fighter);
		state = fighter.state;
		isOffStage = (!fighter.isGrounded() && !fighter.groundBelow());
		isBelowStage = fighter.position.y < DowntiltEngine.getChallenge().getCenterPosition().y;
		outOfDoubleJumps = fighter.doubleJumps < 1;
		isGrounded = fighter.isGrounded();
		isRunning = fighter.isRunning();
		awayFromWall = false;
		playerAttacking = isPlayerAttacking(target);
		distanceFromCenter = DowntiltEngine.getChallenge().getCenterPosition().x - fighter.position.x;
		distanceXFromPlayer = target.position.x - fighter.position.x;
		distanceYFromPlayer = target.position.y - (fighter.position.y + fighter.getHurtBox().height/2);
		direct = fighter.direct();
		if (null != fighter.getActiveMove()) activeMove = fighter.getActiveMove().move;
		else activeMove = null;
		position = fighter.getPosition();
		velocity = fighter.getVelocity();
	}
	
	private boolean isPlayerAttacking(Fighter target){
		if (null == target.getActiveMove()) return !target.attackTimer.timeUp();
		else return !target.attackTimer.timeUp() && target.getActiveMove().id >= 1;
	}
	
	private Fighter getTarget(Fighter fighter){
		Fighter target = DowntiltEngine.getPlayers().get(0);
		for (Fighter player: DowntiltEngine.getPlayers()){
			if (fighter.getPosition().dst(player.getPosition() ) < fighter.getPosition().dst(target.getPosition() )) target = player;
		}
		return target;
	}
}
