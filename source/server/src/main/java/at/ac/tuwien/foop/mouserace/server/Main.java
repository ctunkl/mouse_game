package at.ac.tuwien.foop.mouserace.server;

import at.ac.tuwien.foop.mouserace.common.domain.Game;
import at.ac.tuwien.foop.mouserace.common.domain.Wind;
import at.ac.tuwien.foop.mouserace.server.utils.Games;

/**
 * Created by klaus on 5/30/15.
 */
public class Main {
	public static void main(String[] args) {

		Game game = Games.createWithSimpleField(2);
		Wind wind = new Wind();
		wind.setSpeedX((byte)4);
		wind.setSpeedY((byte)0);
		game.setWind(wind);

		GameOptions options = new GameOptions();
		options.setTickMillis(300);
		options.setConfusedTime(4);

		GameEngine engine = new GameEngine(game, options);
		engine.startGame();
	}
}
