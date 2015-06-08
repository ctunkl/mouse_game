package at.ac.tuwien.foop.mouserace.server.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import at.ac.tuwien.foop.mouserace.common.domain.Cell;
import at.ac.tuwien.foop.mouserace.common.domain.Cheese;
import at.ac.tuwien.foop.mouserace.common.domain.EmptyCell;
import at.ac.tuwien.foop.mouserace.common.domain.EntryCell;
import at.ac.tuwien.foop.mouserace.common.domain.Field;
import at.ac.tuwien.foop.mouserace.common.domain.Figure;
import at.ac.tuwien.foop.mouserace.common.domain.Game;
import at.ac.tuwien.foop.mouserace.common.domain.Limits;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.common.domain.MouseState;
import at.ac.tuwien.foop.mouserace.common.domain.Wind;
import at.ac.tuwien.foop.mouserace.server.IGameClient;
import at.ac.tuwien.foop.mouserace.server.utils.Tuple;



public class GameEngine {

	private Timer tick;

	private Game game;
	private GameOptions options;
	private Cheese cheese;

	private Collection<Mouse> mice;
	private List<Cell> freeEntryCells;

	// for each mouse: how many ticks will it keep sniffing
	private Map<Mouse, Integer> sniffingMice;

	// for each mouse: how many ticks will it keep being confused
	private Map<Mouse, Integer> confusedMice;

	// player for each mouse
	private Map<Mouse, IGameClient> playersForMice;


	private List<IGameClient> listeners;


	private final Logger logger = Logger.getLogger(GameEngine.class.getSimpleName());


	public GameEngine(Game game, GameOptions options) {
		this.game = game;
		this.options = options;

		sniffingMice = new HashMap<>();
		confusedMice = new HashMap<>();
		playersForMice = new HashMap<>();

		Optional<Figure> optCheese = game.getFigures().stream().filter(f -> f instanceof Cheese).findFirst();

		if(optCheese.isPresent() == false) {
			throw new IllegalArgumentException("No cheese in game field");
		}

		this.cheese = (Cheese) optCheese.get();
		printFigureWithPosition(this.cheese);

		freeEntryCells = findCellsOfType(game.getField(), EntryCell.class);

		this.listeners = new ArrayList<>();
	}

	private void printFigureWithPosition(Figure f) {
		logger.info(String.format("%s %d: Position (%d/%d)",
				f.getClass().getSimpleName(),
				f.getId(),
				f.getX(),
				f.getY()));
	}

	private <T extends Cell> List<Cell> findCellsOfType(Field field, Class<T> clazz) {
		List<Cell> cells = new ArrayList<>();

		for(int y=0; y < field.getHeight(); y++) {
			for(int x=0; x < field.getWidth(); x++) {
				Cell cell = field.getCell(x, y);

				if(cell.getClass().equals(clazz)) {
					cells.add(cell);
				}
			}
		}

		return cells;
	}

