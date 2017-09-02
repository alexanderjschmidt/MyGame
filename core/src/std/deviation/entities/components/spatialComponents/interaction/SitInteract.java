package std.deviation.entities.components.spatialComponents.interaction;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.ChairComponent;
import std.deviation.entities.components.PositionComponent;
import std.deviation.entities.components.SitComponent;
import std.deviation.entities.components.ai.AIComponent;
import std.deviation.screen.Play;

import com.badlogic.ashley.core.Entity;

public class SitInteract extends Interact {

	public SitInteract() {
		super(Interact.SIT);
	}

	@Override
	public void interact(Play screen, Entity interacted, Entity interactor) {
		ChairComponent c = EntityController.chair.get(interacted);
		PositionComponent posInteracted = EntityController.position
				.get(interacted);
		SitComponent s = EntityController.sit.get(interactor);
		PositionComponent posInteractor = EntityController.position
				.get(interactor);
		AIComponent ai = EntityController.ai.get(interactor);
		if (c == null || s == null) {
			return;
		}
		if (!c.inUse) {
			posInteractor.x = posInteracted.x + c.sitX;
			posInteractor.y = posInteracted.y + c.sitY;
			s.sitting = true;
			c.inUse = true;
			ai.movingdir = c.dir;
			s.type = c.getType();
		} else {
			posInteractor.x = posInteracted.x + c.standX;
			posInteractor.y = posInteracted.y + c.standY;
			s.sitting = false;
			c.inUse = false;
			s.type = "";
		}
	}
}
