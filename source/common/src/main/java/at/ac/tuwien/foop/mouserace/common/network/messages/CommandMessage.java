package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.common.network.exceptions.MessageParsingException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by klaus on 6/5/15.
 */
public abstract class CommandMessage extends GameMessage {
	public abstract CommandType getCommandType();

	/**
	 * Parses a byte array to a new instance of this class.
	 *
	 * @return a new instance of this class
	 * @throws EOFException if the input provided is shorter than the expected length
	 */
	public static CommandMessage specificCommandMessageFromByteArray(byte[] data) throws EOFException, MessageParsingException {
		Objects.requireNonNull(data);

		try {
			return specificCommandMessageFromInputStream(new DataInputStream(new ByteArrayInputStream(data)));
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
	public static CommandMessage specificCommandMessageFromInputStream(DataInputStream input) throws MessageParsingException, IOException {
		Objects.requireNonNull(input);

		input.mark(2);
		byte commandTypeByte = input.readByte();
		input.reset();
		final CommandType commandType;
		try {
			commandType = CommandType.fromByte(commandTypeByte);
		} catch (IllegalArgumentException e) {
			throw new MessageParsingException(e);
		}

		switch (commandType) {
			case REGISTER:
				return RegisterMessage.fromInputStream(input);
			case REGISTER_ACK:
				return RegisterAckMessage.fromInputStream(input);
			case READY:
				return ReadyMessage.fromInputStream(input);
			case READY_ACK:
				return ReadyAckMessage.fromInputStream(input);
			case START:
				return StartMessage.fromInputStream(input);
			case USER_INPUT:
				return UserInputMessage.fromInputStream(input);
			case GAME_STATE:
				return GameStateMessage.fromInputStream(input);
			case END:
				return EndMessage.fromInputStream(input);
			case SERVER_ERROR:
				return ServerErrorMessage.fromInputStream(input);
			case CLIENT_ERROR:
				return ClientErrorMessage.fromInputStream(input);
			default:
				throw new IllegalArgumentException(String.format("Unknown command type %s", commandType.name()));
		}
	}
}
