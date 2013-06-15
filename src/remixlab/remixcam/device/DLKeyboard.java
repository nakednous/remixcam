package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.core.Constants.DOF_0Action;
import remixlab.remixcam.event.*;
import remixlab.remixcam.ownevent.DLKeyEvent;
import remixlab.remixcam.profile.*;

public class DLKeyboard extends AbstractKeyboard {
	public DLKeyboard(AbstractScene scn, String n) {
		super(scn, n);
		profile = new KeyboardProfile();

		// D e f a u l t s h o r t c u t s
		keyboardProfile().setShortcut('a', DOF_0Action.DRAW_AXIS);
		keyboardProfile().setShortcut('f', DOF_0Action.DRAW_FRAME_SELECTION_HINT);
		keyboardProfile().setShortcut('g', DOF_0Action.DRAW_GRID);
		keyboardProfile().setShortcut('i', DOF_0Action.FOCUS_INTERACTIVE_FRAME);
		keyboardProfile().setShortcut(' ', DOF_0Action.CAMERA_PROFILE);
		keyboardProfile().setShortcut('e', DOF_0Action.CAMERA_TYPE);
		keyboardProfile().setShortcut('h', DOF_0Action.GLOBAL_HELP);
		keyboardProfile().setShortcut('H', DOF_0Action.CURRENT_CAMERA_PROFILE_HELP);
		keyboardProfile().setShortcut('r', DOF_0Action.EDIT_CAMERA_PATH);

		keyboardProfile().setShortcut('s', DOF_0Action.INTERPOLATE_TO_FIT_SCENE);
		keyboardProfile().setShortcut('S', DOF_0Action.SHOW_ALL);

		keyboardProfile().setShortcut(DLKeyEvent.RIGHT, DOF_0Action.MOVE_CAMERA_RIGHT);
		keyboardProfile().setShortcut(DLKeyEvent.LEFT, DOF_0Action.MOVE_CAMERA_LEFT);
		keyboardProfile().setShortcut(DLKeyEvent.UP, DOF_0Action.MOVE_CAMERA_UP);
		keyboardProfile().setShortcut(DLKeyEvent.DOWN, DOF_0Action.MOVE_CAMERA_DOWN);

		keyboardProfile().setShortcut((DLKeyEvent.ALT | DLKeyEvent.SHIFT), 'l',	DOF_0Action.MOVE_CAMERA_LEFT);

		/**
		 * // K e y f r a m e s s h o r t c u t k e y s
		 * setAddKeyFrameKeyboardModifier(Event.CTRL);
		 * setDeleteKeyFrameKeyboardModifier(Event.ALT); setPathKey('1', 1);
		 * setPathKey('2', 2); setPathKey('3', 3); setPathKey('4', 4);
		 * setPathKey('5', 5);
		 */
	}

	@Override
	public KeyboardProfile keyboardProfile() {
		return (KeyboardProfile)profile;
	}

	// /**
	@Override
	public void handle(DLEvent<?> event) {
		if(event == null)	return;
		profile.handle(event);
		if( scene.isDeviceRegistered(this) ) event.enqueue(scene);
	}

	public void handleKey(DLEvent<?> event) {
		if(event == null)	return;
		profile.handleKey(event);
		if( scene.isDeviceRegistered(this) ) event.enqueue(scene);
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
