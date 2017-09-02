package std.deviation.screen;

import std.deviation.Application;
import std.deviation.entities.EntityController;
import std.deviation.gameworld.MapRenderer;
import std.deviation.gameworld.Sun;
import std.deviation.hud.Hud;
import std.deviation.managers.KeyManager;
import std.deviation.managers.ResourceManager;
import std.deviation.utils.WorldClock;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class Play implements Screen {

	private Application app;

	private final Hud hud;
	private final MapRenderer map;
	private final EntityController controller;
	private final WorldClock clock;
	private final Sun sun;

	public boolean DEVMODE = false;

	private boolean paused = false;

	public static int SIZE = 16;

	public Play(Application app) {
		this.app = app;
		hud = new Hud(this, app);
		map = new MapRenderer(this);
		controller = new EntityController(this);
		clock = new WorldClock();

		sun = new Sun();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		float d = delta;

		getApp().getCamera().update();
		map.setView(getApp().getCamera());
		getApp().getBatch().setProjectionMatrix(getApp().getCamera().combined);
		if (paused)
			d = 0;

		clock.update(d);
		map.render(d);
		controller.update(d);

		if (Gdx.input.isKeyJustPressed(KeyManager.DEVMODE)) {
			DEVMODE = !DEVMODE;
		}

		sun.update(delta, app);

		hud.render();

		ResourceManager.get().update(delta);
	}

	@Override
	public void show() {
		map.load("PlayerHome");
		controller.load();
		app.getCamera().load(map.getWidth(), map.getHeight());
	}

	@Override
	public void resize(int width, int height) {
		app.getCamera().viewportWidth = width;
		app.getCamera().viewportHeight = height;
		app.getCamera().update();
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		sun.dispose();
	}

	public Application getApp() {
		return app;
	}

	public MapRenderer getMap() {
		return map;
	}

	public EntityController getController() {
		return controller;
	}

	public Hud getHud() {
		return hud;
	}

	public RayHandler getSun() {
		return sun;
	}
}
