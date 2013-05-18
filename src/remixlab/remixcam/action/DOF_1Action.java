package remixlab.remixcam.action;

import remixlab.remixcam.core.Constants;

public enum DOF_1Action implements Actionable, Constants {
  //DOF_0
	NO_ACTION(DLAction.NO_ACTION),
	
	ZOOM(DLAction.ZOOM),
	
	CUSTOM1(DLAction.CUSTOM1),
	CUSTOM2(DLAction.CUSTOM2),
	CUSTOM3(DLAction.CUSTOM3),
	CUSTOM4(DLAction.CUSTOM4);

	@Override
	public DLAction action() {
		return act;
	}

	@Override
	public String description() {
		return this.action().description();
	}
	
	@Override
	public boolean is2D() {
		return act.is2D();
	}

	DLAction act;

	DOF_1Action(DLAction a) {
		act = a;
	}
}
