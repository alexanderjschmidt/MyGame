package std.deviation.entities.components;

import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

public class HealthComponent implements SerializableComponent {

	public int health = 100, maxHealth = 100;
	public String deadRender = "ko";
	public byte numdead = 1;

	public HealthComponent() {

	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Health);
		pointer = SerializationUtils.writeBytes(dest, pointer, health);
		pointer = SerializationUtils.writeBytes(dest, pointer, deadRender);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		health = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		deadRender = SerializationUtils.readString(src, pointer);
		pointer += Type.getSize(Type.SHORT) + deadRender.length();
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + Type.getSize(Type.INTEGER)
				+ Type.getSize(Type.SHORT) + deadRender.length();
	}

	public void ragdoll() {
		deadRender = "dead" + ((int) (Math.random() * numdead));
	}

}
