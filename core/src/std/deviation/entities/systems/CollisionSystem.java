package std.deviation.entities.systems;

import java.util.ArrayList;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.PositionComponent;
import std.deviation.entities.components.VelocityComponent;
import std.deviation.entities.components.spatialComponents.CollisionComponent;
import std.deviation.entities.components.spatialComponents.SpatialHash;
import std.deviation.gameworld.GridRenderer;
import std.deviation.gameworld.MapRenderer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Intersector;

public class CollisionSystem extends IteratingSystem {

	private MapRenderer map;
	private SpatialHash spatial;

	public CollisionSystem(MapRenderer mapRenderer, SpatialHash spatial) {
		super(Family.all(CollisionComponent.class, PositionComponent.class)
				.get());
		this.map = mapRenderer;
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
		VelocityComponent velocity = EntityController.velocity.get(entity);
		CollisionComponent collision = EntityController.collision.get(entity);
		PositionComponent position = EntityController.position.get(entity);

		if (velocity != null) {
			ArrayList<Entity> entities = spatial.getNearByEntities(position.x
					+ collision.xOffset, position.y + collision.yOffset,
					collision.rect.getBoundingRectangle().width,
					collision.rect.getBoundingRectangle().height,
					EntityController.collision);
			if (!collision(position.x + velocity.vx + collision.xOffset,
					position.y + collision.yOffset, collision, entities)) {
				position.x += velocity.vx;
			} else {
				velocity.vx = 0;
			}
			if (!collision(position.x + collision.xOffset, position.y
					+ velocity.vy + collision.yOffset, collision, entities)) {
				position.y += velocity.vy;
			} else {
				velocity.vy = 0;
			}
		}

		collision.rect.setPosition(position.x + collision.xOffset, position.y
				+ collision.yOffset);
	}

	private boolean collision(float xMove, float yMove,
			CollisionComponent collision, ArrayList<Entity> entities) {
		return getCollision(xMove, yMove, collision)
				|| getCollision(xMove
						+ collision.rect.getBoundingRectangle().width - 1,
						yMove, collision)
				|| getCollision(xMove,
								yMove + collision.rect.getBoundingRectangle().height
								- 1, collision)
				|| getCollision(xMove
										+ collision.rect.getBoundingRectangle().width - 1,
										yMove + collision.rect.getBoundingRectangle().height
										- 1, collision)
				|| collide(xMove, yMove, collision, entities);
	}

	private boolean getCollision(float moveX, float moveY,
			CollisionComponent collision) {
		int x = (int) (moveX / GridRenderer.currentSize);
		int y = (int) (moveY / GridRenderer.currentSize);
		if (x >= map.getGrid().trueClearance.length
				|| y >= map.getGrid().trueClearance[0].length || x < 0 || y < 0) {
			return true;
		}
		return map.getGrid().trueClearance[x][y] == 0 || (moveX < 0)
				|| (moveY < 0) || (moveX > map.getWidth())
				|| (moveY > map.getHeight());
	}

	private boolean collide(float xMove, float yMove,
			CollisionComponent collision, ArrayList<Entity> entities) {
		collision.rect.setPosition(xMove, yMove);
		CollisionComponent c = null;
		for (Entity e : entities) {
			c = EntityController.collision.get(e);
			if (c != collision
					&& Intersector
							.overlapConvexPolygons(c.rect, collision.rect)) {
				return true;
			}
		}
		return false;
	}
}
