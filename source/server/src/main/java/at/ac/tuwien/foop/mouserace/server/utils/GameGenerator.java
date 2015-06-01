package at.ac.tuwien.foop.mouserace.server.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.ac.tuwien.foop.mouserace.common.domain.Cell;
import at.ac.tuwien.foop.mouserace.common.domain.Cheese;
import at.ac.tuwien.foop.mouserace.common.domain.EmptyCell;
import at.ac.tuwien.foop.mouserace.common.domain.EntryCell;
import at.ac.tuwien.foop.mouserace.common.domain.Field;
import at.ac.tuwien.foop.mouserace.common.domain.Figure;
import at.ac.tuwien.foop.mouserace.common.domain.Game;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.common.domain.WallCell;
import at.ac.tuwien.foop.mouserace.common.domain.Wind;

public class GameGenerator {

	private List<String> rows;
	private int width;

	private List<Cell> entryCells;

	private int nextId;


	public GameGenerator() {
		reset();
	}

	public void reset() {
		rows = new ArrayList<>();
		entryCells = new ArrayList<>();
		nextId = 0;
	}

	public GameGenerator row(String row) {
		if(rows.isEmpty()) {
			width = row.length();
		}

		if(validRow(row) == false) {
			throw new IllegalArgumentException("Invalid characters or length");
		}
		rows.add(row);
		return this;
	}

	private boolean validRow(String row) {
		return row.matches("[ xc.]+") && row.length() == width;
	}

	public Game build(int mice) {
		if(rows.isEmpty()) {
			throw new IllegalArgumentException("Found no field data");
		}

		final int WIDTH = rows.get(0).length();
		final int HEIGHT = rows.size();

		Field field = new Field(WIDTH, HEIGHT);
		Set<Figure> figures = new HashSet<>();

		// create field, save entry cells and cheese for later
		for(int y=0; y<HEIGHT; y++) {
			String row = rows.get(y);

			for(int x=0; x<WIDTH; x++) {
				Cell cell = cellForCharacter(row.charAt(x));
				field.setCell(x, y, cell);

				if(cell instanceof EntryCell) {
					entryCells.add(cell);
				}

				Figure f = figureForCharacter(row.charAt(x));
				if(f != null) {
					f.setX(x);
					f.setY(y);
					figures.add(f);
				}
			}
		}

		if(mice > entryCells.size()) {
			throw new IllegalArgumentException(String.format("Only %d mice are allowed on this field",
					entryCells.size()));
		}


		// add mice, put them on entry cells
		Collections.shuffle(this.entryCells);

		for(int i = 0; i < mice; i++) {
			Cell entryCell = this.entryCells.get(i);

			Mouse mouse= new Mouse(nextId());
			mouse.setX(entryCell.getX());
			mouse.setY(entryCell.getY());

			figures.add(mouse);
		}

		Game game = new Game();
		game.setField(field);
		game.setFigures(figures);
		game.setWind(new Wind());

		return game;
	}

	private Cell cellForCharacter(char c) {
		switch(c) {
			case 'x':
				return new WallCell();
			case '.':
				return new EntryCell();
			default:
				return new EmptyCell();
		}
	}

	private Figure figureForCharacter(char c) {
		switch(c) {
			case 'c':
				return new Cheese(nextId());
			default:
				return null;
		}
	}

	private int nextId() {
		return nextId++;
	}
}
