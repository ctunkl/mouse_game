package at.ac.tuwien.foop.mouserace.common.network.exceptions;

/**
 * Created by klaus on 6/5/15.
 */
public class MessageParsingException extends NetworkProtocolException {
	public MessageParsingException() {
		super();
	}

	public MessageParsingException(String s) {
		super(s);
	}

	public MessageParsingException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public MessageParsingException(Throwable throwable) {
		super(throwable);
	}
}
