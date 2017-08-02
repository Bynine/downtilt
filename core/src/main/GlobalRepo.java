package main;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import entities.Basic;
import entities.Hittable;

public class GlobalRepo {
	
	/* GLOBAL VARIABLES */
	
	public static final int TILE = 32;
	public static final int GOODTEAM = 0;
	public static final int BADTEAM = 1;
	public static final int NOTEAM = 2;
	public static final float HEROHITSTUNMOD = 0.7f;
	public static final float ENEMYHITSTUNMOD = 0.9f;
	public static final int WHITEFREEZE = 1;
	private static BlockColor blockColor = BlockColor.G;
	
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
	
	public static String getTimeString(int sec){
		int minutes = (sec /60);
		int seconds = (sec) - (minutes);
		String secondsString = "" + seconds;
		if (secondsString.length() == 1) secondsString = "0".concat(secondsString);
		return minutes + ":" + secondsString;
	}
	
}
