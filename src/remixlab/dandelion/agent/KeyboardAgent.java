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
import remixlab.tersehandling.core.EventConstants;
import remixlab.tersehandling.core.EventGrabberTuple;
import remixlab.tersehandling.event.KeyboardEvent;
import remixlab.tersehandling.event.TerseEvent;
import remixlab.tersehandling.generic.agent.GenericKeyboardAgent;
import remixlab.tersehandling.generic.event.GenericKeyboardEvent;
import remixlab.tersehandling.generic.profile.Duoable;
import remixlab.tersehandling.generic.profile.GenericKeyboardProfile;
import remixlab.tersehandling.generic.profile.KeyDuoable;

public class KeyboardAgent extends GenericKeyboardAgent<GenericKeyboardProfile<Constants.KeyboardAction>> {	
	int addMod;
	int delMod;
	public KeyboardAgent(AbstractScene scn, String n) {
		super(new GenericKeyboardProfile<Constants.KeyboardAction>(), scn.terseHandler(), n);
		//profile = new GenericKeyboardProfile<Constants.DOF_0Action>();
		setDefaultGrabber(scn);

		// D e f a u l t s h o r t c u t s
		keyboardProfile().setShortcut('a', KeyboardAction.DRAW_AXIS);
		keyboardProfile().setShortcut('f', KeyboardAction.DRAW_FRAME_SELECTION_HINT);
		keyboardProfile().setShortcut('g', KeyboardAction.DRAW_GRID);
		keyboardProfile().setShortcut('m', KeyboardAction.ANIMATION);
		//keyboardProfile().setShortcut('i', DOF0Action.FOCUS_INTERACTIVE_FRAME);
		//keyboardProfile().setShortcut(' ', DOF0Action.CAMERA_PROFILE);
		keyboardProfile().setShortcut('e', KeyboardAction.CAMERA_TYPE);
		keyboardProfile().setShortcut('h', KeyboardAction.GLOBAL_HELP);
		//keyboardProfile().setShortcut('H', DOF0Action.CURRENT_CAMERA_PROFILE_HELP);
		keyboardProfile().setShortcut('r', KeyboardAction.EDIT_CAMERA_PATH);

		keyboardProfile().setShortcut('s', KeyboardAction.INTERPOLATE_TO_FIT);
		keyboardProfile().setShortcut('S', KeyboardAction.SHOW_ALL);

		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_RIGHT, KeyboardAction.MOVE_CAMERA_RIGHT);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_LEFT, KeyboardAction.MOVE_CAMERA_LEFT);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_UP, KeyboardAction.MOVE_CAMERA_UP);
		keyboardProfile().setShortcut(GenericKeyboardEvent.TH_DOWN, KeyboardAction.MOVE_CAMERA_DOWN);

		keyboardProfile().setShortcut((GenericKeyboardEvent.TH_ALT | GenericKeyboardEvent.TH_SHIFT), 'l',	KeyboardAction.MOVE_CAMERA_LEFT);
		
		setAddKeyFrameKeyboardModifier(EventConstants.TH_CTRL);
		setDeleteKeyFrameKeyboardModifier(EventConstants.TH_ALT);
		
	  //TODO testing:
		keyboardProfile().setShortcut('z', KeyboardAction.RESET_ARP);
	}
	
	@Override
	public GenericKeyboardEvent<Constants.KeyboardAction> feed() {
		return null;
	}

	@Override
	public GenericKeyboardProfile<Constants.KeyboardAction> keyboardProfile() {
		return profile;
	}
	
	public void setAddKeyFrameKeyboardModifier(int modifier) {
		if( modifier != EventConstants.TH_SHIFT &&
				modifier != EventConstants.TH_CTRL &&
				modifier != EventConstants.TH_ALT &&
				modifier != EventConstants.TH_ALT_GRAPH &&
				modifier != EventConstants.TH_META )
  			System.out.println("Expected a modifier here");
		else
			addMod = modifier;
	}
	
	public void setDeleteKeyFrameKeyboardModifier(int modifier) {
		if( modifier != EventConstants.TH_SHIFT &&
				modifier != EventConstants.TH_CTRL &&
				modifier != EventConstants.TH_ALT &&
				modifier != EventConstants.TH_ALT_GRAPH &&
				modifier != EventConstants.TH_META )
  			System.out.println("Expected a modifier here");
		else
			delMod = modifier;
	}
	
	@Override
	public void handle(TerseEvent event) {
		if(event == null || !handler.isAgentRegistered(this)) return;
		
	  //grabber is external, i.e., action -> null
		if( grabber() != null )
			if(!( grabber() instanceof InteractiveFrame ) && !( grabber() instanceof AbstractScene ) ) {
				handler.enqueueEventTuple(new EventGrabberTuple(event, grabber()));
				return;
			}
		
		//add keyframe?
		if( (((KeyboardEvent) event).getModifiers() & addMod) != 0 ) {
			if( KeyboardEvent.getKey(((KeyboardEvent) event).getKeyCode()) == null ) return;
			int path = Character.getNumericValue( KeyboardEvent.getKey(((KeyboardEvent) event).getKeyCode()) );
			if( 0<= path && path <= 9 ) {
				handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, KeyboardAction.ADD_KEYFRAME_TO_PATH, grabber()));				
				return;
			}
		}
		
		//remove frame?
		if( (((KeyboardEvent) event).getModifiers() & delMod) != 0 ) {
			if( KeyboardEvent.getKey(((KeyboardEvent) event).getKeyCode()) == null ) return;
			int path = Character.getNumericValue( KeyboardEvent.getKey(((KeyboardEvent) event).getKeyCode()) );
			if( 0<= path && path <= 9 ) {
				handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, KeyboardAction.DELETE_PATH, grabber()));
				return;
			}
		}
				
		//"normal" case, just:
		if(event instanceof Duoable<?>)
			handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, keyboardProfile().handle((Duoable<?>)event), grabber()));
	}
	
	@Override
	public void handleKey(TerseEvent event) {
		if(event == null || !handler.isAgentRegistered(this)) return;	
		
	  //grabber is external, i.e., action -> null
		if( grabber() != null )
			if(!( grabber() instanceof InteractiveFrame ) && !( grabber() instanceof AbstractScene ) ) {
				handler.enqueueEventTuple(new EventGrabberTuple(event, grabber()));
				return;
			}
		
		//need to play a path?
		if( ((KeyboardEvent) event).getModifiers() == 0 ) {
			int path = Character.getNumericValue( ((KeyboardEvent) event).getKey());
			if( 0<= path && path <= 9 ) {
				handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, KeyboardAction.PLAY_PATH, grabber()));
				return;
			}
		}
		
	  //"normal" case, just:
		if(event instanceof KeyDuoable<?>)
			handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, keyboardProfile().handleKey((KeyDuoable<?>)event), grabber()));
	}
}
