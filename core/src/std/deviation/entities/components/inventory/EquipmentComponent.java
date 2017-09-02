package std.deviation.entities.components.inventory;

import std.deviation.entities.serialization.SerializableComponent;

import com.badlogic.gdx.utils.Array;

public class EquipmentComponent implements SerializableComponent {
	public Array<InvSlot> items = new Array<InvSlot>(10);

	public boolean equip(InvSlot e) {

		return true;
	}

	@Override
	public int writeBytes(byte[] dest, int pointer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readBytes(byte[] src, int pointer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}
