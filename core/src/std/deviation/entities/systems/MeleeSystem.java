package std.deviation.entities.systems;

import java.util.ArrayList;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.HealthComponent;
import std.deviation.entities.components.MeleeComponent;
import std.deviation.entities.components.PositionComponent;
import std.deviation.entities.components.VelocityComponent;
import std.deviation.entities.components.ai.AIComponent;
import std.deviation.entities.components.ai.Direction;
import std.deviation.entities.components.spatialComponents.HitboxComponent;
import std.deviation.entities.components.spatialComponents.SpatialHash;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class MeleeSystem extends IteratingSystem {

	private SpatialHash spatial;

	public MeleeSystem(SpatialHash spatial) {
		super(Family.all(MeleeComponent.class, PositionComponent.class,
				AIComponent.class).get());
		this.spatial = spatial;
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
		AIComponent ai = EntityController.ai.get(entity);
		MeleeComponent melee = EntityController.melee.get(entity);
		PositionComponent position = EntityController.position.get(entity);

		if (melee.attacking && melee.update(deltaTime)) {
			Vector2 dir = Direction.getVector(ai.movingdir, 48);
			melee.hitBox.setPosition(position.x + dir.x
					- (MeleeComponent.SIZE / 2), position.y + dir.y
					- (MeleeComponent.SIZE / 2));
			if (melee.hits > 0) {
				ArrayList<Entity> entities = spatial.getNearByEntities(
						melee.hitBox.x, melee.hitBox.y, melee.hitBox.width,
						melee.hitBox.height, EntityController.hitbox);
				for (Entity e : entities) {
					HitboxComponent hitbox = EntityController.hitbox.get(e);
					if (melee.hits > 0 && e != entity
							&& melee.hitBox.overlaps(hitbox.rect)
							&& !melee.alreadyHit(e)) {
						melee.hits--;
						melee.alreadyHit.add(e);
						VelocityComponent velocity = EntityController.velocity
								.get(e);
						if (velocity != null) {
							velocity.push(ai.movingdir, 5);
						}
						HealthComponent health = EntityController.health.get(e);
						if (health != null) {
							if (health.health > 0) {
								health.health -= 20;
							} else {
								health.ragdoll();
								AIComponent a = EntityController.ai.get(e);
								if (a != null) {
									a.movingdir = Direction.values()[(int) (Math
											.random() * (Direction.values().length - 1)) + 1];
								}
							}
						}
					}
				}
			}
		}
	}
}
