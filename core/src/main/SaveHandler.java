package main;

import java.util.Hashtable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import challenges.Victory;

/**
 * Controls the game's save system.
 */
public class SaveHandler {

	private static final String scoreKey = "skoss", optionsKey = "yuptens", saveFile = "keosv";
	private static final Preferences save = Gdx.app.getPreferences(saveFile);
	public static final int arrayX = 3, arrayY = 7;
	private static int[][] scores = new int[arrayX][arrayY];
	private static int[] options = new int[3];

	static void writeScore(int x, int y, int score){
		if (!DowntiltEngine.saveOn()) return;
		if (scores[x][y] < score) scores[x][y] = score;
	}
	
	static void writeOptions(int music, int sfx, int shake){
		if (!DowntiltEngine.saveOn()) return;
		options[0] = music;
		options[1] = sfx;
		options[2] = shake;
	}

	static void save(){
		if (!DowntiltEngine.saveOn()) return;
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		Json json = new Json();
		hashTable.put(scoreKey, json.toJson(scores));
		hashTable.put(optionsKey, json.toJson(options));
		save.put(hashTable);
		save.flush();
	}

	static void loadSave(){
		if (!DowntiltEngine.saveOn()) return;
		Json json = new Json();
		loadScores(json);
		loadOptions(json);
	}
	
	private static void loadScores(Json json){
		String serializedScores = Gdx.app.getPreferences(saveFile).getString(scoreKey);
		int[][] loadedScores = json.fromJson(int[][].class, serializedScores);
		if (loadedScores == null) return;
		scores = json.fromJson(int[][].class, serializedScores);
	}
	
	private static void loadOptions(Json json){
		String serializedOptions = Gdx.app.getPreferences(saveFile).getString(optionsKey);
		int[] loadedOptions = json.fromJson(int[].class, serializedOptions);
		if (loadedOptions == null) {
			options[0] = 2; // normal music volume
			options[1] = 2; // normal sfx volume
			options[2] = 2; // normal screenshake
			return;
		}
		options = json.fromJson(int[].class, serializedOptions);
	}

	static void wipeSave(){
		if (!DowntiltEngine.saveOn()) return;
		save.clear();
	}

	public static int[][] getScores(){
		return scores;
	}
	
	public static int[] getOptions(){
		return options;
	}
	
	public static boolean AdvancedUnlocked(){
		return !DowntiltEngine.debugOn() && getScores()[0][1] >= Victory.AdventureVictory.A;
	}
	
	public static boolean NightmareUnlocked(){
		return !DowntiltEngine.debugOn() && getScores()[0][2] >= Victory.AdventureVictory.A;
	}

}
