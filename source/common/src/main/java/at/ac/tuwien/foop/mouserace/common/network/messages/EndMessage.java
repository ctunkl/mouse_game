package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.domain.Figure;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.common.network.exceptions.MessageParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Created by klaus on 6/5/15.
 */
public class EndMessage extends CommandMessage {
	private static final Logger logger = LoggerFactory.getLogger(EndMessage.class.getName());

	public static final int MAX_MICE_LENGTH = 0xFF;
	public static final CommandType COMMAND_TYPE = CommandType.END;

	private final Mouse[] winningMice;

	public EndMessage(Mouse... winningMice) {
		this.winningMice = Objects.requireNonNull(winningMice);

		if (winningMice.length > MAX_MICE_LENGTH)
			throw new IllegalArgumentException(String.format("no more than %d winningMice allowed", MAX_MICE_LENGTH));
	}

	/**
	 * Parses a byte array to a new instance of this class.
	 *
	 * @return a new instance of this class
	 * @throws EOFException if the input provided is shorter than the expected length
	 */
	public static EndMessage fromByteArray(byte[] data) throws EOFException, MessageParsingException {
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
	public static EndMessage fromInputStream(DataInput input) throws MessageParsingException, IOException {
		Objects.requireNonNull(input);

		byte commandType = input.readByte();
		if (commandType != COMMAND_TYPE.getValue())
			throw new MessageParsingException(
					String.format("wrong command type byte: expected: 0x%02X; found: 0x%02X",
							COMMAND_TYPE.getValue(), commandType));

		int length = input.readUnsignedByte();
		byte[] winningMiceBytes = new byte[length];
		input.readFully(winningMiceBytes);

		Mouse[] winningMice = new Mouse[length];

		// Convert the bytes to the enumeration
		for (int i = 0; i < winningMiceBytes.length; i++) {
			try {
				winningMice[i] = new Mouse(Byte.toUnsignedInt(winningMiceBytes[i]));
			} catch (IllegalArgumentException e) {
				throw new MessageParsingException(e);
			}
		}

		return new EndMessage(winningMice);
	}

	public Mouse[] getWinningMice() {
		return winningMice;
	}

	@Override
	public byte[] toByteArray() {

		ByteBuffer bb = ByteBuffer.allocate(1 /* CMD */ + 1 /* LEN */ + winningMice.length);

		bb.put(getCommandType().getValue());
		bb.put((byte) winningMice.length);

		for (Mouse m : winningMice) {
			bb.put((byte)m.getId());
		}

		return bb.array();
	}

	@Override
	public CommandType getCommandType() {
		return COMMAND_TYPE;
	}
}
