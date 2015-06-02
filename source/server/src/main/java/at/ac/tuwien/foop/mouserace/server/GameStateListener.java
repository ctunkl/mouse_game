package at.ac.tuwien.foop.mouserace.server;

import at.ac.tuwien.foop.mouserace.common.domain.Field;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;

public interface GameStateListener {
	public void fieldChosen(Field field);
	public void currentState(GameState state);
	public void gameEnded(Mouse winner);
}
