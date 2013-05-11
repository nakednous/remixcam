package remixlab.remixcam.event;

public enum DLKeyboardAction implements Actionable {
	/**
	 * Defines the different actions that can be associated with a specific
	 * keyboard key.
	 */
	DRAW_AXIS(DLAction.DRAW_AXIS),
	DRAW_GRID(DLAction.DRAW_GRID),
	CAMERA_PROFILE(DLAction.CAMERA_PROFILE),
	CAMERA_TYPE(DLAction.CAMERA_TYPE),
	CAMERA_KIND(DLAction.CAMERA_KIND),
	ANIMATION(DLAction.ANIMATION),
	ARP_FROM_PIXEL(DLAction.ARP_FROM_PIXEL),
	RESET_ARP(DLAction.RESET_ARP),
	GLOBAL_HELP(DLAction.GLOBAL_HELP),
	CURRENT_CAMERA_PROFILE_HELP(DLAction.CURRENT_CAMERA_PROFILE_HELP),
	EDIT_CAMERA_PATH(DLAction.EDIT_CAMERA_PATH),
	FOCUS_INTERACTIVE_FRAME(DLAction.FOCUS_INTERACTIVE_FRAME),
	DRAW_FRAME_SELECTION_HINT(DLAction.DRAW_FRAME_SELECTION_HINT),
	CONSTRAIN_FRAME(DLAction.CONSTRAIN_FRAME),
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

	DLKeyboardAction(DLAction a) {
		act = a;
	}
}
