package std.deviation.entities.components.ai.packets;

import std.deviation.entities.components.ai.AIComponent;
import std.deviation.managers.KeyManager;

import com.badlogic.gdx.Gdx;

public class PlayerPacket extends AIPacket {

	public PlayerPacket() {
		super(AIPacket.PLAYER);
	}

	@Override
	public AIReturnData update(AIComponent ai, float deltaTime) {

		int dir = 0;
		dir += Gdx.input.isKeyPressed(KeyManager.DOWN) ? 6 : Gdx.input
				.isKeyPressed(KeyManager.UP) ? 3 : 0;
		dir += Gdx.input.isKeyPressed(KeyManager.LEFT) ? 2 : Gdx.input
				.isKeyPressed(KeyManager.RIGHT) ? 1 : 0;
		boolean shouldInteract = Gdx.input.isKeyJustPressed(KeyManager.SELECT);
		boolean shouldAttack = Gdx.input.isKeyJustPressed(KeyManager.ATTACK);

		return new AIReturnData(dir, shouldInteract, shouldAttack);
	}

}
