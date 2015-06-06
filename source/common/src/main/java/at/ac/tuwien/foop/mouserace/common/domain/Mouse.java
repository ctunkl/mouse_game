package at.ac.tuwien.foop.mouserace.common.domain;

import at.ac.tuwien.foop.mouserace.common.network.messages.FigureType;

/**
 * Created by klaus on 5/30/15.
 */
public class Mouse extends Figure {
	private MouseState state;

	public Mouse(int id) {
		super(id);
		state = MouseState.NORMAL;
	}

	@Override
	public FigureType getFigureType() {
		return FigureType.MOUSE;
	}

	public MouseState getState() {
		return state;
	}

	public void setState(MouseState state) {
		this.state = state;
	}
}
