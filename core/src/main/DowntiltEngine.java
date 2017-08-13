package main;

import inputs.InputHandlerController;
import inputs.InputHandlerController.ControllerType;
import inputs.InputHandlerKeyboard;
import inputs.InputHandlerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Stage;
import maps.Stage_Standard;
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
	
	private static boolean demoBuild = false;
	
	private static boolean musicToggle = false;
	private static boolean debugToggle = true;
	private static boolean saveToggle = true;
	private static boolean fpsLoggle = false;

	private static final Timer hitlagTimer = new Timer(0), waitTimer = new Timer(0), slowTimer = new Timer(0);
	private static final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(hitlagTimer, waitTimer, slowTimer));
	private static final List<Fighter> playerList = new ArrayList<Fighter>();
	private static final List<Controller> controllerList = new ArrayList<Controller>();
	private static int deltaTime = 0;
	private static FPSLogger fpsLogger = new FPSLogger();
	private static boolean paused = false;
	private static Mode activeMode;
	private static GameState gameState = GameState.GAMEMENU;
	private static InputHandlerPlayer primaryInputHandler = null, secondaryInputHandler = null;
	private static float masterVolume = 1.0f, musicVolume = 1.0f, sfxVolume = 1.0f, screenShakeMod = 1.0f;
	private static ShaderProgram p2Palette;
	private static HomeMenu homeMenu;
	private static GameMenu gameMenu;
	private static OptionMenu optionMenu;
	private static CreditScreen creditScreen;
	private static RankingScreen rankingScreen;
	private static VictoryScreen activeVictory;
	
	public void create () {
		activeMode = new Endless(new Stage_Standard());
		SaveHandler.loadSave();
		p2Palette = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/p2.glsl"));
		for (Controller c: Controllers.getControllers()) {
			if (isXBox360Controller(c) || isPS3Controller(c)) controllerList.add(c);
		}
		if (release) gameState = GameState.HOME;

		primaryInputHandler = setupInputHandler(0);
		secondaryInputHandler = setupInputHandler(1);

		beginFighters(true);
		GraphicsHandler.begin();
		MapHandler.begin();
		
		int[] options = SaveHandler.getOptions();
		optionMenu = new OptionMenu(options);
		optionMenu.setOptions(); 

		homeMenu = new HomeMenu();
		gameMenu = new GameMenu();
		creditScreen = new CreditScreen();
		rankingScreen = new RankingScreen();
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
		case GAME:		updateGame();			break;
		case HOME: 		homeMenu.update(); 		break;
		case GAMEMENU:	gameMenu.update();		break;
		case CREDIT: 	creditScreen.update();	break;
		case OPTIONS: 	optionMenu.update();	break;
		case RANKING:	rankingScreen.update(); break;
		case VICTORY: 	activeVictory.update();	break;
		}
	}

	private void updateGame(){
		activeMode.update();
		if (!isWaiting()){
			MapHandler.updateInputs();
			if (!paused){
				MapHandler.activeRoom.update(deltaTime);
				MapHandler.updateActionCircleInteractions();
				if (outOfHitlag()) MapHandler.updateEntities();
			}
		}
		GraphicsHandler.updateGraphics();
		GraphicsHandler.updateCamera();
		TransitionGraphicsHandler.update();
	}

	private void updateTimers(){
		for (Timer t: timerList) t.countUp();
	}

	public static void causeHitlag(int length){
		hitlagTimer.reset(length);
	}

	public static void wait(int length){
		primaryInputHandler.refresh();
		secondaryInputHandler.refresh();
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
		slowTimer.end();
		for (Fighter player: getPlayers()){
			player.setPosition(stage.getStartPosition());
		}
		MapHandler.updateRoomMap(stage);
		GraphicsHandler.updateRoomGraphics(getPlayers().get(0));
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

	public static void startGameMenu(){
		toMenu(GameState.GAMEMENU);
	}

	public static void startHomeMenu(){
		toMenu(GameState.HOME);
	}

	public static void startOptionMenu(){
		toMenu(GameState.OPTIONS);
	}

	public static void startRankingScreen(){
		toMenu(GameState.RANKING);
	}

	public static void startCreditScreen(){
		toMenu(GameState.CREDIT);
	}

	public static void startVictoryScreen(Victory v){
		activeVictory = new VictoryScreen();
		activeVictory.start(v);
		toMenu(GameState.VICTORY);
	}

	private static void toMenu(GameState gs){
		paused = false;
		beginFighters(true);
		getChallenge().getStage().getMusic().stop();
		gameMenu.begin();
		gameState = gs;
	}

	public static Challenge getChallenge(){
		return activeMode.getActiveChallenge();
	}

	public enum GameState{
		GAME, HOME, GAMEMENU, CREDIT, OPTIONS, RANKING, VICTORY
	}

	public static void resetDeltaTime() {
		deltaTime = 0;
	}

	public static float getMusicVolume(){ return masterVolume * musicVolume; }
	public static float getSFXVolume(){ return masterVolume * sfxVolume; }
	public static float getScreenShakeMod(){ return screenShakeMod; }
	
	public static void setMusicVolume(float v){ musicVolume = v; }
	public static void setSFXVolume(float v){ sfxVolume = v; }
	public static void setScreenShake(float ss) { screenShakeMod = ss; }
	
	public static int getDeltaTime(){ return deltaTime; }
	public static boolean isPaused() { return paused; }
	public static List<Fighter> getPlayers(){ return playerList; }
	public static boolean outOfHitlag(){ return hitlagTimer.timeUp(); }
	public static boolean isSlowed(){ return !slowTimer.timeUp(); }
	public static boolean justOutOfHitlag() { return hitlagTimer.timeJustUp(); }
	public static boolean isWaiting() { return !waitTimer.timeUp(); }
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
	public static boolean saveOn(){
		return saveToggle || release;
	}
	public static boolean isDemoBuild(){
		return demoBuild;
	}
	public static InputHandlerPlayer getPrimaryInputHandler() { 
		return primaryInputHandler; 
	}
	public static InputHandlerPlayer getSecondaryInputHandler() { 
		return secondaryInputHandler; 
	}
	public static void addTimer(Timer t){
		timerList.add(t);
	}
	public static Mode getActiveMode(){
		return activeMode;
	}

}