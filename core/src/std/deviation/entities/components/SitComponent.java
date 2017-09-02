package std.deviation.entities.components;

import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

public class SitComponent implements SerializableComponent {

	public String type = "";
	public boolean sitting = false;

	public SitComponent() {
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Sit);
		pointer = SerializationUtils.writeBytes(dest, pointer, type);
		pointer = SerializationUtils.writeBytes(dest, pointer, sitting);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		type = SerializationUtils.readString(src, pointer);
		pointer += Type.getSize(Type.SHORT) + type.length();
		sitting = SerializationUtils.readBoolean(src, pointer);
		pointer += Type.getSize(Type.BOOLEAN);
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + Type.getSize(Type.SHORT)
				+ Type.getSize(Type.BOOLEAN);
	}

}
