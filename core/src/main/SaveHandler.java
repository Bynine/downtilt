package main;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

/**
 * Controls the game's save system.
 */
public class SaveHandler {

	private static final Preferences save = Gdx.app.getPreferences("saveFile");
	private static final String scoreKey = "avocado toast";
	private static int[][] scores; // have a score table for each mode instead?
	
	static void begin(){
		scores = new int[3][8];
	}

	static void writeScore(int x, int y, int score){
		if (!DowntiltEngine.saveOn()) return;
		try{
			scores[x][y] = score;
		}
		catch(Exception e){
			System.out.println("Exception " + e + " while trying to write score to " + x + ", "+ y + " in scores");
		}
	}

	static void save(){
		if (!DowntiltEngine.saveOn()) return;
		Hashtable<String, String> hashTable = new Hashtable<String, String>();

	    Json json = new Json();

	    hashTable.put("test", json.toJson(scores) ); //here you are serializing the array
		Map<String, int[][]> map = new HashMap<String, int[][]>(){
			private static final long serialVersionUID = -888666L;
			{
				put(scoreKey, scores);
			}
		};
		//String serializedInts = Gdx.app.getPreferences("preferences").getString("test");
	    //int[] deserializedInts = json.fromJson(int[].class, serializedInts); //you need to pass the class type - be aware of it!
		save.put(map);
		save.flush();
		System.out.println("Saved file");
	}

	static void loadSave(){
		if (!DowntiltEngine.saveOn()) return;
		scores = (int[][]) save.get().get(scoreKey);
		System.out.println(save.get().values().toString());
	}

	static void wipeSave(){
		if (!DowntiltEngine.saveOn()) return;
		save.clear();
	}

	public static int[][] getScores(){
		return scores;
	}

}
