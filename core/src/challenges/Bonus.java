package challenges;

public abstract class Bonus {
	protected int points = 0;
	protected String name = "DUMMY";
	protected boolean adventure = 	false;
	protected boolean timetrial = 	false;
	protected boolean endless = 	false;

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
			adventure = true;
			timetrial = true;
		}	
	}
	public static class ComboMultMid extends MultBonus{
		public ComboMultMid() {
			points = 2;
			name = "Gnarly Combo";
			adventure = true;
			timetrial = true;
		}	
	}
	public static class ComboMultHigh extends MultBonus{
		public ComboMultHigh() {
			points = 4;
			name = "Tubular Combo";
			adventure = true;
			timetrial = true;
		}	
	}

	public static class ComboLow extends Bonus{
		public ComboLow() {
			points = 10;
			name = "Combo Expert";
			adventure = true;
			timetrial = true;
		}	
	}
	public static class ComboMid extends Bonus{
		public ComboMid() {
			points = 25;
			name = "Combo Legend";
			adventure = true;
			timetrial = true;
		}	
	}
	public static class ComboHigh extends Bonus{
		public ComboHigh() {
			points = 50;
			name = "Combo God";
			adventure = true;
			timetrial = true;
		}	
	}
	
	public static class NoveltyBonus extends MultBonus{
		public NoveltyBonus(){
			points = 1;
			if (Math.random() < 0.5) name = "Plantain Enthusiast";
			else name = "Ichythologist";
			adventure = true;
			timetrial = true;
			endless = true;
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
			name = "Special-Free: 1.5x";
			
			adventure = true;
			timetrial = true;
			endless = true;
		}
	}

	/* ADVENTURE */

	public static class TimeBonus extends Bonus{
		public TimeBonus(int sec){
			points = 0;
			final int minute = 60;
			final int possibleBonus = 18;
			for (int i = possibleBonus; i > 0; --i){
				if (sec < minute * i) points = (possibleBonus - i) * 5;
			}
			name = "Time Bonus";
			
			adventure = true;
		}
	}

	public static class NeverLostAnyTries extends Bonus{
		public NeverLostAnyTries(){
			points = 50;
			name = "Immortal";

			adventure = true;
		}
	}

	public static class NeverLostAllTries extends Bonus{
		public NeverLostAllTries(){
			points = 15;
			name = "Unstoppable";

			adventure = true;
		}
	}

	/* SINGLE */

	public static class KOBonus extends MultBonus{
		public KOBonus(){
			points = 1;
			name = "KO Bonus";
			
			timetrial = true;
			endless = true;
		}
	}

}
