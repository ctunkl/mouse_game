package at.ac.tuwien.foop.mouserace.common.domain;

import at.ac.tuwien.foop.mouserace.common.network.messages.CellType;
import at.ac.tuwien.foop.mouserace.common.network.messages.FigureType;

/**
 * Created by klaus on 5/30/15.
 */
public abstract class Cell {
	private Integer x;
	private Integer y;

	/**
	 * This method gets the x coordinate of the cell in the game field.
	 * @return the width index in the interval [0, width-1] or <code>null</code> if the Cell is not
	 * associated with game field.
	 */
	public Integer getX() {
		return x;
	}

	/**
	 * This method gets the y coordinate of the cell in the game field.
	 * @return the height index in the interval [0, height-1] or <code>null</code> if the Cell is not
	 * associated with game field.
	 */
	public Integer getY() {
		return y;
	}

	/**
	 * This setter must not be invoked by a class other than {@link Field}.
	 * @param x the width index in the interval [0, width-1]
	 * @param y the height index in the interval [0, height-1]
	 */
	protected void setFieldPosition(int x, int y) {
		if(x < 0 || x > Limits.CELL_MAX_X)
			throw new IllegalArgumentException(String.format("x must be positive and lower than or equal %d", Limits.CELL_MAX_X));
		if(y < 0 || y > Limits.CELL_MAX_Y)
			throw new IllegalArgumentException(String.format("y must be positive and lower than or equal %d", Limits.CELL_MAX_Y));

		this.x = x;
		this.y = y;
	}

	/**
	 * This setter must not be invoked by other classes than {@link Field}.
	 */
	protected void unsetFieldPosition() {
		this.x = null;
		this.y = null;
	}

	/**
	 * Determines if this cell is currently positioned on a game field.
	 *
	 * @return true if and only if both position member fields (x, y) are set.
	 */
	public boolean isOnField() {
		return this.x != null && this.y != null;
	}

	public abstract CellType getCellType();

	public static Cell fromCellType(CellType cellType) {
		switch (cellType) {
			case WALL:
				return new WallCell();
			case ENTRY:
				return new EntryCell();
			case EMPTY:
				return new EmptyCell();
			default:
				throw new IllegalArgumentException(String.format("Unknown cell type %s", cellType.name()));
		}
	}
}
