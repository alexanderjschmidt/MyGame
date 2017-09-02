package std.deviation.hud;

import std.deviation.Application;
import std.deviation.managers.KeyManager;
import std.deviation.managers.ResourceManager;
import std.deviation.screen.Play;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {
	private Stage stage;

	public Label debugLabel, interact;
	private Label devLabel;

	private Play screen;
	private Application app;

	private Menu open, closing;
	private final Menu[] menus;
	private final InventoryMenu invMenu;

	public Hud(Play screen, Application app) {
		this.screen = screen;
		this.app = app;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		Viewport viewport = new StretchViewport(w, h, new OrthographicCamera());
		stage = new Stage(viewport);
		Gdx.input.setInputProcessor(stage);

		debugLabel = new Label("FPS: " + Gdx.graphics.getFramesPerSecond() + " Delta: " + Gdx.graphics.getDeltaTime(), ResourceManager.get().skin);
		interact = new Label("Interact", ResourceManager.get().skin);
		interact.setPosition(w - interact.getWidth(), h - interact.getHeight());

		invMenu = new InventoryMenu(stage);
		menus = new Menu[] { new MainMenu(app), invMenu };

		devLabel = new Label("DEV MODE ACTIVE, Selected: ", ResourceManager.get().skin);
		devLabel.setPosition(0, h - interact.getHeight());

		stage.addActor(debugLabel);
		stage.addActor(interact);
		stage.addActor(devLabel);
	}

	public void render() {
		handleInputs();
		Batch batch = stage.getBatch();
		batch.setProjectionMatrix(stage.getCamera().combined);

		debugLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
		devLabel.setVisible(screen.DEVMODE);
		devLabel.setText("DEV MODE ACTIVE, Selected: ");
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void openInv(Entity entity, Entity e2, boolean selling) {
		invMenu.update(entity, e2, selling);
		open(Menus.Inv);
	}

	private void handleInputs() {
		if (closing != null && !closing.isMenuOpen()) {
			closing.remove();
			closing = null;
			if (open != null) {
				stage.addActor(open);
				open.open();
				app.getScreen().pause();
			}
		}
		if (open != null) {
			open.handleInputs();
		}
		if (Gdx.input.isKeyJustPressed(KeyManager.INVENTORY) && open == null) {
			openInv(screen.getController().getPlayer(), null, false);
		}
		if (Gdx.input.isKeyJustPressed(KeyManager.ESCAPE) && open == null) {
			open(Menus.Main);
		}
		else if (Gdx.input.isKeyJustPressed(KeyManager.ESCAPE)) {
			close();
		}
	}

	public void open(Menus m) {
		close();
		open = menus[m.ordinal()];

		if (closing != null) {
			return;
		}
		stage.addActor(open);
		open.open();
		app.getScreen().pause();

	}

	public void close() {
		if (open == null)
			return;
		app.getScreen().resume();
		open.close();
		closing = open;
		open = null;
	}

	public void dispose() {
		stage.dispose();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	public enum Menus {
		Main, Inv, Talk, Quest;
	}
}
