package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import inputs.InputHandlerPlayer;
import main.DowntiltEngine;
import main.SFX;
import maps.Stage;

public class ChallengeTutorial extends ChallengeEndless {

	private int index = 0;
	private final List<ToolTip> toolTipList = new ArrayList<ToolTip>();

	public ChallengeTutorial(Stage stage, List<Wave> waves) {
		super(stage, waves);
		InputHandlerPlayer ihp = DowntiltEngine.getPrimaryInputHandler();
		ToolTip tt_INTRO = new ToolTip(
				"In this game, the only way to get KO'd\n"
						+ "is to fall off the stage! Let's learn some\n"
						+ "moves. Press " + ihp.getSelectString() + " to advance\n"
						+ "the tutorial.");
		ToolTip tt_MOVE = new ToolTip(Ping.MOVE,  
				"Now, try moving with " + ihp.getMoveString() + ".\n"
						+ "Hold down to crouch.");
		ToolTip tt_NORMAL = new ToolTip(Ping.NORMAL, 
				"Press " + ihp.getNormalString() + " to do a quick attack.\n"
						+ "You'll do a different attack depending on the\n"
						+ "direction you hold. This is true for every button!\n"
						+ "You can also do a slide attack if you're running.\n");
		ToolTip tt_CHARGE = new ToolTip(Ping.CHARGE,  
				"Press " + ihp.getChargeString() + " to do a charge attack. If you hold\n"
						+ "down " + ihp.getChargeString() + ", you'll charge the move - the longer you charge\n"
						+ "the stronger it is. Wham!");
		ToolTip tt_SPECIAL = new ToolTip(Ping.SPECIAL, 
				"Press " + ihp.getSpecialString() + " to use a special attack. These attacks\n"
						+ "are very useful, but will use up some of your\n"
						+ "Special Meter (shown below), so watch out!\n"
						+ "Be sure to use Up Special (hold up and press " + ihp.getSpecialString() + ")\n"
						+ "if you're far off the stage.");
		ToolTip tt_JUMP = new ToolTip(Ping.JUMP,  
				"Press " + ihp.getJumpString() + " to jump. While you're in the air,\n"
						+ "press " + ihp.getJumpString() + " again to double jump,\n"
						+ "or press " + ihp.getNormalString() + " or " + ihp.getChargeString() + " to use an aerial attack.\n"
						+ "If you hold toward a wall and jump, you'll do "
						+ "a wall jump!");
		ToolTip tt_GRAB = new ToolTip(Ping.GRAB,  
				"Press " + ihp.getGrabString() + " to grab. If you grab something you can\n"
						+ "throw it by flicking the control stick in any direction.\n"
						+ "You can also grab in the air! Throw enemies at\n"
						+ "each other to really lay on the hurt.");
		ToolTip tt_GUARD = new ToolTip(Ping.GUARD,  
				"Finally, press " + ihp.getGuardString() + " to guard. If an enemy\n"
						+ "hits you during your guard, you'll counterattack - nice!\n"
						+ "If you flick the control stick left or right\n"
						+ "during this guard, or press L or R in the air,\n"
						+ "you'll perform a dodge.");
		ToolTip tt_KNOCKBACK = new ToolTip( 
				"Hitting enemies increases their damage.\n"
						+ "The more damaged they are, the redder they become,\n"
						+ "and the farther they'll fly when you hit them. KAPOW!");
		ToolTip tt_COMBO = new ToolTip( 
				"To combo an enemy, keep them in the air while hitting\n"
						+ "them with different moves. When they touch the ground\n"
						+ "or get KO'd, the combo finishes. Combos \n"
						+ "replenish your Special meter, so be creative!");
		ToolTip tt_END = new ToolTip(Ping.END,
				"Now press " + ihp.getStartString() + " to pause the game,\n"
						+ "then press " + ihp.getChargeString() + " to finish the tutorial!");
		toolTipList.addAll(Arrays.asList(tt_INTRO, tt_MOVE, tt_NORMAL, tt_CHARGE, tt_SPECIAL, tt_JUMP, tt_GRAB, tt_GUARD,
				tt_KNOCKBACK, tt_COMBO, tt_END));
		lifeSetting = INFINITELIVES;
	}

	public ToolTip getToolTip(){
		return toolTipList.get(index);
	}

	public void advanceToolTip(){
		if (!getToolTip().satisfied) return;
		new SFX.LightHit().play();
		index++;
		if (index >= toolTipList.size()) index--;
	}

	public static class ToolTip{
		private final Ping p;
		private final String str;
		private boolean satisfied = false;

		ToolTip(Ping p, String str){
			this.p = p;
			this.str = str;
			if (p == Ping.END) satisfied = true;
		}

		ToolTip(String str){
			this.p = Ping.NONE;
			this.str = str;
			satisfied = true;
		}

		public void checkPing(Ping p){
			if (p == this.p && !satisfied && !DowntiltEngine.isPaused()) {
				new SFX.TutorialSuccess().play();
				satisfied = true;
			}
		}

		public String getString(){
			return str;
		}

		public boolean isSatisfied(){
			return satisfied;
		}

		public Ping getPing() {
			return p;
		}

	}

	public static enum Ping{
		NONE, MOVE, NORMAL, CHARGE, SPECIAL, JUMP, GRAB, GUARD, KNOCKBACK, COMBO, END
	}

}
