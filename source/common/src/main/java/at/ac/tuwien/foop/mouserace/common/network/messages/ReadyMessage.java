package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.domain.*;
import at.ac.tuwien.foop.mouserace.common.network.exceptions.MessageParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by klaus on 6/5/15.
 */
public class ReadyMessage extends CommandMessage {
	private static final Logger logger = LoggerFactory.getLogger(ReadyMessage.class.getName());

	public static final CommandType COMMAND_TYPE = CommandType.READY;

	private final Field field;
	private final Figure[] figures;
	private final int ownId;

	public ReadyMessage(Field field, int ownId, Figure... figures) {
		this.figures = Objects.requireNonNull(figures);

		if (figures.length > NetworkLimits.READY_MAX_FIGURES_LENGTH)
			throw new IllegalArgumentException(String.format("no more than %d figures allowed", NetworkLimits.READY_MAX_FIGURES_LENGTH));

		this.field = Objects.requireNonNull(field);

		if (ownId < 0 || ownId > Limits.FIGURE_MAX_ID)
			throw new IllegalArgumentException(String.format("own id must be positive and lower than or equal %d", Limits.FIGURE_MAX_ID));
		this.ownId = ownId;
	}

	/**
	 * Parses a byte array to a new instance of this class.
	 *
	 * @return a new instance of this class
	 * @throws EOFException if the input provided is shorter than the expected length
	 */
	public static ReadyMessage fromByteArray(byte[] data) throws EOFException, MessageParsingException {
		Objects.requireNonNull(data);

		try {
			return fromInputStream(new DataInputStream(new ByteArrayInputStream(data)));
		} catch (IOException e) {
			if (e instanceof EOFException)
				throw (EOFException) e;
			// Should never be the case since we are reading from an byte array
			throw new RuntimeException();
		}
	}

	/**
	 * Parses a byte array to a new instance of this class.
	 *
	 * @throws MessageParsingException if the data cannot be parsed because it doesn't match the network
	 *                                 protocol specification
	 * @throws EOFException            if the input provided is shorter than the expected length
	 */
	public static ReadyMessage fromInputStream(DataInput input) throws MessageParsingException, IOException {
		Objects.requireNonNull(input);

		byte commandType = input.readByte();
		if (commandType != COMMAND_TYPE.getValue())
			throw new MessageParsingException(
					String.format("wrong command type byte: expected: 0x%02X; found: 0x%02X",
							COMMAND_TYPE.getValue(), commandType));

		int width = input.readUnsignedShort();
		int height = input.readUnsignedShort();
		Field field = new Field(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				byte cellTypeByte = input.readByte();
				CellType cellType = CellType.fromByte(cellTypeByte);
				Cell cell = Cell.fromCellType(cellType);
				field.setCell(x, y, cell);
			}
		}

		int figuresLength = input.readUnsignedByte();
		Figure[] figures = new Figure[figuresLength];
		for (int i = 0; i < figures.length; i++) {
			FigureStructure fs = FigureStructure.fromInputStream(input);
			figures[i] = fs.getFigure();
		}

		int ownId = input.readUnsignedByte();

		return new ReadyMessage(field, ownId, figures);
	}

	public Figure[] getFigures() {
		return figures;
	}

	public Field getField() {
		return field;
	}

	public int getOwnId() {
		return ownId;
	}

	@Override
	public byte[] toByteArray() {

		ByteBuffer bb = ByteBuffer.allocate(1 /* CMD */ + 2 /* WIDTH */ + 2 /* HEIGHT */
				+ field.getWidth() * field.getHeight() /* FIELD */ + 1 /* FIGLEN */
				+ figures.length * FigureStructure.STRUCTURE_LENGTH /* FIGURES */ + 1 /* OWN */);

		bb.put(getCommandType().getValue());
		bb.putShort((short)field.getWidth());
		bb.putShort((short)field.getHeight());


		for (Iterator<Cell> it = field.rowFirstIterator(); it.hasNext();) {
			Cell cell = it.next();
			bb.put(cell.getCellType().getValue());
		}

		bb.put((byte) figures.length);

		for (Figure f : figures) {
			bb.put(new FigureStructure(f).toByteArray());
		}

		bb.put((byte)ownId);

		return bb.array();
	}

	@Override
	public CommandType getCommandType() {
		return COMMAND_TYPE;
	}
}