	public void startGame() {
		this.mice =  this.game.getFigures().stream()
				.filter(f -> f instanceof Mouse)
				.map(f -> (Mouse) f)
				.collect(Collectors.toList());

		if(mice.size() == 0) {
			throw new IllegalStateException("No mice! Cannot start game.");
		}

		this.listeners.forEach(l -> l.fieldChosen(game.getField()));

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
	private void play() {
		logger.info("Tick!");

		// TODO receive key presses from players
		List<Direction> directions = Arrays.asList(Direction.UP, Direction.UP, Direction.LEFT,
						Direction.LEFT, Direction.DOWN, Direction.LEFT);

		Wind wind = calculateWind(game.getWind(), directions);
		game.setWind(wind);

		for(Mouse m : this.mice) {
			moveMouse(m);
			checkForCollisions();

			if(hasReachedCheese(m, this.cheese)) {
				endGame(m);
				return;
			}
		}

		this.listeners.forEach(l -> l.currentState(new GameState(game.getFigures(), game.getWind())));
	}

	private void checkForCollisions() {
		for(Mouse a : this.mice) {
			for(Mouse b : this.mice) {
				if(a.getId() != b.getId()) {
					if(a.getX() == b.getX() && a.getY() == b.getY()) {
						mouseCollision(a);
						mouseCollision(b);
					}
				}
			}
		}
	}

	private boolean hasReachedCheese(Mouse m, Cheese cheese) {
		return m.getX() == cheese.getX() && m.getY() == cheese.getY();
	}

	private void endGame(Mouse winner) {
		tick.cancel();
		logger.info(String.format("Game over! Mouse %d (Player: %s) wins!", winner.getId(), playersForMice.get(winner)));

		this.listeners.forEach(l -> l.gameEnded(winner));
	}

	private void moveMouse(Mouse m) {
		List<Cell> emptyNeighbours = emptyNeighbours(game.getField(), m.getX(), m.getY());

		Integer newX = m.getX();
		Integer newY = m.getY();

		if(m.getState().equals(MouseState.SNIFFING)) {
			int remainingTicks = sniffingMice.getOrDefault(m, 0);

			if(remainingTicks == 0) {
				m.setState(MouseState.CONFUSED);
				logger.info(String.format("  Mouse %d: Stopped sniffing. Gonna move randomly now.", m.getId()));
				confusedMice.put(m,  options.getConfusedTime());
			} else {
				sniffingMice.put(m,  remainingTicks-1);
				logger.info(String.format("  Mouse %d: Gonna keep sniffing for %s more ticks.", m.getId(), remainingTicks-1));
			}
		}

		if(m.getState().equals(MouseState.CONFUSED)) {
			int remainingTicks = confusedMice.getOrDefault(m, 0);

			if(remainingTicks == 0) {
				m.setState(MouseState.NORMAL);
				logger.info(String.format("  Mouse %d: Back to normal.", m.getId()));
			} else {
				confusedMice.put(m,  remainingTicks-1);
				logger.info(String.format("  Mouse %d: Gonna keep moving aimlessly for %s more ticks.", m.getId(), remainingTicks-1));

				Random r = new Random();

				Cell nextCell = emptyNeighbours.get(r.nextInt(emptyNeighbours.size()));
				newX = nextCell.getX();
				newY = nextCell.getY();

				logger.info(String.format("  Mouse %d: Moving from (%d,%d) to (%d,%d)",
						m.getId(), m.getX(), m.getY(), newX, newY));

				m.setX(nextCell.getX());
				m.setY(nextCell.getY());
			}
		}

		if(m.getState().equals(MouseState.NORMAL)) {
			Optional<Cell> nextCell = nextCell(game.getField(), game.getWind(), m.getX(), m.getY());

			if(nextCell.isPresent()) {
				newX = nextCell.get().getX();
				newY = nextCell.get().getY();

				logger.info(String.format("  Mouse %d: Moving from (%d,%d) to (%d,%d)",
						m.getId(), m.getX(), m.getY(), newX, newY));

				m.setX(newX);
				m.setY(newY);

			} else {
				logger.info(String.format("  Mouse %d: No idea where to go.", m.getId()));
				m.setState(MouseState.CONFUSED);
				confusedMice.put(m, options.getConfusedTime());
			}
		}
	}

	private void mouseCollision(Mouse m) {
		if(m.getState().equals(MouseState.SNIFFING) == false) {
			m.setState(MouseState.SNIFFING);
			sniffingMice.put(m, options.getSniffingTime());

			logger.info(String.format("  Mouse %d: Encountered another mouse. *sniff*", m.getId()));
		}
	}

	/**
	 * For given coordinates, returns all empty neighbour cells which lead
	 * closer to a given cell
	 */
	private List<Cell> neighboursLeadingToCell(Field field, int x, int y, Cell cell) {
		List<Cell> cells = emptyNeighbours(field, x, y);

		Integer currentDistance = manhattanDistance(x, y, cell.getX(), cell.getY());

		return cells.stream().filter(
				c -> manhattanDistance(c.getX(), c.getY(), cell.getX(), cell.getY()) < currentDistance)
				.collect(Collectors.toList());
	}


	/**
	 * For given coordinates, return the best move.
	 * Currently: Picks any cell which leads closer to the cheese
	 */
	private Optional<Cell> nextCell(Field field, Wind wind, int x, int y) {
		Cell target = calculateTargetCell(field, wind, cheese);

		List<Cell> cells = neighboursLeadingToCell(field, x, y, target);

		if(cells.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(cells.get(0));
		}
	}

	/**
	 * Uses the wind to determine a new target cell for the mice,
	 * relative to the cheese
	 */
	private Cell calculateTargetCell(Field field, Wind wind, Cheese cheese) {
		double relativeSpeedX = (double)wind.getSpeedX()/Limits.WIND_MAX_SPEED;
		double relativeSpeedY = (double)wind.getSpeedY()/Limits.WIND_MAX_SPEED;

		int newX = moveCoordinate(cheese.getX(), field.getWidth(), relativeSpeedX);
		int newY = moveCoordinate(cheese.getY(), field.getHeight(), relativeSpeedY);

		if(newX != cheese.getX() || newY != cheese.getY()) {
			logger.info(String.format("  The Wind makes it seem like the cheese is actually at (%d/%d)", newX, newY));
		}

		return field.getCell(newX, newY);
	}

	/**
	 * Given an origin coordinate, moves it relatively along its axis
	 * e.g.
	 * 		offset -1: returns the coordinate of the left/top end
	 * 		offset  1: returns the coordinate of right/bottom end
	 * 		offset  0: returns the origin
	 * 		offset: 0.5: halfway between origin and the right/bottom end
	 * 		etc.
	 *
	 * @param origin the coordinate
	 * @param length the full length of the axis
	 * @param relativeOffset relative offset. must be between -1 and 1
	 * @return
	 */
	private int moveCoordinate(int origin, int length, double relativeOffset) {
		int cells;
		if(relativeOffset > 0) {
			cells = length - 1 - origin;
		} else {
			cells = origin;
		}
		return origin + (int)(cells * relativeOffset);
	}


	private Integer manhattanDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}


