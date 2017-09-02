package std.deviation.entities.components.renderComponents.renderParts;

public enum RenderPacket {
	StaticIdle(1), Intersect(2), Walking(4), Running(8), Behavior(16), Collapse(
			32), Sit(64), Face(128), KO(256), Paperdoll(512);

	// 110011100
	public long bit;

	private RenderPacket(long bit) {
		this.bit = bit;
	}
}
