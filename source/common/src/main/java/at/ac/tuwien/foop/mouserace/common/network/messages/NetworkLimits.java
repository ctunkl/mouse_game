package at.ac.tuwien.foop.mouserace.common.network.messages;

/**
 * <p>
 * This class contains the limits of the various data types in the network messages. These limits are closely coupled
 * with the network implementation. If you change these limits you have to adopt the changes in the network
 * implementation.
 * </p><p>
 * Created by klaus on 6/6/15.
 * </p>
 */
public final class NetworkLimits {

	public static final int READY_MAX_FIGURES_LENGTH = 0xFF;
	public static final int GAME_STATE_MAX_MICE_LENGTH = 0xFF;
	public static final int USER_INPUT_MAX_BUTTON_LENGTH = 0xFF;
	public static final int END_MAX_MICE_LENGTH = 0xFF;

	public static final int FIELD_WIDTH_HEIGHT_BYTES = 2;
	public static final int FIGURE_ID_BYTES = 1;
}
