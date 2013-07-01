/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
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
import remixlab.dandelion.core.Constants.DOF0Action;
import remixlab.tersehandling.duoable.agent.GenericKeyboardAgent;
import remixlab.tersehandling.duoable.event.GenericKeyboardEvent;
import remixlab.tersehandling.duoable.profile.GenericKeyboardProfile;

//TODO find a better name for this
public class KeyboardAgent extends GenericKeyboardAgent<GenericKeyboardProfile<Constants.DOF0Action>> {
	public KeyboardAgent(AbstractScene scn, String n) {
		super(new GenericKeyboardProfile<Constants.DOF0Action>(), scn.terseHandler(), n);
		//profile = new GenericKeyboardProfile<Constants.DOF_0Action>();
		setDefaultGrabber(scn);

		// D e f a u l t s h o r t c u t s
		keyboardProfile().setShortcut('a', DOF0Action.DRAW_AXIS);
		keyboardProfile().setShortcut('f', DOF0Action.DRAW_FRAME_SELECTION_HINT);
		keyboardProfile().setShortcut('g', DOF0Action.DRAW_GRID);
		keyboardProfile().setShortcut('i', DOF0Action.FOCUS_INTERACTIVE_FRAME);
		keyboardProfile().setShortcut(' ', DOF0Action.CAMERA_PROFILE);
		keyboardProfile().setShortcut('e', DOF0Action.CAMERA_TYPE);
		keyboardProfile().setShortcut('h', DOF0Action.GLOBAL_HELP);
		keyboardProfile().setShortcut('H', DOF0Action.CURRENT_CAMERA_PROFILE_HELP);
		keyboardProfile().setShortcut('r', DOF0Action.EDIT_CAMERA_PATH);

		keyboardProfile().setShortcut('s', DOF0Action.INTERPOLATE_TO_FIT_SCENE);
		keyboardProfile().setShortcut('S', DOF0Action.SHOW_ALL);

		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_RIGHT, DOF0Action.MOVE_CAMERA_RIGHT);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_LEFT, DOF0Action.MOVE_CAMERA_LEFT);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_UP, DOF0Action.MOVE_CAMERA_UP);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_DOWN, DOF0Action.MOVE_CAMERA_DOWN);

		keyboardProfile().setShortcut((GenericKeyboardEvent.TH_ALT | GenericKeyboardEvent.TH_SHIFT), 'l',	DOF0Action.MOVE_CAMERA_LEFT);

		/**
		 * // K e y f r a m e s s h o r t c u t k e y s
		 * setAddKeyFrameKeyboardModifier(Event.CTRL);
		 * setDeleteKeyFrameKeyboardModifier(Event.ALT); setPathKey('1', 1);
		 * setPathKey('2', 2); setPathKey('3', 3); setPathKey('4', 4);
		 * setPathKey('5', 5);
		 */
	}
	
	@Override
	public GenericKeyboardEvent<Constants.DOF0Action> feed() {
		return null;
	}

	@Override
	public GenericKeyboardProfile<Constants.DOF0Action> keyboardProfile() {
		return profile;
	}
}
