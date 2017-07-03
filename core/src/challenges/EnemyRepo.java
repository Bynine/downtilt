package challenges;

import inputs.Brain;
import entities.*;

public class EnemyRepo {

	/**
	 * Enemies for each wave.
	 */
	public static final EnemyType zen = new EnemyType(Basic.class, Brain.Recover.class);
	
	public static final EnemyType basic = new EnemyType(Basic.class, Brain.MookBrain.class);
	public static final EnemyType bombs = new EnemyType(Basic.Bomb.class, Brain.MookBrain.class);
	public static final EnemyType beefy = new EnemyType(Basic.Beefy.class, Brain.MookBrain.class);
	public static final EnemyType fatbasic = new EnemyType(Basic.class, Brain.MookBrain.class, EnemyType.PowerUp.ALL);
	
	public static final EnemyType shoots = new EnemyType(Shoot.class, Brain.ShootBrain.class);
	
	public static final EnemyType flies = new EnemyType(Fly.class, Brain.FlyBrain.class);

}
