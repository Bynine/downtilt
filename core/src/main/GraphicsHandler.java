package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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

import challenges.Challenge;
import challenges.ChallengeTimed;
import challenges.ChallengeTutorial;
import challenges.ChallengeTutorial.ToolTip;
import entities.Entity;
import entities.Fighter;
import entities.Hittable;

public class GraphicsHandler {

	private static SpriteBatch batch;
	private static final OrthographicCamera cam = new OrthographicCamera();
	private static final OrthographicCamera parallaxCam = new OrthographicCamera();
	private static final OrthographicCamera dualParallaxCam = new OrthographicCamera();
	private static final int camAdjustmentLimiter = 16;
	private static OrthogonalTiledMapRenderer renderer;
	private static final float screenAdjust = 2f;
	private static final ShapeRenderer debugRenderer = new ShapeRenderer();
	private static BitmapFont font = new BitmapFont(), comboFont = new BitmapFont(), timeLimitFont = new BitmapFont();
	private static final Color fontColor = Color.WHITE;
	private static TextureRegion 
	guiBar = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/guibar.png"))),
	textBar = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/textbar.png"))),
	textBarSatisfied = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/textbarcomplete.png"))),
	defend = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/defend.png"))),
	perfectdefend = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/defendperfect.png"))),
	specialfull = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/specialfull.png"))),
	specialempty = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/specialempty.png"))),
	specialbar = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/specialbar.png"))),
	stock = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/stock.png"))),
	pauseOverlay = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/overlay_pause.png"))),
	ripplebg = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/ripplebg.png")));
	private static ShaderProgram hitstunShader, slowShader, airShader, defenseShader, powerShader, speedShader, wavyShader;

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
		slowShader = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/slow.glsl"));
		wavyShader = new ShaderProgram(Gdx.files.internal("shaders/vertwavy.glsl"), Gdx.files.internal("shaders/wavy.glsl"));
		batch = new SpriteBatch();
		cam.setToOrtho(false, SCREENWIDTH, SCREENHEIGHT);
		cam.zoom = ZOOM;
		parallaxCam.setToOrtho(false, SCREENWIDTH, SCREENHEIGHT);
		parallaxCam.zoom = ZOOM;
		dualParallaxCam.setToOrtho(false, SCREENWIDTH, SCREENHEIGHT);
		dualParallaxCam.zoom = ZOOM;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nes.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.color = fontColor;
		parameter.size = 8;
		parameter.spaceY = 2;
		font = generator.generateFont(parameter);
		parameter.borderWidth = 1;
		comboFont = generator.generateFont(parameter);
		parameter.borderWidth = 0;
		parameter.size = 12;
		parameter.gamma = 10;
		timeLimitFont = generator.generateFont(parameter); 
		generator.dispose();
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
			if (parallaxCam.position.x == 0) {
				parallaxCam.position.x = blackBounds;
				dualParallaxCam.position.x = blackBounds;
			}
			if (!DowntiltEngine.isPaused()) {
				float inc = DowntiltEngine.getChallenge().getStage().getScrollSpeed();
				if (DowntiltEngine.isSlowed()) inc = inc/6.0f;
				parallaxCam.position.x += inc;
				dualParallaxCam.position.x += inc/2.0f;
			}
			if (parallaxCam.position.x + blackBounds > MapHandler.mapWidth) parallaxCam.position.x = blackBounds;
			if (dualParallaxCam.position.x + blackBounds > MapHandler.mapWidth) dualParallaxCam.position.x = blackBounds;
		}
		else{
			parallaxCam.position.x = cam.position.x;
			float parallax = 1;
			int updateSpeed = 4;
			float updatePosition = cam.position.x - cam.viewportWidth + DowntiltEngine.getPlayers().get(0).getCenter().x;
			parallaxCam.position.x = 
					(parallaxCam.position.x * (updateSpeed - 1) +
							( (cam.position.x * (parallax - 1)) + updatePosition)/parallax)/updateSpeed;
			dualParallaxCam.position.x = (parallaxCam.position.x + cam.position.x)/2;
		}
		parallaxCam.position.y = cam.position.y;
		dualParallaxCam.position.y = cam.position.y;

		if (!DowntiltEngine.outOfHitlag() && !DowntiltEngine.isPaused()) shakeScreen();
		if (DowntiltEngine.justOutOfHitlag()) {
			cam.position.x = origCamPosition.x;
			cam.position.y = origCamPosition.y;
		}
		else origCamPosition.set(cam.position.x, cam.position.y);

		parallaxCam.position.x = (int) parallaxCam.position.x;
		dualParallaxCam.position.x = (int) dualParallaxCam.position.x;
		cam.update();
		parallaxCam.update();
		dualParallaxCam.update();
	}

	private static float screenBoundary(float dimension){
		return dimension/(screenAdjust/ZOOM) + GlobalRepo.TILE * 1;
	}

	private static class FighterSort implements Comparator<Entity> {

		@Override
		public int compare(Entity o1, Entity o2) {
			int o1Num = setNum(o1);
			int o2Num = setNum(o2);

			if (o1Num > o2Num) return 1;
			if (o1Num < o2Num) return -1;
			else return 0;
		}

		private int setNum(Entity o){
			if (o instanceof Hittable && ((Hittable)o).isCaught()) return 2;
			if (DowntiltEngine.entityIsPlayer(o)) return 1;
			else return 0;
		}

	}

	static void updateGraphics(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		int[] arr;
		batch.setProjectionMatrix(cam.combined);

		renderer.setView(cam);
		arr = new int[]{0};  // render sky
		renderer.render(arr);

		if (DowntiltEngine.getChallenge().getStage().crazy()) drawCrazyBG();

		renderer.setView(dualParallaxCam);
		arr = new int[]{1};  // render slow parallax
		renderer.render(arr);

		batch.begin();  // render bg entities
		batch.setProjectionMatrix(parallaxCam.combined);
		for (Entity e: MapHandler.activeRoom.getEntityList()) if (e.getLayer() == Entity.Layer.PARALLAXFAST) renderEntity(e);
		batch.end();
		font.setColor(1, 1, 1, 1);

		renderer.setView(parallaxCam);
		arr = new int[]{2};  // render fast parallax
		renderer.render(arr);
		renderer.getBatch().getShader().end();
		renderer.getBatch().setShader(null);

		batch.begin();  // render bg entities
		batch.setProjectionMatrix(cam.combined);
		for (Entity e: MapHandler.activeRoom.getEntityList()) if (e.getLayer() == Entity.Layer.MIDDLEBACK) renderEntity(e);
		batch.end();
		font.setColor(1, 1, 1, 1);

		renderer.setView(cam);
		arr = new int[]{3}; // render middle back
		renderer.render(arr);
		renderer.getBatch().setShader(null);

		batch.begin();  // render normal entities
		List<Entity> normEntityList = MapHandler.activeRoom.getEntityList();
		normEntityList.sort(new FighterSort());
		for (Entity e: normEntityList) if (e.getLayer() == Entity.Layer.MIDDLEFRONT) renderEntity(e);
		batch.end();
		font.setColor(1, 1, 1, 1);

		batch.begin(); 
		arr = new int[]{4, 5};  // render middle front, front
		renderer.render(arr);
		batch.end();

		batch.begin();  // render fg entities
		for (Entity e: MapHandler.activeRoom.getEntityList()) if (e.getLayer() == Entity.Layer.FRONT) renderEntity(e);
		batch.end();
		font.setColor(1, 1, 1, 1);

		if (DowntiltEngine.debugOn()) debugRender();
	}

	private static void drawCrazyBG(){ 
		batch.begin();
		float time = (float) Math.sin(DowntiltEngine.getDeltaTime()/200.0f);
		if (DowntiltEngine.getChallenge().bossHurt()) time = (float) Math.tan(Math.PI * time);
		wavyShader.begin();
		wavyShader.setUniformf("time", time);
		wavyShader.setUniformf("resolutionx", SCREENWIDTH);
		wavyShader.setUniformf("resolutiony", SCREENHEIGHT);
		wavyShader.end();
		batch.setShader(wavyShader);
		batch.draw(ripplebg, cameraBoundaries().get(0), cameraBoundaries().get(2));
		batch.setShader(null);
		batch.end();
	}

	private static void renderEntity(Entity en){
		boolean drawShield = false, drawPerfectShield = false;
		batch.setColor(en.getColor());

		if (en instanceof Fighter) {
			Fighter fi = (Fighter) en;
			setupFighter(fi);
			if (fi.isPerfectGuarding()) drawPerfectShield = true;
			else if (fi.isGuarding()) drawShield = true;
		}

		if (en instanceof Hittable) setupHittable((Hittable)en);

		batch.setColor(
				MathUtils.clamp(batch.getColor().r, 0, 1),
				MathUtils.clamp(batch.getColor().b, 0, 1),
				MathUtils.clamp(batch.getColor().g, 0, 1),
				batch.getColor().a);

		if (!(DowntiltEngine.entityIsPlayer(en) && DowntiltEngine.getChallenge().isFailed())) {
			batch.draw(en.getImage(), en.getPosition().x, en.getPosition().y);
		}

		if (en.trails()) drawTrail(en);

		batch.setShader(null);
		batch.setColor(1, 1, 1, 1);

		drawShields(en, drawShield, drawPerfectShield);
	}

	private static void setupFighter(Fighter fi){
		if (fi.getCombo().getRank() > 0) drawCombo(fi);
		if (isOffScreen(fi) && !fi.inHitstun()) drawFighterIcon(fi);
		if (DowntiltEngine.debugOn()) drawPercentage(fi);

		float colorMod = 0.5f * fi.getPercentage() / fi.getWeight();
		float rColor = MathUtils.clamp(fi.getColor().r - colorMod / 2, 0.45f, 1);
		float gbColor = MathUtils.clamp(fi.getColor().g - colorMod, 0.15f, 1);

		if (fi.getArmor() > 1) drawAfterImage(fi, batch.getShader(), defenseShader);

		if (null != fi.getPalette()) {
			ShaderProgram palette = fi.getPalette();
			palette.begin();
			float alpha = 1;
			try{
				if (fi.isInvincible()) alpha = 0.5f;
				palette.setUniformf(GlobalRepo.alpha, alpha);
				palette.setUniformf(GlobalRepo.red, rColor);
				palette.setUniformf(GlobalRepo.gb, gbColor);
			}
			catch(IllegalArgumentException iae){
				System.out.println("Encountered a problem while setting palette parameters. Source:\n" + iae.getMessage());
			}
			palette.end();
			batch.setShader(palette);
		}
		else {
			batch.setColor(fi.getColor());
			batch.setColor(rColor, gbColor, gbColor, 1);
			if (fi.isInvincible()) batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.5f);
		}

		if (fi.powerActive() && fi.defenseActive() && fi.speedActive() && fi.airActive()){
			drawRainbowAfterImage(fi, batch.getShader());
		}
		else {
			if (fi.powerActive()) drawAfterImage(fi, batch.getShader(), powerShader);
			if (fi.defenseActive()) drawAfterImage(fi, batch.getShader(), defenseShader);
			if (fi.speedActive()) drawAfterImage(fi, batch.getShader(), speedShader);
			if (fi.airActive()) drawAfterImage(fi,  batch.getShader(), airShader);
		}
	}

	private static void setupHittable(Hittable hi){
		if (DowntiltEngine.isSlowed() && !DowntiltEngine.entityIsPlayer(hi)) batch.setShader(slowShader);
		if (hi.getHitstunTimer().getCounter() < GlobalRepo.WHITEFREEZE) {
			if (hi instanceof Fighter) ((Fighter)hi).setHitstunImage();
			batch.setShader(hitstunShader);
		}
	}

	private static void drawTrail(Entity en){
		batch.setColor(1, 1, 1, 0.5f);
		batch.draw(en.getImage(), en.getPosition().x - (en.getVelocity().x * 2), en.getPosition().y - (en.getVelocity().y * 2));
		batch.setColor(1, 1, 1, 0.25f);
		batch.draw(en.getImage(), en.getPosition().x - (en.getVelocity().x * 4), en.getPosition().y - (en.getVelocity().y * 4));
	}

	private static void drawShields(Entity en, boolean drawShield, boolean drawPerfectShield){
		float posX = en.getCenter().x - 16 + en.direct() * 6;
		float posY = en.getCenter().y - 8;
		if (drawShield) batch.draw(defend, posX, posY);
		if (drawPerfectShield) batch.draw(perfectdefend, posX, posY);
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
		TextureRegion image = fi.getImage();
		int disp = 8;
		for (int i = 0; i < repeat; ++i){
			batch.draw(image, posX - disp/2, posY - disp/2, image.getRegionWidth() + disp, image.getRegionHeight() + disp);
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
	
	private static void drawCombo(Fighter fi) {
		float xPos = fi.getPosition().x + fi.getImage().getWidth()/2 - 8;
		float yPos = fi.getPosition().y + fi.getImage().getHeight() + font.getLineHeight();
		float comboColor = (fi.getCombo().getRank() - 1)/8.0f;
		final float powMod = 0.8f;

		if (comboColor > 1) comboColor = 1;
		comboFont.setColor((float) Math.pow(1 - comboColor, powMod), (float) Math.pow(comboColor, powMod), 0, 1);
		comboFont.draw(batch, fi.getCombo().getRank() + "x", xPos, yPos + 8);
		comboFont.setColor(fontColor);
	}

	private static void drawPercentage(Entity e) {
		Fighter fi = (Fighter) e;
		float xPos = fi.getPosition().x - 16 + fi.getImage().getWidth()/2;
		float yPos = fi.getPosition().y + fi.getImage().getHeight() + font.getLineHeight();
		font.draw(batch, (int)fi.getPercentage() + "%", xPos, yPos);
	}

	static void renderGUI(){
		batch.begin();
		float stockLocationMod = 1/4.3f;
		batch.draw(guiBar, cameraBoundaries().get(0), cameraBoundaries().get(2));
		float posY = cam.position.y - SCREENHEIGHT * stockLocationMod + 6;
		float posX = cam.position.x - 8 - SCREENWIDTH * stockLocationMod;
		int spacing = stock.getRegionWidth() + 1;
		int spaceMod = 0;

		int lives = DowntiltEngine.getChallenge().getLives();
		if (lives <= 5){
			font.draw(batch, "tries: ", posX, posY);
			for (int i = 0; i < lives; ++i){
				batch.draw(stock, posX + 48 + spaceMod, posY - 8);
				spaceMod += spacing; 
			}
		}

		int special = (int) (DowntiltEngine.getChallenge().getSpecialMeter() * 2);
		int posYMod = -2;
		spacing = specialfull.getRegionWidth();
		spaceMod = 0;
		posX = cam.position.x - SCREENWIDTH * (stockLocationMod/1.5f);

		batch.draw(specialbar, posX, posY + posYMod + specialfull.getRegionHeight() - specialbar.getRegionHeight());
		for (int i = 0; i < special; ++i){
			batch.draw(specialfull, posX + spaceMod, posY + posYMod);
			spaceMod += spacing; 
		}
		for (int i = spaceMod/spacing; i < Challenge.SPECIALMETERMAX * 2; ++i){
			batch.draw(specialempty, posX + spaceMod, posY + posYMod);
			spaceMod += spacing;
		}

		if (DowntiltEngine.getChallenge() instanceof ChallengeTimed) {
			int timeLeft = ((ChallengeTimed)DowntiltEngine.getChallenge()).getTimeLeftInSeconds();
			int limitRed = 10;
			int limitOrange = 30;
			int limitYellow = 60;
			if (timeLeft == limitRed || timeLeft == limitOrange || timeLeft == limitYellow) timeLimitFont.setColor(Color.WHITE);
			else if (timeLeft < limitRed) timeLimitFont.setColor(Color.RED);
			else if (timeLeft < limitOrange) timeLimitFont.setColor(Color.ORANGE);
			else if (timeLeft < limitYellow) timeLimitFont.setColor(Color.YELLOW);
			else timeLimitFont.setColor(Color.GREEN);
			timeLimitFont.draw(batch, DowntiltEngine.getChallenge().getTime(), cam.position.x - 32, posY);
		}
		else font.draw(batch, DowntiltEngine.getChallenge().getTime(), cam.position.x, posY);

		font.draw(batch, "BEST COMBO: " + DowntiltEngine.getChallenge().getLongestCombo(), cam.position.x + SCREENWIDTH * (stockLocationMod/3.0f), posY);
		font.draw(batch, DowntiltEngine.getChallenge().getWaveCounter(), cam.position.x + SCREENWIDTH * (stockLocationMod/1.4f), posY);
		if (DowntiltEngine.getChallenge() instanceof ChallengeTutorial) drawToolTip();

		if (DowntiltEngine.isPaused()) {
			batch.draw(pauseOverlay, cameraBoundaries().get(0), cameraBoundaries().get(2), SCREENWIDTH, SCREENHEIGHT);
			comboFont.draw(batch, "PAUSED", cam.position.x - 20, cam.position.y);
			font.draw(batch, "(Press " + DowntiltEngine.getPrimaryInputHandler().getChargeString() + " to quit)", cam.position.x - 60, cam.position.y - GlobalRepo.TILE);
		}
		batch.end();
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

	public static void updateRoomGraphics() {
		renderer = new OrthogonalTiledMapRenderer(MapHandler.activeMap, 1);
		cam.position.x = DowntiltEngine.getPlayers().get(0).getPosition().x;
		cam.position.y = DowntiltEngine.getPlayers().get(0).getPosition().y;
		cam.update();
		renderer.setView(cam);
	}

	private static void shakeScreen(){
		cam.position.x += shakeScreenHelper();
		cam.position.y += shakeScreenHelper();
	}

	private static double shakeScreenHelper() { 
		double posOrNeg = Math.signum(0.5 - Math.random());
		return DowntiltEngine.getScreenShakeMod() * posOrNeg * 9.0 * (0.95 + (Math.random()/10.0));
	}

	public static void drawToolTip(){
		ToolTip tt = ((ChallengeTutorial)DowntiltEngine.getChallenge()).getToolTip();
		String text = tt.getString();
		float posX = cameraBoundaries().get(0) + 100;
		float posY = cameraBoundaries().get(3) - textBar.getRegionHeight() - GlobalRepo.TILE / 2;
		if (tt.isSatisfied()) {
			batch.draw(textBarSatisfied, posX, posY);
			comboFont.draw(batch, DowntiltEngine.getPrimaryInputHandler().getSelectString() + ">", posX + textBar.getRegionWidth() - 64, posY + 12);
		}
		else batch.draw(textBar, posX, posY);
		font.draw(batch, text, posX + GlobalRepo.TILE / 4, posY + 54);
	}

	private static void debugRender(){
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

	public static void drawMessage(String errorMessage) {
		batch.begin();
		comboFont.draw(batch, 
				  "Encountered error:\n"
				  + errorMessage + "\n"
				  + "Sorry!. Please email me at byninegiga@gmail.com\n"
				  + "with a screencap of this error.",
				cameraBoundaries().get(0), cameraBoundaries().get(3));
		batch.end();
	}

}
