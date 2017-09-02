package std.deviation.entities.systems;

import java.util.Comparator;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.MeleeComponent;
import std.deviation.entities.components.PositionComponent;
import std.deviation.entities.components.RenderPositionComponent;
import std.deviation.entities.components.inventory.InvSlot;
import std.deviation.entities.components.inventory.InventoryComponent;
import std.deviation.entities.components.renderComponents.RenderComponent;
import std.deviation.items.EquippedableItem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;

public class RenderSystem extends SortedIteratingSystem {

	private Batch batch;

	public RenderSystem(Batch batch) {
		super(Family.all(RenderComponent.class, PositionComponent.class).get(),
				new YComparator());

		this.batch = batch;
	}

	@Override
	public void update(float deltaTime) {
		batch.begin();
		super.forceSort();
		super.update(deltaTime);
		batch.end();

	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PositionComponent p = EntityController.position.get(entity);
		RenderComponent render = EntityController.render.get(entity);
		RenderPositionComponent renderPos = EntityController.renderposition
				.get(entity);
		InventoryComponent inv = EntityController.inventory.get(entity);

		float x = p.x;
		float y = p.y;
		if (renderPos != null) {
			x += renderPos.pos.getX();
			y += renderPos.pos.getY();
		}

		render.render(batch, x, y, deltaTime);

		if (inv != null) {
			for (InvSlot i : inv) {
				if (i.isEquipped()) {
					((EquippedableItem) i.getItem()).render(batch,
							render.selected, x, y, render.elapsedTime,
							render.mode, true);
				}
			}
		}

		MeleeComponent melee = EntityController.melee.get(entity);
		if (melee != null && melee.attacking) {
			melee.render(batch);
		}
	}

	private static class YComparator implements Comparator<Entity> {
		@Override
		public int compare(Entity e1, Entity e2) {
			return (int) Math
					.signum((EntityController.position.get(e2).y - EntityController.render
							.get(e2).getHeight())
							- (EntityController.position.get(e1).y - EntityController.render
									.get(e1).getHeight()));
		}
	}
}
