package inputs;

import entities.Fighter;

public class InputHandlerCPU extends InputHandler {

	float xInput = 0;
	float yInput = 0;
	Brain cpuBrain = new Brain.Braindead(this);
	final Fighter cpu;

	public <T extends Brain> InputHandlerCPU(Fighter cpu, Class<T> brain){
		super(cpu);
		this.cpu = cpu;
		try {
			this.cpuBrain = brain.getDeclaredConstructor(InputHandlerCPU.class).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Declare a brain dead CPU **/
	public InputHandlerCPU(Fighter cpu){ 
		super(cpu);
		this.cpu = cpu;
	}

	public void update() {
		cpuBrain.update(fighter.getInputPackage());
	}

	public boolean isCharging() {
		return cpuBrain.isCharging();
	}
	
	public boolean isTeching() {
		return false;
	}

	public float getXInput() {
		return xInput;
	}

	public float getYInput() {
		return -yInput;
	}

	public void handleJumpHeld() {
		cpu.handleJumpHeld(true);
	}
	
	public void handleBlockHeld() {
		cpu.handleBlockHeld(true);
	}

}
