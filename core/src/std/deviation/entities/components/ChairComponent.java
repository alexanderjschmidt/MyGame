package std.deviation.entities.components;

import std.deviation.entities.components.ai.Direction;
import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

public class ChairComponent implements SerializableComponent {

	public int sitX, sitY, standX, standY;
	// sittingAni=true sitting, sittingAni=false, sleeping
	private boolean sittingAni;
	public boolean inUse;
	public Direction dir;

	public ChairComponent(int sitX, int sitY, int standX, int standY,
			Direction dir, boolean sitting) {
		this.sitX = sitX;
		this.sitY = sitY;
		this.standX = standX;
		this.standY = standY;
		this.dir = dir;
		this.sittingAni = sitting;
	}

	public String getType() {
		return sittingAni ? "sitting" : "sleeping";
	}

	public ChairComponent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Chair);
		pointer = SerializationUtils.writeBytes(dest, pointer, sitX);
		pointer = SerializationUtils.writeBytes(dest, pointer, sitY);
		pointer = SerializationUtils.writeBytes(dest, pointer, standX);
		pointer = SerializationUtils.writeBytes(dest, pointer, standY);
		pointer = SerializationUtils.writeBytes(dest, pointer, dir.ordinal());
		pointer = SerializationUtils.writeBytes(dest, pointer, sittingAni);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		sitX = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		sitY = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		standX = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		standY = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		dir = Direction.values()[SerializationUtils.readInt(src, pointer)];
		pointer += Type.getSize(Type.INTEGER);
		sittingAni = SerializationUtils.readBoolean(src, pointer);
		pointer += Type.getSize(Type.BOOLEAN);
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + (Type.getSize(Type.INTEGER) * 5)
				+ Type.getSize(Type.BOOLEAN);
	}

}
