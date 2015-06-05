package at.ac.tuwien.foop.mouserace.common.network.exceptions;

/**
 * Created by klaus on 6/5/15.
 */
public abstract class NetworkProtocolException extends Exception {
	public NetworkProtocolException() {
		super();
	}

	public NetworkProtocolException(String s) {
		super(s);
	}

	public NetworkProtocolException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public NetworkProtocolException(Throwable throwable) {
		super(throwable);
	}
}
