package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import moves.ActionCircle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import entities.Entity;
import entities.Fighter;
import entities.Hittable;

public class GraphicsHandler {

	private static SpriteBatch batch;
	private static final OrthographicCamera cam = new OrthographicCamera();
	private static final OrthographicCamera parallaxCam = new OrthographicCamera();
	private static final int camAdjustmentLimiter = 16;
	private static OrthogonalTiledMapRenderer renderer;
	private static final float screenAdjust = 2f;
	private static final ShapeRenderer debugRenderer = new ShapeRenderer();
	private static BitmapFont font = new BitmapFont();
	private static TextureRegion guiBar = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/guibar.png")));
	private static ShaderProgram hitstunShader, airShader, defenseShader, powerShader, speedShader;

	public static final int SCREENWIDTH  = (int) ((42 * GlobalRepo.TILE));
	public static final int SCREENHEIGHT = (int) ((24 * GlobalRepo.TILE));
	public static final float ZOOM2X = 1/2f;
	public static final float ZOOM1X = 1/1f;
	static float ZOOM = ZOOM2X;

	private final static Vector2 origCamPosition = new Vector2(0, 0);

	public static void begin() {
		hitstunShader = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/hit.glsl"));
		airShader = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/air.glsl"));
		defenseShader = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/defense.glsl"));
		powerShader = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/power.glsl"));
		speedShader = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/speed.glsl"));
		batch = new SpriteBatch();
		cam.setToOrtho(false, SCREENWIDTH, SCREENHEIGHT);
		cam.zoom = ZOOM;
		parallaxCam.setToOrtho(false, SCREENWIDTH, SCREENHEIGHT);
		parallaxCam.zoom = ZOOM;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nes.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 8;
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	public static Predicate<? super Fighter> isDead() {
		return p -> p.getLives() <= 0;
	}

	static void updateCamera(){
		float centerX = 0;
		float centerY = 0;
		float yDisplacement = GlobalRepo.TILE * 1;

		Vector2 combatPosition = DowntiltEngine.getChallenge().getCenterPosition();
		centerX = combatPosition.x;
		centerY = combatPosition.y + yDisplacement * 2;

		cam.position.x = (cam.position.x*(camAdjustmentLimiter-1) + centerX)/camAdjustmentLimiter;
		cam.position.y = (cam.position.y*(camAdjustmentLimiter-1) + yDisplacement + centerY)/camAdjustmentLimiter;

		cam.position.x = MathUtils.round(MathUtils.clamp(cam.position.x, screenBoundary(SCREENWIDTH), MapHandler.mapWidth - screenBoundary(SCREENWIDTH)));
		cam.position.y = MathUtils.round(MathUtils.clamp(cam.position.y, screenBoundary(SCREENHEIGHT), MapHandler.mapHeight - screenBoundary(SCREENHEIGHT)));

		parallaxCam.position.x = cam.position.x/2 + SCREENWIDTH/4;
		parallaxCam.position.y = cam.position.y;

		if (!DowntiltEngine.outOfHitlag()) shakeScreen();
		if (DowntiltEngine.justOutOfHitlag()) {
			cam.position.x = origCamPosition.x;
			cam.position.y = origCamPosition.y;
		}
		else origCamPosition.set(cam.position.x, cam.position.y);

		cam.update();
		parallaxCam.update();
	}

	private static float screenBoundary(float dimension){
		return dimension/(screenAdjust/ZOOM) + GlobalRepo.TILE * 1;
	}

	static void updateGraphics(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		int[] arr;
		renderer.setView(parallaxCam);

		arr = new int[]{0};  // render sky
		renderer.render(arr);

		batch.begin();  // render bg entities
		for (Entity e: MapHandler.activeRoom.getEntityList()) if (e.getLayer() == Entity.Layer.BACKGROUND) renderEntity(e);
		batch.end();
		font.setColor(1, 1, 1, 1);

		arr = new int[]{1};  // render back
		renderer.render(arr);

		renderer.setView(cam);
		int numLayers = MapHandler.activeMap.getLayers().getCount() - 2;  // render tiles
		arr = new int[numLayers];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = i + 2;
		}
		renderer.render(arr);
		renderer.getBatch().setShader(null);

		batch.begin();  // render fg entities
		for (Entity e: MapHandler.activeRoom.getEntityList()) if (e.getLayer() == Entity.Layer.FOREGROUND) renderEntity(e);
		batch.end();
		font.setColor(1, 1, 1, 1);

		arr = new int[]{numLayers-1};  // render foreground
		renderer.render(arr);

		if (DowntiltEngine.debugToggle) debugRender();

		batch.begin();
		renderGUI();
		batch.end();
	}

