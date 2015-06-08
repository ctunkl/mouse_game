package at.ac.tuwien.foop.mouserace.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import at.ac.tuwien.foop.mouserace.common.domain.Field;
import at.ac.tuwien.foop.mouserace.common.domain.Mouse;
import at.ac.tuwien.foop.mouserace.server.game.Direction;
import at.ac.tuwien.foop.mouserace.server.game.GameState;

public class GameClient implements Runnable, IGameClient {

	private Socket client;
	private InputStream is;
	private OutputStream os;

	private String id;

	public GameClient(Socket client) throws IOException {
		this.client = client;
		this.is = client.getInputStream();
		this.os = client.getOutputStream();

		this.id = client.getInetAddress().toString();
	}

	@Override
	public void run() {
		System.out.println("Run");
	}

	@Override
	public void fieldChosen(Field field) {
		System.out.println(id+" Field chosen");
	}

	@Override
	public void currentState(GameState state) {
	}

	@Override
	public void gameEnded(Mouse winner) {
		System.out.println(id+" ended");
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
