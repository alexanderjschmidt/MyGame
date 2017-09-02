package std.deviation.entities.components.ai.packets;

import std.deviation.entities.components.ai.AIComponent;

public abstract class AIPacket {

	public byte type;

	public AIPacket(byte type) {
		this.type = type;
	}

	public abstract AIReturnData update(AIComponent ai, float deltaTime);

	public static final byte DEFAULT = 0;
	public static final byte PLAYER = 1;

	public static AIPacket getPacket(byte type) {
		switch (type) {
		case DEFAULT:
			return new DefaultPacket();
		case PLAYER:
			return new PlayerPacket();
		}
		return new DefaultPacket();
	}

}
