package std.deviation.entities.components.renderComponents;

import std.deviation.entities.components.renderComponents.renderParts.AnimationPart;
import std.deviation.entities.components.renderComponents.renderParts.RenderPacket;
import std.deviation.entities.components.renderComponents.renderParts.RenderPart;
import std.deviation.entities.components.renderComponents.renderParts.TexturePart;
import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderComponent extends TextureManager implements
		SerializableComponent {

	private static final long serialVersionUID = 1L;
	public float elapsedTime = 0;
	public PlayMode mode = PlayMode.LOOP;
	public RenderActive state = RenderActive.NotActive;
	public String selected = "";

	private long bitmask = 0;
	private byte[] imageData = null;

	public void render(Batch batch, float x, float y, float deltaTime) {
		elapsedTime += deltaTime;
		if (get(selected) != null)
			get(selected).render(batch, x, y, elapsedTime, mode);
	}

	public float getHeight() {
		if (get(selected) == null)
			return 0;
		return get(selected).getHeight();
	}

	public void setSelected(String selected, PlayMode mode) {
		if (!this.selected.equals(selected) || this.mode != mode) {
			elapsedTime = 0;
		}
		this.mode = mode;
		this.selected = selected;
	}

	private boolean isAnimationFinished() {
		RenderPart p = get(selected);
		if (!(p.getClass().equals(AnimationPart.class))) {
			return false;
		}
		AnimationPart a = (AnimationPart) p;
		return a.isAnimationFinished(elapsedTime);
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Render);
		pointer = SerializationUtils.writeBytes(dest, pointer, bitmask);
		pointer = SerializationUtils.writeBytes(dest, pointer, folder);
		pointer = SerializationUtils
				.writeBytes(dest, pointer, imageData.length);
		pointer = SerializationUtils.writeBytes(dest, pointer, imageData);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		bitmask = SerializationUtils.readLong(src, pointer);
		pointer += Type.getSize(Type.LONG);
		folder = SerializationUtils.readString(src, pointer);
		pointer += folder.length() + Type.getSize(Type.SHORT);
		int size = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		imageData = new byte[size];
		SerializationUtils.readBytes(src, pointer, imageData);
		pointer += size * Type.getSize(Type.BYTE);
		load();
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + Type.getSize(Type.LONG)
				+ folder.length() + Type.getSize(Type.SHORT)
				+ Type.getSize(Type.INTEGER)
				+ (imageData.length * Type.getSize(Type.BYTE));
	}

	public boolean containsPacket(RenderPacket packet) {
		return (bitmask & packet.bit) > 0;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public void load() {
		int pointer = 0;
		while (pointer < imageData.length) {
			pointer = loadNext(pointer);
		}
		if (containsPacket(RenderPacket.Walking)) {
			addSet("walking", (byte) 8, .1f);
			addSet("idle", new String[] { "idle1", "idle" }, (byte) 3, .2f);
		}
		if (containsPacket(RenderPacket.Running)) {
			addSet("running", (byte) 8, .1f);
		}
		if (containsPacket(RenderPacket.Behavior)) {
			addSet("behavior", new String[] { "behavior1", "kneel" }, (byte) 3,
					.15f);
		}
		if (containsPacket(RenderPacket.Collapse)) {
			addSet("collapse", (byte) 3, .1f);
		}
		if (containsPacket(RenderPacket.Running)) {
			addSet("running", (byte) 8, .1f);
		}
		if (containsPacket(RenderPacket.Sit)) {
			addSet("sitting", new String[] { "sitting", "sitting1" }, (byte) 3,
					.15f);
			addSet("sleeping", (byte) 3, .15f);
		}
		if (containsPacket(RenderPacket.Face)) {
			addTexture("neutral", new String[] { "Bust_1", "Bust", "Battler" });
			addTexture("happy", new String[] { "Bust_2", "Bust", "Battler" });
			addTexture("angry", new String[] { "Bust_3", "Bust", "Battler" });
			addTexture("blink", new String[] { "Bust_4", "Bust", "Battler" });
			addTexture("bored", new String[] { "Bust_5", "Bust", "Battler" });
			addTexture("disgust", new String[] { "Bust_6", "Bust", "Battler" });
			addTexture("thinking", new String[] { "Bust_7", "Bust", "Battler" });
			addTexture("hblink", new String[] { "Bust_8", "Bust", "Battler" });
		}
		if (containsPacket(RenderPacket.KO)) {
			addSet("ko", (byte) 3, .15f);
			addDeadSet("ko", (byte) 2, (byte) 0);
			addDeadSet("dead", (byte) 0, (byte) 1);
			addDeadSet("dead", (byte) 1, (byte) 2);
		}
		if (containsPacket(RenderPacket.Paperdoll)) {
			addTexture("Paperdoll", new String[] { "Paperdoll", "Battler" });
		}
	}

	public static final byte IMAGE = 0;
	public static final byte ROWANI = 1;
	public static final byte COLANI = 2;

	private int loadNext(int pointer) {
		String path = SerializationUtils.readString(imageData, pointer);
		pointer += path.length() + Type.getSize(Type.SHORT);
		String name = SerializationUtils.readString(imageData, pointer);
		pointer += name.length() + Type.getSize(Type.SHORT);
		byte type = SerializationUtils.readByte(imageData, pointer);
		pointer += Type.getSize(Type.BYTE);
		byte w = SerializationUtils.readByte(imageData, pointer);
		pointer += Type.getSize(Type.BYTE);
		byte h = SerializationUtils.readByte(imageData, pointer);
		pointer += Type.getSize(Type.BYTE);
		byte coord1 = SerializationUtils.readByte(imageData, pointer);
		pointer += Type.getSize(Type.BYTE);
		if (type == IMAGE) {
			byte coord2 = SerializationUtils.readByte(imageData, pointer);
			pointer += Type.getSize(Type.BYTE);
			loadImage(path, name, w, h, coord1, coord2);
			return pointer;
		}
		float delay = SerializationUtils.readFloat(imageData, pointer);
		pointer += Type.getSize(Type.FLOAT);
		if (type == ROWANI) {
			loadRowAni(path, name, w, h, coord1, delay);
		} else {
			loadColAni(path, name, w, h, coord1, delay);
		}
		return pointer;
	}

	public enum RenderActive {
		NotActive, Activating, Active, Deactivating;
	}

	public void update(boolean behaviorActivate, boolean moving) {
		if (state == RenderActive.Activating && isAnimationFinished()) {
			state = RenderActive.Active;
		} else if (state == RenderActive.Deactivating && isAnimationFinished()) {
			state = RenderActive.NotActive;
		} else if (state == RenderActive.NotActive && behaviorActivate) {
			state = RenderActive.Activating;
		} else if (state == RenderActive.Active && (behaviorActivate || moving)) {
			state = RenderActive.Deactivating;
		}
	}

	public TextureRegion getEmotion(String emotion) {
		if (containsKey(emotion))
			return ((TexturePart) get(emotion)).region;
		return null;
	}
}
