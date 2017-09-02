package std.deviation.entities.components.renderComponents;

import java.util.HashMap;

import std.deviation.entities.components.renderComponents.renderParts.AnimationPart;
import std.deviation.entities.components.renderComponents.renderParts.RenderPart;
import std.deviation.entities.components.renderComponents.renderParts.TexturePart;
import std.deviation.managers.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager extends HashMap<String, RenderPart> {

	private static final long serialVersionUID = 1L;
	public String folder = null;
	public String pathBase = "Textures/Objects/";

	// For Faces
	public void addTexture(String name, String[] possiblePathNames) {
		String base = pathBase + folder + "/" + folder;
		String normal = null;
		for (int i = 0; i < possiblePathNames.length; i++) {
			normal = base + "_" + possiblePathNames[i] + ".png";
			if (Gdx.files.internal(normal).exists()) {
				break;
			}
		}
		Texture tex = ResourceManager.get().getTexture(
				ResourceManager.get().loadTexture(normal));
		if (tex != null)
			put(name, new TexturePart(new TextureRegion(tex)));
	}

	// Directional Movement
	public void addSet(String name, String[] possiblePathNames, byte length,
			float duration) {
		String base = pathBase + folder + "/" + folder;
		String normal = null;
		String diagonal = null;
		for (int i = 0; i < possiblePathNames.length; i++) {
			normal = base + "_" + possiblePathNames[i] + ".png";
			if (Gdx.files.internal(normal).exists()) {
				diagonal = base + "_" + possiblePathNames[i] + "_diag.png";
				break;
			}
		}
		if (diagonal == null)
			return;

		loadRowAni(normal, name + "NORTH", length, (byte) 4, (byte) 3, duration);
		loadRowAni(normal, name + "SOUTH", length, (byte) 4, (byte) 0, duration);
		loadRowAni(normal, name + "WEST", length, (byte) 4, (byte) 1, duration);
		loadRowAni(normal, name + "EAST", length, (byte) 4, (byte) 2, duration);
		if (Gdx.files.internal(diagonal).exists()) {
			loadRowAni(diagonal, name + "NORTHWEST", length, (byte) 4,
					(byte) 1, duration);
			loadRowAni(diagonal, name + "SOUTHEAST", length, (byte) 4,
					(byte) 2, duration);
			loadRowAni(diagonal, name + "SOUTHWEST", length, (byte) 4,
					(byte) 0, duration);
			loadRowAni(diagonal, name + "NORTHEAST", length, (byte) 4,
					(byte) 3, duration);
		}
	}

	public void addSet(String name, byte length, float duration) {
		this.addSet(name, new String[] { name }, length, duration);
	}

	// Dead images
	public int addDeadSet(String name, byte column, byte num) {
		String base = pathBase + folder + "/" + folder;
		String normal = base + "_" + name + ".png";
		if (!Gdx.files.internal(normal).exists()) {
			return 0;
		}
		String diagonal = base + "_" + name + "_diag.png";
		loadTexture(normal, "dead" + num + "NORTH", (byte) 3, (byte) 4, column,
				(byte) 3);
		loadTexture(normal, "dead" + num + "SOUTH", (byte) 3, (byte) 4, column,
				(byte) 0);
		loadTexture(normal, "dead" + num + "WEST", (byte) 3, (byte) 4, column,
				(byte) 1);
		loadTexture(normal, "dead" + num + "EAST", (byte) 3, (byte) 4, column,
				(byte) 2);
		if (Gdx.files.internal(diagonal).exists()) {
			loadTexture(diagonal, "dead" + num + "NORTHWEST", (byte) 3,
					(byte) 4, column, (byte) 1);
			loadTexture(diagonal, "dead" + num + "SOUTHEAST", (byte) 3,
					(byte) 4, column, (byte) 2);
			loadTexture(diagonal, "dead" + num + "SOUTHWEST", (byte) 3,
					(byte) 4, column, (byte) 0);
			loadTexture(diagonal, "dead" + num + "NORTHEAST", (byte) 3,
					(byte) 4, column, (byte) 3);
		}
		return 1;
	}

	protected void loadTexture(String path, String name, byte numWide,
			byte numHigh, byte column, byte row) {
		Texture tex = ResourceManager.get().getTexture(
				ResourceManager.get().loadTexture(path));
		int w = tex.getWidth() / numWide;
		int h = tex.getHeight() / numHigh;
		put(name, new TexturePart(new TextureRegion(tex, w * column, h * row,
				w, h)));
	}

	// Single static Image
	protected void loadImage(String path, String name, byte width, byte height,
			byte column, byte row) {
		Texture tex = ResourceManager.get().getTexture(
				ResourceManager.get().loadTexture(path));
		put(name, new TexturePart(new TextureRegion(tex, row * 48, column * 48,
				width * 48, height * 48)));
	}

	// Single Animation
	protected void loadRowAni(String path, String aniName, byte numWide,
			byte numHigh, byte rowX, float delay) {
		int[] x = new int[numWide];
		int[] y = new int[numWide];
		for (int i = 0; i < numWide; i++) {
			x[i] = rowX;
			y[i] = i;
		}
		loadAnimation(path, aniName, delay, numWide, numHigh, x, y);
	}

	protected void loadColAni(String path, String aniName, byte numWide,
			byte numHigh, byte colY, float delay) {
		int[] x = new int[numHigh];
		int[] y = new int[numHigh];
		for (int i = 0; i < numHigh; i++) {
			x[i] = i;
			y[i] = colY;
		}
		loadAnimation(path, aniName, delay, numWide, numHigh, x, y);
	}

	private void loadAnimation(String path, String aniName, float delay,
			byte numWide, byte numHigh, int[] x, int[] y) {
		ResourceManager.get().loadTexture(path);
		Texture tex = ResourceManager.get().getTexture(path);
		TextureRegion[][] region = TextureRegion.split(tex, tex.getWidth()
				/ numWide, tex.getHeight() / numHigh);

		TextureRegion[] frames = new TextureRegion[x.length];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = region[x[i]][y[i]];
		}

		put(aniName, new AnimationPart(new Animation(delay, frames)));
	}

}
