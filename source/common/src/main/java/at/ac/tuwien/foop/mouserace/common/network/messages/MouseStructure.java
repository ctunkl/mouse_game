package at.ac.tuwien.foop.mouserace.common.network.messages;

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
public class MouseStructure extends GameMessage {
	private static final Logger logger = LoggerFactory.getLogger(MouseStructure.class.getName());

	/**
	 * The length in bytes of the structure
	 */
	public static final int STRUCTURE_LENGTH = 1 /* ID */ + 1 /* TYPE */ + 2 /* X */ + 2 /* Y */ + 1 /* STATE */;

	private final Mouse mouse;

	public MouseStructure(Mouse mouse) {
		this.mouse = Objects.requireNonNull(mouse);
	}

	/**
	 * Parses a byte array to a new instance of this class.
	 *
	 * @return a new instance of this class
	 * @throws EOFException if the input provided is shorter than the expected length
	 */
	public static MouseStructure fromByteArray(byte[] data) throws EOFException, MessageParsingException {
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
	public static MouseStructure fromInputStream(DataInput input) throws MessageParsingException, IOException {
		Objects.requireNonNull(input);

		int id = input.readUnsignedByte();

		byte type = input.readByte();
		if (type != FigureType.MOUSE.getValue())
			throw new MessageParsingException(
					String.format("wrong type byte: expected: 0x%02X; found: 0x%02X",
							FigureType.MOUSE.getValue(), type));

		int x = input.readUnsignedShort();
		int y = input.readUnsignedShort();

		byte stateByte = input.readByte();
		MouseState state;
		try {
			state = MouseState.fromByte(stateByte);
		} catch (IllegalArgumentException e) {
			throw new MessageParsingException(e);
		}

		Mouse mouse = new Mouse(id);
		mouse.setX(x);
		mouse.setY(y);
		mouse.setState(state);

		return new MouseStructure(mouse);
	}

	public Mouse getMouse() {
		return mouse;
	}

	@Override
	public byte[] toByteArray() {
		ByteBuffer bb = ByteBuffer.allocate(STRUCTURE_LENGTH);

		bb.put((byte) mouse.getId());
		bb.put(mouse.getFigureType().getValue());
		bb.putShort((short)mouse.getX());
		bb.putShort((short)mouse.getY());
		bb.put(mouse.getState().getValue());

		return bb.array();
	}
}
