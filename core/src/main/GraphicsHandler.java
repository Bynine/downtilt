package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import moves.ActionCircle;

import com.badlogic.gdx.Gdx;
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

import entities.Boss;
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
	private static TextureRegion 
	guiBar = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/guibar.png"))),
	defend = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/defend.png")));
	private static ShaderProgram hitstunShader, airShader, defenseShader, powerShader, speedShader;

	public static final int SCREENWIDTH  = (int) ((42 * GlobalRepo.TILE)), SCREENHEIGHT = (int) ((24 * GlobalRepo.TILE));
	public static final float ZOOM4X = 1/4f, ZOOM2X = 1/2f, ZOOM1X = 1/1f;
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

		if (DowntiltEngine.getChallenge().getStage().scrolls()){
			final float blackBounds = SCREENWIDTH/4;
			if (parallaxCam.position.x == 0) parallaxCam.position.x = blackBounds;
			if (!DowntiltEngine.isPaused()) parallaxCam.position.x += 12;
			if (parallaxCam.position.x + blackBounds > MapHandler.mapWidth) parallaxCam.position.x = blackBounds;
		}
		else{
			parallaxCam.position.x = cam.position.x;
			int parallax = 2;
			int updateSpeed = 4;
			float updatePosition = cam.position.x - cam.viewportWidth + DowntiltEngine.getPlayers().get(0).getPosition().x;
			parallaxCam.position.x = 
					(parallaxCam.position.x * (updateSpeed - 1) +
							( (cam.position.x * (parallax - 1)) + updatePosition)/parallax)/updateSpeed;
		}
		parallaxCam.position.y = cam.position.y;

		if (!DowntiltEngine.outOfHitlag() && !DowntiltEngine.isPaused()) shakeScreen();
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

		arr = new int[]{1};  // render back
		renderer.render(arr);

		batch.begin();  // render bg entities
		for (Entity e: MapHandler.activeRoom.getEntityList()) if (e.getLayer() == Entity.Layer.BACKGROUND) renderEntity(e);
		batch.end();
		font.setColor(1, 1, 1, 1);

		renderer.setView(cam);
		int numLayers = MapHandler.activeMap.getLayers().getCount() - 3;  // render tiles
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

		batch.begin(); 
		arr = new int[]{numLayers - 0, numLayers - 1};  // render front
		renderer.render(arr);
		batch.end();

		batch.begin();  // render fg entities
		for (Entity e: MapHandler.activeRoom.getEntityList()) if (e.getLayer() == Entity.Layer.FRONT) renderEntity(e);
		batch.end();
		font.setColor(1, 1, 1, 1);

		if (DowntiltEngine.debugOn()) debugRender();

		batch.begin();
		renderGUI();
		batch.end();
	}

	private static void renderEntity(Entity e){
		boolean drawShield = false;
		batch.setColor(e.getColor());
		if (e instanceof Fighter) {
			Fighter fi = (Fighter) e;
			if (fi.getCombo().getRank() > 0) drawCombo(fi);
			if (isOffScreen(fi) && !fi.inHitstun()) drawFighterIcon(fi);
			if (DowntiltEngine.debugOn()) drawPercentage(e);

			float colorMod = 0.5f * fi.getPercentage() / fi.getWeight();
			float rColor = MathUtils.clamp(1 - colorMod / 2, 0.45f, 1);
			float gbColor = MathUtils.clamp(1 - colorMod, 0.15f, 1);
			batch.setColor(rColor, gbColor, gbColor, 1);

			if (fi.isInvincible()) batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.5f);
			if (null != fi.getPalette()) batch.setShader(fi.getPalette());
			if (fi.powerActive() && fi.defenseActive() && fi.speedActive() && fi.airActive()){
				drawRainbowAfterImage(fi, batch.getShader());
			}
			else {
				if (fi.powerActive()) drawAfterImage(fi, batch.getShader(), powerShader);
				if (fi.defenseActive()) drawAfterImage(fi, batch.getShader(), defenseShader);
				if (fi.speedActive()) drawAfterImage(fi, batch.getShader(), speedShader);
				if (fi.airActive()) drawAfterImage(fi,  batch.getShader(), airShader);
			}
			if (fi.isGuarding() || fi.isPerfectGuarding()) drawShield = true;
		}
		if (e instanceof Hittable){
			Hittable h = (Hittable) e;
			if (h.getHitstunTimer().getCounter() < GlobalRepo.WHITEFREEZE) {
				if (h instanceof Fighter) ((Fighter)e).setHitstunImage();
				batch.setShader(hitstunShader);
			}
		}
		if (e instanceof Boss && DowntiltEngine.debugOn()){
			Boss b = (Boss) e;
			font.draw(batch, b.getHealth() + "", b.getPosition().x, b.getPosition().y + b.getImage().getHeight());
		}
		batch.draw(e.getImage(), e.getPosition().x, e.getPosition().y);
		if (drawShield) batch.draw(defend, e.getCenter().x - 16, e.getCenter().y - 16);
		batch.setColor(1, 1, 1, 1);
		batch.setShader(null);
	}

	private static void drawAfterImage(Fighter fi, ShaderProgram origSG, ShaderProgram newSG){
		batch.setShader(newSG);
		drawAfterImageHelper(fi, fi.getPosition().x - fi.getVelocity().x*2, fi.getPosition().y - fi.getVelocity().y*2, 4);
		drawAfterImageHelper(fi, fi.getPosition().x - fi.getVelocity().x*4, fi.getPosition().y - fi.getVelocity().y*4, 2);
		drawAfterImageHelper(fi, fi.getPosition().x - fi.getVelocity().x*6, fi.getPosition().y - fi.getVelocity().y*6, 1);
		batch.setShader(origSG);
	}

	private static void drawRainbowAfterImage(Fighter fi,ShaderProgram origSG){
		batch.setShader(powerShader);
		drawAfterImageHelper(fi, fi.getPosition().x - fi.getVelocity().x*2, fi.getPosition().y - fi.getVelocity().y*2, 4);
		batch.setShader(airShader);
		drawAfterImageHelper(fi, fi.getPosition().x - fi.getVelocity().x*4, fi.getPosition().y - fi.getVelocity().y*4, 3);
		batch.setShader(speedShader);
		drawAfterImageHelper(fi, fi.getPosition().x - fi.getVelocity().x*6, fi.getPosition().y - fi.getVelocity().y*6, 4);
		batch.setShader(defenseShader);
		drawAfterImageHelper(fi, fi.getPosition().x - fi.getVelocity().x*8, fi.getPosition().y - fi.getVelocity().y*8, 1);
		batch.setShader(origSG);
	}

	private static void drawAfterImageHelper(Fighter fi, float posX, float posY, int repeat){
		for (int i = 0; i < repeat; ++i){
			batch.draw(fi.getImage(), posX, posY);
		}
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
		font.draw(batch, DowntiltEngine.getChallenge().getTime(), 
				cam.position.x, cam.position.y - SCREENHEIGHT * stockLocationMod);
		font.draw(batch,  "BEST COMBO: " + DowntiltEngine.getChallenge().getLongestCombo(), 
				cam.position.x + SCREENWIDTH * (stockLocationMod/ 3f), cam.position.y - SCREENHEIGHT * stockLocationMod);
		font.draw(batch,  DowntiltEngine.getChallenge().getWaveCounter(), 
				cam.position.x + SCREENWIDTH * (stockLocationMod/1.4f), cam.position.y - SCREENHEIGHT * stockLocationMod);
		
		if (DowntiltEngine.isPaused()) {
			font.draw(batch, "PAUSED", cam.position.x, cam.position.y);
			font.draw(batch, "Press Y to quit", cam.position.x, cam.position.y - GlobalRepo.TILE * 2);
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

	private static void drawCombo(Fighter fi) {
		float xPos = fi.getPosition().x + fi.getImage().getWidth()/2;
		float yPos = fi.getPosition().y + fi.getImage().getHeight() + font.getLineHeight();
		float combo = (fi.getCombo().getRank() - 1)/8.0f;
		final float powMod = 0.75f;

		if (combo > 1) combo = 1;
		font.setColor((float) Math.pow(1 - combo, powMod), (float) Math.pow(combo, powMod), 0, 1);
		font.draw(batch, fi.getCombo().getRank() + "x", xPos, yPos + 8);
	}

	private static void drawPercentage(Entity e) {
		Fighter fi = (Fighter) e;
		float xPos = fi.getPosition().x - 16 + fi.getImage().getWidth()/2;
		float yPos = fi.getPosition().y + fi.getImage().getHeight() + font.getLineHeight() * 3;
		font.draw(batch, (int)fi.getPercentage() + "%", xPos, yPos);
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
		debugRenderer.begin(ShapeType.Line);
		for (ActionCircle ac: MapHandler.activeRoom.getActionCircleList()){
			Circle actionCircle = ac.getArea();
			debugRenderer.setColor(ac.getColor());
			if (ac.toRemove()) debugRenderer.setColor(0.9f, 1, 1, 0.5f);
			debugRenderer.circle(actionCircle.x, actionCircle.y, actionCircle.radius);
		}

		for (Entity e: MapHandler.activeRoom.getEntityList()){
			Rectangle hurtbox = e.getHurtBox();
			if (e instanceof Hittable) {
				debugRenderer.setColor(0, 1, 0, 0.4f);
				debugRenderer.rect(hurtbox.x, hurtbox.y, hurtbox.width, hurtbox.height);
			}
			if (e instanceof Fighter){
				Fighter fi = (Fighter) e;
				Rectangle groundBelowRect = fi.groundBelowRect();
				if (fi.groundBelow()) debugRenderer.setColor(1.0f, 1.0f, 0.0f, 0.75f);
				else debugRenderer.setColor(0.0f, 0.0f, 1.0f, 0.75f);
				debugRenderer.rect(groundBelowRect.x, groundBelowRect.y, groundBelowRect.width, groundBelowRect.height);
			}
		}
		debugRenderer.end();
	}

}
