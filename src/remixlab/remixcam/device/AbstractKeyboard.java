package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.core.Constants.DOF_0Action;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.*;

public class AbstractKeyboard extends AbstractDevice {
	KeyboardProfile profile;

	public AbstractKeyboard(AbstractScene scn, String n) {
		super(scn, n);
		profile = new KeyboardProfile();

		// D e f a u l t s h o r t c u t s
		profile.setShortcut('a', DOF_0Action.DRAW_AXIS);
		profile.setShortcut('f', DOF_0Action.DRAW_FRAME_SELECTION_HINT);
		profile.setShortcut('g', DOF_0Action.DRAW_GRID);
		profile.setShortcut('i', DOF_0Action.FOCUS_INTERACTIVE_FRAME);
		profile.setShortcut(' ', DOF_0Action.CAMERA_PROFILE);
		profile.setShortcut('e', DOF_0Action.CAMERA_TYPE);
		profile.setShortcut('h', DOF_0Action.GLOBAL_HELP);
		profile.setShortcut('H', DOF_0Action.CURRENT_CAMERA_PROFILE_HELP);
		profile.setShortcut('r', DOF_0Action.EDIT_CAMERA_PATH);

		profile.setShortcut('s', DOF_0Action.INTERPOLATE_TO_FIT_SCENE);
		profile.setShortcut('S', DOF_0Action.SHOW_ALL);

		profile.setShortcut(DLKeyEvent.RIGHT, DOF_0Action.MOVE_CAMERA_RIGHT);
		profile.setShortcut(DLKeyEvent.LEFT, DOF_0Action.MOVE_CAMERA_LEFT);
		profile.setShortcut(DLKeyEvent.UP, DOF_0Action.MOVE_CAMERA_UP);
		profile.setShortcut(DLKeyEvent.DOWN, DOF_0Action.MOVE_CAMERA_DOWN);

		profile.setShortcut((DLKeyEvent.ALT | DLKeyEvent.SHIFT), 'l',
				DOF_0Action.MOVE_CAMERA_LEFT);

		/**
		 * // K e y f r a m e s s h o r t c u t k e y s
		 * setAddKeyFrameKeyboardModifier(Event.CTRL);
		 * setDeleteKeyFrameKeyboardModifier(Event.ALT); setPathKey('1', 1);
		 * setPathKey('2', 2); setPathKey('3', 3); setPathKey('4', 4);
		 * setPathKey('5', 5);
		 */
	}

	public KeyboardProfile keyboardProfile() {
		return profile;
	}

	public void setKeyboardProfile(KeyboardProfile kprofile) {
		profile = kprofile;
	}

	// /**
	@Override
	public void handle(DLEvent<?> event) {
		profile.handle(event);
		event.enqueue(scene);
	}

	public void handleKey(DLEvent<?> event) {
		profile.handleKey(event);
		event.enqueue(scene);
	}
	// */

	/**
	 * @Override public void handle(DLEvent event) { profile.handle(event);
	 *           event.enqueue(scene); }
	 * 
	 *           // /** public void handleKey(DLEvent event) {
	 *           profile.handleKey(event); event.enqueue(scene); } //
	 */
}
