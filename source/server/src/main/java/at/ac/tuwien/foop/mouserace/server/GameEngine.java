package at.ac.tuwien.foop.mouserace.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import at.ac.tuwien.foop.mouserace.common.domain.Cell;
import at.ac.tuwien.foop.mouserace.common.domain.Cheese;
import at.ac.tuwien.foop.mouserace.common.domain.EmptyCell;
import at.ac.tuwien.foop.mouserace.common.domain.Field;
import at.ac.tuwien.foop.mouserace.common.domain.Figure;
import at.ac.tuwien.foop.mouserace.common.domain.Game;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.common.domain.MouseState;
import at.ac.tuwien.foop.mouserace.common.domain.Wind;
import at.ac.tuwien.foop.mouserace.server.utils.Tuple;

public class GameEngine {

	private Timer tick;

	private Game game;
	private GameOptions options;
	private List<Mouse> mice;
	private Cheese cheese;

	// for each mouse: how many ticks will it keep sniffing
	private Map<Mouse, Integer> sniffingMice;

	public GameEngine(Game game, GameOptions options) {
		this.game = game;
		this.options = options;

		mice = new ArrayList<>();
		for(Figure f : game.getFigures()) {
			if(f instanceof Mouse) {
				mice.add((Mouse) f);
			}
			if(f instanceof Cheese) {
				this.cheese = (Cheese) f;
			}
		}

		sniffingMice = new HashMap<>();
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

		// TODO receive key presses from players
		List<Direction> directions = Arrays.asList(Direction.UP, Direction.UP, Direction.LEFT,
						Direction.LEFT, Direction.DOWN, Direction.RIGHT);

		Wind wind = calculateWind(directions);
		game.setWind(wind);

		for(Mouse m : this.mice) {
			moveMouse(game.getField(), m, this.cheese);
		}
	}

	private void moveMouse(Field f, Mouse m, Cheese c) {
		//List<Cell> next = emptyNeighbours(f, m.getX(), m.getY());

		if(m.getState().equals(MouseState.SNIFFING)) {
			int mouseIdleTicks = sniffingMice.getOrDefault(m, 0);

			if(mouseIdleTicks == 0) {
				m.setState(MouseState.NORMAL);
				System.out.println(String.format("  Mouse %d: Back to normal.", m.getId()));
			} else {
				sniffingMice.put(m,  mouseIdleTicks-1);
				System.out.println(String.format("  Mouse %d: Gonna keep sniffing for %s more ticks.", m.getId(), mouseIdleTicks-1));
			}
		}

		if(m.getState().equals(MouseState.NORMAL)) {
			// TODO pathfinding

			Random r = new Random();
			int nx = m.getX();
			int ny = m.getY();

			if(r.nextBoolean())
				nx = m.getX() + (r.nextBoolean() ? 1 : -1);
			else
				ny = m.getY()+ (r.nextBoolean() ? 1 : -1);

			System.out.println(String.format("  Mouse %d: Moving from (%d,%d) to (%d,%d)",
					m.getId(), m.getX(), m.getY(), nx, ny));

			m.setX(nx);
			m.setY(ny);
		}
	}

	/**
	 *  Calculate new wind based on player input
	 */
	public Wind calculateWind(Collection<Direction> directions) {
		Wind wind = new Wind();

		// count how many players voted for each direction
		Map<Direction, Long> counted = directions.stream().collect(
				Collectors.groupingBy(o -> o, Collectors.counting()));

		int up = counted.getOrDefault(Direction.UP, 0L).intValue();
		int down = counted.getOrDefault(Direction.DOWN, 0L).intValue();
		int left = counted.getOrDefault(Direction.LEFT, 0L).intValue();
		int right = counted.getOrDefault(Direction.RIGHT, 0L).intValue();

		// for each axis: the direction with more votes wins. the wind strength
		// is the difference between the opposing votes.
		int speedX = Math.min(Math.max(right - left, Wind.MIN_WIND), Wind.MAX_WIND);
		int speedY = Math.min(Math.max(up - down, Wind.MIN_WIND), Wind.MAX_WIND);

		wind.setSpeedX((byte) speedX);
		wind.setSpeedY((byte) speedY);

		return wind;
	}
}
