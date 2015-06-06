package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.network.exceptions.MessageParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

/**
 * Created by klaus on 6/5/15.
 */
public class ReadyAckMessage extends CommandMessage {
	private static final Logger logger = LoggerFactory.getLogger(ReadyAckMessage.class.getName());

	public static final CommandType COMMAND_TYPE = CommandType.READY_ACK;

	/**
	 * Parses a byte array to a new instance of this class.
	 *
	 * @return a new instance of this class
	 * @throws EOFException if the input provided is shorter than the expected length
	 */
	public static ReadyAckMessage fromByteArray(byte[] data) throws EOFException, MessageParsingException {
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
	public static ReadyAckMessage fromInputStream(DataInput input) throws MessageParsingException, IOException {
		Objects.requireNonNull(input);

		byte commandType = input.readByte();
		if (commandType != COMMAND_TYPE.getValue())
			throw new MessageParsingException(
					String.format("wrong command type byte: expected: 0x%02X; found: 0x%02X",
							COMMAND_TYPE.getValue(), commandType));

		return new ReadyAckMessage();
	}

	@Override
	public byte[] toByteArray() {
		return new byte[]{getCommandType().getValue()};
	}

	@Override
	public CommandType getCommandType() {
		return COMMAND_TYPE;
	}
}
