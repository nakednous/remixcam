package remixlab.remixcam.action;

import remixlab.remixcam.core.Constants;

public enum DOF_3Action implements Actionable, Constants {
  NO_ACTION(DLAction.NO_ACTION),
	
  //DOF_1
	ZOOM(DLAction.ZOOM),
  
	//DOF_2
	ROTATE(DLAction.ROTATE),
	CAD_ROTATE(DLAction.CAD_ROTATE),
	TRANSLATE(DLAction.TRANSLATE),
	MOVE_FORWARD(DLAction.MOVE_FORWARD),
	MOVE_BACKWARD(DLAction.MOVE_BACKWARD),
	LOOK_AROUND(DLAction.LOOK_AROUND),
	SCREEN_ROTATE(DLAction.SCREEN_ROTATE),
	ROLL(DLAction.ROLL),
	DRIVE(DLAction.DRIVE),
	SCREEN_TRANSLATE(DLAction.SCREEN_TRANSLATE),
	ZOOM_ON_REGION(DLAction.ZOOM_ON_REGION),
	
  //DOF_3
	TRANSLATE3(DLAction.TRANSLATE3),	
	FROM_EULER_ANGLES(DLAction.TRANSLATE3),
	
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

	DOF_3Action(DLAction a) {
		act = a;
	}
}
