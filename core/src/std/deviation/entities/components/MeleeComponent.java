package std.deviation.entities.components;

import java.util.ArrayList;

import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;
import std.deviation.managers.ResourceManager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class MeleeComponent implements SerializableComponent {

	public static final int SIZE = 128;
	private Animation[] animations;
	private int currentAni;
	private float elapsed = 0;
	public boolean attacking = false;
	public Rectangle hitBox;
	public int hits = 3;
	public ArrayList<Entity> alreadyHit;

	public MeleeComponent() {
		animations = new Animation[] { getAni("Slashing", 6),
				getAni("Slashing", 7), getAni("Slashing", 8),
				getAni("Slashing", 9), getAni("Slashing", 10),
				getAni("Slashing", 11) };
		hitBox = new Rectangle(0, 0, SIZE, SIZE);
		alreadyHit = new ArrayList<Entity>();
	}

	public boolean update(float delta) {
		elapsed += delta;
		if (animations[currentAni].isAnimationFinished(elapsed)) {
			currentAni = (int) (Math.random() * animations.length);
			elapsed = 0;
			hits = 3;
			attacking = false;
			alreadyHit.clear();
			return false;
		}
		return true;
	}

	public void render(Batch batch) {
		batch.draw(animations[currentAni].getKeyFrame(elapsed), hitBox.x,
				hitBox.y, SIZE, SIZE);
	}

	public Animation getAni(String type, int num) {
		TextureRegion[][] imageParts = TextureRegion
				.split(ResourceManager.get().getTexture(
						ResourceManager.get().loadTexture(
								"Textures/Battle_Animations/" + type + "/"
										+ type + " " + num + ".png")), 192, 192);
		TextureRegion[] frames = new TextureRegion[imageParts.length
		                                           * imageParts[0].length];
		for (int i = 0; i < imageParts.length; i++) {
			for (int j = 0; j < imageParts[0].length; j++) {
				frames[(i * imageParts[0].length + j)] = imageParts[i][j];
			}
		}
		return new Animation(.25f / frames.length, frames);
	}

	public boolean alreadyHit(Entity entity) {
		for (Entity e : alreadyHit) {
			if (e == entity) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Melee);
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE);
	}
}
