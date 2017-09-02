package std.deviation.hud;

import std.deviation.entities.components.inventory.InvSlot;
import std.deviation.entities.components.inventory.InventoryComponent;
import std.deviation.entities.components.renderComponents.RenderComponent;
import std.deviation.items.EquippedableItem;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class FaceImage extends Group {

	private RenderComponent face;
	private InventoryComponent inv;
	private String emotion;

	@Override
	public void draw(Batch batch, float delta) {
		if (face != null && face.getEmotion(emotion) != null)
			batch.draw(face.getEmotion(emotion), this.getX(), this.getY());
		if (inv != null) {
			for (InvSlot i : inv) {
				if (i.isEquipped()
						&& ((EquippedableItem) i.getItem()).get("bust", true) != null) {
					batch.draw(
							((EquippedableItem) i.getItem()).get("bust", true),
							this.getX(), this.getY());
				}
			}
		}
	}

	public void update(RenderComponent face, InventoryComponent inv,
			final String emotion) {
		this.face = face;
		this.inv = inv;
		this.emotion = emotion;
	}
}