	private static void renderEntity(Entity e){
		batch.setColor(e.getColor());
		if (e instanceof Fighter) {
			Fighter fi = (Fighter) e;
			drawFighterPercentage(fi);
			if (isOffScreen(fi) && !fi.inHitstun()) drawFighterIcon(fi);
			if (DowntiltEngine.debugToggle) drawState(e);

			batch.setColor(batch.getColor().r - 0.1f, batch.getColor().g - 0.1f, batch.getColor().g - 0.1f, 1);
			if (fi.isInvincible()) 
				batch.setColor(batch.getColor().r - 0.5f, batch.getColor().g * 2, batch.getColor().g * 2, 1);
			else if (fi.isCharging()) 
				batch.setColor(batch.getColor().r + 0.1f, batch.getColor().g + 0.1f, batch.getColor().g + 0.1f, 1);
			if (null != fi.getPalette()) batch.setShader(fi.getPalette());

			if (!fi.powerTimer.timeUp()) drawAfterImage(fi, batch.getColor(), batch.getShader(), powerShader);
			if (!fi.defenseTimer.timeUp()) drawAfterImage(fi, batch.getColor(),  batch.getShader(), defenseShader);
			if (!fi.airTimer.timeUp()) drawAfterImage(fi, batch.getColor(),  batch.getShader(), airShader);
			if (!fi.speedTimer.timeUp()) drawAfterImage(fi, batch.getColor(),  batch.getShader(), speedShader);
		}
		if (e instanceof Hittable){
			Hittable h = (Hittable) e;
			if (h.getHitstunTimer().getCounter() < GlobalRepo.WHITEFREEZE) batch.setShader(hitstunShader);
		}
		batch.draw(e.getImage(), e.getPosition().x, e.getPosition().y);
		batch.setColor(1, 1, 1, 1);
		batch.setShader(null);
	}

	private static void drawAfterImage(Fighter fi, Color c, ShaderProgram origSG, ShaderProgram newSG){
		batch.setShader(newSG);
		batch.draw(fi.getImage(), fi.getPosition().x - fi.getVelocity().x*2, fi.getPosition().y - fi.getVelocity().y*2);
		batch.draw(fi.getImage(), fi.getPosition().x - fi.getVelocity().x*2, fi.getPosition().y - fi.getVelocity().y*2);
		batch.draw(fi.getImage(), fi.getPosition().x - fi.getVelocity().x*2, fi.getPosition().y - fi.getVelocity().y*2);
		batch.setColor(new Color(c.r, c.b, c.g, c.a/2));
		batch.draw(fi.getImage(), fi.getPosition().x - fi.getVelocity().x*4, fi.getPosition().y - fi.getVelocity().y*4);
		batch.draw(fi.getImage(), fi.getPosition().x - fi.getVelocity().x*4, fi.getPosition().y - fi.getVelocity().y*4);
		batch.setColor(new Color(c.r, c.b, c.g, c.a/2));
		batch.draw(fi.getImage(), fi.getPosition().x - fi.getVelocity().x*6, fi.getPosition().y - fi.getVelocity().y*6);
		batch.setColor(c);
		batch.setShader(origSG);
	}

	private static boolean isOffScreen(Entity e){
		boolean left = e.getPosition().x + e.getImage().getWidth() < cameraBoundaries().get(0);
		boolean right = e.getPosition().x > cameraBoundaries().get(1);
		boolean top = e.getPosition().y + e.getImage().getHeight() < cameraBoundaries().get(2);
		boolean bottom = e.getPosition().y > cameraBoundaries().get(3);
		return left || right || top || bottom;
	}

	private final static float iconDimension = 32;
	private static void drawFighterIcon(Fighter fi){
		float iconX = MathUtils.clamp(fi.getPosition().x, cameraBoundaries().get(0), cameraBoundaries().get(1) - iconDimension);
		float iconY = MathUtils.clamp(fi.getPosition().y, cameraBoundaries().get(2), cameraBoundaries().get(3) - iconDimension);
		batch.draw(fi.getIcon(), iconX, iconY);
	}

