package at.ac.tuwien.foop.mouserace.server.game;

public class GameOptions {
	private int tickMillis = 1000;
	private int sniffingTime = 5;
	private int confusedTime = 7;

	public GameOptions() {
	}

	public int getTickMillis() {
		return tickMillis;
	}

	public void setTickMillis(int tickMillis) {
		this.tickMillis = tickMillis;
	}

	public int getSniffingTime() {
		return sniffingTime;
	}

	public void setSniffingTime(int sniffingTime) {
		this.sniffingTime = sniffingTime;
	}

	public int getConfusedTime() {
		return confusedTime;
	}

	public void setConfusedTime(int confusedTime) {
		this.confusedTime = confusedTime;
	}
}
