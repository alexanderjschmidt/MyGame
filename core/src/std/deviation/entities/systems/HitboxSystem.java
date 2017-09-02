package std.deviation.entities.systems;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.PositionComponent;
import std.deviation.entities.components.spatialComponents.HitboxComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class HitboxSystem extends IteratingSystem {

	public HitboxSystem() {
		super(Family.all(HitboxComponent.class, PositionComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		if (deltaTime == 0) {
			return;
		}
		super.update(deltaTime);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		HitboxComponent hitbox = EntityController.hitbox.get(entity);
		PositionComponent position = EntityController.position.get(entity);

		hitbox.rect.setPosition(position.x + hitbox.xOffset, position.y
				+ hitbox.yOffset);
	}
}
