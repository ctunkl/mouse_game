package at.ac.tuwien.foop.mouserace.common.domain;

import java.util.Set;

/**
 * Created by klaus on 5/30/15.
 */
public class Game {
	private Field field;
	private Wind wind;
	private Set<Figure> figures;

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Wind getWind() {
		return wind;
	}

	public void setWind(Wind wind) {
		this.wind = wind;
	}

	public Set<Figure> getFigures() {
		return figures;
	}

	public void setFigures(Set<Figure> figures) {
		this.figures = figures;
	}
}
