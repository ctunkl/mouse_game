package at.ac.tuwien.foop.mouserace.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.base.MoreObjects;

import at.ac.tuwien.foop.mouserace.common.domain.Game;
import at.ac.tuwien.foop.mouserace.server.game.GameEngine;
import at.ac.tuwien.foop.mouserace.server.game.GameOptions;

public class GameServer implements Runnable {

	private GameEngine engine;

	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private List<IGameClient> players;

	private ServerState state;

	private static final Integer MIN_PLAYERS = 0;
	private static final Integer TIMEOUT_MILLIS = 5000;
	private Timer waitingTimer;

	private static final Integer DEFAULT_PORT = 9000;
	private Integer port;

	public GameServer(Game game, GameOptions options) throws IOException {
		this.engine = new GameEngine(game, options);

		this.players = new ArrayList<>();
		this.executorService = Executors.newCachedThreadPool();
		this.state = ServerState.WAITING;
	}

	@Override
	public void run() {
		try {
			this.run(MoreObjects.firstNonNull(port, DEFAULT_PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);

		/*startTimer();

		while(this.state == ServerState.WAITING) {
			try {
				Socket clientSocket = serverSocket.accept();

				System.out.println("Client connected. Resetting timeout...");

				restartTimer();

				GameClient client = new GameClient(clientSocket);
				client.setId(String.format("Player #%d", players.size()));

				this.executorService.execute(client);

			} catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}*/

		GameClient client1 = new GameClient();
		client1.setId("John");
		this.players.add(client1);

		GameClient client2 = new GameClient();
		client2.setId("Jane");
		this.players.add(client2);


		System.out.println("Enough players. Game ready");

		this.players.forEach(engine::registerPlayer);
		engine.startGame();
	}

	public void addClient() {

	}

	private void startTimer() {
		this.waitingTimer = new Timer();
		this.waitingTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				checkPlayerCount();
			}
		}, TIMEOUT_MILLIS, TIMEOUT_MILLIS);
	}

	private void checkPlayerCount() {
		if(this.players.size() < MIN_PLAYERS)
			return;

		this.state = ServerState.READY;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.waitingTimer.cancel();
	}

	private void restartTimer() {
		this.waitingTimer.cancel();
		startTimer();
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
