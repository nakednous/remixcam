package remixlab.remixcam.action;

import remixlab.remixcam.core.Constants.DLAction;

//public interface VActionable <DA extends DandelionActionable> {
public interface VActionable {
	//DA action();
	DLAction action();
	String description();
	public boolean is2D();
	public int dofs();
}
