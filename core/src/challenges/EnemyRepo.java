package challenges;

import inputs.Brain;
import entities.*;

public class EnemyRepo {

	/**
	 * Enemies for each wave.
	 */
	public static final Enemy zen = new Enemy(Basic.class, Brain.Recover.class);
	public static final Enemy shoots = new Enemy(Shoot.class, Brain.ShootBrain.class);
	public static final Enemy flies = new Enemy(Fly.class, Brain.FlyBrain.class);
	public static final Enemy bad = new Enemy(Basic.Bad.class, Brain.MookBrain.class);
	public static final Enemy basic = new Enemy(Basic.class, Brain.MookBrain.class);
	public static final Enemy bombs = new Enemy(Basic.Bomb.class, Brain.MookBrain.class);
	public static final Enemy beefy = new Enemy(Basic.Beefy.class, Brain.MookBrain.class);
}
