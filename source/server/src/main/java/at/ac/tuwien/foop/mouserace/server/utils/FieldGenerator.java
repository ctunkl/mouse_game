package at.ac.tuwien.foop.mouserace.server.utils;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.foop.mouserace.common.domain.Cell;
import at.ac.tuwien.foop.mouserace.common.domain.EmptyCell;
import at.ac.tuwien.foop.mouserace.common.domain.Field;
import at.ac.tuwien.foop.mouserace.common.domain.WallCell;

public class FieldGenerator {

	private List<String> rows;
	private int width;

	public FieldGenerator() {
		reset();
	}

	public void reset() {
		rows = new ArrayList<>();
	}

	public FieldGenerator row(String row) {
		if(rows.isEmpty()) {
			width = row.length();
		}

		if(validRow(row) == false) {
			throw new IllegalArgumentException("Invalid characters or length");
		}
		rows.add(row);
		return this;
	}

	private boolean validRow(String row) {
		return row.matches("[ x]+") && row.length() == width;
	}

	public Field build() {
		if(rows.isEmpty()) {
			throw new IllegalArgumentException("Found no field data");
		}

		final int WIDTH = rows.get(0).length();
		final int HEIGHT = rows.size();

		Field field = new Field(WIDTH, HEIGHT);
		String row;

		for(int y=0; y<HEIGHT; y++) {
			row = rows.get(y);

			for(int x=0; x<WIDTH; x++) {
				field.setCell(x, y, cellForCharacter(row.charAt(x)));
			}
		}

		return field;
	}

	private Cell cellForCharacter(char c) {
		switch(c) {
			case 'x':
				return new WallCell();
			default:
				return new EmptyCell();
		}
	}
}
