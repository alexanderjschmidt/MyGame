package std.deviation.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Menu extends Group {
	private boolean menuOpen;
	private int startX, startY, endX, endY;

	public Menu(int startX, int startY, int endX, int endY, int width, int height, Texture tex) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;

		this.setSize(width, height);

		Image image = new Image(tex);
		image.setFillParent(true);
		this.addActor(image);
	}

	public void open() {
		menuOpen = true;
		this.setPosition(startX, startY);
		this.addAction(Actions.moveTo(endX, endY, 1f, Interpolation.pow5Out));
	}

	public void close() {
		this.setPosition(endX, endY);
		this.addAction(Actions.sequence(Actions.moveTo(startX, startY, 1f, Interpolation.pow5Out), Actions.run(new Runnable() {
			@Override
			public void run() {
				menuOpen = false;
			}
		})));
	}

	public abstract void handleInputs();

	public boolean isMenuOpen() {
		return menuOpen;
	}

}
