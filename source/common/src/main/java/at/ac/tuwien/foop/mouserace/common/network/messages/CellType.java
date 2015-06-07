package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.domain.Cell;
import at.ac.tuwien.foop.mouserace.common.domain.EmptyCell;
import at.ac.tuwien.foop.mouserace.common.domain.EntryCell;
import at.ac.tuwien.foop.mouserace.common.domain.WallCell;

/**
 * Created by klaus on 6/5/15.
 */
public enum CellType {
	WALL((byte) 0x01, WallCell.class),
	ENTRY((byte) 0x02, EntryCell.class),
	EMPTY((byte) 0x03, EmptyCell.class);

	private byte value;
	private Class<? extends Cell> clazz;

	CellType(byte value, Class<? extends Cell> clazz) {
		this.value = value;
		this.clazz = clazz;
	}

	public static CellType fromByte(byte data) throws IllegalArgumentException {
		for (CellType cellType : CellType.values()) {
			if (cellType.value == data)
				return cellType;
		}
		throw new IllegalArgumentException(String.format("Unknown cell type 0x%02X", data));
	}

	public byte getValue() {
		return this.value;
	}

	public Class getClazz() {
		return clazz;
	}
}
