package std.deviation.entities.components.spatialComponents.interaction;

import std.deviation.screen.Play;

import com.badlogic.ashley.core.Entity;

public class NullInteract extends Interact {

	public NullInteract() {
		super(Interact.NULL);
	}

	@Override
	public void interact(Play screen, Entity interacted, Entity interactor) {
	}

}
