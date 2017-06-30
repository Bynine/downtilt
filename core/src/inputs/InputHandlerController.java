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

public class InputHandlerController extends InputHandlerPlayer implements ControllerListener {

	Controller control = null;
	private float currShoulder, prevShoulder;
	
	private final float depressed = 0.1f;
	float xInput = 0, yInput = 0;
	public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
	public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
	public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
	public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
	StickDir leftX = new StickDir(AXIS_LEFT_X), leftY = new StickDir(AXIS_LEFT_Y);
	StickDir rightX = new StickDir(AXIS_RIGHT_X), rightY = new StickDir(AXIS_RIGHT_Y);
	private final List<StickDir> stickDirs = new ArrayList<StickDir>(Arrays.asList(leftX, leftY, rightX, rightY));
	public static final int AXIS_SHOULDER = 4;
	int prevButton = commandNone;
	Timer pauseSelectBuffer = new Timer(10);
	ControllerType controllerType;

	public InputHandlerController(Fighter player) {
		super(player);
		this.fighter = player;
	}

	public boolean setupController(int index){
		writeControllersToConsole();
		if (Controllers.getControllers().size <= index) return false;
		for (Controller c: Controllers.getControllers()){
			if ((c.getName().toLowerCase().contains("xbox") && c.getName().contains("360"))) {
				controllerType = ControllerType.XBOX360;
				control = c;
			}
			if ((c.getName().toLowerCase().contains("ps3") || c.getName().contains("playstation"))) {
				controllerType = ControllerType.PS3;
				control = c;
			}
		}
		if (null == control) return false;
		Controllers.addListener(this);
		return true;
	}
	
	private void writeControllersToConsole(){
		if (Controllers.getControllers().size > 0){
			System.out.println(Controllers.getControllers().size + " controller(s found:");
			for (Controller c: Controllers.getControllers()) System.out.println("- " + c.getName());
		}
		else System.out.println("No controllers found");
	}

	private final float pushed = 0.85f;
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
		if (paused) pauseSelectBuffer.restart();
		return paused;
	}
	
	public boolean select(){ 
		boolean selected = pauseSelectBuffer.timeUp() && control.getButton(commandSelect);
		if (selected) pauseSelectBuffer.restart();
		return selected;
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
	
	private class StickDir{
		static final int lastSize = 2;
		final List<Float> lastPositions = new ArrayList<Float>(lastSize);
		private float curr = 0, prev = 0, flick = 0.9f;
		final int axis;
		
		StickDir(int axis){
			this.axis = axis;
			for (int i = 0; i < lastSize; ++i) lastPositions.add((float) 0);
		}
		
		void update(){
			lastPositions.add(control.getAxis(axis));
			prev = lastPositions.remove(0);
			curr = lastPositions.get(lastSize - 1);
			if (Math.abs(curr) < pushed) curr = 0;
		}
		
		boolean flick(int flickTo){
			return Math.abs(curr - prev) > flick && Math.signum(curr) == flickTo;
		}
	}
	
	private enum ControllerType{
		XBOX360, PS3, OTHER
	}

}
