package std.deviation.entities.components;

import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

public class PositionComponent implements SerializableComponent {
	public float x = 0, y = 0;

	public PositionComponent(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public PositionComponent() {
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Position);
		pointer = SerializationUtils.writeBytes(dest, pointer, x);
		pointer = SerializationUtils.writeBytes(dest, pointer, y);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		x = SerializationUtils.readFloat(src, pointer);
		pointer += Type.getSize(Type.FLOAT);
		y = SerializationUtils.readFloat(src, pointer);
		pointer += Type.getSize(Type.FLOAT);
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + Type.getSize(Type.FLOAT)
				+ Type.getSize(Type.FLOAT);
	}

}
