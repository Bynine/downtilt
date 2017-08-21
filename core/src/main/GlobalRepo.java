package main;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import challenges.Victory.Ranking;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import entities.Basic;
import entities.Hittable;
import maps.Stage_Blocks;
import maps.Stage_Boss;
import maps.Stage_Mushroom;
import maps.Stage_Rooftop;
import maps.Stage_Sky;
import maps.Stage_Space;
import maps.Stage_Standard;
import maps.Stage_Truck;

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
	public final static String alpha = "alpha";
	public final static String red = "red";
	public final static String gb = "gb";

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
		int minutes = (sec / 60);
		int seconds = (sec) - (minutes * 60);
		String secondsString = "" + seconds;
		if (secondsString.length() == 1) secondsString = "0".concat(secondsString);
		return minutes + ":" + secondsString;
	}

	public static String getStageName(int y){
		String prepend = "";
		if (y == Stage_Standard.getStaticNumber()) prepend = Stage_Standard.getName();
		else if (y == Stage_Blocks.getStaticNumber()) prepend = Stage_Blocks.getName();
		else if (y == Stage_Boss.getStaticNumber()) prepend = Stage_Boss.getName();
		else if (y == Stage_Mushroom.getStaticNumber()) prepend = Stage_Mushroom.getName();
		else if (y == Stage_Rooftop.getStaticNumber()) prepend = Stage_Rooftop.getName();
		else if (y == Stage_Sky.getStaticNumber()) prepend = Stage_Sky.getName();
		else if (y == Stage_Space.getStaticNumber()) prepend = Stage_Space.getName();
		else if (y == Stage_Truck.getStaticNumber()) prepend = Stage_Truck.getName();
		return prepend;
	}

	public static String getDifficultyName(int y){
		if (y == 0) return "Beginner";
		if (y == 1) return "Standard";
		if (y == 2) return "Advanced";
		if (y == 3) return "Nightmare";
		return "";
	}

	public static Color getColorByRanking(Ranking r){
		switch(r){
		case N: return Color.DARK_GRAY;
		case F: return Color.PURPLE;
		case D: return Color.BLUE;
		case C: return Color.GREEN;
		case B: return Color.YELLOW;
		case A: return Color.ORANGE;
		case S: return Color.RED;
		case X: return Color.PINK;
		default: return Color.GRAY;
		}
	}

}
