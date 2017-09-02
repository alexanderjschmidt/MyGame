package std.deviation.items;

public enum ItemType {
	Pants(ItemCategory.Apparel), Top(ItemCategory.Apparel), Helmet(
			ItemCategory.Apparel), Gloves(ItemCategory.Apparel), Boots(
			ItemCategory.Apparel), Shield(ItemCategory.Weapons), OneHandedWeapon(
			ItemCategory.Weapons), TwoHandedWeapon(ItemCategory.Weapons), Potion(
			ItemCategory.Consumable), Food(ItemCategory.Consumable), Ingrediant(
			ItemCategory.Consumable), Material(ItemCategory.Misc);

	public ItemCategory category;

	private ItemType(ItemCategory c) {
		category = c;
	}
}