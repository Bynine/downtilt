package main;

public class CreditScreen extends Menu {
	
	@Override
	protected void draw(){
		super.draw();
		batch.begin();
		font.draw(batch, ""
				+ "\nDIRECTORY, PROGRAMMER, ARTIST: TYLER \"BYNINE\" MCMASTER"
				+ "\n\nDESIGNER: JARROD \"DOCNINJ\" ROBERTS"
				+ "\n\nSTAGE MUSIC: RUNBOW"
				+ "\n\nSFX: BFXR, NINTENDO"
				+ "", 200, 550);
		batch.end();
	}

	@Override
	protected void back(){
		DowntiltEngine.startHomeMenu();
	}
	
}
