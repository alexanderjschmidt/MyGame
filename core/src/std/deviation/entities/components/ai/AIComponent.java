package std.deviation.entities.components.ai;

import std.deviation.entities.components.ai.packets.AIPacket;
import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

public class AIComponent implements SerializableComponent {

	public Direction movingdir = Direction.values()[(int) (Math.random() * (Direction
			.values().length - 1)) + 1];
	public boolean moving;
	public AIPacket packet;

	public AIComponent(AIPacket packet) {
		this.packet = packet;
	}

	public AIComponent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils
				.writeBytes(dest, pointer, ComponentType.AI);
		pointer = SerializationUtils.writeBytes(dest, pointer,
				movingdir.ordinal());
		pointer = SerializationUtils.writeBytes(dest, pointer, moving);
		pointer = SerializationUtils.writeBytes(dest, pointer, packet.type);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		movingdir = Direction.values()[SerializationUtils.readInt(src, pointer)];
		pointer += Type.getSize(Type.INTEGER);
		moving = SerializationUtils.readBoolean(src, pointer);
		pointer += Type.getSize(Type.BOOLEAN);
		packet = AIPacket.getPacket(SerializationUtils.readByte(src, pointer));
		pointer += Type.getSize(Type.BYTE);
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + Type.getSize(Type.INTEGER)
				+ Type.getSize(Type.BOOLEAN) + Type.getSize(Type.BYTE);
	}
}
