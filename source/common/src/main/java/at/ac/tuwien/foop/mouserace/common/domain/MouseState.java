package at.ac.tuwien.foop.mouserace.common.domain;

/**
 * Created by klaus on 5/30/15.
 */
public enum MouseState {
	NORMAL((byte) 0x01),
	CONFUSED((byte) 0x02),
	SNIFFING((byte) 0x03);

	private byte value;

	MouseState(byte value) {
		this.value = value;
	}

	public static MouseState fromByte(byte data) throws IllegalArgumentException {
		for (MouseState m : MouseState.values()) {
			if (m.value == data)
				return m;
		}
		throw new IllegalArgumentException(String.format("Unknown mouse state 0x%02X", data));
	}

	public byte getValue() {
		return this.value;
	}
}