	/**
	 * For given coordinates, return all empty neighbour cells
	 */
	private List<Cell> emptyNeighbours(Field field, int x, int y) {
		return neighbourCells(field, x, y).stream().filter(c -> c instanceof EmptyCell).collect(Collectors.toList());
	}


	/**
	 * For given coordinates, return all direct neighbour cells within the field
	 */
	private List<Cell> neighbourCells(Field field, int x, int y) {
		List<Cell> cells = new ArrayList<>();

		List<Tuple<Integer, Integer>> coords = new ArrayList<>();
		coords.add(new Tuple<>(x, y-1));
		coords.add(new Tuple<>(x, y+1));
		coords.add(new Tuple<>(x-1, y));
		coords.add(new Tuple<>(x+1, y));

		for(Tuple<Integer, Integer> tuple : coords) {
			try {
				cells.add(field.getCell(tuple.left, tuple.right));
			} catch(IllegalArgumentException e) {}
		}

		return cells;
	}



	/**
	 *  Calculate new wind based on player input
	 */
	private Wind calculateWind(Wind wind, Collection<Direction> directions) {
		// count how many players voted for each direction
		Map<Direction, Long> counted = directions.stream().collect(
				Collectors.groupingBy(o -> o, Collectors.counting()));

		int up = counted.getOrDefault(Direction.UP, 0L).intValue();
		int down = counted.getOrDefault(Direction.DOWN, 0L).intValue();
		int left = counted.getOrDefault(Direction.LEFT, 0L).intValue();
		int right = counted.getOrDefault(Direction.RIGHT, 0L).intValue();

		// for each axis: the direction with more votes wins. the wind strength
		// is the difference between the opposing votes.
		int speedX = Math.min(Math.max(right - left, Limits.WIND_MIN_SPEED), Limits.WIND_MAX_SPEED);
		int speedY = Math.min(Math.max(up - down, Limits.WIND_MIN_SPEED), Limits.WIND_MAX_SPEED);

		Wind newWind = new Wind();
		newWind.setSpeedX((byte) speedX);
		newWind.setSpeedY((byte) speedY);

		return wind;
	}

	public void registerPlayer(IGameClient listener) {
		if(this.freeEntryCells.isEmpty()) {
			throw new IllegalStateException(String.format("Game is full, no more room for more players"));
		}

		Collections.shuffle(this.freeEntryCells);

		Cell entryCell = this.freeEntryCells.get(0);

		Mouse mouse= new Mouse(nextFreeFigureId(this.game.getFigures()));
		mouse.setX(entryCell.getX());
		mouse.setY(entryCell.getY());

		logger.info(String.format("Registering new player %s: Assigned mouse: %d: ", listener.toString(), mouse.getId()));
		logger.info(String.format("Mouse %d will start from (%d/%d)", mouse.getId(), mouse.getX(), mouse.getY()));

		this.listeners.add(listener);
		this.playersForMice.put(mouse, listener);
		this.game.getFigures().add(mouse);
		this.freeEntryCells.remove(entryCell);
	}

	private int nextFreeFigureId(Collection<Figure> figures) {
		return figures.stream().map(f -> f.getId()).max(Integer::max).orElse(0) + 1;
	}
}
