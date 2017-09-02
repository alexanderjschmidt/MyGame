package std.deviation.items;

import java.util.HashMap;

public class ItemManager {

	private Item[] items;
	private HashMap<String, Item> itemMap;

	public static final short NUM_ITEMS = 256;

	private static ItemManager manager;

	public static synchronized ItemManager get() {
		if (manager == null) {
			manager = new ItemManager();
		}
		return manager;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private ItemManager() {
		itemMap = new HashMap<String, Item>(NUM_ITEMS);
		items = new Item[NUM_ITEMS];
		addItem((short) 32, "Bread", 10, 10, ItemType.Food, 0, 43);
		addItem((short) 33, "Cloth Chest", "Male_Top_10", "Female_Top_10", 9,
				6, ItemType.Top, 0, 32);
		addItem((short) 34, "Cloth Shoes", "Male_Feet_3", "Female_Feet_3", 8,
				4, ItemType.Boots, 1, 32);
		addItem((short) 35, "Cloth Hat", "Male_Head_1", "Female_Head_1", 7, 8,
				ItemType.Helmet, 2, 32);
		addItem((short) 36, "Cloth Cape", "Male_Accessory_1",
				"Female_Accessory_1", 6, 2, ItemType.Pants, 3, 32);
		addItem((short) 37, "Leather Chest", "Male_Top_2", "Female_Top_2", 5,
				1, ItemType.Top, 4, 32);
		addItem((short) 38, "Leather Shoes", "Male_Feet_2", "Female_Feet_2", 4,
				8, ItemType.Boots, 5, 32);
		addItem((short) 39, "Leather Hat", "Male_Head_2", "Female_Head_2", 3,
				9, ItemType.Helmet, 6, 32);
		addItem((short) 40, "Chain Chest", "Male_Top_1", "Female_Top_1", 2, 10,
				ItemType.Top, 7, 32);
		addItem((short) 41, "Chain Shoes", "Male_Feet_1", "Female_Feet_1", 1,
				4, ItemType.Boots, 8, 32);
		addItem((short) 42, "Chain Hat", "Male_Head_3", "Female_Head_3", 5, 3,
				ItemType.Helmet, 9, 32);
		addItem((short) 43, "Plate Chest", "Male_Top_20", "Female_Top_20", 7,
				10, ItemType.Top, 10, 32);
		addItem((short) 44, "Plate Shoes", "Male_Feet_5", "Female_Feet_5", 8,
				5, ItemType.Boots, 11, 32);
		addItem((short) 45, "Plate Hat", "Male_Head_4", "Female_Head_4", 9, 4,
				ItemType.Helmet, 12, 32);
		addItem((short) 45, "Plate Greaves", "Male_Pants_4", "Female_Pants_4",
				9, 4, ItemType.Pants, 12, 32);
	}

	private void addItem(short id, String name, int price, int weight,
			ItemType type, int x, int y) {
		items[id] = new Item(id, name, price, weight, type, x, y);
		itemMap.put(name, items[id]);
	}

	private void addItem(short id, String name, String malefolder,
			String femalefolder, int price, int weight, ItemType type, int x,
			int y) {
		items[id] = new EquippedableItem(id, name, malefolder, femalefolder,
				price, weight, type, x, y);
		itemMap.put(name, items[id]);
	}

	public Item get(String name) {
		return itemMap.get(name);
	}

	public Item get(short id) {
		return items[id];
	}

}
