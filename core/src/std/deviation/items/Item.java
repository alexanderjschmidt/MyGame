package std.deviation.items;

import std.deviation.managers.ResourceManager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Item implements Comparable<Item> {

	public short id;
	private String name;

	private int price;
	private int weight;
	public Sprite sprite;

	public ItemType type;

	public Item(short id, String name, int price, int weight, ItemType type,
			int x, int y) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.weight = weight;
		// if (x != -1 && y != -1)
		// sprite = new Sprite(ResourceManager.get().sprites[y][x]);
		// else
		sprite = new Sprite(ResourceManager.get().ui[0][3]);

		this.type = type;
	}

	@Override
	public String toString() {
		return name;
	}

	public int getWeight() {
		return weight;
	}

	public int getPrice() {
		return price;
	}

	public void render(Batch batch, float f, float g) {
		sprite.setPosition(f, g);
		sprite.draw(batch);
	}

	@Override
	public int compareTo(Item arg0) {
		return name.compareTo(arg0.name);
	}

}
