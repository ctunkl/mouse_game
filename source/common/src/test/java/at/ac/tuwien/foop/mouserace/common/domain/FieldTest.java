package at.ac.tuwien.foop.mouserace.common.domain;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by klaus on 5/30/15.
 */
public class FieldTest {

	@Test
	public void testIsFieldComplete_shouldPass1() throws Exception {
		final int width = 10;
		final int height = 10;
		final Field field = new Field(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				field.setCell(x, y, new EmptyCell());
			}
		}

		assertTrue(field.isFieldComplete());
	}

	@Test
	public void testIsFieldComplete_shouldFail1() throws Exception {
		final int width = 10;
		final int height = 10;
		final Field field = new Field(width, height);

		assertFalse(field.isFieldComplete());
	}

	@Test
	public void testIsFieldComplete_shouldFail2() throws Exception {
		final int width = 10;
		final int height = 10;
		final Field field = new Field(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				// Introduce error here
				if(x == 0 && y == 0)
					continue;

				field.setCell(x, y, new EmptyCell());
			}
		}

		assertFalse(field.isFieldComplete());
	}

	@Test
	public void testIsFieldValid_shouldFail1() throws Exception {
		final int width = 10;
		final int height = 10;
		final Field field = new Field(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				field.setCell(x, y, new EmptyCell());
			}
		}

		assertFalse(field.isFieldValid());
	}

	@Test
	public void testIsFieldValid_shouldFail2() throws Exception {
		final int width = 10;
		final int height = 10;
		final Field field = new Field(width, height);

		assertFalse(field.isFieldValid());
	}


	@Test
	public void testIsFieldValid_shouldPass1() throws Exception {
		final int width = 10;
		final int height = 10;
		final Field field = new Field(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(x == 0 || y == 0 || x >= width-1 || y >= height-1) {
					if ((x == 0 || x >= width-1) && y == height / 2)
						field.setCell(x, y, new EntryCell()); // Set entries
					else
						field.setCell(x, y, new WallCell()); // Set surrounding wall
				} else {
					field.setCell(x, y, new EmptyCell());
				}
			}
		}

		assertTrue(field.isFieldValid());
	}
}