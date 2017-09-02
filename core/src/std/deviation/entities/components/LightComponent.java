package std.deviation.entities.components;

import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;

public class LightComponent implements SerializableComponent {

	public PointLight light;
	public Color color;
	public float dist;

	public LightComponent() {
		color = Color.GOLDENROD;
		dist = 700;
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Light);
		pointer = SerializationUtils.writeBytes(dest, pointer,
				Color.rgba8888(color));
		pointer = SerializationUtils.writeBytes(dest, pointer, dist);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		Color.rgba8888ToColor(color, SerializationUtils.readInt(src, pointer));
		pointer += Type.getSize(Type.INTEGER);
		dist = SerializationUtils.readFloat(src, pointer);
		pointer += Type.getSize(Type.FLOAT);
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + Type.getSize(Type.INTEGER)
				+ Type.getSize(Type.FLOAT);
	}

}
