package std.deviation.entities;

import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

public class EntitySaver {

	public static int writeEntity(byte[] dest, int pointer, Entity e) {
		ImmutableArray<Component> components = e.getComponents();
		pointer = SerializationUtils.writeBytes(dest, pointer,
				components.size());
		for (Component c : components) {
			SerializableComponent sc = (SerializableComponent) c;
			pointer = sc.writeBytes(dest, pointer);
		}
		return pointer;
	}

	public static int readEntity(byte[] src, int pointer, Entity e) {
		int numComponents = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		for (int i = 0; i < numComponents; i++) {
			byte type = SerializationUtils.readByte(src, pointer);
			SerializableComponent sc = ComponentType.getComponent(type);
			pointer = sc.readBytes(src, pointer);
			e.add(sc);
		}
		return pointer;
	}

	public static int entitySize(Entity e) {
		ImmutableArray<Component> components = e.getComponents();
		int size = Type.getSize(Type.INTEGER);
		for (Component c : components) {
			SerializableComponent sc = (SerializableComponent) c;
			size += sc.getSize();
		}
		return size;
	}
}
