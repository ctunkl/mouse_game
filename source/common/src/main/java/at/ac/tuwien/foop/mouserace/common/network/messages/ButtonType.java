package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.domain.Cell;
import at.ac.tuwien.foop.mouserace.common.domain.EmptyCell;
import at.ac.tuwien.foop.mouserace.common.domain.EntryCell;
import at.ac.tuwien.foop.mouserace.common.domain.WallCell;

/**
 * Created by klaus on 6/5/15.
 */
public enum ButtonType {
	LEFT((byte) 0x01),
	UP((byte) 0x02),
	RIGHT((byte) 0x03),
	DOWN((byte) 0x04);

	private byte value;

	ButtonType(byte value) {
		this.value = value;
	}

	public static ButtonType fromByte(byte data) throws IllegalArgumentException {
		for (ButtonType buttonType : ButtonType.values()) {
			if (buttonType.value == data)
				return buttonType;
		}
		throw new IllegalArgumentException(String.format("Unknown button type 0x%02X", data));
	}

	public byte getValue() {
		return this.value;
	}
}
