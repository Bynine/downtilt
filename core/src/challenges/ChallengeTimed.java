package challenges;

import java.util.List;

import main.DowntiltEngine;
import maps.Stage;

public class ChallengeTimed extends Challenge {
	
	protected static final int MINUTE = 60;
	protected int time;

	ChallengeTimed(Stage stage, List<Wave> waves, double sec) {
		super(stage, waves);
		this.time = (int) (MINUTE * sec);
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
		return "TIME: ";
	}
	
	@Override
	protected boolean inFailState(){
		boolean fail = super.inFailState();
		if (getTimeMinSec()[0] <= 0 && getTimeMinSec()[1] <= 0) fail = true;
		return fail;
	}

}
