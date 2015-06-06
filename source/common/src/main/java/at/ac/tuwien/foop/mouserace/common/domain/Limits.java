package at.ac.tuwien.foop.mouserace.common.domain;

import at.ac.tuwien.foop.mouserace.common.network.messages.NetworkLimits;

/**
 * <p>
 * This class contains the limits of the various data types in the domain objects. These limits are closely coupled
 * with the network implementation. If you change these limits you have to adopt the changes in the network
 * implementation.
 * </p><p>
 * Created by klaus on 6/6/15.
 * </p>
 */
public final class Limits {
	public static final int FIELD_MIN_WIDTH_HEIGHT = 3;
	public static final int FIELD_MAX_WIDTH = getMaxUnsignedValueFromByteCount(NetworkLimits.FIELD_WIDTH_HEIGHT_BYTES);
	public static final int FIELD_MAX_HEIGHT =  getMaxUnsignedValueFromByteCount(NetworkLimits.FIELD_WIDTH_HEIGHT_BYTES);

	public static final int FIGURE_MAX_ID = getMaxUnsignedValueFromByteCount(NetworkLimits.FIGURE_ID_BYTES);
	public static final int FIGURE_MAX_X = FIELD_MAX_WIDTH - 1;
	public static final int FIGURE_MAX_Y = FIELD_MAX_HEIGHT - 1;

	public static final int CELL_MAX_X = FIGURE_MAX_X;
	public static final int CELL_MAX_Y = FIGURE_MAX_Y;

	public static final int WIND_MAX_SPEED = 5;
	public static final int WIND_MIN_SPEED = -WIND_MAX_SPEED;

	private static int getMaxUnsignedValueFromByteCount(int byteCount) {
		return (1 << (byteCount * 8)) - 1;
	}
}
