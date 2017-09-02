package std.deviation.gameworld;

import std.deviation.screen.Play;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapRenderer extends OrthogonalTiledMapRenderer {

	private Play screen;

	private final GridRenderer grid;

	private int width, height;

	public MapRenderer(Play screen) {
		super(new TiledMap(), screen.getApp().getBatch());
		this.screen = screen;
		this.setView(screen.getApp().getCamera());
		grid = new GridRenderer();

		load("PlayerHome");
	}

	public void load(String name) {
		this.setMap(new TmxMapLoader().load("ConstantData/Maps/" + name + "/" + name + ".tmx"));
		width = this.getMap().getProperties().get("width", Integer.class) * GridRenderer.currentSize;
		height = this.getMap().getProperties().get("height", Integer.class) * GridRenderer.currentSize;
		getGrid().update((TiledMapTileLayer) map.getLayers().get("Collision"), width, height);
	}

	public void render(float delta) {
		beginRender();
		map.getLayers().get("Collision").setVisible(screen.DEVMODE);
		for (MapLayer layer : map.getLayers()) {
			if (layer.isVisible()) {
				if (layer.getClass().equals(TiledMapTileLayer.class)) {
					renderTileLayer((TiledMapTileLayer) layer);
				}
				else if (layer.getClass().equals(TiledMapImageLayer.class)) {
					renderImageLayer((TiledMapImageLayer) layer);
				}
				else {
					renderObjects(layer);
				}
			}
		}
		endRender();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public GridRenderer getGrid() {
		return grid;
	}

}
