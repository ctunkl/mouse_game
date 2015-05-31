package at.ac.tuwien.foop.mouserace.server.utils;

import at.ac.tuwien.foop.mouserace.common.domain.Field;

public class Fields {


	public static Field createComplexField() {
		FieldGenerator fg = new FieldGenerator();
		fg.reset();

		fg.row("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		fg.row("x      xxxxxxx     xx        xxx");
		fg.row("x  xx   xxxxxx xxxxxxxxx xxx xxx");
		fg.row("x x   x xx            xx  x   xx");
		fg.row("x xxxxx xx xxxx xxxxxxxxx     xx");
		fg.row("x  xxxx xx xxxx x x   xxxx xx xx");
		fg.row("x          xx x x x x      xx xx");
		fg.row("xx   xxxxxxxx   x x xxxxx  xx xx");
		fg.row("x xx  xxxxxxx xxx xxx xxxx xx xx");
		fg.row("x xxx  x    xxx  xxxx xxxx xx xx");
		fg.row("x        xx                   xx");
		fg.row("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

		return fg.build();
	}

	public static Field createSimpleField() {
		FieldGenerator fg = new FieldGenerator();
		fg.reset();

		fg.row("xxxxxxxxxx");
		fg.row("x        x");
		fg.row("x   xx   x");
		fg.row("x        x");
		fg.row("x        x");
		fg.row("x        x");
		fg.row("xxxxxxxxxx");

		return fg.build();
	}

}
