package std.deviation.hud;

import std.deviation.entities.EntityController;
import std.deviation.entities.components.inventory.InventoryComponent;
import std.deviation.entities.components.renderComponents.RenderComponent;
import std.deviation.managers.KeyManager;
import std.deviation.managers.ResourceManager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class InventoryMenu extends Menu {

	private InventoryComponent inv1;
	private InventoryComponent inv2;
	private RenderComponent face1;
	private RenderComponent face2;
	private FaceImage face;
	private boolean selling;

	private Image cursor;
	private int selected = 0;

	private InventoryPanel panel;
	private static final String[] filterNames = { "Favorites", "All",
			"Weapons", "Apparel", "Consumable", "Misc" };
	private Image[] filters = new Image[filterNames.length];
	private int selectedFilter = 1;

	private Label gold;

	public static final String path = "Textures/UI/InvBackground.png";

	public InventoryMenu(Stage stage) {
		super(1280, 0, 780, 0, 500, 720, ResourceManager.get().getTexture(path));
		panel = new InventoryPanel(ResourceManager.get().skin);
		panel.setFlickScroll(false);
		panel.setFadeScrollBars(false);

		TextureRegion cursorTex = ResourceManager.get().ui[0][0];
		cursor = new Image(cursorTex);
		cursor.scaleBy(-.5f);
		cursor.setPosition(0, 495);

		for (int i = 0; i < filterNames.length; i++) {
			filters[i] = new Image(ResourceManager.get().inventoryIcons[0][i]);
			filters[i].setSize(50, 50);
			filters[i].setPosition(60 + i * 60, 550);
			this.addActor(filters[i]);
		}
		filters[selectedFilter].setColor(Color.RED);

		Label title = new Label("Inventory", ResourceManager.get().skin,
				"title");
		title.setPosition(40, 600);

		gold = new Label("", ResourceManager.get().skin);
		gold.setPosition(430, 570);

		face = new FaceImage();

		this.addActor(title);
		this.addActor(cursor);
		this.addActor(panel);
		this.addActor(gold);
		stage.addActor(face);
	}

	@Override
	public void open() {
		super.open();
		face.setPosition(-512, 0);
		face.addAction(Actions.moveTo(0, 0, 1f, Interpolation.pow5Out));
	}

	@Override
	public void close() {
		super.close();
		face.setPosition(0, 0);
		face.addAction(Actions.moveTo(-512, 0, 1f, Interpolation.pow5Out));
	}

	public void update(Entity e1, Entity e2, boolean selling) {
		this.inv1 = EntityController.inventory.get(e1);
		this.inv2 = e2 == null ? null : EntityController.inventory.get(e2);
		this.face1 = EntityController.render.get(e1);
		this.face2 = e2 == null ? null : EntityController.render.get(e2);
		this.selling = selling;
		panel.update(inv1, filterNames[selectedFilter]);
		face.update(face1, inv1, "neutral");
		selected = 0;
		cursor.setPosition(0, 495);
	}

	private void swap() {
		selected = 0;
		cursor.setPosition(0, 495);
		InventoryComponent temp = inv1;
		inv1 = inv2;
		inv2 = temp;
		RenderComponent tempf = face1;
		face1 = face2;
		face2 = tempf;
		this.addAction(Actions.sequence(Actions.alpha(0, .3f,
				Interpolation.pow5Out), Actions.run(new Runnable() {
			@Override
			public void run() {
				panel.update(inv1, filterNames[selectedFilter]);
			}
		}), Actions.moveBy(200, 0), Actions.parallel(
				Actions.alpha(1f, .5f, Interpolation.pow5Out),
				Actions.moveBy(-200, 0, .8f, Interpolation.pow5Out))));
		face.addAction(Actions.sequence(Actions.alpha(0, .3f,
				Interpolation.pow5Out), Actions.run(new Runnable() {
			@Override
			public void run() {
				face.update(face1, inv1, "neutral");
			}
		}), Actions.moveBy(-200, 0), Actions.parallel(
				Actions.alpha(1f, .5f, Interpolation.pow5Out),
				Actions.moveBy(200, 0, .8f, Interpolation.pow5Out))));

	}

	@Override
	public void handleInputs() {
		gold.setText("$" + inv1.gold);
		cursor.setVisible(inv1.size() > 0);
		if (Gdx.input.isKeyJustPressed(KeyManager.SHIFT)) {
			filters[selectedFilter].setColor(Color.WHITE);
			selectedFilter = (selectedFilter + 1) % filterNames.length;
			filters[selectedFilter].setColor(Color.RED);
			panel.update(inv1, filterNames[selectedFilter]);
		} else if (Gdx.input.isKeyJustPressed(KeyManager.SWITCHINVENTORY)
				&& inv2 != null) {
			swap();
		} else if (Gdx.input.isKeyJustPressed(KeyManager.FAVORITE)
				&& inv1.size() > 0 && inv2 == null) {
			inv1.get(selected).toggleFavorite();
			panel.update(inv1, filterNames[selectedFilter]);
		} else if (Gdx.input.isKeyJustPressed(KeyManager.EQUIPPED)
				&& inv1.size() > 0 && inv2 == null) {
			inv1.get(selected).toggleEquipped();
			panel.update(inv1, filterNames[selectedFilter]);
		} else if (Gdx.input.isKeyJustPressed(KeyManager.UP) && selected > 0) {
			selected--;
			cursor.addAction(Actions.moveBy(0, 32, .2f, Interpolation.pow5Out));
		} else if (Gdx.input.isKeyJustPressed(KeyManager.DOWN)
				&& selected < inv1.size() - 1) {
			selected++;
			cursor.addAction(Actions.moveBy(0, -32, .2f, Interpolation.pow5Out));
		} else if (Gdx.input.isKeyJustPressed(KeyManager.SELECT)
				&& inv1.size() > 0) {
			select();
			panel.update(inv1, filterNames[selectedFilter]);
		}
	}

	public void select() {
		if (inv2 != null) {
			if (selling) {
				int price = inv1.get(selected).getItem().getPrice();
				if (inv2.gold < price) {
					return;
				}
				inv2.gold -= price;
				inv1.gold += price;
			}
			inv2.add(inv1.remove(selected, (short) 1));
		}
		if (selected >= inv1.size()) {
			cursor.setVisible(inv1.size() > 0);
			selected = inv1.size() - 1;
			cursor.setPosition(0, 495 - (selected * 32));
		}
	}

}
