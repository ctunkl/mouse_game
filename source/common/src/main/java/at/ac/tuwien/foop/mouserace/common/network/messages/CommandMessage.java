package at.ac.tuwien.foop.mouserace.common.network.messages;

/**
 * Created by klaus on 6/5/15.
 */
public abstract class CommandMessage extends GameMessage {
	public abstract CommandType getCommandType();
}
