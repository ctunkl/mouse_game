package at.ac.tuwien.foop.mouserace.common.domain;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by klaus on 5/30/15.
 */
public class Field {
	public static final int MIN_SIZE = 3;

	private final Cell[][] cells;

	/**
	 * Constructs a new field with specified width and height.
	 * @param width the game field width
	 * @param height the game field height
	 */
	public Field(int width, int height) {
		if(width < MIN_SIZE || height < MIN_SIZE)
			throw new IllegalArgumentException(String.format("Both width and height have to be greater than %d", MIN_SIZE));

		cells = new Cell[width][height];
	}

	/**
	 * Sets the cell on position (x, y).
	 * @param x the width index in the interval [0, width-1]
	 * @param y the height index in the interval [0, height-1]
	 * @param cell the cell which will be set on the specified position
	 */
	public void setCell(int x, int y, Cell cell) {
		if(!checkPosition(x, y))
			throw new IllegalArgumentException("Either x or y is out of range");

		cells[x][y] = Objects.requireNonNull(cell);
	}

	public Cell getCell(int x, int y) {
		if(!checkPosition(x, y))
			throw new IllegalArgumentException("Either x or y is out of range");

		return cells[x][y];
	}

	/**
	 * Checks whether the position indexed by x and y is in the range of the field.
	 * @param x the width index in the interval [0, width-1]
	 * @param y the height index in the interval [0, height-1]
	 * @return true, if and only if x and y are valid indices in the field.
	 */
	private boolean checkPosition(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}

	public int getWidth() {
		return cells.length;
	}

	public int getHeight() {
		if(cells[0] == null)
			throw new IllegalStateException("The game field is not a valid two dimensional field");

		return cells[0].length;
	}

	public boolean isFieldComplete() {
		return Arrays.stream(cells).flatMap(Arrays::stream).allMatch((cell) -> cell != null);
	}

	/**
	 * Checks if the field is a valid game field and the cells are all placed correctly.
	 * @return True, if and only if the field is valid.
	 */
	public boolean isFieldValid() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				final Cell cell = getCell(x, y);

				if(cell == null)
					return false;

				// Check if edge cells are either entry cells or wall cells
				if(x == 0 || x >= getWidth()-1 || y == 0 || y >= getHeight()-1) {
					// We're somewhere on the edge

					if(!(cell instanceof EntryCell || cell instanceof WallCell))
						return false;
				}

				// TODO Add further checks
			}
		}

		return true;
	}
}
