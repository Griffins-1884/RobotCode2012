package driveSystems;

public class Movement {
	public final Vector translation;
	public final double rotation;
	public final boolean isAbsolute;
	public Movement(Vector translation, double rotation) {
		this(translation, rotation, false);
	}
	public Movement(Vector translation, double rotation, boolean isAbsolute) {
		this.translation = translation;
		this.rotation = rotation;
		this.isAbsolute = isAbsolute;
	}
	public Movement asRelative(double angle) {
		if(!isAbsolute) {
			return this;
		}
		return new Movement(translation.rotate(angle), rotation, false);
	}
	public Movement asAbsolute(double angle) {
		if(isAbsolute) {
			return this;
		}
		return new Movement(translation.rotate(-angle), rotation, true);
	}
}