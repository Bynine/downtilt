package challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import inputs.InputHandlerKeyboard;
import main.DowntiltEngine;
import main.SFX;
import maps.Stage;

public class ChallengeTutorial extends ChallengeEndless {

	private final ToolTip tt_INTRO = new ToolTip(
			"In this game, the only way to get KO'd\n"
					+ "is to fall off the stage! Let's learn some\n"
					+ "moves. Press SELECT to advance\n"
					+ "the tutorial.");
	private final ToolTip ttk_INTRO = new ToolTip(
			"In this game, the only way to get KO'd\n"
					+ "is to fall off the stage! Let's learn some\n"
					+ "moves. Press [ to advance\n"
					+ "the tutorial."); 

	private final ToolTip tt_MOVE = new ToolTip(Ping.MOVE,  
			"Now, try moving with the Control Stick.\n"
					+ " Push slowly to walk or quickly to run.\n"
					+ " Hold down to crouch.");
	private final ToolTip ttk_MOVE = new ToolTip(Ping.MOVE,  
			"Now, try moving with WASD.\n"
					+ " Hold down to crouch.");

	private final ToolTip tt_NORMAL = new ToolTip(Ping.NORMAL, 
			"Press the A button to do a quick attack. \n"
					+ "You'll do a different attack depending on the \n "
					+ "direction you hold. This is true for every button! \n "
					+ "You can also do a slide attack if you're running. \n");
	private final ToolTip ttk_NORMAL = new ToolTip(Ping.NORMAL, 
			"Press J to do a quick attack. \n"
					+ "You'll do a different attack depending on the \n "
					+ "direction you hold. This is true for every button! \n "
					+ "You can also do a slide attack if you're running. \n");

	private final ToolTip tt_CHARGE = new ToolTip(Ping.CHARGE,  
			"Press the Y button to do a charge attack. If you hold\n"
					+ "down Y, you'll charge the move - the longer you charge\n "
					+ "the stronger it is. Wham!");
	private final ToolTip ttk_CHARGE = new ToolTip(Ping.CHARGE,  
			"Press L to do a charge attack. If you hold\n"
					+ "down L, you'll charge the move - the longer you charge\n "
					+ "the stronger it is. Wham!");

	private final ToolTip tt_SPECIAL = new ToolTip(Ping.SPECIAL, 
			"Press B to use a special attack. These attacks \n"
					+ "are very useful, but will use up some of your\n"
					+ "Special Meter, so watch out! \n"
					+ "Be sure to use Up Special (hold Up and press B) \n"
					+ "if you're far off the stage.");
	private final ToolTip ttk_SPECIAL = new ToolTip(Ping.SPECIAL, 
			"Press K to use a special attack. These attacks \n"
					+ "are very useful, but will use up some of your\n"
					+ "Special Meter, so watch out! \n"
					+ "Be sure to use Up Special (hold W and press K) \n"
					+ "if you're far off the stage.");

	private final ToolTip tt_JUMP = new ToolTip(Ping.JUMP,  
			"Press X to jump. While you're in the air, \n"
					+ "press X again to double jump, \n"
					+ "or press A or Y to use an aerial attack. \n"
					+ "If you hold toward a wall and jump, you'll do "
					+ "a wall jump!");
	private final ToolTip ttk_JUMP = new ToolTip(Ping.JUMP,  
			"Press SPACE to jump. While you're in the air, \n"
					+ "press SPACE again to double jump, \n"
					+ "or press J or L to use an aerial attack. \n"
					+ "If you hold toward a wall and jump, you'll do "
					+ "a wall jump!");

	private final ToolTip tt_GRAB = new ToolTip(Ping.GRAB,  
			"Press RB to grab. If you grab something you can \n"
					+ "throw it by flicking the control stick in any direction.\n"
					+ "You can also grab in the air! Throw enemies at \n"
					+ "each other to really lay on the hurt.");
	private final ToolTip ttk_GRAB = new ToolTip(Ping.GRAB,  
			"Press O to grab. If you grab something you can \n"
					+ "throw it by flicking the control stick in any direction.\n"
					+ "You can also grab in the air! Throw enemies at \n"
					+ "each other to really lay on the hurt.");

	private final ToolTip tt_GUARD = new ToolTip(Ping.GUARD,  
			"Finally, press L or R to guard. If an enemy \n"
					+ "hits you during your guard, you'll counterattack - nice!\n"
					+ "If you flick the control stick left or right\n"
					+ "during this guard, or press L or R in the air,\n"
					+ "you'll perform a dodge.");
	private final ToolTip ttk_GUARD = new ToolTip(Ping.GUARD,  
			"Finally, press I to guard. If an enemy \n"
					+ "hits you during your guard, you'll counterattack - nice!\n"
					+ "If you flick the control stick left or right\n"
					+ "during this guard, or press I in the air,\n"
					+ "you'll perform a dodge.");

	private final ToolTip tt_KNOCKBACK = new ToolTip( 
			"Hitting enemies increases their damage\n"
					+ "- the more damaged they are, the redder they become,\n"
					+ "and the farther they'll fly when you hit them. KAPOW!");

	private final ToolTip tt_COMBO = new ToolTip( 
			"To combo an enemy, keep them in the air while hitting\n"
					+ "them with different moves. When they touch the ground\n"
					+ "or get KO'd, the combo finishes. Combos \n"
					+ "replenish your Special meter, so be creative!");

	private final ToolTip tt_END = new ToolTip(
			"Now press Start to pause the game,\n"
					+ "then press Y to finish the tutorial!");
	private final ToolTip ttk_END = new ToolTip(
			"Now press P to pause the game,\n"
					+ "then press L to finish the tutorial!");

	private int index = 0;
	private final List<ToolTip> toolTipList = new ArrayList<ToolTip>(Arrays.asList(
			tt_INTRO, tt_MOVE, tt_NORMAL, tt_CHARGE, tt_SPECIAL, tt_JUMP, tt_GRAB, tt_GUARD, tt_KNOCKBACK, tt_COMBO, tt_END
			));

	public ChallengeTutorial(Stage stage, List<Wave> waves) {
		super(stage, waves);
		if (DowntiltEngine.getPrimaryInputHandler() instanceof InputHandlerKeyboard){
			toolTipList.clear();
			toolTipList.addAll(Arrays.asList(
					ttk_INTRO, ttk_MOVE, ttk_NORMAL, ttk_CHARGE, ttk_SPECIAL, ttk_JUMP, ttk_GRAB, ttk_GUARD, tt_KNOCKBACK, tt_COMBO, ttk_END));
		}
		lifeSetting = BASICALLYINFINITELIVES;
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
		}

		ToolTip(String str){
			this.p = Ping.NONE;
			this.str = str;
			satisfied = true;
		}

		public void checkPing(Ping p){
			if (p == this.p && !satisfied && !DowntiltEngine.isPaused()) {
				new SFX.Victory().play();
				satisfied = true;
			}
		}

		public String getString(){
			return str;
		}

		public boolean isSatisfied(){
			return satisfied;
		}

	}

	public static enum Ping{
		NONE, MOVE, NORMAL, CHARGE, SPECIAL, JUMP, GRAB, GUARD, KNOCKBACK, COMBO, END
	}

}
