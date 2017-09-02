package std.deviation.entities.components;

import std.deviation.entities.components.ai.Direction;
import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements SerializableComponent {
	public static final float SQRT_TWO = 1.4142135624f, MARGIN = 7 / 10f;

	public static final float ACCELERATION = 60f, FRICTION = 40f,
			FRIC_DIFF = ACCELERATION - FRICTION;
	private float MAXVELOCITY = 7;
	private float MAX_VELOCITY_MARGIN = MAXVELOCITY * MARGIN,
			MARGIN_SQRD = MAX_VELOCITY_MARGIN * MAX_VELOCITY_MARGIN,
			MAX_VEL_HYP = MAXVELOCITY / SQRT_TWO;

	public float vx, vy;

	public VelocityComponent() {
		setMaxVel(9);
	}

	public void setMaxVel(float maxvel) {
		MAXVELOCITY = maxvel;
		MAX_VELOCITY_MARGIN = MAXVELOCITY * MARGIN;
		MARGIN_SQRD = MAX_VELOCITY_MARGIN * MAX_VELOCITY_MARGIN;
		MAX_VEL_HYP = MAXVELOCITY / SQRT_TWO;
	}

	public boolean nearMaxSpeed() {
		return ((vx * vx) + (vy * vy)) >= MARGIN_SQRD;
	}

	public float getMaxVel() {
		if (vx != 0 && vy != 0) {
			return MAX_VEL_HYP;
		}
		return MAXVELOCITY;
	}

	public void push(Direction movingdir, int dist) {
		Vector2 v = Direction.getVector(movingdir, dist);
		vx += v.x;
		vy += v.y;
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Velocity);
		pointer = SerializationUtils.writeBytes(dest, pointer, vx);
		pointer = SerializationUtils.writeBytes(dest, pointer, vy);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		vx = SerializationUtils.readFloat(src, pointer);
		pointer += Type.getSize(Type.FLOAT);
		vy = SerializationUtils.readFloat(src, pointer);
		pointer += Type.getSize(Type.FLOAT);
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + Type.getSize(Type.FLOAT)
				+ Type.getSize(Type.FLOAT);
	}
}
