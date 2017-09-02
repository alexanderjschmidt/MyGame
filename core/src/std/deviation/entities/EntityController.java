package std.deviation.entities;

import java.util.HashMap;

import std.deviation.entities.components.ChairComponent;
import std.deviation.entities.components.HealthComponent;
import std.deviation.entities.components.LightComponent;
import std.deviation.entities.components.MeleeComponent;
import std.deviation.entities.components.PositionComponent;
import std.deviation.entities.components.RenderPositionComponent;
import std.deviation.entities.components.SitComponent;
import std.deviation.entities.components.VelocityComponent;
import std.deviation.entities.components.ai.AIComponent;
import std.deviation.entities.components.ai.Direction;
import std.deviation.entities.components.ai.packets.DefaultPacket;
import std.deviation.entities.components.ai.packets.PlayerPacket;
import std.deviation.entities.components.inventory.InventoryComponent;
import std.deviation.entities.components.renderComponents.RenderComponent;
import std.deviation.entities.components.spatialComponents.CollisionComponent;
import std.deviation.entities.components.spatialComponents.HitboxComponent;
import std.deviation.entities.components.spatialComponents.InteractionComponent;
import std.deviation.entities.components.spatialComponents.SpatialHash;
import std.deviation.entities.components.spatialComponents.interaction.ChestInteract;
import std.deviation.entities.components.spatialComponents.interaction.NullInteract;
import std.deviation.entities.components.spatialComponents.interaction.SitInteract;
import std.deviation.entities.systems.AISystem;
import std.deviation.entities.systems.CollisionSystem;
import std.deviation.entities.systems.HitboxSystem;
import std.deviation.entities.systems.InteractionSystem;
import std.deviation.entities.systems.LightSystem;
import std.deviation.entities.systems.MeleeSystem;
import std.deviation.entities.systems.RenderSelectionSystem;
import std.deviation.entities.systems.RenderSystem;
import std.deviation.entities.systems.StaticRenderSelectionSystem;
import std.deviation.entities.systems.VelocitySystem;
import std.deviation.items.ItemManager;
import std.deviation.screen.Play;
import box2dLight.PointLight;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;

public class EntityController extends Engine {

	public static final ComponentMapper<PositionComponent> position = ComponentMapper
			.getFor(PositionComponent.class);
	public static final ComponentMapper<AIComponent> ai = ComponentMapper
			.getFor(AIComponent.class);
	public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper
			.getFor(VelocityComponent.class);
	public static final ComponentMapper<CollisionComponent> collision = ComponentMapper
			.getFor(CollisionComponent.class);
	public static final ComponentMapper<InteractionComponent> interaction = ComponentMapper
			.getFor(InteractionComponent.class);
	public static final ComponentMapper<HitboxComponent> hitbox = ComponentMapper
			.getFor(HitboxComponent.class);
	public static final ComponentMapper<MeleeComponent> melee = ComponentMapper
			.getFor(MeleeComponent.class);
	public static final ComponentMapper<InventoryComponent> inventory = ComponentMapper
			.getFor(InventoryComponent.class);
	public static final ComponentMapper<ChairComponent> chair = ComponentMapper
			.getFor(ChairComponent.class);
	public static final ComponentMapper<RenderComponent> render = ComponentMapper
			.getFor(RenderComponent.class);
	public static final ComponentMapper<RenderPositionComponent> renderposition = ComponentMapper
			.getFor(RenderPositionComponent.class);
	public static final ComponentMapper<HealthComponent> health = ComponentMapper
			.getFor(HealthComponent.class);
	public static final ComponentMapper<SitComponent> sit = ComponentMapper
			.getFor(SitComponent.class);
	public static final ComponentMapper<LightComponent> light = ComponentMapper
			.getFor(LightComponent.class);
	public SpatialHash spatial;
	public HashMap<String, Byte[]> templates = new HashMap<String, Byte[]>();

	private Play play;
	private Entity player;

