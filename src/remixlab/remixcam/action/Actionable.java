package remixlab.remixcam.action;

//import remixlab.remixcam.core.Constants.DLAction;

/**
public interface Actionable {
	DLAction action();
	String description();
	public boolean is2D();
	public int dofs();
}
*/

//public interface Actionable <A extends DandelionActionable> {
public interface Actionable <E extends Enum<E>> {
	//A action();
	E action();
	String description();
	public boolean is2D();
	public int dofs();
	E defaultAction();
}
