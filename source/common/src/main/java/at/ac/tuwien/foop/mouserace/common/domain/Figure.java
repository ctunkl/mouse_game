package at.ac.tuwien.foop.mouserace.common.domain;

import at.ac.tuwien.foop.mouserace.common.network.messages.FigureType;

/**
 * Created by klaus on 5/30/15.
 */
public abstract class Figure {

	private final int id;
	private int x;
	private int y;

	public Figure(int id) {
		if(id < 0 || id > Limits.FIGURE_MAX_ID)
			throw new IllegalArgumentException(String.format("id must be positive and lower than or equal %d", Limits.FIGURE_MAX_ID));

		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		if(x < 0 || x > Limits.FIGURE_MAX_X)
			throw new IllegalArgumentException(String.format("x must be positive and lower than or equal %d", Limits.FIGURE_MAX_X));

		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		if(y < 0 || y > Limits.FIGURE_MAX_Y)
			throw new IllegalArgumentException(String.format("y must be positive and lower than or equal %d", Limits.FIGURE_MAX_Y));

		this.y = y;
	}

	public abstract FigureType getFigureType();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Figure)) return false;

		Figure figure = (Figure) o;

		return id == figure.id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
