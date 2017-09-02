package std.deviation.entities.systems;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.VelocityComponent;
import std.deviation.entities.components.ai.AIComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class VelocitySystem extends IteratingSystem {

	public VelocitySystem() {
		super(Family.all(VelocityComponent.class).get());
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
		VelocityComponent velocity = EntityController.velocity.get(entity);

		Vector2 direction = new Vector2(0, 0);
		AIComponent ai = EntityController.ai.get(entity);
		if (ai != null) {
			direction = ai.movingdir.getDirVector();
			float moveChange = ai.moving ? VelocityComponent.ACCELERATION
					* deltaTime : 0;
			direction.x *= moveChange;
			direction.y *= moveChange;
		}

		float dragChange = (VelocityComponent.FRICTION + (Math.abs(velocity.vx)
				* VelocityComponent.FRIC_DIFF / velocity.getMaxVel()))
				* deltaTime;
		if (velocity.vx > 0)
			dragChange = Math.max(-velocity.vx, -dragChange);
		else if (velocity.vx < 0)
			dragChange = Math.min(-velocity.vx, dragChange);
		else
			dragChange = 0;
		velocity.vx += (direction.x) + dragChange;

		dragChange = (VelocityComponent.FRICTION + (Math.abs(velocity.vy)
				* VelocityComponent.FRIC_DIFF / velocity.getMaxVel()))
				* deltaTime;
		if (velocity.vy > 0)
			dragChange = Math.max(-velocity.vy, -dragChange);
		else if (velocity.vy < 0)
			dragChange = Math.min(-velocity.vy, dragChange);
		else
			dragChange = 0;
		velocity.vy += (direction.y) + dragChange;

	}
}
