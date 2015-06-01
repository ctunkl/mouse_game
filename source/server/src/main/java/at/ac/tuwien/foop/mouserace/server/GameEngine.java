package at.ac.tuwien.foop.mouserace.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import at.ac.tuwien.foop.mouserace.common.domain.Cell;
import at.ac.tuwien.foop.mouserace.common.domain.Cheese;
import at.ac.tuwien.foop.mouserace.common.domain.EmptyCell;
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

	// for each mouse: how many ticks will it keep being confused
	private Map<Mouse, Integer> confusedMice;


	public GameEngine(Game game, GameOptions options) {
		this.game = game;
		this.options = options;

		sniffingMice = new HashMap<>();
		confusedMice = new HashMap<>();

		// extract mice and cheese from Figure list for convenience
		mice = new ArrayList<>();

		for(Figure f : game.getFigures()) {
			if(f instanceof Mouse) {
				mice.add((Mouse) f);
			}
			if(f instanceof Cheese) {
				this.cheese = (Cheese) f;
			}
		}

		Objects.requireNonNull(cheese, "No cheese found");
	}

	public void startGame() {
		tick = new Timer(this.getClass().getSimpleName());
		tick.schedule(new TimerTask() {

			@Override
			public void run() {
				GameEngine.this.play();

			}
		}, 0, options.getTickMillis());
	}

	/**
	 * 	This method is called on each tick. It processes the player input,
	 *  moves the mice and updates their state, and finally sends the game state
	 *  back to its players
	 */
	public void play() {
		System.out.println("Tick!");

		// TODO receive key presses from players
		List<Direction> directions = Arrays.asList(Direction.UP, Direction.UP, Direction.LEFT,
						Direction.LEFT, Direction.DOWN, Direction.RIGHT);

		Wind wind = calculateWind(directions);
		game.setWind(wind);

		for(Mouse m : this.mice) {
			moveMouse(m);

			if(hasReachedCheese(m)) {
				endGame(m);
				return;
			}
		}
	}

	private boolean hasReachedCheese(Mouse m) {
		return m.getX() == this.cheese.getX() && m.getY() == this.cheese.getY();
	}

	private void endGame(Mouse winner) {
		tick.cancel();
		System.out.println(String.format("Game over! Mouse %d wins!", winner.getId()));
	}

	private void moveMouse(Mouse m) {
		List<Cell> emptyNeighbours = emptyNeighbours(m.getX(), m.getY());

		Integer newX = m.getX();
		Integer newY = m.getY();

		if(m.getState().equals(MouseState.NORMAL)) {
			Optional<Cell> nextCell = nextCell(m.getX(), m.getY());

			if(nextCell.isPresent()) {
				newX = nextCell.get().getX();
				newY = nextCell.get().getY();

				System.out.println(String.format("  Mouse %d: Moving from (%d,%d) to (%d,%d)",
						m.getId(), m.getX(), m.getY(), newX, newY));

				m.setX(newX);
				m.setY(newY);

			} else {
				System.out.println(String.format("  Mouse %d: No idea where to go.", m.getId()));
				m.setState(MouseState.CONFUSED);
				confusedMice.put(m, options.getConfusedTime());
			}
		}
		else if(m.getState().equals(MouseState.SNIFFING)) {
			int remainingTicks = sniffingMice.getOrDefault(m, 0);

			if(remainingTicks == 0) {
				m.setState(MouseState.CONFUSED);
				System.out.println(String.format("  Mouse %d: Stopped sniffing. Gonna move randomly now.", m.getId()));
				confusedMice.put(m,  options.getConfusedTime());
			} else {
				sniffingMice.put(m,  remainingTicks-1);
				System.out.println(String.format("  Mouse %d: Gonna keep sniffing for %s more ticks.", m.getId(), remainingTicks-1));
			}
		}
		else if(m.getState().equals(MouseState.CONFUSED)) {
			int remainingTicks = confusedMice.getOrDefault(m, 0);

			if(remainingTicks == 0) {
				m.setState(MouseState.NORMAL);
				System.out.println(String.format("  Mouse %d: Back to normal.", m.getId()));
			} else {
				confusedMice.put(m,  remainingTicks-1);
				System.out.println(String.format("  Mouse %d: Gonna keep moving aimlessly for %s more ticks.", m.getId(), remainingTicks-1));

				Random r = new Random();

				Cell nextCell = emptyNeighbours.get(r.nextInt(emptyNeighbours.size()));
				newX = nextCell.getX();
				newY = nextCell.getY();

				System.out.println(String.format("  Mouse %d: Moving from (%d,%d) to (%d,%d)",
						m.getId(), m.getX(), m.getY(), newX, newY));

				m.setX(nextCell.getX());
				m.setY(nextCell.getY());
			}
		}
	}

	private void mouseCollision(Mouse a, Mouse b) {
		a.setState(MouseState.SNIFFING);
		b.setState(MouseState.SNIFFING);

		sniffingMice.put(a, options.getSniffingTime());
		sniffingMice.put(b, options.getSniffingTime());
	}

	/**
	 * For given coordinates, returns all empty neighbour cells which lead
	 * closer to the cheese
	 */
	public List<Cell> neighboursLeadingToCheese(int x, int y) {
		List<Cell> cells = emptyNeighbours(x, y);

		Integer currentDistance = manhattanDistance(x, y, cheese.getX(), cheese.getY());

		return cells.stream().filter(
				c -> manhattanDistance(c.getX(), c.getY(), cheese.getX(), cheese.getY()) < currentDistance)
				.collect(Collectors.toList());
	}

	/**
	 * For given coordinates, return the best move.
	 * Currently: Picks any cell which leads closer to the cheese
	 */
	public Optional<Cell> nextCell(int x, int y) {
		List<Cell> cells = neighboursLeadingToCheese(x, y);

		if(cells.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(cells.get(0));
		}
	}


	private Integer manhattanDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	/**
	 * For given coordinates, return all empty neighbour cells
	 */
	public List<Cell> emptyNeighbours(int x, int y) {
		return neighbourCells(x, y).stream().filter(c -> c instanceof EmptyCell).collect(Collectors.toList());
	}

	/**
	 * For given coordinates, return all direct neighbour cells within the field
	 */
	public List<Cell> neighbourCells(int x, int y) {
		List<Cell> cells = new ArrayList<>();

		List<Tuple<Integer, Integer>> coords = new ArrayList<>();
		coords.add(new Tuple<>(x, y-1));
		coords.add(new Tuple<>(x, y+1));
		coords.add(new Tuple<>(x-1, y));
		coords.add(new Tuple<>(x+1, y));


		for(Tuple<Integer, Integer> tuple : coords) {
			try {
				cells.add(game.getField().getCell(tuple.left, tuple.right));
			} catch(IllegalArgumentException e) {}
		}

		return cells;
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
