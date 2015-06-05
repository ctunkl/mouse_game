package at.ac.tuwien.foop.mouserace.common.domain;

/**
 * Created by klaus on 5/30/15.
 */
public class Wind {
	public static final byte MIN_WIND = -5;
	public static final byte MAX_WIND = 5;

	private byte speedX;
	private byte speedY;

	public Wind() {
		speedX = 0;
		speedY = 0;
	}

	public byte getSpeedX() {
		return speedX;
	}

	public void setSpeedX(byte speedX) {
		if (speedX < MIN_WIND || speedX > MAX_WIND)
			throw new IllegalArgumentException(String.format("Specified speed is out of range [%d, %d]", MIN_WIND, MAX_WIND));

		this.speedX = speedX;
	}

	public byte getSpeedY() {
		return speedY;
	}

	public void setSpeedY(byte speedY) {
		if (speedY < MIN_WIND || speedY > MAX_WIND)
			throw new IllegalArgumentException(String.format("Specified speed is out of range [%d, %d]", MIN_WIND, MAX_WIND));

		this.speedY = speedY;
	}
}
