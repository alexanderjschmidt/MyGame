package std.deviation.entities.components;

import std.deviation.entities.components.ai.Direction;
import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class RenderPositionComponent implements SerializableComponent {

	public Actor pos;

	public RenderPositionComponent() {
		pos = new Actor();
	}

	public void vibrate(Direction dir) {

	}

	public void lunge(Direction dir) {

	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.RenderPosition);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE);
	}

}
