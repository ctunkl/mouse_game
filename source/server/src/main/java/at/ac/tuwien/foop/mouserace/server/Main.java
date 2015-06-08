package at.ac.tuwien.foop.mouserace.server;

import java.io.DataOutputStream;
import java.net.Socket;

import at.ac.tuwien.foop.mouserace.common.domain.Game;
import at.ac.tuwien.foop.mouserace.common.domain.Wind;
import at.ac.tuwien.foop.mouserace.common.network.messages.RegisterMessage;
import at.ac.tuwien.foop.mouserace.server.game.GameOptions;
import at.ac.tuwien.foop.mouserace.server.utils.Games;

/**
 * Created by klaus on 5/30/15.
 */
public class Main {
	public static void main(String[] args) {

		Game game = Games.createWithSimpleField();
		Wind wind = new Wind();
		wind.setSpeedX((byte)4);
		wind.setSpeedY((byte)0);
		game.setWind(wind);

		GameOptions options = new GameOptions();
		options.setTickMillis(300);
		options.setConfusedTime(4);

		addPlayer(1000);
		addPlayer(2000);

		try {


			GameServer server = new GameServer(game, options);
			server.run(8000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addPlayer(long delay) {
		new Thread() {
			@Override
			public void run() {
				System.out.println("Thread started");

				try {
					Thread.sleep(delay);

					Socket s = new Socket("localhost", 8000);
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					Thread.sleep(2000);
					dos.write(new RegisterMessage().toByteArray());
					dos.flush();
					System.out.println("Sent RegisterMessage");
				} catch (Exception e) {
				}

			}
		}.start();
	}
}
