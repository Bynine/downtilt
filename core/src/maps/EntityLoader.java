package maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

import entities.*;
import entities.Hurlable;

public class EntityLoader {

	Entity loadEntity(MapObject m){
		MapProperties mp = m.getProperties();
		float x = mp.get("x", Float.class);
		float y = mp.get("y", Float.class);

		switch(m.getName().toLowerCase()){
		case "trash": return new Hurlable.TrashCan(x, y);
		case "treasure": return new TreasureChest(x, y);
		case "spikes": return new Hazard.Spikes(x, y);
		case "bounceleft": return new Bounce.BounceLeft(x, y);
		case "bounceright": return new Bounce.BounceRight(x, y);
		case "changea": return new ChangeBlock.BlockR(x, y);
		case "changeb": return new ChangeBlock.BlockG(x, y);
		case "changec": return new ChangeBlock.BlockB(x, y);
		case "switchblock": return new SwitchButton(x, y);
		default: {
			return new Hurlable.TrashCan(x, y);
		}
		}
	}

//	private Fighter makeNewEnemy(Class <? extends Fighter> fiClass, Class <? extends Brain> brClass, float x, float y){
//		Fighter enemy;
//		try {
//			enemy = fiClass.getDeclaredConstructor(float.class, float.class, int.class).newInstance(x, y, 1);
//		} catch (InstantiationException | IllegalAccessException
//				| IllegalArgumentException | InvocationTargetException
//				| NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//			enemy = new Mook(x, y, 1);
//		}
//		enemy.setInputHandler(new InputHandlerCPU(enemy, brClass));
//		return enemy;
//	}
}
