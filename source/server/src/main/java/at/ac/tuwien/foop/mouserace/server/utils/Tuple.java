package at.ac.tuwien.foop.mouserace.server.utils;

public class Tuple<A, B> {
	public final A left;
	public final B right;

	public Tuple(A left, B right) {
		this.left = left;
		this.right = right;
	}
}
