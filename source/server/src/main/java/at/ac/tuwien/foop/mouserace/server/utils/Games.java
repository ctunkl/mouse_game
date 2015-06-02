package at.ac.tuwien.foop.mouserace.server.utils;

import at.ac.tuwien.foop.mouserace.common.domain.Game;

public class Games {

	public static Game createWithSimpleField() {
		GameGenerator gen = new GameGenerator();
		gen.reset();

		gen.row("xxxxxxxxxx");
		gen.row("x    c   x");
		gen.row("x   xx   x");
		gen.row("x        x");
		gen.row("x        x");
		gen.row("x .    . x");
		gen.row("xxxxx.xxxx");

		return gen.build();
	}

	public static Game createWithComplexField() {
		GameGenerator gen = new GameGenerator();
		gen.reset();

		gen.row("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		gen.row("x                  xx    c   xxx");
		gen.row("x  xx   xxxxxx xxxxxx xx xxx xxx");
		gen.row("x x   x xx            xx  x   xx");
		gen.row("x xxxxx xx xxxx xxxxxxxxx     xx");
		gen.row("x  xxxx xx    x   x   xxxx xx xx");
		gen.row("x          xx x x x x      xx xx");
		gen.row("xx   xxxxxxx    x x xxxxx  xx xx");
		gen.row("x xx  xxxx   xxx  xxx xxxx xx xx");
		gen.row("x xxx  x    xxx  xxxx xxxx xx xx");
		gen.row("x .      xx     .             xx");
		gen.row("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

		return gen.build();
	}
}