	public EntityController(final Play play) {
		super();
		this.play = play;
		spatial = new SpatialHash(this, 16);
		this.addEntityListener(Family.all(LightComponent.class).get(),
				new EntityListener() {
					@Override
					public void entityAdded(Entity entity) {
						LightComponent l = light.get(entity);
						l.light = new PointLight(play.getSun(), 32, l.color,
								l.dist, 0, 0);
					}

					@Override
					public void entityRemoved(Entity entity) {
						LightComponent l = light.get(entity);
						l.light.remove();
					}
				});
		this.addSystem(new AISystem());
		this.addSystem(new VelocitySystem());
		this.addSystem(new CollisionSystem(play.getMap(), spatial));
		this.addSystem(new InteractionSystem(spatial, play));
		this.addSystem(new HitboxSystem());
		this.addSystem(new MeleeSystem(spatial));
		this.addSystem(new LightSystem());
		this.addSystem(new RenderSelectionSystem());
		this.addSystem(new StaticRenderSelectionSystem());
		this.addSystem(new RenderSystem(play.getApp().getBatch()));
	}

	@Override
	public void update(float delta) {
		spatial.update();
		super.update(delta);
	}

	public void load() {
		spatial.load(play.getMap().getWidth(), play.getMap().getHeight());
		loadEntities();
	}

	private void loadEntities() {
		Entity entity = new Entity();
		PositionComponent p = new PositionComponent(700, 400);
		entity.add(p);
		RenderComponent r = new RenderComponent();
		r.readBytes(new byte[] { 0xa, 0, 0, 0, 0, 0, 0, 0x3, (byte) 0xfc, 0,
				0xf, 0x4d, 0x61, 0x6c, 0x65, 0x5f, 0x54, 0x65, 0x6d, 0x70,
				0x6c, 0x61, 0x74, 0x65, 0x5f, 0x30, 0, 0, 0, 0 }, 0);

		// r.readBytes(new byte[] { 0xa, 0, 0, 0, 0, 0, 0, 0x3, (byte) 0xfc, 0,
		// 0x4, 0x4d, 0x61, 0x69, 0x6e, 0, 0, 0, 0 }, 0);
		entity.add(r);
		InventoryComponent i = new InventoryComponent();
		i.add(ItemManager.get().get("Bread"), (short) 5, false, false);
		i.add(ItemManager.get().get("Cloth Chest"), (short) 5, false, false);
		i.add(ItemManager.get().get("Cloth Shoes"), (short) 5, false, false);
		i.add(ItemManager.get().get("Cloth Chest"), (short) 5, false, false);
		i.add(ItemManager.get().get("Cloth Hat"), (short) 5, false, false);
		i.add(ItemManager.get().get("Plate Greaves"), (short) 5, false, false);
		i.gold = 500;
		entity.add(i);
		entity.add(new AIComponent(new PlayerPacket()));
		entity.add(new SitComponent());
		entity.add(new HealthComponent());
		entity.add(new VelocityComponent());
		entity.add(new MeleeComponent());
		entity.add(new CollisionComponent(-16, -40, 32, 32, 0));
		entity.add(new InteractionComponent(-48, -72, 64, 64,
				new NullInteract()));
		entity.add(new HitboxComponent(-16, -16, 32, 32));
		entity.add(new LightComponent());

		byte[] dest = new byte[EntitySaver.entitySize(entity)];
		EntitySaver.writeEntity(dest, 0, entity);

		for (Byte b : dest) {
			System.out.print(b + " ");
		}
		System.out.println();
		System.out.println(dest.length);

		Entity e = new Entity();
		EntitySaver.readEntity(dest, 0, e);

		player = e;
		this.addEntity(e);
		createEntity();
		createDog();
		createChest();
		createSitChest();
		play.getApp().getCamera()
		.lockEntity(player.getComponent(PositionComponent.class));

	}

	public void createEntity() {
		Entity entity = new Entity();
		PositionComponent p = new PositionComponent(1100, 700);
		entity.add(p);
		RenderComponent r = new RenderComponent();
		r.readBytes(new byte[] { 0xa, 0, 0, 0, 0, 0, 0, 0x3, (byte) 0xfc, 0,
				0x4, 0x4d, 0x61, 0x69, 0x6e, 0, 0, 0, 0 }, 0);
		entity.add(r);
		InventoryComponent i = new InventoryComponent();
		entity.add(i);
		entity.add(new AIComponent(new DefaultPacket()));
		entity.add(new HealthComponent());
		entity.add(new VelocityComponent());
		entity.add(new HitboxComponent(-16, -16, 32, 32));
		entity.add(new CollisionComponent(-16, -40, 32, 32, 0));
		entity.add(new InteractionComponent(-48, -72, 64, 64,
				new ChestInteract()));
		entity.add(new LightComponent());

		this.addEntity(entity);
	}

