package std.deviation.entities.systems;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.HealthComponent;
import std.deviation.entities.components.MeleeComponent;
import std.deviation.entities.components.SitComponent;
import std.deviation.entities.components.ai.AIComponent;
import std.deviation.entities.components.ai.Direction;
import std.deviation.entities.components.ai.packets.AIReturnData;
import std.deviation.entities.components.renderComponents.RenderComponent;
import std.deviation.entities.components.renderComponents.RenderComponent.RenderActive;
import std.deviation.entities.components.renderComponents.renderParts.RenderPacket;
import std.deviation.entities.components.spatialComponents.InteractionComponent;
import std.deviation.managers.KeyManager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;

public class AISystem extends IteratingSystem {

	public AISystem() {
		super(Family.all(AIComponent.class).get());
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
		InteractionComponent interact = EntityController.interaction
				.get(entity);
		RenderComponent render = EntityController.render.get(entity);
		MeleeComponent melee = EntityController.melee.get(entity);
		HealthComponent health = EntityController.health.get(entity);
		SitComponent sit = EntityController.sit.get(entity);

		AIReturnData data = ai.packet.update(ai, deltaTime);
		if (interact != null) {
			interact.shouldInteract = data.interact;
		}
		boolean shouldAttack = data.attack;

		Direction direction = Direction.values()[data.dir];
		ai.moving = direction != Direction.NODIRECTION;

		boolean allowDirChange = true;

		if (render.containsPacket(RenderPacket.Behavior)) {
			render.update(Gdx.input.isKeyJustPressed(KeyManager.KNEEL),
					ai.moving);
			if (render.state != RenderActive.NotActive) {
				ai.moving = false;
			}
		}
		if (melee != null) {
			if (melee.attacking) {
				allowDirChange = false;
			} else if (shouldAttack) {
				melee.attacking = true;
				allowDirChange = false;
			}
		}
		if (health != null && health.health <= 0) {
			ai.moving = false;
		}
		if (sit != null && sit.sitting) {
			ai.moving = false;
		}

		if (ai.moving && allowDirChange) {
			ai.movingdir = direction;
		}

	}

}
