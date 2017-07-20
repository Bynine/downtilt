package movelists;

import main.SFX;
import moves.Action;
import moves.Hitbox;
import moves.Move;
import moves.Windbox;
import entities.Fighter;

public class M_Fly extends MoveList {

	public M_Fly(Fighter user) {
		super(user);
	}

	@Override
	public Move nWeak() {
		Move m = new Move(user, 10);
		return m;
	}

	@Override
	public Move slide() {
		Move m = new Move(user, 10);
		return m;
	}

	@Override
	public Move nAir() {
		int end = 54;
		int startSwoop = 15;
		int iter = 6;
		int endSwoop = startSwoop + (iter * 4);
		
		Move m = new Move(user, end);
		m.setAnimation("sprites/fighters/fly/nair.png", 3, 20);
		Hitbox hit = new Hitbox(user, 4.2f, 2.8f, 16, 80, 10, -10, 22, new SFX.MidHit());
		hit.setMovesAheadMod(1);
		m.eventList.addConstantVelocity(user, 0, startSwoop, -1, 1);
		m.eventList.addConstantVelocity(user, startSwoop, startSwoop + iter, 7, -7);
		m.eventList.addConstantVelocity(user, startSwoop + iter, startSwoop + iter * 2, 7, -4);
		m.eventList.addConstantVelocity(user, startSwoop + iter * 2, startSwoop + iter * 3, 6, 0);
		m.eventList.addConstantVelocity(user, startSwoop + iter * 3, startSwoop + iter * 4, 2, 3);
		m.eventList.addActionCircle(hit, startSwoop, endSwoop);
		return m;
	}

	@Override
	public Move nCharge() {
		int frame = 20;
		int frames = 2;
		
		Move m = new Move(user, frame * 6);
		m.setAnimation("sprites/fighters/fly/nSpecial.png", frames, frame);
		Windbox wind1 = new Windbox(user, 0.9f, 0.3f, 60, 0, 30);
		Windbox wind3 = new Windbox(user, 1.4f, 0.5f, 30, 0, 20);
		Windbox wind2 = new Windbox(user, 0.7f, 0.2f, 40, 0, 40);
		wind1.setRefresh(4);
		wind2.setRefresh(4);
		m.eventList.addConstantVelocity(user, 0, frame * 1, 0, 0);
		m.eventList.addConstantVelocity(user, frame * 1, frame * 5, -1, 0);
		m.eventList.addActionCircle(wind1, frame * 1, frame * 3);
		m.eventList.addActionCircle(wind3, frame * 1, frame * 3);
		m.eventList.addActionCircle(wind2, frame * 3, frame * 5);
		return m;
	}

	@Override
	public Move nSpecial() {
		Move m = new Move(user, 10);
		return m;
	}

	@Override
	public Move uSpecial() {
		Move m = new Move(user, 20);
		m.setHelpless();
		m.setAnimation("sprites/fighters/fly/jump.png", 1, 1);
		m.eventList.addVelocityChange(user, 5, Action.ChangeVelocity.noChange, 8);
		return m;
	}

	@Override
	public Move rollForward() {
		user.flip();
		return roll();
	}

	@Override
	public Move rollBack() {
		return roll();
	}

	private final float getUpYSpeed = 3.4f;
	private Move roll(){
		Move m = new Move(user, 48);
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/fly/jump.png", 2, 16);
		m.eventList.addConstantVelocity(user, 2, 20, -4, getUpYSpeed);
		return m;
	}

	@Override
	public Move dodge() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/fly/jump.png", 1, 1);
		m.eventList.addConstantVelocity(user, 2, 20, 0, getUpYSpeed);
		return m;
	}

	@Override
	public Move land() {
		Move m = new Move(user, 6);
		m.setAnimation("sprites/fighters/fly/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move skid() {
		Move m = new Move(user, 6);
		m.setAnimation("sprites/fighters/fly/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move taunt() {
		return new Move(user, 60);
	}
	
	public Move block(){
		int invinc = 24;
		int length = 42;
		
		Move m = new Move(user, length);
		m.setAnimation("sprites/fighters/fly/dodge.png", 1, 1);
		m.eventList.addConstantVelocity(user, 5, invinc, 6, 0);
		m.eventList.addInvincible(user, 10, invinc);
		m.eventList.addConstantVelocity(user, invinc, length, 0, 0);
		return m;
	}
	
	@Override
	public Move parry() {	
		Move m = new Move(user, 1);
		return m;
	}

}
