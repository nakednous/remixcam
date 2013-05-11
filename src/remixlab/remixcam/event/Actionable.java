package remixlab.remixcam.event;

public interface Actionable {
	DLAction action();
	String description();
	public boolean is2D();
}
