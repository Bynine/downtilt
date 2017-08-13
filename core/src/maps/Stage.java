package maps;

import java.util.ArrayList;
import java.util.List;

import main.DowntiltEngine;
import main.GlobalRepo;
import main.MapHandler;
import moves.ActionCircle;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import entities.ChangeBlock;
import entities.Entity;
import entities.Fighter;
import entities.Raindrop;

public abstract class Stage {
	protected MapObjects mapCollision, mapEntities;
	protected final TmxMapLoader tmxMapLoader = new TmxMapLoader();
	protected final List<Rectangle> rectangleList = new ArrayList<>();
	protected final List<Entity> entityList = new ArrayList<>(), newEntityList = new ArrayList<>();
	private final List<ActionCircle> actionCircleList = new ArrayList<>(), newActionCircleList = new ArrayList<>();
	protected Music roomMusic;
	protected float r, g, b = 1;
	protected float a = 0;
	protected float wind = 0;
	protected float gravity = 1;
	protected float dispX = GlobalRepo.TILE * 2;
	protected boolean scrolls = false;

	public Stage(){
		clearOut();
	}

	public void setup(){
		MapLayers layers = getMap().getLayers();
		mapCollision = layers.get(layers.getCount()-2).getObjects(); // gets the collision layer
		for(RectangleMapObject mapObject: mapCollision.getByType(RectangleMapObject.class)){		
			rectangleList.add(mapObject.getRectangle());
		}
		mapEntities = layers.get(layers.getCount()-1).getObjects();
		roomMusic.setLooping(true);
		
	}

	public void initEntities(){
		clearOut();
		for (Fighter player: DowntiltEngine.getPlayers()){
			player.setPercentage(0);
			player.setPosition(getStartPosition());
			player.setRespawnPoint(getStartPosition());
			entityList.add(player);
		}
		for (MapObject m: mapEntities) entityList.add(new EntityLoader().loadEntity(m));
	}

	public void addEntity(Entity e){
		newEntityList.add(e);
	}

	public ActionCircle addActionCircle(ActionCircle ac) {
		newActionCircleList.add(ac);
		return newActionCircleList.get(newActionCircleList.size() - 1);
	}

	public void update(int deltaTime){
		for (Entity e: newEntityList) {
			entityList.add(e);
		}
		newEntityList.clear();
		for (ActionCircle ac: newActionCircleList) {
			actionCircleList.add(ac);
		}
		newActionCircleList.clear();
		for (ActionCircle ac: actionCircleList) ac.update(deltaTime);
		for (Entity en: entityList) if (en instanceof ChangeBlock) ((ChangeBlock)en).update(entityList);
	}

	void clearOut(){
		rectangleList.clear();
		entityList.clear();
	}

	void setStartPosition(float x, float y){
		getStartPosition().x = x;
		getStartPosition().y = y;
	}

	public void removeEntity(Entity en){
		getEntityList().remove(en);
		getRectangleList().remove(en.getImage().getBoundingRectangle());
	}
	
	protected void rain(){
		float raindropY = 16f * GlobalRepo.TILE;
		if (DowntiltEngine.getDeltaTime() % 6 == 0 || DowntiltEngine.getDeltaTime() % 5 == 3) MapHandler.addEntity(new Raindrop(getRaindropLocation(), raindropY));
	}
	
	private float getRaindropLocation(){
		return (float) ((10 * GlobalRepo.TILE) + (Math.random() * 24 * GlobalRepo.TILE));
	}

	public abstract TiledMap getMap();
	public abstract Vector2 getStartPosition();
	public Vector2 getCenterPosition(){
		return getStartPosition();
	}

	public List<Rectangle> getRectangleList(){ return rectangleList; }
	public List<Entity> getEntityList(){ return entityList; }
	public List<ActionCircle> getActionCircleList() { return actionCircleList; }
	public Music getMusic(){ return roomMusic; }
	public float getR(){ return r; }
	public float getB(){ return b; }
	public float getG(){ return g; }
	public float getA(){ return a; }
	public float getWind() { return wind; }
	public float getGravity() { return gravity; }
	public float getDispX() { return dispX; }
	public boolean scrolls() { return scrolls; }
	
	public static int getStaticNumber(){
		return -1;
	}
	
	public abstract int getNumber();
	
	public static String getName(){
		return "OOPS";
	}

}
