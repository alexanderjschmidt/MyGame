package std.deviation.entities.components.spatialComponents.interaction;

import std.deviation.screen.Play;

import com.badlogic.ashley.core.Entity;

public abstract class Interact {

	public byte type;

	public Interact(byte type) {
		this.type = type;
	}

	public abstract void interact(Play screen, Entity interacted,
			Entity interactor);

	public static final byte NULL = 0;
	public static final byte CHEST = 1;
	public static final byte SIT = 2;

	public static Interact getInteract(byte interact) {
		switch (interact) {
		case NULL:
			return new NullInteract();
		case CHEST:
			return new ChestInteract();
		case SIT:
			return new SitInteract();
		}
		return new NullInteract();
	}
}
