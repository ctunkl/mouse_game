package at.ac.tuwien.foop.mouserace.common.domain;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Created by klaus on 5/30/15.
 */
public class Field implements Iterable<Cell> {
	public static final int MIN_SIZE = 3;

	private final Cell[][] cells;

	/**
	 * Constructs a new field with specified width and height.
	 * @param width the game field width
	 * @param height the game field height
	 */
	public Field(int width, int height) {
		if(width < MIN_SIZE || height < MIN_SIZE)
			throw new IllegalArgumentException(String.format("Both width and height have to be greater than %d", MIN_SIZE));

		cells = new Cell[width][height];
	}

	/**
	 * Sets the cell on position (x, y). In addition it sets the position attributes (x, y) of the
	 * new cell and unsets the position attributes (x, y) of the old cell.
	 *
	 * @param x the width index in the interval [0, width-1]
	 * @param y the height index in the interval [0, height-1]
	 * @param cell the cell which will be set on the specified position
	 * @return the old cell at the specified position
	 */
	public Cell setCell(int x, int y, Cell cell) {
		if(!checkPosition(x, y))
			throw new IllegalArgumentException("Either x or y is out of range");
		Objects.requireNonNull(cell, "cell must not be null");

		Cell oldCell = cells[x][y];
		if(oldCell != null) {
			oldCell.unsetFieldPosition();
		}
		cell.setFieldPosition(x, y);
		cells[x][y] = cell;

		return oldCell;
	}

	public Cell getCell(int x, int y) {
		if(!checkPosition(x, y))
			throw new IllegalArgumentException("Either x or y is out of range");

		return cells[x][y];
	}

	/**
	 * Checks whether the position indexed by x and y is in the range of the field.
	 * @param x the width index in the interval [0, width-1]
	 * @param y the height index in the interval [0, height-1]
	 * @return true, if and only if x and y are valid indices in the field.
	 */
	private boolean checkPosition(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}

	public int getWidth() {
		return cells.length;
	}

	public int getHeight() {
		if(cells[0] == null)
			throw new IllegalStateException("The game field is not a valid two dimensional field");

		return cells[0].length;
	}

	public boolean isFieldComplete() {
		return Arrays.stream(cells).flatMap(Arrays::stream).allMatch((cell) -> cell != null);
	}

	/**
	 * Checks if the field is a valid game field and the cells are all placed correctly.
	 * @return True, if and only if the field is valid.
	 */
	public boolean isFieldValid() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				final Cell cell = getCell(x, y);

				if(cell == null)
					return false;

				// Check if edge cells are either entry cells or wall cells
				if(x == 0 || x >= getWidth()-1 || y == 0 || y >= getHeight()-1) {
					// We're somewhere on the edge

					if(!(cell instanceof EntryCell || cell instanceof WallCell))
						return false;
				}

				// TODO Add further checks
			}
		}

		return true;
	}

	/**
	 * Same as {@link #columnFirstIterator()}
	 * @see #columnFirstIterator()
	 */
	@Override
	public Iterator<Cell> iterator() {
		return columnFirstIterator();
	}

	/**
	 * <p>
	 * Iterate over all cells of the field by first column and then row. I.e. the positions of the cells will be in
	 * the following order: (0,0), (1,0), (2,0), ..., (0,1), (1,1), (2,1), ... <br/>
	 * where the first value of each tuple is the column (= x) and the second value is the row (= y).
	 * </p><p>
	 * <strong>This iterator will be read-only, so the {@link Iterator#remove()} method will not be supported and
	 * throw an {@linkplain UnsupportedOperationException}. Furthermore, modification </strong>
	 * </p><p>
	 * For iterating over the field with row first, see {@link #rowFirstIterator()}
	 * </p>
	 */
	public Iterator<Cell> columnFirstIterator() {
		return new ColumnFirstIterator();
	}

	/**
	 * <p>
	 * Iterate over all cells of the field by first row and then column. I.e. the positions of the cells will be in
	 * the following order: (0,0), (0,1), (0,2), ..., (1,0), (1,1), (1,2), ... <br/>
	 * where the first value of each tuple is the column (= x) and the second value is the row (= y).
	 * </p><p>
	 * <strong>This iterator will be read-only, so the {@link Iterator#remove()} method will not be supported and
	 * throw an {@linkplain UnsupportedOperationException}.</strong>
	 * </p><p>
	 * For iterating over the field with column first, see {@link #columnFirstIterator()}
	 * </p>
	 */
	public Iterator<Cell> rowFirstIterator() {
		return new RowFirstIterator();
	}

	/**
	 * <p>
	 * Iterate over the rows of the field. The columns of each row can be accessed through the index of the array
	 * returned by the iterator.
	 * </p><p>
	 * <strong>This iterator will be read-only, so the {@link Iterator#remove()} method will not be supported and
	 * throw an {@linkplain UnsupportedOperationException}. Furthermore, modifications in the returned array of
	 * the iterator will not modify this field</strong>
	 * </p>
	 */
	public Iterator<Cell[]> rowIterator() {
		return new RowIterator();
	}

	/**
	 * <p>
	 * Iterate over the columns of the field. The rows of each column can be accessed through the index of the array
	 * returned by the iterator.
	 * </p><p>
	 * <strong>This iterator will be read-only, so the {@link Iterator#remove()} method will not be supported and
	 * throw an {@linkplain UnsupportedOperationException}. Furthermore, modifications in the returned array of
	 * the iterator will not modify this field</strong>
	 * </p>
	 */
	public Iterator<Cell[]> columnIterator() {
		return new ColumnIterator();
	}

	public abstract class AbstractFieldIterator<T> implements Iterator<T> {
		protected int x, y;

		public AbstractFieldIterator() {
			x = 0;
			y = 0;
		}

		@Override
		public boolean hasNext() {
			return x < Field.this.getWidth() || y < Field.this.getHeight();
		}

		@Override
		abstract public T next();
	}

	public class ColumnFirstIterator extends AbstractFieldIterator<Cell> {
		@Override
		public Cell next() {
			if(!hasNext())
				throw new NoSuchElementException();
			Cell ret = Field.this.getCell(x, y);
			if(x >= Field.this.getWidth()-1)
				y++;
			else
				x++;
			return ret;
		}
	}

	public class RowFirstIterator extends AbstractFieldIterator<Cell> {
		@Override
		public Cell next() {
			if(!hasNext())
				throw new NoSuchElementException();
			Cell ret = Field.this.getCell(x, y);
			if(y >= Field.this.getHeight()-1)
				x++;
			else
				y++;
			return ret;
		}
	}

	public class ColumnIterator extends AbstractFieldIterator<Cell[]> {
		@Override
		public Cell[] next() {
			if(!hasNext())
				throw new NoSuchElementException();

			return Arrays.copyOf(cells[x++], Field.this.getHeight());
		}
	}

	public class RowIterator extends AbstractFieldIterator<Cell[]> {
		@Override
		public Cell[] next() {
			if(!hasNext())
				throw new NoSuchElementException();

			Cell[] row = new Cell[Field.this.getWidth()];
			for (int x = 0; x < row.length; x++) {
				row[x] = Field.this.getCell(x, y);
			}
			y++;
			return row;
		}
	}
}
