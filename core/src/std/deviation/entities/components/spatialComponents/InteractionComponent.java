package std.deviation.entities.components.spatialComponents;

import std.deviation.entities.components.spatialComponents.interaction.Interact;
import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

import com.badlogic.gdx.math.Rectangle;

public class InteractionComponent implements SerializableComponent {

	public int xOffset, yOffset;
	public Rectangle rect;
	public Interact interact;
	public boolean shouldInteract = false;
	public boolean intersecting;

	public InteractionComponent(int xOffset, int yOffset, int width,
			int height, Interact interact) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		rect = new Rectangle(xOffset, yOffset, width, height);
		this.interact = interact;
	}

	public InteractionComponent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Interaction);
		pointer = SerializationUtils.writeBytes(dest, pointer, xOffset);
		pointer = SerializationUtils.writeBytes(dest, pointer, yOffset);
		pointer = SerializationUtils
				.writeBytes(dest, pointer, (int) rect.width);
		pointer = SerializationUtils.writeBytes(dest, pointer,
				(int) rect.height);
		pointer = SerializationUtils.writeBytes(dest, pointer, interact.type);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		xOffset = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		yOffset = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		int width = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		int height = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		rect = new Rectangle(xOffset, yOffset, width, height);
		interact = Interact.getInteract(SerializationUtils.readByte(src,
				pointer));
		pointer += Type.getSize(Type.BYTE);
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + (4 * Type.getSize(Type.INTEGER))
				+ Type.getSize(Type.BYTE);
	}
}
