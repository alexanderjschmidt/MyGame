package std.deviation.entities.components.inventory;

import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;
import std.deviation.items.EquippedableItem;
import std.deviation.items.Item;
import std.deviation.items.ItemManager;
import std.deviation.managers.ResourceManager;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class InvSlot extends Table {

	private Item item;
	private boolean equipped;
	private boolean favorite;
	private short stackSize;
	// int between 1 and 100;
	private byte qualityValue;
	private Image icon;
	private Label name, stat, weight, value;

	public InvSlot(Item item, boolean equipped, boolean favorite,
			short stackSize, byte qualityValue) {
		this.item = item;
		this.equipped = equipped;
		this.favorite = favorite;
		this.stackSize = stackSize;
		this.qualityValue = qualityValue;
		createTable();
	}

	public InvSlot() {
	}

	public void createTable() {
		this.qualityValue = qualityValue > 100 ? 100 : (qualityValue < 1 ? 1
				: qualityValue);
		icon = new Image(item.sprite);
		name = new Label("", ResourceManager.get().skin);
		stat = new Label(item.getWeight() + "", ResourceManager.get().skin);
		weight = new Label(item.getWeight() + "", ResourceManager.get().skin);
		value = new Label(item.getPrice() + "", ResourceManager.get().skin);

		this.setSize(500, 32);
		this.add(icon).padRight(10).width(32).height(24);
		this.add(name).padRight(10).width(200);
		this.add(stat).width(70);
		this.add(weight).width(70);
		this.add(value).width(70);

		update();
	}

	public InvSlot getOne() {
		stackSize--;
		return new InvSlot(item, equipped, favorite, (short) 1, qualityValue);
	}

	public void update() {
		name.setText(item.toString()
				+ (stackSize > 1 ? " (" + stackSize + ")" : "")
				+ (favorite ? " *" : "") + (equipped ? " (e)" : ""));
	}

	public void toggleFavorite() {
		favorite = !favorite;
	}

	public void toggleEquipped() {
		if (item.getClass().equals(EquippedableItem.class))
			equipped = !equipped;
	}

	public Item getItem() {
		return item;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public short getStackSize() {
		return stackSize;
	}

	public void remove(int num) {
		stackSize -= num;
	}

	public void add(int num) {
		stackSize += num;
	}

	public byte getQuality() {
		return qualityValue;
	}

	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer, item.id);
		byte bools = (byte) ((favorite ? 2 : 0) + (equipped ? 1 : 0));
		pointer = SerializationUtils.writeBytes(dest, pointer, bools);
		pointer = SerializationUtils.writeBytes(dest, pointer, stackSize);
		pointer = SerializationUtils.writeBytes(dest, pointer, qualityValue);
		return pointer;
	}

	public int readBytes(byte[] src, int pointer) {
		short id = SerializationUtils.readShort(src, pointer);
		item = ItemManager.get().get(id);
		pointer += Type.getSize(Type.SHORT);
		byte bools = SerializationUtils.readByte(src, pointer);
		equipped = (bools & 1) == 1;
		favorite = ((bools >> 1) & 1) == 1;
		pointer += Type.getSize(Type.BYTE);
		stackSize = SerializationUtils.readShort(src, pointer);
		pointer += Type.getSize(Type.SHORT);
		qualityValue = SerializationUtils.readByte(src, pointer);
		pointer += Type.getSize(Type.BYTE);
		createTable();

		return pointer;
	}
}