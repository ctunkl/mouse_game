package at.ac.tuwien.foop.mouserace.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import at.ac.tuwien.foop.mouserace.common.domain.Field;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.common.network.exceptions.MessageParsingException;
import at.ac.tuwien.foop.mouserace.common.network.messages.CommandMessage;
import at.ac.tuwien.foop.mouserace.server.game.Direction;
import at.ac.tuwien.foop.mouserace.server.game.GameState;

public class GameClient implements Runnable, IGameClient {

	private Socket client;
	private InputStream is;
	private OutputStream os;

	private DataInputStream in;
	private PrintWriter out;

	private String id;

	private Thread thread;

	private boolean stop = false;

	public GameClient(Socket client) throws IOException {
		this.client = client;
		this.is = client.getInputStream();
		this.os = client.getOutputStream();

		this.out = new PrintWriter(os);
		this.in = new DataInputStream(new BufferedInputStream(client.getInputStream()));

		this.id = client.getInetAddress().toString();
	}

	public GameClient() {

	}

	@Override
	public void run() {
		System.out.println("Run");
		String s;
		while(stop == false) {
			try {
				CommandMessage message = CommandMessage.specificCommandMessageFromInputStream(this.in);
				handleMessage(message);
			} catch (MessageParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handleMessage(CommandMessage message) {
		System.out.println("Received message: "+message.toHexadecimalString());
	}

	@Override
	public void fieldChosen(Field field) {
		System.out.println(id+" Field chosen");
		//this.thread = new Thread(this);
		//this.thread.start();
	}

	@Override
	public void currentState(GameState state) {
		//System.out.println(id+" received current state");
	}

	@Override
	public void gameEnded(Mouse winner) {
		System.out.println(id+" ended");
		this.stop = true;

	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Direction getDirection() {
		// TODO Auto-generated method stub
		return null;
	}
}
