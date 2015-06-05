package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.domain.Cheese;
import at.ac.tuwien.foop.mouserace.common.domain.Figure;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;

/**
 * Created by klaus on 6/5/15.
 */
public enum FigureType {
	MOUSE((byte) 0x01, Mouse.class),
	CHEESE((byte) 0x02, Cheese.class);

	private byte value;
	private Class<? extends Figure> clazz;

	FigureType(byte value, Class<? extends Figure> clazz) {
		this.value = value;
		this.clazz = clazz;
	}

	public static FigureType fromByte(byte data) throws IllegalArgumentException {
		for (FigureType figureType : FigureType.values()) {
			if (figureType.value == data)
				return figureType;
		}
		throw new IllegalArgumentException(String.format("Unknown figure type 0x%02X", data));
	}

	public byte getValue() {
		return this.value;
	}

	public Class getClazz() {
		return clazz;
	}
}
