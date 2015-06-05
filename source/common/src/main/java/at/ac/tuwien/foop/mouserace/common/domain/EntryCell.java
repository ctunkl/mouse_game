package at.ac.tuwien.foop.mouserace.common.domain;

import at.ac.tuwien.foop.mouserace.common.network.messages.CellType;

/**
 * Created by klaus on 5/30/15.
 */
public class EntryCell extends Cell {
	@Override
	public CellType getCellType() {
		return CellType.ENTRY;
	}
}
