package std.deviation.entities.serialization;

import std.deviation.entities.components.ChairComponent;
import std.deviation.entities.components.HealthComponent;
import std.deviation.entities.components.LightComponent;
import std.deviation.entities.components.MeleeComponent;
import std.deviation.entities.components.PositionComponent;
import std.deviation.entities.components.RenderPositionComponent;
import std.deviation.entities.components.SitComponent;
import std.deviation.entities.components.VelocityComponent;
import std.deviation.entities.components.ai.AIComponent;
import std.deviation.entities.components.inventory.InventoryComponent;
import std.deviation.entities.components.renderComponents.RenderComponent;
import std.deviation.entities.components.spatialComponents.CollisionComponent;
import std.deviation.entities.components.spatialComponents.HitboxComponent;
import std.deviation.entities.components.spatialComponents.InteractionComponent;

public class ComponentType {

	public static final byte Velocity = 0;
	public static final byte Position = 1;
	public static final byte Melee = 2;
	public static final byte Health = 3;
	public static final byte Chair = 4;
	public static final byte Collision = 5;
	public static final byte Hitbox = 6;
	public static final byte Interaction = 7;
	public static final byte Inventory = 8;
	public static final byte AI = 9;
	public static final byte Render = 10;
	public static final byte Sit = 11;
	public static final byte Light = 12;
	public static final byte RenderPosition = 13;

	public static SerializableComponent getComponent(byte type) {
		switch (type) {
		case Velocity:
			return new VelocityComponent();
		case Position:
			return new PositionComponent();
		case Melee:
			return new MeleeComponent();
		case Health:
			return new HealthComponent();
		case Chair:
			return new ChairComponent();
		case Collision:
			return new CollisionComponent();
		case Hitbox:
			return new HitboxComponent();
		case Interaction:
			return new InteractionComponent();
		case Inventory:
			return new InventoryComponent();
		case AI:
			return new AIComponent();
		case Render:
			return new RenderComponent();
		case Sit:
			return new SitComponent();
		case Light:
			return new LightComponent();
		case RenderPosition:
			return new RenderPositionComponent();
			/*
			 * case StaticIdleRender: return new StaticIdleRenderComponent();
			 * case AnimationIdleRender: return new
			 * AnimationIdleRenderComponent(); case IntersectRender: return new
			 * IntersectRenderComponent(); case FaceRender: return new
			 * FaceRenderComponent(); case BehaviorRender: return new
			 * BehaviorRenderComponent(); case CollapseRender: return new
			 * CollapseRenderComponent(); case DirectionalRender: return new
			 * DirectionalRenderComponent(); case RunningRender: return new
			 * RunningRenderComponent(); case SitRender: return new
			 * SitRenderComponent();
			 */
		}
		System.out.println("\n FAILED TO FIND: " + type);
		return null;
	}
}
