package at.ac.tuwien.foop.mouserace.common.domain;

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
	 * This setter must not be invoked by other classes than {@link Field}.
	 * @param x the width index in the interval [0, width-1]
	 * @param y the height index in the interval [0, height-1]
	 */
	protected void setFieldPosition(int x, int y) {
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
}
