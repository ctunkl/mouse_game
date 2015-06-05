package at.ac.tuwien.foop.mouserace.common.domain;

import at.ac.tuwien.foop.mouserace.common.network.messages.FigureType;

/**
 * Created by klaus on 5/30/15.
 */
public class Cheese extends Figure {
	public Cheese(int id) {
		super(id);
	}

	@Override
	public FigureType getFigureType() {
		return FigureType.CHEESE;
	}
}
