package challenges;

public abstract class Bonus {
	protected int points = 0;
	protected String name = "DUMMY";

	public String getName(){
		return name;
	}

	public int getScore(){
		return points;
	}

	/* ALL */

	public static abstract class MultBonus extends Bonus{}

	public static class ComboMultLow extends MultBonus{
		public ComboMultLow() {
			points = 2;
			name = "Rad Combo";
		}	
	}
	public static class ComboMultMid extends MultBonus{
		public ComboMultMid() {
			points = 4;
			name = "Gnarly Combo";
		}	
	}
	public static class ComboMultHigh extends MultBonus{
		public ComboMultHigh() {
			points = 8;
			name = "Tubular Combo";
		}	
	}

	public static class ComboLow extends Bonus{
		public ComboLow() {
			points = 10;
			name = "Combo Expert";
		}	
	}
	public static class ComboMid extends Bonus{
		public ComboMid() {
			points = 30;
			name = "Combo Legend";
		}	
	}
	public static class ComboHigh extends Bonus{
		public ComboHigh() {
			points = 50;
			name = "Combo God";
		}	
	}

	/* ADVENTURE */

	public static class TimeBonus extends Bonus{
		public TimeBonus(int sec){
			
		}
	}

	public static class ImmortalBonus extends Bonus{
		public ImmortalBonus(){
			points = 100;
			name = "Immortal";
		}
	}

	public static class UnstoppableBonus extends Bonus{
		public UnstoppableBonus(int difficultyMod){
			points = 15 * difficultyMod;
			name = "Unstoppable";
		}
	}
	
	/* SINGLE */

	public static class KOBonus extends MultBonus{
		public KOBonus(){
			points = 2;
			name = "KO Bonus";
		}
	}

}
