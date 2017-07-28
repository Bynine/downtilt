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
		return Gdx.input.isKeyJustPressed(Keys.K);
	}
	
	private int chargeInput = Keys.L;
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
		return Gdx.input.isKeyPressed(Keys.P);
	}
	
	public boolean select(){
		return Gdx.input.isKeyPressed(Keys.LEFT_BRACKET);
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
