package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public abstract class SFX {

	String url = null;

	public void play() {
		if (null != url) Gdx.audio.newSound(Gdx.files.internal(url)).play(DowntiltEngine.getSFXVolume());
	}

	public void play(float vol) {
		if (null != url) Gdx.audio.newSound(Gdx.files.internal(url)).play(vol * DowntiltEngine.getSFXVolume());
	}

	public void playDirectional(float pan){
		if (null != url) {
			Sound sfx = Gdx.audio.newSound(Gdx.files.internal(url));
			sfx.setPan(sfx.play(), pan, DowntiltEngine.getSFXVolume());
		}
	}

	protected void setSFX(String url){
		this.url = "sfx" + url;
	}
	
	public static SFX proportionalHit(float dam){ 
		if (dam < 10) return new LightHit();
		else if (dam < 15) return new MidHit(); 
		else if (dam < 20) return new MeatyHit();
		else return new HeavyHit();
	}

	public static class None extends SFX{ }
	public static class EmptyHit extends SFX{ public EmptyHit(){ setSFX("/melee/emptyhit.wav"); } }
	public static class LightHit extends SFX{ public LightHit(){ setSFX("/melee/softhit.wav"); } }
	public static class MidHit extends SFX{ public MidHit(){ setSFX("/melee/smack.wav"); } }
	public static class MeatyHit extends SFX{ public MeatyHit(){ setSFX("/melee/blow.wav"); } }
	public static class HeavyHit extends SFX{ public HeavyHit(){ setSFX("/melee/heavy2.wav"); } }
	public static class SharpHit extends SFX{ public SharpHit(){ setSFX("/melee/shika.wav"); } }
	public static class Slow extends SFX{ public Slow(){ setSFX("/melee/vwoop.wav"); } }
	public static class LaserFire extends SFX{ public LaserFire(){ setSFX("/melee/pew.wav"); } }
	public static class ChargeLaserFire extends SFX{ public ChargeLaserFire(){ setSFX("/melee/vwoop.wav"); } }
	public static class Explode extends SFX{ public Explode(){ setSFX("/melee/fire.wav"); } }
	public static class Die extends SFX{ public Die(){ setSFX("/melee/explode.wav"); } }
	public static class HomeRun extends SFX{ public HomeRun(){ setSFX("/melee/KRRIIIIING.wav"); } }
	public static class FootStool extends SFX{ public FootStool(){ setSFX("/footstool.wav"); } }
	public static class Ground extends SFX{ public Ground(){ setSFX("/land.mp3"); } }
	public static class Tech extends SFX{ public Tech(){ setSFX("/tech.mp3"); } }
	public static class Collect extends SFX{ public Collect(){ setSFX("/powerup.wav"); } }
	public static class Victory extends SFX{ public Victory(){ setSFX("/tada.mp3"); } }
	public static class Break extends SFX{ public Break(){ setSFX("/melee/bury.wav"); } }
	public static class Sparks extends SFX{ public Sparks(){ setSFX("/lightningstart.mp3"); } }
	public static class Lightning extends SFX{ public Lightning(){ setSFX("/lightningstrike.mp3"); } }
	public static class Error extends SFX{ public Error(){ setSFX("/error.wav"); } }
	public static class Gust extends SFX{ public Gust(){ setSFX("/gust.wav"); } }
	public static class Pause extends SFX{ public Pause(){ setSFX("/pause.mp3"); } }
	public static class Unpause extends SFX{ public Unpause(){ setSFX("/unpause.mp3"); } }
	public static class Screech extends SFX{ public Screech(){ setSFX("/screech.wav"); } }
	
}
