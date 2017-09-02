package std.deviation.entities.components.spatialComponents.interaction;

import std.deviation.screen.Play;

import com.badlogic.ashley.core.Entity;

public class ChestInteract extends Interact {

	public ChestInteract() {
		super(Interact.CHEST);
	}

	@Override
	public void interact(Play screen, Entity interacted, Entity interactor) {
		screen.getHud().openInv(interacted, interactor, true);
	}

}
