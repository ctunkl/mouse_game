package at.ac.tuwien.foop.mouserace.common.domain;

/**
 * Created by klaus on 5/30/15.
 */
public class Wind {
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
		if (speedX < Limits.WIND_MIN_SPEED || speedX > Limits.WIND_MAX_SPEED)
			throw new IllegalArgumentException(String.format("Specified speed is out of range [%d, %d]", Limits.WIND_MIN_SPEED, Limits.WIND_MAX_SPEED));

		this.speedX = speedX;
	}

	public byte getSpeedY() {
		return speedY;
	}

	public void setSpeedY(byte speedY) {
		if (speedY < Limits.WIND_MIN_SPEED || speedY > Limits.WIND_MAX_SPEED)
			throw new IllegalArgumentException(String.format("Specified speed is out of range [%d, %d]", Limits.WIND_MIN_SPEED, Limits.WIND_MAX_SPEED));

		this.speedY = speedY;
	}
}
