package std.deviation.entities.components.spatialComponents;

import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;
import std.deviation.gameworld.GridRenderer;

import com.badlogic.gdx.math.Polygon;

public class CollisionComponent implements SerializableComponent {
	public int xOffset, yOffset;
	public Polygon rect;
	public int size;

	public CollisionComponent(int xOffset, int yOffset, int width, int height,
			float rot) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		rect = getPoly(width, height, rot);
		size = Math.max(width, height) / GridRenderer.currentSize;
	}

	public CollisionComponent() {
		// TODO Auto-generated constructor stub
	}

	public Polygon getPoly(int width, int height, float rotation) {
		Polygon poly = new Polygon(new float[] { 0, 0, width, 0, width, height,
				0, height });
		poly.setRotation(rotation);
		return poly;
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Collision);
		pointer = SerializationUtils.writeBytes(dest, pointer, xOffset);
		pointer = SerializationUtils.writeBytes(dest, pointer, yOffset);
		pointer = SerializationUtils.writeBytes(dest, pointer,
				rect.getRotation());
		rect.setRotation(0);
		pointer = SerializationUtils.writeBytes(dest, pointer,
				(int) rect.getBoundingRectangle().width);
		pointer = SerializationUtils.writeBytes(dest, pointer,
				(int) rect.getBoundingRectangle().height);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		xOffset = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		yOffset = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		float rotation = SerializationUtils.readFloat(src, pointer);
		pointer += Type.getSize(Type.FLOAT);
		int width = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		int height = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		rect = getPoly(width, height, rotation);
		size = Math.max(width, height) / GridRenderer.currentSize;
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE)
				+ (4 * Type.getSize(Type.INTEGER) + Type.getSize(Type.FLOAT));
	}
}
