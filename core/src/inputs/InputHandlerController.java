package inputs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timers.Timer;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import entities.Fighter;
import main.DowntiltEngine;

public class InputHandlerController extends InputHandlerPlayer implements ControllerListener {

	Controller control = null;
	private float currShoulder, prevShoulder;

	private final float depressed = 0.1f;
	float xInput = 0, yInput = 0;
	public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
	public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
	public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
	public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
	private final float lFlick = 0.8f, rFlick = 0.6f;
	StickDir leftX = new StickDir(AXIS_LEFT_X, lFlick), leftY = new StickDir(AXIS_LEFT_Y, lFlick);
	StickDir rightX = new StickDir(AXIS_RIGHT_X, rFlick), rightY = new StickDir(AXIS_RIGHT_Y, rFlick);
	private final List<StickDir> stickDirs = new ArrayList<StickDir>(Arrays.asList(leftX, leftY, rightX, rightY));
	public static final int AXIS_SHOULDER = 4;
	int prevButton = commandNone;
	Timer pauseSelectBuffer = new Timer(10);
	ControllerType controllerType;

	public InputHandlerController(Fighter player) {
		super(player);
		this.player = player;
		Controllers.addListener(this);
	}

	public boolean setupController(Controller c){
		//		writeControllersToConsole();
		//		if (Controllers.getControllers().size <= index) return false;
		if (null == c) return false;
		if ((c.getName().toLowerCase().contains("xbox") && c.getName().contains("360"))) {
			setController(c, ControllerType.XBOX360);
		}
		else if ((c.getName().toLowerCase().contains("ps3") || c.getName().contains("playstation"))) {
			setController(c, ControllerType.PS3);
		}
		else return false;
		Controllers.addListener(this);
		return true;
	}

	public void setController(Controller c, ControllerType ct){
		controllerType = ct;
		control = c;
	}

	public void update() {
		pauseSelectBuffer.countUp();
		currShoulder = control.getAxis(AXIS_SHOULDER);
		super.update();
		prevShoulder = currShoulder;

		xInput = control.getAxis(AXIS_LEFT_X);
		yInput = control.getAxis(AXIS_LEFT_Y);

		for (StickDir sd: stickDirs) {
			sd.update();
		}
	}

	public void refresh(){
		xInput = 0;
		yInput = 0;
		for (StickDir s: stickDirs) s.clear();
		player.stickX = 0;
		player.stickY = 0;
	}

	public boolean buttonDown(Controller controller, int buttonCode) {
		if (!control.getButton(buttonCode)) return false;
		handleCommand(buttonCode);
		return true;
	}

	public float getXInput() {
		return xInput;
	}

	public float getYInput() {
		return yInput;
	}

	public boolean dodge(){
		return blockHold() && (prevShoulder - currShoulder) > depressed;
	}

	public boolean blockHold(){
		return Math.abs(currShoulder) > depressed;
	}

	public boolean taunt(){
		return control.getPov(0) != PovDirection.center;
	}

	public boolean chargeHold(){
		return control.getButton(commandCharge);
	}

	public boolean jumpHold(){
		return control.getButton(commandJump);
	}

	public boolean attackHold(){
		return control.getButton(commandAttack);
	}

	public boolean flickLeft(){
		return leftX.flick(-1);
	}

	public boolean flickRight(){
		return leftX.flick(1);
	}

	public boolean flickUp(){
		return leftY.flick(-1);
	}

	public boolean flickDown(){
		return leftY.flick(1);
	}

	public boolean flickCLeft(){
		return rightX.flick(-1);
	}

	public boolean flickCRight(){
		return rightX.flick(1);
	}

	public boolean flickCUp(){
		return rightY.flick(-1);
	}

	public boolean flickCDown(){
		return rightY.flick(1);
	}

	public boolean pause(){ 
		boolean paused = pauseSelectBuffer.timeUp() && control.getButton(commandPause);
		if (paused) pauseSelectBuffer.reset();
		return paused;
	}

	public boolean select(){ 
		boolean selected = pauseSelectBuffer.timeUp() && control.getButton(commandSelect);
		if (selected) pauseSelectBuffer.reset();
		return selected;
	}


	public String getControllerName(){
		return control.getName();
	}

	private class StickDir{
		static final int lastSize = 2;
		final List<Float> lastPositions = new ArrayList<Float>(lastSize);
		private float curr = 0, prev = 0;
		private final float flick;
		private final int axis;

		StickDir(int axis, float flick){
			this.axis = axis;
			this.flick = flick;
			for (int i = 0; i < lastSize; ++i) lastPositions.add((float) 0);
		}

		void update(){
			lastPositions.add(control.getAxis(axis));
			prev = lastPositions.remove(0);
			curr = lastPositions.get(lastSize - 1);
			if (Math.abs(curr) < 0.85f) curr = 0;
		}

		boolean flick(int flickTo){
			return Math.abs(curr - prev) > (flick * DowntiltEngine.getStickSensitivity()) && Math.signum(curr) == flickTo;
		}
		
		void clear(){
			for (int i = 0; i < lastSize; ++i) lastPositions.set(i, (float) 0);
			curr = 0;
			prev = 0;
		}
	}

	public enum ControllerType{
		XBOX360, PS3, OTHER
	}
	
	public boolean inputNormal(){
		return control.getButton(commandAttack);
	}
	public boolean inputSpecial(){
		return control.getButton(commandSpecial);
	}
	public boolean inputCharge(){
		return control.getButton(commandCharge);
	}
	public boolean inputJump(){
		return control.getButton(commandJump);
	}
	public boolean inputGrab(){
		return control.getButton(commandGrab);
	}
	public boolean inputGuard(){
		return blockHold();
	}
	
	@Override
	public String getMoveString(){
		return "the control stick";
	}
	
	@Override
	public String getNormalString() {
		return "A";
	}

	@Override
	public String getSpecialString() {
		return "B";
	}

	@Override
	public String getChargeString() {
		return "Y";
	}

	@Override
	public String getJumpString() {
		return "X";
	}

	@Override
	public String getGrabString() {
		return "RB";
	}

	@Override
	public String getGuardString() {
		return "L or R";
	}
	
	@Override
	public String getFlickString() {
		return "flick the control stick left or right";
	}
	
	@Override
	public String getThrowString() {
		return "flicking the control stick in any direction";
	}

	@Override
	public String getStartString() {
		return "START";
	}

	@Override
	public String getSelectString() {
		return "SELECT";
	}

	/* handled by buttonDown */

	public boolean attack(){ return false; }
	public boolean special(){ return false; }
	public boolean charge(){ return false; }
	public boolean jump(){ return false; }
	public boolean grab(){ return false; }

	/* NOT USED */

	public boolean axisMoved(Controller controller, int axisCode, float value) { return false; }
	public void connected(Controller controller) { }
	public void disconnected(Controller controller) { }
	public boolean buttonUp(Controller controller, int buttonCode) { return false; }
	public boolean povMoved(Controller controller, int povCode, PovDirection value) { return false; }
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }

}
