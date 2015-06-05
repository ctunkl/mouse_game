package at.ac.tuwien.foop.mouserace.common.network.messages;

/**
 * Created by klaus on 6/5/15.
 */
public enum CommandType {
	REGISTER((byte) 0x01),
	REGISTER_ACK((byte) 0x02),
	READY((byte) 0x03),
	READY_ACK((byte) 0x04),
	START((byte) 0x05),
	USER_INPUT((byte) 0x06),
	GAME_STATE((byte) 0x07),
	END((byte) 0x08),
	SERVER_ERROR((byte) 0x09),
	CLIENT_ERROR((byte) 0x0A);

	private byte value;

	CommandType(byte value) {
		this.value = value;
	}

	public static CommandType fromByte(byte data) throws IllegalArgumentException {
		for (CommandType commandType : CommandType.values()) {
			if (commandType.value == data)
				return commandType;
		}
		throw new IllegalArgumentException(String.format("Unknown command 0x%02X", data));
	}

	public byte getValue() {
		return this.value;
	}
}
