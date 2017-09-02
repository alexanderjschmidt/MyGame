package std.deviation.entities.systems;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.HealthComponent;
import std.deviation.entities.components.SitComponent;
import std.deviation.entities.components.VelocityComponent;
import std.deviation.entities.components.ai.AIComponent;
import std.deviation.entities.components.renderComponents.RenderComponent;
import std.deviation.entities.components.renderComponents.RenderComponent.RenderActive;
import std.deviation.entities.components.renderComponents.renderParts.RenderPacket;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class RenderSelectionSystem extends IteratingSystem {

	public RenderSelectionSystem() {
		super(Family.all(AIComponent.class, VelocityComponent.class,
				RenderComponent.class).get());
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
		VelocityComponent velocity = EntityController.velocity.get(entity);
		AIComponent ai = EntityController.ai.get(entity);
		HealthComponent health = EntityController.health.get(entity);
		SitComponent sit = EntityController.sit.get(entity);

		String s = "";
		PlayMode mode = PlayMode.LOOP;
		switch (render.state) {
		case Activating:
			mode = PlayMode.NORMAL;
			break;
		case Active:
			mode = PlayMode.NORMAL;
			break;
		case Deactivating:
			mode = PlayMode.REVERSED;
			break;
		case NotActive:
			mode = PlayMode.LOOP;
			break;
		default:
			mode = PlayMode.LOOP;
			break;
		}

		if (render.containsPacket(RenderPacket.Sit) && sit != null
				&& sit.sitting && sit.type != null) {
			s = sit.type;
		} else if (health != null && health.health <= 0) {
			s = health.deadRender;
			mode = PlayMode.NORMAL;
		} else if (health != null
				&& render.containsPacket(RenderPacket.Collapse)
				&& health.health <= 20
				&& (velocity.vx != 0 || velocity.vy != 0)) {
			s = "collapse";

		} else if (render.containsPacket(RenderPacket.Behavior)
				&& render.state != RenderActive.NotActive) {
			s = "behavior";
		} else if (velocity.vx == 0 && velocity.vy == 0) {
			s = "idle";
		} else if (render.containsPacket(RenderPacket.Running)
				&& velocity.nearMaxSpeed()) {
			s = "running";
		} else {
			s = "walking";
		}

		s += ai.movingdir.toString();
		render.setSelected(s, mode);
	}
	/*
	 * int dist = ai.distBetween; render.reverse = dist >= 3;
	 *
	 * int mod = (ai.movingdir.getClockPos() - ai.facingdir.getClockPos() + 8) %
	 * 8; int facing = (ai.facingdir.getClockPos()); if ((dist == 2 && mod == 2)
	 * || (dist == 3 && mod == 5)) { facing += 1; } else if ((dist == 2 && mod
	 * == 6) || (dist == 3 && mod == 3)) { facing += 7; }
	 *
	 * System.out.println(ai.movingdir + " " + ai.facingdir);
	 * System.out.println(dist + " " + facing + " " + render.reverse); Direction
	 * face = Direction.getDirectionWithClockPos(facing % 8);
	 *
	 * if (velocity.vx == 0 && velocity.vy == 0) { render.selected = "idle" +
	 * ai.facingdir.toString(); return; } else if (running != null &&
	 * velocity.nearMaxSpeed()) { render.selected = "running"; } else {
	 * render.selected = "walking"; } render.selected += face.toString();
	 */
}
