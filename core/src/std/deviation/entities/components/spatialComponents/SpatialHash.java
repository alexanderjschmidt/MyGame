package std.deviation.entities.components.spatialComponents;

import java.util.ArrayList;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.PositionComponent;
import std.deviation.gameworld.GridRenderer;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;

public class SpatialHash {
	private int bucketPixelSize, width, height, size;
	private int margin = 16;
	private Array<Array<Entity>> buckets;
	ImmutableArray<Entity> entities;

	public SpatialHash(Engine engine, int bucketDimension) {
		bucketPixelSize = bucketDimension * GridRenderer.currentSize;
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class).one(CollisionComponent.class, InteractionComponent.class, HitboxComponent.class).get());
	}

	public void load(int width, int height) {
		this.width = (width / bucketPixelSize) + 1;
		this.height = (height / bucketPixelSize) + 1;
		size = this.width * this.height;
		buckets = new Array<Array<Entity>>(size);
		for (int i = 0; i < size; i++) {
			buckets.add(new Array<Entity>(4));
		}
	}

	public void update() {
		clearBuckets();

		PositionComponent temp = null;
		for (Entity e : entities) {
			temp = EntityController.position.get(e);
			buckets.get(getBucketIndex(temp.x, temp.y)).add(e);
		}
	}

	public ArrayList<Entity> getNearByEntities(float xt, float yt, float width2, float height2, ComponentMapper<?> map) {

		int x0 = (int) ((xt - margin) / bucketPixelSize);
		int y0 = (int) ((yt - margin) / bucketPixelSize);
		int x1 = (int) ((xt + width2 + margin) / bucketPixelSize);
		int y1 = (int) ((yt + height2 + margin) / bucketPixelSize);
		ArrayList<Entity> entitys = new ArrayList<Entity>();
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				int index = x + y * width;
				if (index >= 0 && index < size) {
					Array<Entity> temp = buckets.get(index);
					for (Entity e : temp) {
						if (map.has(e))
							entitys.add(e);
					}
				}
			}
		}

		return entitys;
	}

	private int getBucketIndex(float xt, float yt) {
		int x = (int) (xt / bucketPixelSize);
		int y = (int) (yt / bucketPixelSize);
		return x + y * width;
	}

	private void clearBuckets() {
		for (Array<Entity> list : buckets) {
			list.clear();;
		}
	}
}
