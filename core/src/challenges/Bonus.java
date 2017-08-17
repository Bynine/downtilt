package challenges;

public abstract class Bonus {
	protected int points = 0;
	protected String name = "DUMMY";
	protected boolean adventureOnly = false;
	protected boolean singleOnly = false;

	public String getName(){
		return name;
	}

	public int getScore(){
		return points;
	}
	
	public int getMult(){
		return 1;
	}

	/* ALL */

	public static abstract class MultBonus extends Bonus{
		int mult = 1;
		
		public void increase() {
			mult++;
		}
		
		public int getScore(){
			return points * mult;
		}
		
		public int getMult(){
			return mult;
		}
	}

	public static class ComboMultLow extends MultBonus{
		public ComboMultLow() {
			points = 1;
			name = "Rad Combo";
		}	
	}
	public static class ComboMultMid extends MultBonus{
		public ComboMultMid() {
			points = 2;
			name = "Gnarly Combo";
		}	
	}
	public static class ComboMultHigh extends MultBonus{
		public ComboMultHigh() {
			points = 4;
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
			points = 25;
			name = "Combo Legend";
		}	
	}
	public static class ComboHigh extends Bonus{
		public ComboHigh() {
			points = 50;
			name = "Combo God";
		}	
	}
	
	public static abstract class FactorBonus extends Bonus{
		protected float factor = 1;
		public float getFactor(){
			return factor;
		}
	}
	
	public static class NoSpecialBonus extends FactorBonus{
		public NoSpecialBonus() {
			factor = 1.5f;
			name = "Special-Free Multiplier";
		}
	}

	/* ADVENTURE */

	public static class TimeBonus extends Bonus{
		public TimeBonus(int sec){
			adventureOnly = true;
			points = 0;
			name = "Time Bonus";
		}
	}

	public static class ImmortalBonus extends Bonus{
		public ImmortalBonus(){
			adventureOnly = true;
			points = 50;
			name = "Immortal";
		}
	}

	public static class UnstoppableBonus extends Bonus{
		public UnstoppableBonus(){
			adventureOnly = true;
			points = 15;
			name = "Unstoppable";
		}
	}

	/* SINGLE */

	public static class KOBonus extends MultBonus{
		public KOBonus(){
			singleOnly = true;
			points = 1;
			name = "KO Bonus";
		}
	}

}
