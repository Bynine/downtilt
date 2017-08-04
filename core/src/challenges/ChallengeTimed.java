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
	protected int getTimeNum(){
		return (time - DowntiltEngine.getDeltaTime())/60;
	}
	
	public int getTimeLeftInSeconds(){
		return getTimeNum();
	}
	
	@Override
	protected String getTimeString(){
		return "TIME: ";
	}
	
	@Override
	protected boolean inFailState(){
		boolean fail = super.inFailState();
		if (getTimeNum() <= 0) fail = true;
		return fail;
	}

}
