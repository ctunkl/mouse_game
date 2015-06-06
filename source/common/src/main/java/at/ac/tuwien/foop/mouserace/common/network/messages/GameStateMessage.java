package at.ac.tuwien.foop.mouserace.common.network.messages;

import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.common.domain.Wind;
import at.ac.tuwien.foop.mouserace.common.network.exceptions.MessageParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Created by klaus on 6/5/15.
 */
public class GameStateMessage extends CommandMessage {
	private static final Logger logger = LoggerFactory.getLogger(GameStateMessage.class.getName());

	public static final CommandType COMMAND_TYPE = CommandType.GAME_STATE;

	private final Mouse[] mice;
	private final Wind wind;

	public GameStateMessage(Wind wind, Mouse... mice) {
		this.mice = Objects.requireNonNull(mice);

		if (mice.length > NetworkLimits.GAME_STATE_MAX_MICE_LENGTH)
			throw new IllegalArgumentException(String.format("no more than %d mice allowed", NetworkLimits.GAME_STATE_MAX_MICE_LENGTH));

		this.wind = Objects.requireNonNull(wind);
	}

	/**
	 * Parses a byte array to a new instance of this class.
	 *
	 * @return a new instance of this class
	 * @throws EOFException if the input provided is shorter than the expected length
	 */
	public static GameStateMessage fromByteArray(byte[] data) throws EOFException, MessageParsingException {
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
	public static GameStateMessage fromInputStream(DataInput input) throws MessageParsingException, IOException {
		Objects.requireNonNull(input);

		byte commandType = input.readByte();
		if (commandType != COMMAND_TYPE.getValue())
			throw new MessageParsingException(
					String.format("wrong command type byte: expected: 0x%02X; found: 0x%02X",
							COMMAND_TYPE.getValue(), commandType));

		int miceLength = input.readUnsignedByte();
		Mouse[] mice = new Mouse[miceLength];
		for (int i = 0; i < mice.length; i++) {
			MouseStructure ms = MouseStructure.fromInputStream(input);
			mice[i] = ms.getMouse();
		}

		Wind wind = new Wind();
		wind.setSpeedX(input.readByte());
		wind.setSpeedY(input.readByte());

		return new GameStateMessage(wind, mice);
	}

	public Mouse[] getMice() {
		return mice;
	}

	public Wind getWind() {
		return wind;
	}

	@Override
	public byte[] toByteArray() {

		ByteBuffer bb = ByteBuffer.allocate(1 /* CMD */ + 1 /* MLEN */ + mice.length * MouseStructure.STRUCTURE_LENGTH + 1 /* WINDX */ + 1 /* WINDY */);

		bb.put(getCommandType().getValue());
		bb.put((byte) mice.length);

		for (Mouse m : mice) {
			bb.put(new MouseStructure(m).toByteArray());
		}

		bb.put(wind.getSpeedX());
		bb.put(wind.getSpeedY());

		return bb.array();
	}

	@Override
	public CommandType getCommandType() {
		return COMMAND_TYPE;
	}
}
