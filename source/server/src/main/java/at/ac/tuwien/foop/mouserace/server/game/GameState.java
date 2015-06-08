package at.ac.tuwien.foop.mouserace.server.game;

import java.util.HashSet;
import java.util.Set;

import at.ac.tuwien.foop.mouserace.common.domain.Figure;
import at.ac.tuwien.foop.mouserace.common.domain.Wind;

public class GameState {

	private Set<Figure> figures;
	private Wind wind;

	public GameState(Set<Figure> figures, Wind wind) {
		this.figures = figures;
		this.wind = wind;
	}

	public GameState() {
		this.figures = new HashSet<>();
		this.wind = new Wind();
	}

	public Set<Figure> getFigures() {
		return figures;
	}

	public void setFigures(Set<Figure> figures) {
		this.figures = figures;
	}

	public Wind getWind() {
		return wind;
	}

	public void setWind(Wind wind) {
		this.wind = wind;
	}
}
