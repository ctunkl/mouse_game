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
}
