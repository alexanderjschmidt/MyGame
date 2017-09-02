package std.deviation.entities.serialization;

import com.badlogic.ashley.core.Component;

public interface SerializableComponent extends Component {
	public int writeBytes(byte[] dest, int pointer);

	public int readBytes(byte[] src, int pointer);

	public int getSize();
}