	public void createDog() {
		Entity entity = new Entity();
		PositionComponent p = new PositionComponent(1100, 600);
		entity.add(p);
		RenderComponent r = new RenderComponent();
		r.readBytes(new byte[] { 0xa, 0, 0, 0, 0, 0, 0, 0x1, (byte) 0x9c, 0,
				0x10, 0x4d, 0x65, 0x64, 0x69, 0x65, 0x76, 0x61, 0x6c, 0x5f,
				0x54, 0x43, 0x5f, 0x44, 0x6f, 0x67, 0x31, 0, 0, 0, 0 }, 0);
		entity.add(r);
		InventoryComponent i = new InventoryComponent();
		entity.add(i);
		entity.add(new AIComponent(new DefaultPacket()));
		entity.add(new HealthComponent());
		entity.add(new VelocityComponent());
		entity.add(new HitboxComponent(-16, -16, 32, 32));
		entity.add(new CollisionComponent(-16, -40, 32, 32, 0));
		entity.add(new InteractionComponent(-48, -72, 64, 64,
				new NullInteract()));
		entity.add(new LightComponent());

		this.addEntity(entity);
	}

	public void createSitChest() {
		Entity entity = new Entity();
		PositionComponent p = new PositionComponent(1000, 700);
		entity.add(p);
		RenderComponent r = new RenderComponent();
		r.readBytes(new byte[] { 0xa, 0, 0, 0, 0, 0, 0, 0, 0x3, 0, 0, /* start */
		0, 0, 0, 0x2e, 0, 0x21, 0x54, 0x65, 0x78, 0x74, 0x75, 0x72, 0x65, 0x73,
				0x2f, 0x4f, 0x62, 0x6a, 0x65, 0x63, 0x74, 0x73, 0x2f, 0x50,
				0x72, 0x6f, 0x66, 0x65, 0x73, 0x73, 0x69, 0x6f, 0x6e, 0x5f,
				0x31, 0x2e, 0x70, 0x6e, 0x67, 0, 0x4, 0x69, 0x64, 0x6c, 0x65,
				0, 0x1, 0x1, 0xa, 0x8 }, 0);
		entity.add(r);
		entity.add(new CollisionComponent(-16, -16, 32, 32, 0));
		entity.add(new InteractionComponent(-48, -48, 64, 64, new SitInteract()));
		entity.add(new ChairComponent(0, 0, 0, -32, Direction.SOUTH, true));

		this.addEntity(entity);
	}

	public void createChest() {
		Entity entity = new Entity();
		PositionComponent p = new PositionComponent(900, 700);
		entity.add(p);
		InventoryComponent i = new InventoryComponent();
		i.add(ItemManager.get().get("Bread"), (short) 5, false, false);
		i.add(ItemManager.get().get("Cloth Chest"), (short) 5, false, false);
		i.add(ItemManager.get().get("Cloth Shoes"), (short) 5, false, false);
		i.add(ItemManager.get().get("Cloth Hat"), (short) 5, false, false);
		entity.add(i);
		RenderComponent r = new RenderComponent();
		r.readBytes(new byte[] { 0xa, 0, 0, 0, 0, 0, 0, 0, 0x3, 0, 0, /* start */
		0, 0, 0, 0x2e, 0, 0x21, 0x54, 0x65, 0x78, 0x74, 0x75, 0x72, 0x65, 0x73,
				0x2f, 0x4f, 0x62, 0x6a, 0x65, 0x63, 0x74, 0x73, 0x2f, 0x50,
				0x72, 0x6f, 0x66, 0x65, 0x73, 0x73, 0x69, 0x6f, 0x6e, 0x5f,
				0x31, 0x2e, 0x70, 0x6e, 0x67, 0, 0x4, 0x69, 0x64, 0x6c, 0x65,
				0, 0x1, 0x1, 0xa, 0x8 }, 0);
		entity.add(r);
		entity.add(new CollisionComponent(-16, -16, 32, 32, 0));
		entity.add(new InteractionComponent(-48, -48, 64, 64,
				new ChestInteract()));
		entity.add(new LightComponent());

		this.addEntity(entity);
	}

	public Entity getPlayer() {
		return player;
	}
}
