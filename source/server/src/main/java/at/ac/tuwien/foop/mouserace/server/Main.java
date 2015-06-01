package at.ac.tuwien.foop.mouserace.server;

import at.ac.tuwien.foop.mouserace.common.domain.Game;
import at.ac.tuwien.foop.mouserace.server.utils.Games;

/**
 * Created by klaus on 5/30/15.
 */
public class Main {
	public static void main(String[] args) {

		Game game = Games.createWithComplexField(2);
		GameOptions options = new GameOptions();
		options.setTickMillis(100);
		options.setConfusedTime(10);

		GameEngine engine = new GameEngine(game, options);
		engine.startGame();
	}
}
