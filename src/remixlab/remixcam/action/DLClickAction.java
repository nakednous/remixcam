package remixlab.remixcam.action;

import remixlab.remixcam.core.Constants;

public enum DLClickAction implements Actionable, Constants {
	/**
	 * This enum defines mouse click actions to be binded to the mouse.
	 * Actions are defined here, but bindings are defined at the CameraProfile level,
	 * i.e., the scene acts like a bridge between the CameraProfile and proscene low-level classes.
	 */
	NO_CLICK_ACTION(DLAction.NO_CLICK_ACTION),	
	ZOOM_ON_PIXEL(DLAction.ZOOM_ON_PIXEL),	
	ZOOM_TO_FIT(DLAction.ZOOM_TO_FIT),	
	ARP_FROM_PIXEL(DLAction.ARP_FROM_PIXEL),	
	RESET_ARP(DLAction.RESET_ARP),	
	CENTER_FRAME(DLAction.CENTER_FRAME),	
	CENTER_SCENE(DLAction.CENTER_SCENE),	
	SHOW_ALL(DLAction.SHOW_ALL),	
	ALIGN_FRAME(DLAction.ALIGN_FRAME),	
	ALIGN_CAMERA(DLAction.ALIGN_CAMERA),
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

	DLClickAction(DLAction a) {
		act = a;
	}
}
