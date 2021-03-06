package inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import entities.Fighter;

public class InputHandlerKeyboard extends InputHandlerPlayer implements InputProcessor{

	public InputHandlerKeyboard(Fighter player) {
		super(player);
	}
	
	private boolean pressLeft(){
		return Gdx.input.isKeyPressed(Keys.D);
	}
	
	private boolean pressRight(){
		return Gdx.input.isKeyPressed(Keys.A);
	}
	
	private boolean pressUp(){
		return Gdx.input.isKeyPressed(Keys.W);
	}
	
	private boolean pressDown(){
		return Gdx.input.isKeyPressed(Keys.S);
	}
	
	public float getXInput() {
		if (pressLeft()) return  1;
		if (pressRight()) return -1;
		return 0;
	}

	public float getYInput() {
		if (pressDown()) return  1;
		if (pressUp()) return -1;
		return 0;
	}
	
	private int attackInput = Keys.J;
	public boolean attack(){
		return Gdx.input.isKeyJustPressed(attackInput);
	}
	
	public boolean special(){
		return Gdx.input.isKeyJustPressed(Keys.L);
	}
	
	private int chargeInput = Keys.K;
	public boolean charge(){
		return Gdx.input.isKeyJustPressed(chargeInput);
	}
	
	public boolean chargeHold(){
		return Gdx.input.isKeyPressed(chargeInput);
	}
	
	private int jumpInput = Keys.SPACE;
	public boolean jump(){
		return Gdx.input.isKeyJustPressed(jumpInput);
	}
	
	public boolean jumpHold(){
		return Gdx.input.isKeyPressed(jumpInput);
	}
	
	public boolean grab(){
		return Gdx.input.isKeyJustPressed(Keys.O);
	}
	
	private int blockInput = Keys.I;
	public boolean dodge(){
		return Gdx.input.isKeyJustPressed(blockInput);
	}
	
	public boolean blockHold(){
		return Gdx.input.isKeyPressed(blockInput);
	}
	
	public boolean attackHold(){
		return Gdx.input.isKeyPressed(attackInput);
	}
	
	public boolean taunt(){
		return Gdx.input.isKeyJustPressed(Keys.U);
	}
	
	public boolean flickLeft(){
		return Gdx.input.isKeyJustPressed(Keys.A);
	}
	
	public boolean flickRight(){
		return Gdx.input.isKeyJustPressed(Keys.D);
	}
	
	public boolean flickUp(){
		return Gdx.input.isKeyJustPressed(Keys.W);
	}
	
	public boolean flickDown(){
		return Gdx.input.isKeyJustPressed(Keys.S);
	}
	
	public boolean pause(){
		return Gdx.input.isKeyJustPressed(Keys.T);
	}
	
	public boolean select(){
		return Gdx.input.isKeyJustPressed(Keys.Y);
	}
	
	public boolean flickCLeft(){
		return false;
	}
	
	public boolean flickCRight(){
		return false;
	}
	
	public boolean flickCUp(){
		return false;
	}
	
	public boolean flickCDown(){
		return false;
	}
	
	@Override
	public String getControllerName(){
		return "Keyboard";
	}
	
	public boolean inputNormal(){
		return attack();
	}
	public boolean inputSpecial(){
		return special();
	}
	public boolean inputCharge(){
		return charge();
	}
	public boolean inputJump(){
		return jump();
	}
	public boolean inputGrab(){
		return grab();
	}
	public boolean inputGuard(){
		return blockHold();
	}
	
	@Override
	public String getMoveString(){
		return "WASD";
	}
	
	@Override
	public String getNormalString() {
		return "J";
	}

	@Override
	public String getSpecialString() {
		return "L";
	}

	@Override
	public String getChargeString() {
		return "K";
	}

	@Override
	public String getJumpString() {
		return "SPACE";
	}

	@Override
	public String getGrabString() {
		return "O";
	}

	@Override
	public String getGuardString() {
		return "I";
	}

	@Override
	public String getStartString() {
		return "T";
	}

	@Override
	public String getSelectString() {
		return "Y";
	}
	
	@Override
	public String getFlickString() {
		return "press A or D";
	}
	
	@Override
	public String getThrowString() {
		return "pressing W, A, S, or D";
	}
	
	/* NOT USED */

	@Override public boolean keyDown(int keycode) { return false; }
	@Override public boolean keyUp(int keycode) { return false; }
	@Override public boolean keyTyped(char character) { return false; }
	@Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
	@Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
	@Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	@Override public boolean mouseMoved(int screenX, int screenY) { return false; }
	@Override public boolean scrolled(int amount) { return false; }
	
}
