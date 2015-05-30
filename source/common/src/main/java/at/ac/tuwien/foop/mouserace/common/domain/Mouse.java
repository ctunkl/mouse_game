package at.ac.tuwien.foop.mouserace.common.domain;

/**
 * Created by klaus on 5/30/15.
 */
public class Mouse extends Figure {
	private MouseState state;

	public Mouse(int id) {
		super(id);
		state = MouseState.NORMAL;
	}

	public MouseState getState() {
		return state;
	}

	public void setState(MouseState state) {
		this.state = state;
	}
}
