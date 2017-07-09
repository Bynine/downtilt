package main;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

import entities.Basic;
import entities.Entity;
import entities.Entity.Direction;
import entities.Hittable;

public class GlobalRepo {
	
	/* GLOBAL VARIABLES */
	
	public static final int TILE = 32;
	public static final int GOODTEAM = 0;
	public static final int BADTEAM = 1;
	public static final int NOTEAM = 2;
	public static final float HEROHITSTUNMOD = 0.7f;
	public static final float ENEMYHITSTUNMOD = 1.0f;
	public static final int WHITEFREEZE = 1;
	private static BlockColor blockColor = BlockColor.R;
	
	/* GLOBAL METHODS */

	public static Animation makeAnimation(String address, int cols, int rows, float speed, PlayMode playMode){
		Texture sheet = new Texture(Gdx.files.internal(address));
		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/cols, sheet.getHeight()/rows);
		TextureRegion[] frames = new TextureRegion[cols * rows];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		Animation animation = new Animation(speed, frames);
		animation.setPlayMode(playMode);
		return animation;
	}
	
	public static void log(String str){
		System.out.println(str + " at " + DowntiltEngine.getDeltaTime());
	}
	
	public static Rectangle makeRectangleFromRectangle(Rectangle r){
		return new Rectangle(r.x, r.y, r.width, r.height);
	}
	
	public static Rectangle makeHurtBox(Entity user, int width, int height){
		return makeHurtBoxHelper(user, width, height);
	}
	
	public static Rectangle makeHurtBoxDynamic(Entity user, float scaleX, float scaleY){
		Rectangle hurtBox = user.getImage().getBoundingRectangle();
		int newWidth = (int) (hurtBox.getWidth() * scaleX);
		int newHeight = (int) (hurtBox.getHeight() * scaleY);
		return makeHurtBoxHelper(user, newWidth, newHeight);
	}
	
	private static Rectangle makeHurtBoxHelper(Entity user, int width, int height){
		Rectangle hurtBox = GlobalRepo.makeRectangleFromRectangle(user.getImage().getBoundingRectangle());
		float changeX = hurtBox.width - width;
		float changeY = hurtBox.height - height;
		hurtBox.setSize(width, height);
		float newX = hurtBox.x + changeX/2;
		float newY = hurtBox.y + changeY/2;
		hurtBox.setPosition(newX, newY);
		return hurtBox;
	}
	
	public static Rectangle makeHurtBoxInner(Entity user, int width, int height){
		return makeHurtBoxDirection(user, width, height, user.getDirection() == Direction.LEFT);
	}
	
	public static Rectangle makeHurtBoxOuter(Entity user, int width, int height){
		return makeHurtBoxDirection(user, width, height, user.getDirection() == Direction.RIGHT);
	}
	
	private static Rectangle makeHurtBoxDirection(Entity user, int width, int height, boolean dir){
		Rectangle origBox = user.getImage().getBoundingRectangle();
		Rectangle hurtBox = makeHurtBoxHelper(user, width, height);
		if (dir) hurtBox.x = origBox.x + (origBox.width - hurtBox.width);
		else hurtBox.x = origBox.x;
		return hurtBox;
	}
	
	public static <T> T getRandomElementFromList(List<T> lst){
		return lst.get( (int) (Math.random() * lst.size()) );
	}
	
	static final Hittable genericHittable = new Basic(0, 0, NOTEAM);
	public static Hittable getGenericHittable(){
		return genericHittable;
	}
	
	/* MISC */
	

	public enum BlockColor{
		R, G, B
	}

	public static void rotateBlocks() {
		new SFX.Collect().play();
		switch(blockColor){
		case R: blockColor = GlobalRepo.BlockColor.G; break;
		case G: blockColor = GlobalRepo.BlockColor.B; break;
		case B: blockColor = GlobalRepo.BlockColor.R; break;
		}
	}
	
	public static BlockColor getBlockColor(){
		return blockColor;
	}
	
}
