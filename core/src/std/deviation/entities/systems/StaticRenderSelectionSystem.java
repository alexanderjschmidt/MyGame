package std.deviation.entities.systems;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.ai.AIComponent;
import std.deviation.entities.components.renderComponents.RenderComponent;
import std.deviation.entities.components.renderComponents.renderParts.RenderPacket;
import std.deviation.entities.components.spatialComponents.InteractionComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class StaticRenderSelectionSystem extends IteratingSystem {

	public StaticRenderSelectionSystem() {
		super(Family.all(RenderComponent.class).exclude(AIComponent.class)
				.get());
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
		RenderComponent render = EntityController.render.get(entity);
		InteractionComponent interaction = EntityController.interaction
				.get(entity);
		String selected = "idle";
		if (render.containsPacket(RenderPacket.Intersect)
				&& interaction != null && interaction.intersecting) {
			selected = "interset";
		}

		render.setSelected(selected, PlayMode.LOOP);
	}
}
