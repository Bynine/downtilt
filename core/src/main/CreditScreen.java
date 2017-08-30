package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CreditScreen extends Menu {
	
	CreditScreen(){
		canAdvance = false;
		tile = new TextureRegion(new Texture(Gdx.files.internal("sprites/menu/tile_klown.png")));
	}
	
	@Override
	protected void draw(){
		super.draw();
		batch.begin();
		final String spacing = "\n\n\n\n";
		font.draw(batch, ""
				+ spacing + "DIRECTOR, PROGRAMMER, ARTIST: TYLER \"BYNINE\" MCMASTER"
				+ spacing + "DESIGNER: JARROD \"DOCNINJ\" ROBERTS"
				+ spacing + "STAGE MUSIC: RUNBOW, CRYPT OF THE NECRODANCER"
				+ spacing + "SFX: BFXR, NINTENDO"
				+ "", 200, 550);
		batch.end();
	}

	@Override
	protected void back(){
		new SFX.Back().play();
		DowntiltEngine.startHomeMenu();
	}
	
}
