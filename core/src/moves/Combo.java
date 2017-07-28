package moves;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import entities.Fighter;
import entities.Hittable;
import main.DowntiltEngine;

public class Combo {

	private int rank = 0;
	private final List<Integer> moveIDsUsed = new ArrayList<Integer>();

	private static final Sound SOUND_1 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/1.wav"));
	private static final Sound SOUND_2 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/2.wav"));
	private static final Sound SOUND_3 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/3.wav"));
	private static final Sound SOUND_4 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/4.wav"));
	private static final Sound SOUND_5 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/5.wav"));
	private static final Sound SOUND_6 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/6.wav"));
	private static final Sound SOUND_7 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/7.wav"));
	//private static final Sound SOUND_FINISH = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/finish.wav"));
	public static final int knockIntoID = 999, footstoolID = 998;

	public void addMoveID(int id){
		if (id == knockIntoID || !moveIDsUsed.contains(id)){
			playCorrespondingSound();
			rank++;
			DowntiltEngine.getChallenge().updateLongestCombo(rank);
			moveIDsUsed.add(id);
		}
	}

	private void playCorrespondingSound(){
		switch(rank){
		case 1: SOUND_1.play(); break;
		case 2: SOUND_2.play(); break;
		case 3: SOUND_3.play(); break;
		case 4: SOUND_4.play(); break;
		case 5: SOUND_5.play(); break;
		case 6: SOUND_6.play(); break;
		case 7: SOUND_7.play(); break;
		}
	}

	public void end(){
		rank = 0;
		moveIDsUsed.clear();
	}

	public float finish(){
		float finishStrength = 0;
		switch(rank){
		case 0: break;
		case 1: break;
		case 2: finishStrength = 0.5f; break;
		case 3: finishStrength = 1.0f; break;
		case 4: finishStrength = 2.0f; break;
		case 5: finishStrength = 3.5f; break;
		case 6: finishStrength = 5.5f; break;
		default: finishStrength = 8.0f; break;
		}

		finishHelper(finishStrength);
		end();
		return finishStrength;
	}

	public int getRank(){
		return rank;
	}
	
	public boolean isACombo(){
		return rank > 1;
	}

	private void finishHelper(float addedSpecial){
		if (!isACombo()) return;
		//SOUND_FINISH.play();
		for (Fighter player: DowntiltEngine.getPlayers()) {
			player.changeSpecial(addedSpecial);
			if (rank == 5) player.addAll(Hittable.BOOSTTIMERRUSH/4);
			else if (rank == 6) player.addAll(Hittable.BOOSTTIMERRUSH/2);
			else if (rank >= 7) player.addAll(Hittable.BOOSTTIMERRUSH);
		}
	}

}
