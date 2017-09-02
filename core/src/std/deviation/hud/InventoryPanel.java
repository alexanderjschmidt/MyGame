package std.deviation.hud;

import std.deviation.entities.components.inventory.InvSlot;
import std.deviation.entities.components.inventory.InventoryComponent;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public class InventoryPanel extends ScrollPane {

	public InventoryPanel(Skin skin) {
		super(null, skin);
		this.setSize(600, 500);
		this.setPosition(50, 30);
	}

	public void update(InventoryComponent inv, String organize) {
		VerticalGroup v = new VerticalGroup();
		v.setSize(500, 500);
		v.left();
		if (inv != null) {
			for (InvSlot i : inv) {
				if (organize(i, organize)) {
					i.update();
					v.addActor(i);
				}
			}
		}
		this.setWidget(v);
	}

	private boolean organize(InvSlot i, String organize) {
		switch (organize) {
		case "Favorites":
			return i.isFavorite();
		case "All":
			return true;
		default:
			return i.getItem().type.category.toString().equals(organize);
		}
	}

}
