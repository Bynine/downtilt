package moves;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import challenges.Bonus;
import challenges.Challenge;
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
	private static final Sound SOUND_FINISH2 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/finish2.wav"));
	private static final Sound SOUND_FINISH3 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/finish3.wav"));
	private static final Sound SOUND_FINISH4 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/finish4.wav"));
	private static final Sound SOUND_FINISH5 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/finish5.wav"));
	private static final Sound SOUND_FINISH6 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/finish6.wav"));
	private static final Sound SOUND_FINISH7 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/finish7.wav"));
	private static final Sound SOUND_FINISH8 = Gdx.audio.newSound(Gdx.files.internal("sfx/combo/finish8.wav"));
	public static final int knockIntoID = 999, footstoolID = 998;

	public void addMoveID(int id){
		if (!moveIDsUsed.contains(id)){
			playCorrespondingSound();
			rank++;
			DowntiltEngine.getChallenge().updateLongestCombo(rank);
			moveIDsUsed.add(id);
		}
	}

	private void playCorrespondingSound(){
		float vol = DowntiltEngine.getSFXVolume();
		switch(rank){
		case 1: SOUND_1.play(vol); break;
		case 2: SOUND_2.play(vol); break;
		case 3: SOUND_3.play(vol); break;
		case 4: SOUND_4.play(vol); break;
		case 5: SOUND_5.play(vol); break;
		case 6: SOUND_6.play(vol); break;
		case 7: SOUND_7.play(vol); break;
		}
		if (rank >= 8) SOUND_7.play(vol);
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
		case 7: finishStrength = 8.0f; break;
		case 8: finishStrength = 10.0f; break;
		default: finishStrength = Challenge.SPECIALMETERMAX; break;
		}

		finishHelper(rank, finishStrength);
		end();
		return finishStrength;
	}

	public int getRank(){
		return rank;
	}
	
	public boolean isACombo(){
		return rank > 1;
	}

	private void finishHelper(int rank, float addedSpecial){
		if (!isACombo()) return;
		float vol = DowntiltEngine.getSFXVolume();
		switch(rank){
		// implictly by checking for isACombo first, rank should not be 1 or less here
		case 2: SOUND_FINISH2.play(vol); break;
		case 3: SOUND_FINISH3.play(vol); break;
		case 4: SOUND_FINISH4.play(vol); break;
		case 5: SOUND_FINISH5.play(vol); break;
		case 6: SOUND_FINISH6.play(vol); break;
		case 7: SOUND_FINISH7.play(vol); break;
		default: SOUND_FINISH8.play(vol); break;
		}
		
		if (rank >= 7) DowntiltEngine.getMode().addBonus(new Bonus.ComboMultHigh());
		else if (rank >= 5) DowntiltEngine.getMode().addBonus(new Bonus.ComboMultMid());
		else if (rank >= 3) DowntiltEngine.getMode().addBonus(new Bonus.ComboMultLow());
		
		if (rank >= 11) DowntiltEngine.getMode().addBonus(new Bonus.ComboHigh());
		else if (rank >= 9) DowntiltEngine.getMode().addBonus(new Bonus.ComboMid());
		else if (rank >= 7) DowntiltEngine.getMode().addBonus(new Bonus.ComboLow());
		
		DowntiltEngine.getChallenge().resolveCombo(addedSpecial);
		DowntiltEngine.getChallenge().changeSpecial(addedSpecial);
//		for (Fighter player: DowntiltEngine.getPlayers()) {
//			if (rank == 5) player.addAll(Hittable.BOOSTTIMERRUSH/4);
//			else if (rank == 6) player.addAll(Hittable.BOOSTTIMERRUSH/2);
//			else if (rank == 7) player.addAll(Hittable.BOOSTTIMERRUSH);
//			else if (rank >= 8) player.addAll(Hittable.BOOSTTIMERRUSH * 2);
//		}
	}

}
