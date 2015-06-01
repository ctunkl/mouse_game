package at.ac.tuwien.foop.mouserace.server;

public class GameOptions {
	private int tickSeconds = 3;
	private int sniffingTime = 5;
	private int confusedTime = 3;

	public GameOptions() {
	}

	public int getTickSeconds() {
		return tickSeconds;
	}

	public void setTickSeconds(int tickSeconds) {
		this.tickSeconds = tickSeconds;
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
