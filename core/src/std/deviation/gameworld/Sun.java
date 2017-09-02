package std.deviation.gameworld;

import std.deviation.Application;
import std.deviation.utils.WorldClock;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Sun extends RayHandler {

	private float totalTime = 0;
	private Ambiance current = Ambiance.DAYTIME;
	private float startR, startG, startB, startA;
	private Color AMBIENT_COLOR;
	private Color end;

	public Sun() {
		super(new World(new Vector2(0, 0), true));

		RayHandler.setGammaCorrection(true);
		RayHandler.useDiffuseLight(true);
		setAmbientLight(0f, .3f, 0f, .7f);
		setCulling(true);
		setBlur(true);
		setBlurNum(1);
		setShadows(true);
		setAmbiance(Ambiance.DUSK);
	}

	public void update(float delta, Application app) {
		// sun.setPosition(app.getCamera().position.x,
		// app.getCamera().position.y);
		if (totalTime >= current.time) {
			setAmbiance(Ambiance.values()[(current.ordinal() + 1)
			                              % Ambiance.values().length]);
		} else {
			double percent = (totalTime / current.time);
			percent = (Math.cos((percent * Math.PI) - Math.PI) + 1) / 2;
			float r = (float) (startR + ((end.r - startR) * percent));
			float g = (float) (startG + ((end.g - startG) * percent));
			float b = (float) (startB + ((end.b - startB) * percent));
			float a = (float) (startA + ((end.a - startA) * percent));
			AMBIENT_COLOR.set(r, g, b, a);
			totalTime += delta;
		}
		setAmbientLight(AMBIENT_COLOR);
		setCombinedMatrix(app.getCamera());
		updateAndRender();
	}

	private void setAmbiance(Ambiance a) {
		System.out.println(a.name());
		AMBIENT_COLOR = current.color.cpy();
		startR = AMBIENT_COLOR.r;
		startG = AMBIENT_COLOR.g;
		startB = AMBIENT_COLOR.b;
		startA = AMBIENT_COLOR.a;
		current = a;
		end = (a.color);
		totalTime = 0;
	}

	private enum Ambiance {

		MIDNIGHT(new Color(.1f, .1f, .3f, .8f),
				WorldClock.totalSeconds() * 2 / 10), DAWN(new Color(.7f, .4f,
				0f, .8f), WorldClock.totalSeconds() * 3 / 10), DAYTIME(
								new Color(1f, 1f, 1f, 1f), WorldClock.totalSeconds() * 3 / 10), DUSK(
										new Color(.6f, .3f, 0f, .8f),
				WorldClock.totalSeconds() * 2 / 10);

		private Color color;
		private float time;

		private Ambiance(Color color, float time) {
			this.color = color;
			this.time = time;
		}
	}
}
