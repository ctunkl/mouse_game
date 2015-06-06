package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.domain.Cheese;
import at.ac.tuwien.foop.mouserace.common.domain.Figure;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.common.domain.MouseState;
import at.ac.tuwien.foop.mouserace.common.network.exceptions.MessageParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Created by klaus on 6/5/15.
 */
public class FigureStructure extends GameMessage {
	private static final Logger logger = LoggerFactory.getLogger(FigureStructure.class.getName());

	/**
	 * The length in bytes of the structure
	 */
	public static final int STRUCTURE_LENGTH = 1 /* ID */ + 1 /* TYPE */ + 2 /* X */ + 2 /* Y */;

	private final Figure figure;

	public FigureStructure(Figure figure) {
		this.figure = Objects.requireNonNull(figure);
	}

	/**
	 * Parses a byte array to a new instance of this class.
	 *
	 * @return a new instance of this class
	 * @throws EOFException if the input provided is shorter than the expected length
	 */
	public static FigureStructure fromByteArray(byte[] data) throws EOFException, MessageParsingException {
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
	public static FigureStructure fromInputStream(DataInput input) throws MessageParsingException, IOException {
		Objects.requireNonNull(input);

		int id = input.readUnsignedByte();

		byte typeByte = input.readByte();
		FigureType type;
		try {
			type = FigureType.fromByte(typeByte);
		} catch (IllegalArgumentException e) {
			throw new MessageParsingException(e);
		}

		int x = input.readUnsignedShort();
		int y = input.readUnsignedShort();

		Figure figure;
		switch (type) {
			case MOUSE:
				figure = new Mouse(id);
				break;
			case CHEESE:
				figure = new Cheese(id);
				break;
			default:
				throw new MessageParsingException(String.format("Unknown figure type %s", type.name()));
		}
		figure.setX(x);
		figure.setY(y);

		return new FigureStructure(figure);
	}

	public Figure getFigure() {
		return figure;
	}

	@Override
	public byte[] toByteArray() {
		ByteBuffer bb = ByteBuffer.allocate(STRUCTURE_LENGTH);

		bb.put((byte) figure.getId());
		bb.put(figure.getFigureType().getValue());
		bb.putShort((short) figure.getX());
		bb.putShort((short) figure.getY());

		return bb.array();
	}
}
