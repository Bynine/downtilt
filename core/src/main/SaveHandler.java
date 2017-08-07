package main;

import java.util.Hashtable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

/**
 * Controls the game's save system.
 */
public class SaveHandler {

	private static final String scoreKey = "avocado toast", saveFile = "saveFile";
	private static final Preferences save = Gdx.app.getPreferences(saveFile);
	private static final int arrayX = 3, arrayY = 8;
	private static int[][] scores = new int[arrayX][arrayY]; // have a score table for each mode instead?

	static void writeScore(int x, int y, int score){
		if (!DowntiltEngine.saveOn()) return;
		if (scores[x][y] < score) scores[x][y] = score;
	}

	static void save(){
		if (!DowntiltEngine.saveOn()) return;
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		Json json = new Json();
		hashTable.put(scoreKey, json.toJson(scores));
		save.put(hashTable);
		save.flush();
		System.out.println("Saved file");
	}

	static void loadSave(){
		if (!DowntiltEngine.saveOn()) return;
		Json json = new Json();
		String serializedInts = Gdx.app.getPreferences(saveFile).getString(scoreKey);
		int[][] loadedScores = json.fromJson(int[][].class, serializedInts);
		if (loadedScores == null) {
			System.out.println("Can't find scores");
			return;
		}
		scores = json.fromJson(int[][].class, serializedInts);
		//System.out.println(scores[0][0]);
		for (int i = 0; i < arrayX; ++i){
			for (int j = 0; j < arrayY; ++j){
				//System.out.println(scores[i][j]);
			}
		}
	}

	static void wipeSave(){
		if (!DowntiltEngine.saveOn()) return;
		save.clear();
	}

	public static int[][] getScores(){
		return scores;
	}

}
