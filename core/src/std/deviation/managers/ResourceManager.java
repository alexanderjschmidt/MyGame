package std.deviation.managers;

import java.util.HashMap;
import java.util.Set;

import std.deviation.gameworld.Fog;
import std.deviation.hud.InventoryMenu;
import std.deviation.hud.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourceManager extends AssetManager {

	private FreeTypeFontGenerator generator;
	private BitmapFont font12;
	private BitmapFont font24;
	private BitmapFont font36;
	private BitmapFont font72;
	public Skin skin;
	public TextureRegion[][] ui;

	public TextureRegion[][] sprites;
	public TextureRegion[][] inventoryIcons;

	public HashMap<String, Float> textures;

	private static ResourceManager manager;

	public static synchronized ResourceManager get() {
		if (manager == null) {
			manager = new ResourceManager();
		}
		return manager;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private ResourceManager() {
		textures = new HashMap<String, Float>();
		initFonts();
		loadSkin("uiskin");
		loadTexture("Textures/UI/cursor.png");
		ui = TextureRegion.split(getTexture("Textures/UI/cursor.png"), 64, 64);
		loadTexture(MainMenu.path);
		loadTexture(InventoryMenu.path);
		loadTexture("Textures/UI/TalkingBackground.png");
		// loadTexture(Equipment.path);
		loadTexture(Fog.path);
		loadTexture("Textures/splash/badlogic.jpg");
		loadTexture("Textures/splash/pvglogo.png");
		loadTexture("Textures/splash/aschmidtlogo.png");
		loadTexture("Textures/UI/Background.png");

		sprites = TextureRegion.split(
				getTexture(loadTexture("ConstantData/IconSet.png")), 24, 24);
		inventoryIcons = TextureRegion.split(
				getTexture(loadTexture("Textures/UI/icons.png")), 50, 50);
	}

	public void update(float delta) {
		Set<String> set = textures.keySet();
		for (String key : set) {
			float time = textures.get(key);
			if (time == -1)
				continue;
			time -= delta;
			if (time < 0) {
				this.unload(key);
				textures.remove(key);
			} else {
				textures.put(key, time);
			}
		}
	}

	private void loadSkin(String skinName) {
		load("skin/" + skinName + "/" + skinName + ".atlas", TextureAtlas.class);
		finishLoadingAsset("skin/" + skinName + "/" + skinName + ".atlas");
		skin = new Skin();
		skin.addRegions(get("skin/" + skinName + "/" + skinName + ".atlas",
				TextureAtlas.class));
		skin.add("default-font", font24);
		skin.add("title", font72);
		skin.load(Gdx.files.internal("skin/" + skinName + "/" + skinName
				+ ".json"));
	}

	private void initFonts() {
		generator = new FreeTypeFontGenerator(
				Gdx.files.internal("skin/Gabrielle.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();

		parameter.color = Color.WHITE;

		parameter.size = 12;
		font12 = generator.generateFont(parameter);
		parameter.size = 24;
		font24 = generator.generateFont(parameter);
		parameter.size = 36;
		font36 = generator.generateFont(parameter);
		parameter.size = 72;
		font72 = generator.generateFont(parameter);
	}

	public void load() {
		while (!update()) {
			System.out.println("Loaded: " + getProgress() * 100 + "%");
		}
	}

	public String loadTexture(String path) {
		if (isLoaded(path))
			return path;
		if (Gdx.files.internal(path).exists()) {
			load(path, Texture.class);
		}
		// manager.finishLoading();
		return path;
	}

	public String loadPixmap(String path) {
		load(path, Pixmap.class);
		// manager.finishLoading();
		return path;
	}

	// could cause issues from creating file handles over and over
	public Texture getTexture(String path) {
		if (!Gdx.files.internal(path).exists()) {
			return null;
		}
		if (!isLoaded(path)) {
			this.finishLoadingAsset(path);
		}
		textures.put(path, 180f);
		return get(path, Texture.class);
	}

	public Pixmap getPixmap(String path) {
		if (!isLoaded(path)) {
			finishLoadingAsset(path);
		}
		return get(path, Pixmap.class);
	}

	public Boolean isLoaded() {
		if (getProgress() >= 1)
			return true;
		return false;
	}

	@Override
	public void dispose() {
		super.dispose();
		generator.dispose();
		font12.dispose();
		font24.dispose();
		font36.dispose();
		font72.dispose();
	}

}
