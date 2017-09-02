package std.deviation.igmenus;

import std.deviation.Application;
import std.deviation.entities.components.inventory.InvSlot;
import std.deviation.entities.components.inventory.InventoryComponent;
import std.deviation.items.Item.ItemCategory;
import std.deviation.managers.KeyManager;
import std.deviation.managers.ResourceManager;
import std.deviation.screen.Play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class InventoryMenu extends Menu {

	private InventoryComponent otherInv;
	private InventoryComponent playerInv;
	private Image cursor;
	private int selected;
	private int length;
	private Label title;
	private Stage stage;
	private Image character;

	private static final String[] filterNames = { "Favorites", "All", "Weapons", "Apparel", "Consumable", "Misc" };
	private ButtonGroup<TextButton> filterGroup;
	private ScrollPane scroll;
	private Label gold;
	private Slider numSelect;
	private Label numSelected;

	private boolean wait;
	public boolean sell;

	private Application app;

	private ItemCategory organize;

	public InventoryMenu(Application app, Play screen, Stage stage) {
		this.app = app;
		this.stage = stage;
		this.setSize(700, 720);
		this.setPosition(1380, 0);

		Image image = new Image(ResourceManager.get().invUI);
		image.setFillParent(true);

		TextureRegion cursorTex = ResourceManager.get().ui[0][0];
		cursor = new Image(cursorTex);
		cursor.scaleBy(-.5f);

		title = new Label("Inventory", ResourceManager.get().skin, "title");
		title.setPosition(140, 650);

		this.addActor(image);
		this.addActor(cursor);
		this.addActor(title);

		TextButton[] filters = new TextButton[filterNames.length];
		for (int i = 0; i < filters.length; i++) {
			filters[i] = new TextButton(filterNames[i], ResourceManager.get().skin, "toggle");
			filters[i].setSize(90, 40);
			filters[i].setPosition(90 + i * 100, 600);
			filters[i].setProgrammaticChangeEvents(true);
			this.addActor(filters[i]);
		}

		filterGroup = new ButtonGroup<TextButton>(filters);
		filterGroup.setUncheckLast(true);
	}

	@Override
	public void open() {
		app.getScreen().pause();
		this.setPosition(1380, 0);
		addAction(Actions.moveBy(-800, 0, 1f, Interpolation.pow5Out));
		menuOpen = true;

		if (character != null)
			character.remove();

		filterGroup.setChecked(filterNames[1]);
		organize = ItemCategory.Misc;
		wait = true;

		if (otherInv != null) {
			updateVisuals(otherInv);
			if (otherInv.getClass().equals(Human.class)) {
				character = new Image(((Human) otherInv).getFace());
			}
			else {
				character = new Image(playerInv.getFace());
			}
		}
		else {
			this.addActor(playerInv.getInv().getEquip());
			playerInv.getInv().getEquip().addAction(Actions.sequence(Actions.moveTo(0, 320), Actions.moveBy(-170, 0, 1f, Interpolation.pow5Out)));
			character = new Image(playerInv.getFace());
			updateVisuals(playerInv);
		}
		stage.addActor(character);
		character.setPosition(-500, 0);
		character.addAction(Actions.moveBy(450, 0, 1f, Interpolation.pow5Out));

	}

	public void setFace(final Human inv) {
		character.addAction(Actions.sequence(Actions.moveBy(-450, 0, .7f, Interpolation.pow5Out), Actions.run(new Runnable() {
			@Override
			public void run() {
				character.remove();
				character = new Image(inv.getFace());
				character.setPosition(-500, 0);
				character.addAction(Actions.moveBy(450, 0, .7f, Interpolation.pow5Out));
				stage.addActor(character);
			}
		})));
	}

	@Override
	public void close() {
		this.removeActor(playerInv.getInv().getEquip());
		app.getScreen().resume();
		addAction(Actions.sequence(Actions.moveBy(800, 0, 1f, Interpolation.pow5Out), Actions.run(new Runnable() {
			@Override
			public void run() {
				menuOpen = false;
			}
		})));
		character.addAction(Actions.moveBy(-450, 0, 1f, Interpolation.pow5Out));
	}

	private void updateVisuals(Chest inv) {
		title.setText(inv.name + "'s Inventory");
		VerticalGroup list = new VerticalGroup();
		list.addActor(getTitles());
		String temp = filterGroup.getChecked().getText().toString();
		organize = !(temp.equals("Favorites") || temp.equals("All")) ? ItemCategory.valueOf(ItemCategory.class, temp) : null;
		for (InvSlot i : inv.getInv()) {
			if (organize(i)) {
				i.update();
				list.addActor(i);
			}
		}
		list.right();
		removeActor(scroll);
		scroll = new ScrollPane(list, ResourceManager.get().skin);
		scroll.setSize(510, 520);
		scroll.setPosition(140, 50);
		addActor(scroll);

		cursor.setPosition(110, 500);
		selected = 0;
		length = inv.getInv().size();

		removeActor(gold);
		gold = new Label(inv.getInv().gold + " Gold", ResourceManager.get().skin);
		gold.setPosition(550, 20);
		addActor(gold);
		updateSelector(inv);

		cursor.toFront();
	}

	private void updateSelector(final Chest inv) {
		removeActor(numSelect);
		if (length == 0)
			return;
		numSelect = new Slider(1, inv.getInv().get(selected).getStackSize(), 1, false, ResourceManager.get().skin);
		numSelect.setSize(300, 32);
		numSelect.setPosition(150, 20);
		final Group menu = this;
		menu.removeActor(numSelected);
		numSelected = new Label(inv.getInv().get(selected).getItem().toString() + " (" + numSelect.getValue() + ")", ResourceManager.get().skin);
		numSelect.setPosition(200, 5);
		menu.addActor(numSelected);
		numSelect.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				menu.removeActor(numSelected);
				numSelected = new Label(inv.getInv().get(selected).getItem().toString() + " (" + numSelect.getValue() + ")", ResourceManager.get().skin);
				numSelect.setPosition(200, 5);
				menu.addActor(numSelected);
			}
		});
		addActor(numSelect);
	}

	private boolean organize(InvSlot i) {
		switch (filterGroup.getChecked().getText().toString()) {
		case "Favorites":
			return i.isFavorite();
		case "All":
			return true;
		default:
			return i.getItem().type.category.equals(organize);
		}
	}

	private Table getTitles() {
		Table t = new Table();

		Label icon = new Label("", ResourceManager.get().skin);
		Label name = new Label("Name:", ResourceManager.get().skin);
		Label stat = new Label("Stat:", ResourceManager.get().skin);
		Label weight = new Label("Weight:", ResourceManager.get().skin);
		Label value = new Label("Value:", ResourceManager.get().skin);
		t.right();

		t.add(icon).padRight(10).width(32);
		t.add(name).padRight(10).width(200);
		t.add(stat).width(70);
		t.add(weight).padLeft(10).width(70);
		t.add(value).padLeft(10).width(70);

		return t;
	}

	@Override
	public void handleInputs() {
		if (Gdx.input.isKeyJustPressed(KeyManager.SHIFT)) {
			filterGroup.setChecked(filterNames[(filterGroup.getCheckedIndex() + 1) % filterNames.length]);
			if (otherInv != null && title.getText().toString().contains(otherInv.name)) {
				updateVisuals(otherInv);
			}
			else {
				updateVisuals(playerInv);
			}
		}
		if (Gdx.input.isKeyJustPressed(KeyManager.FAVORITE) && title.getText().toString().contains(playerInv.name)) {
			playerInv.getInv().get(selected).toggleFavorite();
			updateVisuals(playerInv);

		}
		if (Gdx.input.isKeyJustPressed(KeyManager.SELECT) && title.getText().toString().contains(playerInv.name) && otherInv == null) {
			playerInv.getInv().equip(selected);
			updateVisuals(playerInv);
		}
		if (otherInv != null) {
			if (Gdx.input.isKeyJustPressed(KeyManager.SWITCHINVENTORY)) {
				this.addAction(Actions.moveBy(700, 0, .8f, Interpolation.pow5Out));
				if (title.getText().toString().contains(otherInv.name)) {
					updateVisuals(playerInv);
					if (otherInv.getClass().equals(Human.class)) {
						setFace(playerInv);
					}
				}
				else {
					updateVisuals(otherInv);
					if (otherInv.getClass().equals(Human.class)) {
						setFace((Human) otherInv);
					}
				}
				this.addAction(Actions.sequence(Actions.delay(.3f), Actions.moveBy(-700, 0, .8f, Interpolation.pow5Out)));
			}
			if (Gdx.input.isKeyJustPressed(KeyManager.SELECT) && !wait && length != 0) {
				transfer();
				if (title.getText().toString().contains(otherInv.name)) {
					updateVisuals(otherInv);
					updateSelector(otherInv);
				}
				else {
					updateVisuals(playerInv);
					updateSelector(playerInv);
				}
			}
		}
		if (Gdx.input.isKeyJustPressed(KeyManager.UP) && selected > 0) {
			cursor.addAction(Actions.moveBy(0, 32, .5f, Interpolation.pow5Out));
			selected--;
			if (otherInv != null && title.getText().toString().contains(otherInv.name)) {
				updateSelector(otherInv);
			}
			else {
				updateSelector(playerInv);
			}
		}
		else if (Gdx.input.isKeyJustPressed(KeyManager.DOWN) && selected < length - 1) {
			cursor.addAction(Actions.moveBy(0, -32, .5f, Interpolation.pow5Out));
			selected++;
			if (otherInv != null && title.getText().toString().contains(otherInv.name)) {
				updateSelector(otherInv);
			}
			else {
				updateSelector(playerInv);
			}
		}
		if (Gdx.input.isKeyJustPressed(KeyManager.LEFT)) {
			numSelect.setValue(numSelect.getValue() - 1);
		}
		else if (Gdx.input.isKeyJustPressed(KeyManager.RIGHT)) {
			numSelect.setValue(numSelect.getValue() + 1);
		}
		if (wait) {
			wait = false;
		}
	}

	private void transfer() {
		if (title.getText().toString().contains(otherInv.name)) {
			if (sell && playerInv.getInv().gold < otherInv.getInv().get(selected).getItem().price) {
				final Label notEnough = new Label("You don't have enough gold.", ResourceManager.get().skin);
				notEnough.setPosition(300, 20);
				notEnough.addAction(Actions.sequence(Actions.fadeIn(1f, Interpolation.pow5In), Actions.fadeOut(1f, Interpolation.pow5Out), Actions.run(new Runnable() {
					@Override
					public void run() {
						notEnough.remove();
					}
				})));
				return;
			}
			else if (sell) {
				playerInv.getInv().gold -= otherInv.getInv().get(selected).getItem().price;
				otherInv.getInv().gold += otherInv.getInv().get(selected).getItem().price;
			}
			playerInv.getInv().add(otherInv.getInv().remove(selected, (int) numSelect.getValue()));
			update(otherInv);
		}
		else {
			if (sell && otherInv.getInv().gold < playerInv.getInv().get(selected).getItem().price) {
				final Label notEnough = new Label(otherInv.name + " doesn't have enough gold.", ResourceManager.get().skin);
				notEnough.setPosition(300, 20);
				notEnough.addAction(Actions.sequence(Actions.fadeIn(1f, Interpolation.pow5In), Actions.fadeOut(1f, Interpolation.pow5Out), Actions.run(new Runnable() {
					@Override
					public void run() {
						notEnough.remove();
					}
				})));
				return;
			}
			else if (sell) {
				otherInv.getInv().gold -= playerInv.getInv().get(selected).getItem().price;
				playerInv.getInv().gold += playerInv.getInv().get(selected).getItem().price;
			}
			otherInv.getInv().add(playerInv.getInv().remove(selected, (int) numSelect.getValue()));
			update(playerInv);
		}

	}

}
