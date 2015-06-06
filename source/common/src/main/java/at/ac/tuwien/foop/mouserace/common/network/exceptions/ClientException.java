package at.ac.tuwien.foop.mouserace.common.network.exceptions;

import at.ac.tuwien.foop.mouserace.common.network.messages.CommandType;

/**
 * Created by klaus on 6/5/15.
 */
public class ClientException extends NetworkProtocolException {
	public ClientException() {
		super();
	}

	public ClientException(String s) {
		super(s);
	}

	public ClientException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public ClientException(Throwable throwable) {
		super(throwable);
	}

	public CommandType getCommandType() {
		return CommandType.CLIENT_ERROR;
	}
}
