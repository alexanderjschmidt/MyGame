package std.deviation.entities.components.inventory;

import java.util.ArrayList;

import std.deviation.entities.serialization.ComponentType;
import std.deviation.entities.serialization.SerializableComponent;
import std.deviation.entities.serialization.SerializationUtils;
import std.deviation.entities.serialization.Type;
import std.deviation.items.Item;

public class InventoryComponent extends ArrayList<InvSlot> implements
		SerializableComponent {

	private static final long serialVersionUID = 1L;
	public int gold;

	private int indexOf(Item key) {
		int lo = 0;
		int hi = size() - 1;
		while (lo <= hi) {
			// Key is in a[lo..hi] or not present.
			int mid = lo + (hi - lo) / 2;
			Item midItem = get(mid).getItem();
			if (key.compareTo(midItem) < 0)
				hi = mid - 1;
			else if (key.compareTo(midItem) > 0)
				lo = mid + 1;
			else
				return mid;
		}
		return lo;
	}

	@Override
	public boolean add(InvSlot i) {
		int index = indexOf(i.getItem());
		if (size() == 0 || index == size() || index == -1) {
			return super.add(i);
		} else if (i.getItem().equals(get(index).getItem())) {
			get(index).add(i.getStackSize());
		} else {
			super.add(index, i);
		}
		return true;
	}

	public boolean add(Item item, short num, boolean equip, boolean fav) {
		return add(new InvSlot(item, equip, fav, num, (byte) 1));
	}

	@Override
	public InvSlot remove(int index) {
		return remove(index, get(index).getStackSize());
	}

	public InvSlot remove(int index, short num) {
		InvSlot slot = get(index);
		if (slot.getStackSize() > num) {
			slot.remove(num);
		} else if (slot.getStackSize() == num) {
			super.remove(index);
		}

		return new InvSlot(slot.getItem(), false, false, num, slot.getQuality());
	}

	public String getData() {
		String info = gold + ",";
		for (InvSlot i : this) {
			info += i.getItem().toString() + ">" + i.getStackSize() + ",";
		}
		return info.substring(0, info.length() - 1);
	}

	public boolean contains(Item i, int numOfItem) {
		for (InvSlot item : this) {
			if (item.getItem() == i && item.getStackSize() >= numOfItem) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		pointer = SerializationUtils.writeBytes(dest, pointer,
				ComponentType.Inventory);
		pointer = SerializationUtils.writeBytes(dest, pointer, gold);
		pointer = SerializationUtils.writeBytes(dest, pointer, this.size());
		for (InvSlot i : this) {
			pointer = i.writeBytes(dest, pointer);
		}
		return pointer;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		pointer += Type.getSize(Type.BYTE);
		gold = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		int size = SerializationUtils.readInt(src, pointer);
		pointer += Type.getSize(Type.INTEGER);
		for (int i = 0; i < size; i++) {
			InvSlot item = new InvSlot();
			pointer = item.readBytes(src, pointer);
			this.add(item);
		}
		return pointer;
	}

	@Override
	public int getSize() {
		return Type.getSize(Type.BYTE) + Type.getSize(Type.INTEGER)
				+ Type.getSize(Type.INTEGER) + size() * 6;
	}

}
