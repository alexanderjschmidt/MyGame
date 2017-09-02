package std.deviation.gameworld;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class GridRenderer {

	public static final int currentSize = 16;

	public int[][] trueClearance;
	private TiledMapTileLayer collision;

	public void update(TiledMapTileLayer tiledMapTileLayer, int mapWidth, int mapHeight) {
		this.collision = tiledMapTileLayer;
		trueClearance = new int[mapWidth / 16][mapHeight / 16];

		for (int x = 0; x < trueClearance.length; x++) {
			for (int y = 0; y < trueClearance[x].length; y++) {
				Cell c = collision.getCell(x, y);
				if (c != null && c.getTile() != null) {
					trueClearance[x][y] = 0;
				}
				else {
					trueClearance[x][y] = 1;
				}
			}
		}

		for (int x = 0; x < trueClearance.length; x++) {
			for (int y = 0; y < trueClearance[x].length; y++) {
				if (trueClearance[x][y] == 0)
					continue;
				boolean solidFound = false;
				int searchDistance = 0;
				while (!solidFound) {
					searchDistance++;
					for (int i = 0; i < searchDistance; i++) {
						if (i + x >= trueClearance.length || searchDistance + y >= trueClearance[x].length || i + y >= trueClearance[x].length || searchDistance + x >= trueClearance.length
							|| (trueClearance[x + i][y + searchDistance] == 0) || (trueClearance[x + searchDistance][y + i] == 0)) {
							solidFound = true;
							break;
						}
					}
				}
				trueClearance[x][y] = searchDistance;
			}
		}

	}

}
