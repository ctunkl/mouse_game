package at.ac.tuwien.foop.mouserace.server;

import java.util.Timer;
import java.util.TimerTask;

import at.ac.tuwien.foop.mouserace.common.domain.Game;

public class GameEngine {

	private Timer tick;

	private Game game;
	private GameOptions options;


	public GameEngine(Game game, GameOptions options) {
		this.game = game;
		this.options = options;

		tick = new Timer(this.getClass().getSimpleName());
	}

	public void startGame() {
		tick.schedule(new TimerTask() {

			@Override
			public void run() {
				GameEngine.this.play();

			}
		}, 0, options.getTickSeconds() * 1000);
	}

	/**
	 * Game loop
	 */
	public void play() {
		System.out.println("Tick!");
	}
}
