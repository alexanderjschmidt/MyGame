package std.deviation.items;

import std.deviation.entities.components.renderComponents.TextureManager;
import std.deviation.entities.components.renderComponents.renderParts.TexturePart;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EquippedableItem extends Item {

	private TextureManager maleAnimation = new TextureManager();
	private TextureManager femaleAnimation = new TextureManager();

	public EquippedableItem(short id, String name, String malefolder,
			String femalefolder, int price, int weight, ItemType type, int x,
			int y) {
		super(id, name, price, weight, type, x, y);
		load(maleAnimation, malefolder);
		load(femaleAnimation, femalefolder);
	}

	private void load(TextureManager animation, String folder) {
		animation.folder = folder;
		animation.pathBase = "Textures/CompiledItems/";
		animation.addSet("walking", (byte) 8, .1f);
		animation.addSet("idle", new String[] { "idle1" }, (byte) 3, .2f);
		animation.addSet("running", (byte) 8, .1f);
		animation.addSet("behavior", new String[] { "kneel" }, (byte) 3, .15f);
		animation.addSet("collapse", (byte) 3, .1f);
		animation.addSet("running", (byte) 8, .1f);
		animation
				.addSet("sitting", new String[] { "sitting1" }, (byte) 3, .15f);
		animation.addSet("sleeping", (byte) 3, .15f);
		animation.addTexture("bust", new String[] { "Bust" });
		animation.addSet("ko", (byte) 3, .15f);
		animation.addDeadSet("ko", (byte) 2, (byte) 0);
		animation.addDeadSet("dead", (byte) 0, (byte) 1);
		animation.addDeadSet("dead", (byte) 1, (byte) 2);
		animation.addTexture("Paperdoll", new String[] { "Paperdoll" });
	}

	public TextureRegion get(String str, boolean gender) {
		if (gender) {
			if (maleAnimation.containsKey(str))
				return ((TexturePart) (maleAnimation.get(str))).region;
		} else {
			if (femaleAnimation.containsKey(str))
				return ((TexturePart) (femaleAnimation.get(str))).region;
		}
		return null;
	}

	public void render(Batch batch, String ani, float x, float y,
			float elapsed, PlayMode mode, boolean gender) {
		if (gender) {
			if (maleAnimation.get(ani) != null)
				maleAnimation.get(ani).render(batch, x, y, elapsed, mode);
		} else {
			if (femaleAnimation.get(ani) != null)
				femaleAnimation.get(ani).render(batch, x, y, elapsed, mode);
		}
	}

}
