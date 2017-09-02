package std.deviation.entities.components.ai.packets;

import std.deviation.entities.components.ai.AIComponent;
import std.deviation.entities.components.ai.Direction;

public class DefaultPacket extends AIPacket {

	public DefaultPacket() {
		super(AIPacket.DEFAULT);
	}

	private float timeSinceChange = 0;

	@Override
	public AIReturnData update(AIComponent ai, float deltaTime) {

		timeSinceChange += deltaTime;
		int dir = (int) (Math.random() * Direction.values().length);
		if (timeSinceChange <= 1) {
			dir = ai.movingdir.ordinal();
		} else {
			timeSinceChange = 0;
		}

		return new AIReturnData(dir, false, false);
	}

}
