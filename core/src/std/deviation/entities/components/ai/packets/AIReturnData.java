package std.deviation.entities.components.ai.packets;

public class AIReturnData {

	public int dir;
	public boolean interact, attack;

	public AIReturnData(int dir, boolean interact, boolean attack) {
		this.dir = dir;
		this.interact = interact;
		this.attack = attack;
	}

}
