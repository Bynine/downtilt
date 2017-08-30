package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import maps.Stage;
import maps.Stage.LightningHandler;
import maps.Stage_Standard;
import moves.ActionCircle;
import moves.Grabbox;
import timers.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import challenges.Bonus;
import entities.*;

public class MapHandler {

	static Stage activeRoom;
	static TiledMap activeMap;
	private static final List<Rectangle> rectangleList = new ArrayList<>();
	static int mapWidth;
	static int mapHeight; 
	private static Timer lowGTimer = new Timer(300), highGTimer = new Timer(300);
	private static final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(lowGTimer, highGTimer));
	private static final List<TimedEntity> timedEntityList = new ArrayList<TimedEntity>();

	static void begin(){
		activeRoom = new Stage_Standard();
		activeMap = activeRoom.getMap();
		DowntiltEngine.changeRoom(activeRoom);
		Gdx.gl.glClearColor(0, 0, 0, 1);

	}

	static void updateInputs(){
		for (Entity en: activeRoom.getEntityList()){
			if (en instanceof Fighter){
				Fighter fi = (Fighter) en;
				fi.getInputHandler().update();
			}
		}
	}

	private static Iterator<Entity> entityIter;
	private static Iterator<TimedEntity> timedEntityIter;
	private static final List<Entity> entityToRemoveList = new ArrayList<Entity>();
	static void updateEntities(){
		for (Timer t: timerList) t.countUp();

		timedEntityIter = timedEntityList.iterator();
		while (timedEntityIter.hasNext()){
			TimedEntity te = timedEntityIter.next();
			te.timeBeforeSpawn.countUp();
			if (te.timeBeforeSpawn.timeUp()){
				addEntity(te.entity);
				timedEntityIter.remove();
			}
		}

		entityIter = activeRoom.getEntityList().iterator();
		while (entityIter.hasNext()) {
			Entity en = entityIter.next();
			if (shouldUpdate(en)) {
				en.update(rectangleList, activeRoom.getEntityList(), DowntiltEngine.getDeltaTime()); 
			}

			Rectangle boundary = new Rectangle(0, 0, mapWidth, mapHeight);
			Rectangle cameraBoundary = GraphicsHandler.getCameraBoundary();
			if (en.shouldDie(boundary, cameraBoundary)) {

				if (!(en instanceof Fighter)) entityIter.remove();
				else {
					Fighter fi = ((Fighter) en);
					kill(fi);
				}
			}
		}

		for (Entity en: entityToRemoveList){
			activeRoom.getEntityList().remove(en);
			en.dispose();
		}
		entityToRemoveList.clear();

	}

	private static boolean shouldUpdate(Entity en){
		int slowMod = 8;

		if (en instanceof Hittable){
			boolean slow = !DowntiltEngine.entityIsPlayer(en);
			if (slow && DowntiltEngine.isSlowed() && DowntiltEngine.getDeltaTime() % slowMod != 0) return false;
		}
		return DowntiltEngine.outOfHitlag() || en instanceof Graphic;
	}

	public static boolean kill(Fighter fi){
		Rectangle cameraBoundary = GraphicsHandler.getCameraBoundary();
		if (fi.noKill()) return false;
		fi.setNoKill();

		if (fi.isOOB(cameraBoundary)) drawOOBDieGraphic(fi);
		else drawDisappearGraphic(fi);
		new SFX.Die().play();
		if (DowntiltEngine.entityIsPlayer(fi) && DowntiltEngine.getChallenge().getLives() >= 1) {
			fi.respawn();
			DowntiltEngine.getChallenge().removeLife();
			return false;
		}
		fi.kill();
		if (fi.getPosition().y > cameraBoundary.y + cameraBoundary.height) {
			addEntity(new FallingEnemy(fi.getPosition().x, cameraBoundary.y + cameraBoundary.height));
		}

		if (fi.getTeam() == GlobalRepo.BADTEAM) DowntiltEngine.getMode().pendValidBonus(new Bonus.KOBonus());
		entityToRemoveList.add(fi);
		return true;
	}

	private static void drawOOBDieGraphic(Fighter fi){
		int mod = 5;
		addEntity(new Graphic.Die(
				(fi.getCenter().x * (mod - 1) + GraphicsHandler.getCameraPos().x) / mod, 
				(fi.getCenter().y * (mod - 1) + GraphicsHandler.getCameraPos().y) / mod
				));
	}

	private static void drawDisappearGraphic(Fighter fi){
		addEntity(new Graphic.Disappear(fi.getCenter().x + fi.getVelocity().x, fi.getCenter().y + fi.getVelocity().y));
	}

	static void updateActionCircleInteractions(){
		for (Entity en: activeRoom.getEntityList()){
			if (en instanceof Hittable) {
				fighterHitboxInteract(en);
				removeGrabboxes();
			}
		}
		removeActionCircles();
	}

	private static void removeActionCircles(){
		Iterator<ActionCircle> actionCircleIter = activeRoom.getActionCircleList().iterator();
		while (actionCircleIter.hasNext()) {
			ActionCircle ac = actionCircleIter.next();
			if (ac.toRemove()) actionCircleIter.remove();
		}
	}

	private static void removeGrabboxes(){
		Iterator<ActionCircle> actionCircleIter = activeRoom.getActionCircleList().iterator();
		while (actionCircleIter.hasNext()) {
			ActionCircle ac = actionCircleIter.next();
			if (ac.toRemove() && ac instanceof Grabbox) actionCircleIter.remove();
		}
	}

	private static void fighterHitboxInteract(Entity en){
		for (ActionCircle ac: activeRoom.getActionCircleList()){
			ac.hitTarget((Hittable) en);
		}
	}

	public static void updateRoomMap(Stage room) {
		clearRoom();
		activeRoom = room;
		activeMap = activeRoom.getMap();
		activeRoom.initEntities();
		rectangleList.clear();
		activeRoom.setup();
		rectangleList.addAll(activeRoom.getRectangleList());
		mapWidth  = activeMap.getProperties().get("width",  Integer.class)*GlobalRepo.TILE;
		mapHeight = activeMap.getProperties().get("height", Integer.class)*GlobalRepo.TILE;
	}
	
	public static void clearRoom(){
		if (null != activeRoom){
			Iterator<Entity> entityIter = activeRoom.getEntityList().iterator();
			while(entityIter.hasNext()){
				Entity en = entityIter.next();
				if (!DowntiltEngine.entityIsPlayer(en)){
					en.dispose();
					entityIter.remove();
				}
			}
		}
	}

	public static void resetRoom() {
		DowntiltEngine.changeRoom(activeRoom); 
	}

	public static void addEntity(Entity e){ 
		activeRoom.addEntity(e); 
	}

	public static void addTimedEntity(Entity e, int x){ 
		timedEntityList.add(new TimedEntity(e, x));
	}

	public static ActionCircle addActionCircle(ActionCircle ac){ 
		return activeRoom.addActionCircle(ac); 
	}

	public static List<Entity> getEntities() {
		if (activeRoom == null) return new ArrayList<Entity>();
		else return activeRoom.getEntityList();
	}

	public static float getRoomWind(){
		if (activeRoom == null) return 0;
		else return activeRoom.getWind();
	}

	public static float getRoomGravity(){
		if (activeRoom == null) return 1;
		float gravity = activeRoom.getGravity();
		if (!lowGTimer.timeUp()) gravity *= 0.66f;
		if (!highGTimer.timeUp()) gravity *= 1.5f;
		return gravity;
	}

	public static List<ActionCircle> getActionCircles(){
		if (activeRoom == null) return new ArrayList<ActionCircle>();
		else return activeRoom.getActionCircleList();
	}

	public static void setLowGravity(){
		lowGTimer.reset();
		highGTimer.end();
	}

	public static void setHighGravity(){
		highGTimer.reset();
		lowGTimer.end();
	}

	public static void addLightningHandler(LightningHandler lh){
		if (activeRoom == null) return;
		else activeRoom.addLightningHandler(lh);
	}

	private static class TimedEntity{
		final Timer timeBeforeSpawn;
		final Entity entity;
		TimedEntity(Entity e, int dur){
			timeBeforeSpawn = new Timer(dur);
			this.entity = e;
		}
	}

	public static boolean entityNotAlive(Entity en) {
		if (activeRoom == null) return false;
		else return (!activeRoom.getEntityList().contains(en));
	}

	public static void removeAllNonPlayerEntities() {
		if (activeRoom == null) return;
		else{
			for (Entity e: activeRoom.getEntityList()){
				if (!DowntiltEngine.entityIsPlayer(e)) e.setRemove();
			}
			for (ActionCircle ac: activeRoom.getActionCircleList()){
				ac.remove();
			}
			activeRoom.clearLightningHandlers();
			highGTimer.end();
			lowGTimer.end();
		}
	}

}
