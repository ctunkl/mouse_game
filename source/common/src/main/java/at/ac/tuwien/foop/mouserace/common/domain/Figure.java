package at.ac.tuwien.foop.mouserace.common.domain;

import at.ac.tuwien.foop.mouserace.common.network.messages.FigureType;

/**
 * Created by klaus on 5/30/15.
 */
public abstract class Figure {
	public static final int MAX_ID = 0xFF;

	private final int id;
	private int x;
	private int y;

	public Figure(int id) {
		if(id < 0 || id > MAX_ID)
			throw new IllegalArgumentException(String.format("id must be positive and lower than or equal %d", MAX_ID));

		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
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
