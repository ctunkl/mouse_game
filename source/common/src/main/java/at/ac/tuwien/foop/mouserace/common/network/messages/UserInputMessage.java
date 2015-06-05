package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.network.exceptions.MessageParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Created by klaus on 6/5/15.
 */
public class UserInputMessage extends CommandMessage {
	private static final Logger logger = LoggerFactory.getLogger(UserInputMessage.class.getName());

	public static final int MAX_BUTTON_LENGTH = 0xFF;
	public static final CommandType COMMAND_TYPE = CommandType.USER_INPUT;

	private final ButtonType[] buttons;

	public UserInputMessage(ButtonType... buttons) {
		this.buttons = Objects.requireNonNull(buttons);

		if (buttons.length > MAX_BUTTON_LENGTH)
			throw new IllegalArgumentException(String.format("no more than %d buttons allowed", MAX_BUTTON_LENGTH));
	}

	/**
	 * Parses a byte array to a new instance of this class.
	 *
	 * @return a new instance of this class
	 * @throws EOFException if the input provided is shorter than the expected length
	 */
	public static UserInputMessage fromByteArray(byte[] data) throws EOFException, MessageParsingException {
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
	public static UserInputMessage fromInputStream(DataInput input) throws MessageParsingException, IOException {
		Objects.requireNonNull(input);

		byte commandType = input.readByte();
		if (commandType != COMMAND_TYPE.getValue())
			throw new MessageParsingException(
					String.format("wrong command type byte: expected: 0x%02X; found: 0x%02X",
							COMMAND_TYPE.getValue(), commandType));

		int length = input.readUnsignedByte();
		byte[] buttonsBytes = new byte[length];
		input.readFully(buttonsBytes);

		ButtonType[] buttons = new ButtonType[length];

		// Convert the bytes to the enumeration
		for (int i = 0; i < buttonsBytes.length; i++) {
			try {
				buttons[i] = ButtonType.fromByte(buttonsBytes[i]);
			} catch (IllegalArgumentException e) {
				logger.debug(e.getMessage());
			}
		}

		return new UserInputMessage(buttons);
	}

	public ButtonType[] getButtons() {
		return buttons;
	}

	@Override
	public byte[] toByteArray() {

		ByteBuffer bb = ByteBuffer.allocate(1 /* CMD */ + 1 /* LEN */ + buttons.length);

		bb.put(getCommandType().getValue());
		bb.put((byte) buttons.length);

		for (ButtonType m : buttons) {
			bb.put(m.getValue());
		}

		return bb.array();
	}

	@Override
	public CommandType getCommandType() {
		return COMMAND_TYPE;
	}
}
