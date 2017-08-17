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
	private static int[] options = new int[8];
	private static boolean saveFileExists = true;

	static void writeScore(int x, int y, int score){
		if (!DowntiltEngine.saveOn()) return;
		if (scores[x][y] < score) scores[x][y] = score;
	}
	
	static void writeOptions(int music, int sfx, int shake, int palette){
		if (!DowntiltEngine.saveOn()) return;
		options[0] = music;
		options[1] = sfx;
		options[2] = shake;
		options[3] = palette;
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
		if (loadedScores == null) {
			saveFileExists = false;
			return;
		}
		scores = json.fromJson(int[][].class, serializedScores);
	}
	
	private static void loadOptions(Json json){
		String serializedOptions = Gdx.app.getPreferences(saveFile).getString(optionsKey);
		int[] loadedOptions = json.fromJson(int[].class, serializedOptions);
		if (loadedOptions == null) {
			options[0] = 2; // normal music volume
			options[1] = 2; // normal sfx volume
			options[2] = 2; // normal screenshake
			options[3] = 0; // normal palette
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
	
	/* Stages & Modes */
	
	public static boolean AdvancedUnlocked(){
		return isUnlocked(getScores()[0][1], Victory.AdventureVictory.A);
	}
	
	public static boolean NightmareUnlocked(){
		return isUnlocked(getScores()[0][2], Victory.AdventureVictory.A);
	}
	
	public static boolean BlocksUnlocked(){
		return isUnlocked(getScores()[1][0], Victory.TrialVictory.C);
	}

	public static boolean MushroomUnlocked(){
		return isUnlocked(getScores()[1][1], Victory.TrialVictory.B);
	}
	
	public static boolean SpaceUnlocked(){
		return isUnlocked(getScores()[2][2], Victory.EndlessVictory.C);
	}

	public static boolean SkyUnlocked(){
		return isUnlocked(getScores()[2][3], Victory.EndlessVictory.B);
	}
	
	/* Palettes */
	
	public static boolean PaletteMushroomUnlocked(){
		return isUnlocked(getScores()[1][4], Victory.TrialVictory.A);
	}
	
	public static boolean PaletteSpaceUnlocked(){
		return isUnlocked(getScores()[2][5], Victory.EndlessVictory.A);
	}
	
	public static boolean PaletteSkyUnlocked(){
		return isUnlocked(getScores()[1][6], Victory.TrialVictory.A);
	}
	
	public static boolean PaletteNightmareUnlocked(){
		return isUnlocked(getScores()[0][3], Victory.AdventureVictory.A);
	}
	
	public static boolean PaletteSAdventureUnlocked(){
		return isUnlockedRange(0, 4, Victory.AdventureVictory.S);
	}
	
	public static boolean PaletteSTimeTrialUnlocked(){
		return isUnlockedRange(1, 7, Victory.TrialVictory.S);
	}
	
	public static boolean PaletteSEndlessUnlocked(){
		return isUnlockedRange(2, 7, Victory.EndlessVictory.S);
	}
	
	private static boolean isUnlocked(int scoreA, int scoreB){
		return DowntiltEngine.debugOn() || scoreA >= scoreB;
	}
	
	private static boolean isUnlockedRange(int x, int y, int scoreB){
		boolean unlocked = false;
		for (int i = 0; i < y; ++i){
			if (isUnlocked(getScores()[x][i], scoreB)) unlocked = true;
		}
		return unlocked;
	}
	
	public static boolean saveFileExists(){
		return saveFileExists;
	}

}
