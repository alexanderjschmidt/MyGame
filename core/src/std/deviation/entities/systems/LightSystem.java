package std.deviation.entities.systems;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.LightComponent;
import std.deviation.entities.components.PositionComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class LightSystem extends IteratingSystem {

	public LightSystem() {
		super(Family.all(PositionComponent.class, LightComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		LightComponent light = EntityController.light.get(entity);
		PositionComponent position = EntityController.position.get(entity);

		light.light.setPosition(position.x, position.y);
	}

}
