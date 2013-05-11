package remixlab.remixcam.event;

public enum DLDeviceAction implements Actionable {
	/**
	 * This enum defines mouse actions (click + drag) to be binded to the mouse.
	 * Actions are defined here, but bindings are defined at the CameraProfile level,
	 * i.e., the scene acts like a bridge between the CameraProfile and proscene low-level classes.
	 */
	NO_DEVICE_ACTION(DLAction.NO_DEVICE_ACTION),	
	ROTATE(DLAction.ROTATE),	
	CAD_ROTATE(DLAction.CAD_ROTATE),	
	ZOOM(DLAction.ZOOM),	
	TRANSLATE(DLAction.TRANSLATE),	
	MOVE_FORWARD(DLAction.MOVE_FORWARD),	
	MOVE_BACKWARD(DLAction.MOVE_BACKWARD),	
	LOOK_AROUND(DLAction.LOOK_AROUND),	
	SCREEN_ROTATE(DLAction.SCREEN_ROTATE),	
	ROLL(DLAction.ROLL),	
	DRIVE(DLAction.DRIVE),	
	SCREEN_TRANSLATE(DLAction.SCREEN_TRANSLATE),	
	ZOOM_ON_REGION(DLAction.ZOOM_ON_REGION),
	CUSTOM1(DLAction.CUSTOM1),
	CUSTOM2(DLAction.CUSTOM2),
	CUSTOM1_2D(DLAction.CUSTOM1_2D),
	CUSTOM2_2D(DLAction.CUSTOM2_2D);

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

	DLDeviceAction(DLAction a) {
		act = a;
	}
}
