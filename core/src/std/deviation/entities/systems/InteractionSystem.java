package std.deviation.entities.systems;

import java.util.ArrayList;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.HealthComponent;
import std.deviation.entities.components.PositionComponent;
import std.deviation.entities.components.spatialComponents.InteractionComponent;
import std.deviation.entities.components.spatialComponents.SpatialHash;
import std.deviation.entities.components.spatialComponents.interaction.ChestInteract;
import std.deviation.screen.Play;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class InteractionSystem extends IteratingSystem {

	private SpatialHash spatial;
	private Play screen;

	public InteractionSystem(SpatialHash spatial, Play screen) {
		super(Family.all(InteractionComponent.class, PositionComponent.class)
				.get());
		this.spatial = spatial;
		this.screen = screen;
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
		InteractionComponent interaction = EntityController.interaction
				.get(entity);
		PositionComponent position = EntityController.position.get(entity);
		HealthComponent health = EntityController.health.get(entity);

		if (health != null && health.health <= 0
				&& !interaction.interact.getClass().equals(ChestInteract.class)) {
			interaction.interact = new ChestInteract();
		}

		interaction.rect.setPosition(position.x, position.y);
		interaction.intersecting = false;
		ArrayList<Entity> entities = spatial.getNearByEntities(position.x
				+ interaction.xOffset, position.y + interaction.yOffset,
				interaction.rect.width, interaction.rect.height,
				EntityController.interaction);
		InteractionComponent i = null;
		for (Entity e : entities) {
			i = EntityController.interaction.get(e);
			if (i != interaction && i.rect.overlaps(interaction.rect)) {
				interaction.intersecting = true;
				if (interaction.shouldInteract) {
					i.interact.interact(screen, e, entity);
				}
				return;
			}
		}
	}
}
