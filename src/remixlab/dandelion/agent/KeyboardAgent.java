/**
 *                     RemixCam (version 0.70.0)      
 *      Copyright (c) 2013 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This java library provides classes to ease the creation of interactive 3D
 * scenes in various languages and frameworks such as JOGL, WebGL and Processing.
 * 
 * This source file is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * A copy of the GNU General Public License is available on the World Wide Web
 * at <http://www.gnu.org/copyleft/gpl.html>. You can also obtain it by
 * writing to the Free Software Foundation, 51 Franklin Street, Suite 500
 * Boston, MA 02110-1335, USA.
 */

package remixlab.dandelion.agent;

import remixlab.dandelion.core.*;
import remixlab.dandelion.core.Constants.DOF_0Action;
import remixlab.dandelion.event.KeyboardEvent;
import remixlab.dandelion.profile.KeyboardProfile;
import remixlab.duoable.agent.AbstractKeyboardAgent;

public class KeyboardAgent extends AbstractKeyboardAgent {
	public KeyboardAgent(AbstractScene scn, String n) {
		super(scn, n);
		profile = new KeyboardProfile();
		setDefaultGrabber(scn);

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

		keyboardProfile().setShortcut(KeyboardEvent.RIGHT, DOF_0Action.MOVE_CAMERA_RIGHT);
		keyboardProfile().setShortcut(KeyboardEvent.LEFT, DOF_0Action.MOVE_CAMERA_LEFT);
		keyboardProfile().setShortcut(KeyboardEvent.UP, DOF_0Action.MOVE_CAMERA_UP);
		keyboardProfile().setShortcut(KeyboardEvent.DOWN, DOF_0Action.MOVE_CAMERA_DOWN);

		keyboardProfile().setShortcut((KeyboardEvent.ALT | KeyboardEvent.SHIFT), 'l',	DOF_0Action.MOVE_CAMERA_LEFT);

		/**
		 * // K e y f r a m e s s h o r t c u t k e y s
		 * setAddKeyFrameKeyboardModifier(Event.CTRL);
		 * setDeleteKeyFrameKeyboardModifier(Event.ALT); setPathKey('1', 1);
		 * setPathKey('2', 2); setPathKey('3', 3); setPathKey('4', 4);
		 * setPathKey('5', 5);
		 */
	}
	
	@Override
	public KeyboardEvent feed() {
		return null;
	}

	@Override
	public KeyboardProfile keyboardProfile() {
		return (KeyboardProfile)profile;
	}
}
