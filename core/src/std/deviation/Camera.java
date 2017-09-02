package std.deviation;

import std.deviation.entities.components.PositionComponent;
import std.deviation.managers.KeyManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Camera extends OrthographicCamera {

	private PositionComponent pos;
	private int mapWidth, mapHeight;

	public Camera() {
		super();
	}

	public void load(int mapWidth, int mapHeight) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}

	public void lockEntity(PositionComponent pos) {
		this.pos = pos;
	}

	@Override
	public void update() {
		super.update();
		if (pos != null) {
			updateLockedPos();
			return;
		}
		if (Gdx.input.isKeyPressed(KeyManager.UP)) {
			position.y += 10;
		}
		else if (Gdx.input.isKeyPressed(KeyManager.DOWN)) {
			position.y -= 10;
		}
		if (Gdx.input.isKeyPressed(KeyManager.RIGHT)) {
			position.x += 10;
		}
		else if (Gdx.input.isKeyPressed(KeyManager.LEFT)) {
			position.x -= 10;
		}
	}

	private void updateLockedPos() {
		int w = Gdx.graphics.getWidth() / 2;
		int h = Gdx.graphics.getHeight() / 2;
		if (pos.x < w)
			position.x = w;
		else if (pos.x > mapWidth - w)
			position.x = mapWidth - w;
		else
			position.x = pos.x;

		if (pos.y < h)
			position.y = h;
		else if (pos.y > mapHeight - h)
			position.y = mapHeight - h;
		else
			position.y = pos.y;

		if (h * 2 > mapHeight)
			position.y = mapHeight / 2;
		if (w * 2 > mapWidth)
			position.x = mapWidth / 2;

	}
}
