package main;

import inputs.InputHandlerController;
import inputs.InputHandlerController.ControllerType;
import inputs.InputHandlerKeyboard;
import inputs.InputHandlerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Stage;
import timers.Timer;
import entities.*;
import challenges.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class DowntiltEngine extends ApplicationAdapter {

	/**
	 * MUST BE ON before making a jar/releasing!
	 */
	private static boolean release = false;
	
	private static final Timer hitlagTimer = new Timer(0), waitTimer = new Timer(0), slowTimer = new Timer(0);
	private static final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(hitlagTimer, waitTimer, slowTimer));
	private static final List<Fighter> playerList = new ArrayList<Fighter>();
	private static final List<Controller> controllerList = new ArrayList<Controller>();
	private static int deltaTime = 0;
	private static FPSLogger fpsLogger = new FPSLogger();
	private static boolean paused = false;
	private static Mode activeMode;
	private static GameState gameState = GameState.MENU;
	private static InputHandlerPlayer primaryInputHandler = null, secondaryInputHandler = null;
	private static float volume	= 1f;
	private static ShaderProgram p2Palette;

	private static boolean musicToggle = false;
	private static boolean debugToggle = true;
	private static boolean fpsLoggle = false;

	public void create () {
		p2Palette = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/p2.glsl"));
		for (Controller c: Controllers.getControllers()) {
			if (isXBox360Controller(c) || isPS3Controller(c)) controllerList.add(c);
		}
		
		primaryInputHandler = setupInputHandler(0);
		secondaryInputHandler = setupInputHandler(1);
		
		beginFighters(true);
		GraphicsHandler.begin();
		MapHandler.begin();

		Menu.initialize();
		DebugMenu.initialize();
		MainMenu.initialize();
	}
	
	private static boolean isXBox360Controller(Controller c){
		return (c.getName().toLowerCase().contains("xbox") && c.getName().contains("360"));
	}
	
	private static boolean isPS3Controller(Controller c){
		return (c.getName().toLowerCase().contains("ps3") || c.getName().contains("playstation"));
	}
	
	private static InputHandlerPlayer setupInputHandler(int i){
		if (i >= controllerList.size()) return new InputHandlerKeyboard(null);
		
		Controller c = controllerList.get(i);
		if (null == c) return new InputHandlerKeyboard(null);
		if (isXBox360Controller(c)) {
			InputHandlerController ihc = new InputHandlerController(null);
			ihc.setController(c, ControllerType.XBOX360);
			return ihc;
		}
		else if (isPS3Controller(c)) {
			InputHandlerController ihc = new InputHandlerController(null);
			ihc.setController(c, ControllerType.PS3);
			return ihc;
		}
		else return new InputHandlerKeyboard(null);
	}
	
	private static void beginFighters(boolean coop){
		Fighter p1 = new Hero(0, 0, 0);
		beginFighter(p1, primaryInputHandler);
		if (!coop) return;
		Fighter p2 = new Hero(0, 0, 0);
		p2.setPalette(p2Palette);
		beginFighter(p2, secondaryInputHandler);
	}

	/**
	 * Creates a fighter and initializes their controller
	 */
	private static void beginFighter(Fighter player, InputHandlerPlayer inputHandlerPlayer){
		inputHandlerPlayer.setPlayer(player);
		player.setInputHandler(inputHandlerPlayer);
		playerList.add(player);
	}

	/**
	 * Core loop of the game.
	 */
	@Override
	public void render () {
		if (!paused) {
			deltaTime++;
			updateTimers();
		}
		if (fpsLoggle && !release) fpsLogger.log();
		switch(gameState){
		case GAME:	updateGame();		break;
		case DEBUG: DebugMenu.update();	break;
		case MENU:	MainMenu.update();	break;
		}
	}

	private void updateGame(){
		activeMode.update();
		if (waitTimer.timeUp()) MapHandler.updateInputs();
		if (!paused){
			MapHandler.activeRoom.update(deltaTime);
			MapHandler.updateActionCircleInteractions();
			if (outOfHitlag()) MapHandler.updateEntities();
		}
		GraphicsHandler.updateGraphics();
		TransitionGraphicsHandler.update();
		GraphicsHandler.updateCamera();
	}

	private void updateTimers(){
		for (Timer t: timerList) t.countUp();
	}

	public static void causeHitlag(int length){
		hitlagTimer.reset(length);
	}

	public static void wait(int length){
		waitTimer.reset(length);
	}
	
	public static void slow(int length){
		slowTimer.reset(length);
	}

	public static void pauseGame() {
		if (gameState != GameState.GAME) return;
		if (paused) new SFX.Unpause().play();
		else new SFX.Pause().play();
		paused = !paused;
	}

	public static void changeRoom (Stage stage) {
		deltaTime = 0;
		slowTimer.end();
		for (Fighter player: getPlayers()){
			player.setPosition(stage.getStartPosition());
		}
		MapHandler.updateRoomMap(stage);
		GraphicsHandler.updateRoomGraphics(getPlayers().get(0));
	}

	public static void startDebugChallenge(List<Fighter> newPlayers, int startChallenge, boolean debug){
//		startMode(newPlayers, startChallenge);
//		debugToggle = debug;
	}

	public static void startMode(Mode mode, int numPlayers, int initialChallenge){
		activeMode = mode;
		gameState = GameState.GAME;
		playerList.clear();
		paused = false;
		
		if (numPlayers == 2) beginFighters(true);
		else beginFighters(false);
		GraphicsHandler.begin();
		MapHandler.begin();
	}

	public static void startDebugMenu(){
		toMenu(GameState.DEBUG);
	}

	public static void returnToMenu(){
		toMenu(GameState.MENU);
	}
	
	private static void toMenu(GameState gs){
		paused = false;
		beginFighters(true);
		getChallenge().getStage().getMusic().stop();
		MainMenu.begin();
		gameState = gs;
	}

	public static Challenge getChallenge(){
		return activeMode.getActiveChallenge();
	}

	public enum GameState{
		GAME, DEBUG, MENU
	}

	public static float getVolume(){ return volume; }
	public static int getDeltaTime(){ return deltaTime; }
	public static boolean isPaused() { return paused; }
	public static List<Fighter> getPlayers(){ return playerList; }
	public static boolean outOfHitlag(){ return hitlagTimer.timeUp(); }
	public static boolean isSlowed(){ return !slowTimer.timeUp(); }
	public static boolean justOutOfHitlag() { return hitlagTimer.timeJustUp(); }
	public static GameState getGameState() { return gameState; }
	public static boolean entityIsPlayer(Entity en){
		for (Fighter player: DowntiltEngine.getPlayers()){
			if (en.equals(player)) return true;
		}
		return false;
	}
	public static boolean musicOn(){
		return musicToggle || release;
	}
	public static boolean debugOn(){
		return debugToggle && !release;
	}
	public static InputHandlerPlayer getPrimaryInputHandler() { 
		return primaryInputHandler; 
	}
	public static void addTimer(Timer t){
		timerList.add(t);
	}

}