package at.ac.tuwien.foop.mouserace.server;

import java.util.HashSet;
import java.util.Set;

import at.ac.tuwien.foop.mouserace.common.domain.Cheese;
import at.ac.tuwien.foop.mouserace.common.domain.Field;
import at.ac.tuwien.foop.mouserace.common.domain.Figure;
import at.ac.tuwien.foop.mouserace.common.domain.Game;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.common.domain.Wind;
import at.ac.tuwien.foop.mouserace.server.utils.Fields;

/**
 * Created by klaus on 5/30/15.
 */
public class Main {
	public static void main(String[] args) {
		Game game = new Game();
		GameOptions options = new GameOptions();

		Set<Figure> figures = new HashSet<>();

		Mouse m = new Mouse(1);
		m.setX(1);
		m.setY(1);
		figures.add(m);

		Cheese c = new Cheese(2);
		c.setX(6);
		c.setY(8);
		figures.add(c);

		Field f = Fields.createSimpleField();

		game.setFigures(figures);
		game.setField(f);
		game.setWind(new Wind());


		GameEngine engine = new GameEngine(game, options);
		engine.startGame();
	}
}
