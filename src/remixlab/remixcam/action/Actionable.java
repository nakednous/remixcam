package remixlab.remixcam.action;

import remixlab.remixcam.core.Constants;

public interface Actionable extends Constants {
	DLAction action();
	String description();
	public boolean is2D();
}
