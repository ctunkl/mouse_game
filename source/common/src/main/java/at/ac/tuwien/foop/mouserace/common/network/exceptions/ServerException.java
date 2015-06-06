package at.ac.tuwien.foop.mouserace.common.network.exceptions;

import at.ac.tuwien.foop.mouserace.common.network.messages.CommandType;

/**
 * Created by klaus on 6/5/15.
 */
public class ServerException extends NetworkProtocolException {
	public ServerException() {
		super();
	}

	public ServerException(String s) {
		super(s);
	}

	public ServerException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public ServerException(Throwable throwable) {
		super(throwable);
	}

	public CommandType getCommandType() {
		return CommandType.SERVER_ERROR;
	}
}
