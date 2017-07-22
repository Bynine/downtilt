package challenges;

import java.util.List;

import main.DowntiltEngine;
import maps.Stage;

public class ChallengeTimed extends Challenge {
	
	private static final int MINUTE = 3600;
	private final int time;

	ChallengeTimed(Stage stage, List<Wave> waves, double time) {
		super(stage, waves);
		this.time = (int) (MINUTE * time);
	}
	
	@Override
	protected int[] getTimeMinSec(){
		int newDelta = time - DowntiltEngine.getDeltaTime();
		int minutes = (newDelta /3600);
		int seconds = (newDelta / 60) - (minutes * 60);
		return new int[]{minutes, seconds};
	}
	
	@Override
	protected String getTimeString(){
		return "TIME LEFT: ";
	}
	
	@Override
	protected boolean inFailState(){
		boolean fail = super.inFailState();
		if (getTimeMinSec()[0] <= 0 && getTimeMinSec()[1] <= 0) fail = true;
		return fail;
	}

}
