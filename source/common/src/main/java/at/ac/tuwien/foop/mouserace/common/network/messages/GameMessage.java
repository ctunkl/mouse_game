package at.ac.tuwien.foop.mouserace.common.network.messages;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * <p>
 * All messages are implemented according to the network protocol, see
 * <a href="https://docs.google.com/document/d/13wOzBx6HrblbQI7l3-AVBCynF_FHXis3zk69oH8xGFY">
 * the GDrive Document</a>
 * </p>
 */
public abstract class GameMessage {
	public static final Charset CHARSET = Charset.defaultCharset();
	public static final ByteOrder NETWORK_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Function to get a hex string from a byte array. Copied from <a href="http://stackoverflow.com/a/9855338">http://stackoverflow.com/a/9855338</a>.
	 */
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public abstract byte[] toByteArray();

	public String toHexadecimalString() {
		byte[] bytes = toByteArray();
		return bytesToHex(bytes);
	}
}
