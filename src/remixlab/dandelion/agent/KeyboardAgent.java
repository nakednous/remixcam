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
import remixlab.dandelion.core.Constants.KeyboardAction;
import remixlab.tersehandling.generic.agent.GenericKeyboardAgent;
import remixlab.tersehandling.generic.event.GenericKeyboardEvent;
import remixlab.tersehandling.generic.profile.GenericKeyboardProfile;

public class KeyboardAgent extends GenericKeyboardAgent<GenericKeyboardProfile<Constants.KeyboardAction>> {	
	public KeyboardAgent(AbstractScene scn, String n) {
		super(new GenericKeyboardProfile<Constants.KeyboardAction>(), scn.terseHandler(), n);
		//profile = new GenericKeyboardProfile<Constants.DOF_0Action>();
		setDefaultGrabber(scn);

		// D e f a u l t s h o r t c u t s
		keyboardProfile().setShortcut('a', KeyboardAction.DRAW_AXIS);
		keyboardProfile().setShortcut('f', KeyboardAction.DRAW_FRAME_SELECTION_HINT);
		keyboardProfile().setShortcut('g', KeyboardAction.DRAW_GRID);
		keyboardProfile().setShortcut('m', KeyboardAction.ANIMATION);
		
		keyboardProfile().setShortcut('e', KeyboardAction.CAMERA_TYPE);
		keyboardProfile().setShortcut('h', KeyboardAction.GLOBAL_HELP);
		keyboardProfile().setShortcut('r', KeyboardAction.EDIT_CAMERA_PATH);

		keyboardProfile().setShortcut('s', KeyboardAction.INTERPOLATE_TO_FIT);
		keyboardProfile().setShortcut('S', KeyboardAction.SHOW_ALL);

		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_RIGHT, KeyboardAction.MOVE_CAMERA_RIGHT);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_LEFT, KeyboardAction.MOVE_CAMERA_LEFT);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_UP, KeyboardAction.MOVE_CAMERA_UP);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_DOWN, KeyboardAction.MOVE_CAMERA_DOWN);

		keyboardProfile().setShortcut((GenericKeyboardEvent.TH_ALT | GenericKeyboardEvent.TH_SHIFT), 'l',	KeyboardAction.MOVE_CAMERA_LEFT);
		
		//only one not working but horrible: 
		//keyboardProfile().setShortcut('1', KeyboardAction.PLAY_PATH);
		
		//keyboardProfile().setShortcut(49, KeyboardAction.PLAY_PATH);
		//keyboardProfile().setShortcut(GenericKeyboardEvent.TH_CTRL, 49, KeyboardAction.ADD_KEYFRAME_TO_PATH);
		//keyboardProfile().setShortcut(GenericKeyboardEvent.TH_ALT, 49, KeyboardAction.DELETE_PATH);
		//keyboardProfile().setShortcut(GenericKeyboardEvent.TH_NOMODIFIER_MASK, '1', KeyboardAction.PLAY_PATH_1);
		
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_CTRL, '1', KeyboardAction.ADD_KEYFRAME_TO_PATH_1);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_ALT, '1', KeyboardAction.DELETE_PATH_1);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_CTRL, '2', KeyboardAction.ADD_KEYFRAME_TO_PATH_2);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_ALT, '2', KeyboardAction.DELETE_PATH_2);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_CTRL, '3', KeyboardAction.ADD_KEYFRAME_TO_PATH_3);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_ALT, '3', KeyboardAction.DELETE_PATH_3);
		
		setKeyToPlayPath('1', 1);
		setKeyToPlayPath('2', 2);
		setKeyToPlayPath('3', 3);
		
	  //TODO testing:
		keyboardProfile().setShortcut('z', KeyboardAction.RESET_ARP);
	}
	
	public void setKeyToPlayPath(char key, int path) {			
		switch (path) {
		case 1 :
			keyboardProfile().setShortcut(GenericKeyboardEvent.TH_NOMODIFIER_MASK, key, KeyboardAction.PLAY_PATH_1);
			break;
		case 2 :
			keyboardProfile().setShortcut(GenericKeyboardEvent.TH_NOMODIFIER_MASK, key, KeyboardAction.PLAY_PATH_2);
			break;
		case 3 :
			keyboardProfile().setShortcut(GenericKeyboardEvent.TH_NOMODIFIER_MASK, key, KeyboardAction.PLAY_PATH_3);
			break;
		default :
			break;
		}		
	}
	
	/**
	public void setAddKeyFrameKeyboardModifier(int modifier) {
		if( modifier != EventConstants.TH_SHIFT &&
				modifier != EventConstants.TH_CTRL &&
				modifier != EventConstants.TH_ALT &&
				modifier != EventConstants.TH_ALT_GRAPH &&
				modifier != EventConstants.TH_META )
  			System.out.println("Expected a modifier here");
		else {
			keyboardProfile().setShortcut(GenericKeyboardEvent.TH_CTRL, '1', KeyboardAction.ADD_KEYFRAME_TO_PATH_1);
		}
	}
	
	public void setDeleteKeyFrameKeyboardModifier(int modifier) {
		if( modifier != EventConstants.TH_SHIFT &&
				modifier != EventConstants.TH_CTRL &&
				modifier != EventConstants.TH_ALT &&
				modifier != EventConstants.TH_ALT_GRAPH &&
				modifier != EventConstants.TH_META )
  			System.out.println("Expected a modifier here");
		else {
			keyboardProfile().setShortcut(GenericKeyboardEvent.TH_CTRL, '1', KeyboardAction.DELETE_PATH_1);
		}
	}
	*/
	
	@Override
	public GenericKeyboardEvent<Constants.KeyboardAction> feed() {
		return null;
	}

	@Override
	public GenericKeyboardProfile<Constants.KeyboardAction> keyboardProfile() {
		return profile;
	}
}