	private static void renderGUI(){
		float lineHeight = 8;
		float stockLocationMod = 1/4.3f;
		batch.draw(guiBar, cameraBoundaries().get(0), cameraBoundaries().get(2));
		for (Fighter player: DowntiltEngine.getPlayers()){
			font.draw(batch, "lives: " + player.getLives(), 
					cam.position.x - SCREENWIDTH * stockLocationMod, cam.position.y - SCREENHEIGHT * stockLocationMod + lineHeight);
			font.draw(batch, "special: " + player.getSpecialMeter(), 
					cam.position.x - SCREENWIDTH * (stockLocationMod/1.5f), cam.position.y - SCREENHEIGHT * stockLocationMod + lineHeight);
			lineHeight *= -1/2;
		}
		font.draw(batch,  DowntiltEngine.getChallenge().getEnemyCounter(), 
				cam.position.x + SCREENWIDTH * (stockLocationMod/1.6f), cam.position.y - SCREENHEIGHT * stockLocationMod);
		if (DowntiltEngine.isPaused()) {
			font.draw(batch, "PAUSED", cam.position.x, cam.position.y);
			font.draw(batch, "Press Select to quit", cam.position.x, cam.position.y - GlobalRepo.TILE * 2);
		}
	}

	private static List<Float> cameraBoundaries(){
		return new ArrayList<Float>(Arrays.asList( 
				(cam.position.x - SCREENWIDTH/2*ZOOM),
				(cam.position.x + SCREENWIDTH/2*ZOOM),
				(cam.position.y - SCREENHEIGHT/2*ZOOM),
				(cam.position.y + SCREENHEIGHT/2*ZOOM)
				) );
	}

	public static Rectangle getCameraBoundary() {
		List<Float> bounds = cameraBoundaries();
		return new Rectangle(bounds.get(0), bounds.get(2), bounds.get(1) - bounds.get(0), bounds.get(3) - bounds.get(2));
	}

	public static Vector3 getCameraPos(){
		return cam.position;
	}

	private static void drawFighterPercentage(Entity e) {
		Fighter fi = (Fighter) e;
		float darken = fi.getPercentage()*0.0075f;
		font.setColor(1, 1 - darken, 1 - (darken*1.1f), 1);
		float xPos = fi.getPosition().x + fi.getImage().getWidth()/2;
		float yPos = fi.getPosition().y + fi.getImage().getHeight() + font.getLineHeight();
		font.draw(batch, "" + (int)(fi.getPercentage()), xPos, yPos);
	}

	private static void drawState(Entity e) {
		Fighter fi = (Fighter) e;
		float xPos = fi.getPosition().x - 16 + fi.getImage().getWidth()/2;
		float yPos = fi.getPosition().y + fi.getImage().getHeight() + font.getLineHeight() * 2;
		font.draw(batch, fi.getState().toString(), xPos, yPos);
	}

	public static void updateRoomGraphics(Fighter player) {
		renderer = new OrthogonalTiledMapRenderer(MapHandler.activeMap, 1);
		cam.position.x = player.getPosition().x;
		cam.position.y = player.getPosition().y;
		cam.update();
		renderer.setView(cam);
	}

	private static void shakeScreen(){
		cam.position.x += shakeScreenHelper();
		cam.position.y += shakeScreenHelper();
	}

	private static double shakeScreenHelper() { 
		double posOrNeg = Math.signum(0.5 - Math.random());
		return posOrNeg * 9.0 * (0.95 + (Math.random()/20.0));
	}

	public static void debugRender(){
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Filled);
		for (ActionCircle ac: MapHandler.activeRoom.getActionCircleList()){
			Circle c = ac.getArea();
			debugRenderer.setColor(ac.getColor());
			if (ac.toRemove()) debugRenderer.setColor(0.9f, 1, 1, 0.5f);
			debugRenderer.circle(c.x, c.y, c.radius);
		}
		debugRenderer.setColor(0, 1, 0, 0.4f);
		for (Entity e: MapHandler.activeRoom.getEntityList()){
			Rectangle ell = e.getHurtBox();
			if (e instanceof Hittable) {
				debugRenderer.rect(ell.x, ell.y, ell.width, ell.height);
			}
		}
		debugRenderer.end();
	}

}
