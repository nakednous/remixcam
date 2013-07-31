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
import remixlab.tersehandling.core.EventConstants;
//import remixlab.tersehandling.core.Grabbable;
import remixlab.tersehandling.generic.event.GenericDOF2Event;
import remixlab.tersehandling.generic.profile.GenericClickProfile;
import remixlab.tersehandling.generic.profile.GenericMotionProfile;

public class MouseAgent extends GenericBiMotionAgent<GenericMotionProfile<Constants.DOF2Action>> implements Constants {
	public MouseAgent(AbstractScene scn, String n) {
		super(new GenericMotionProfile<Constants.DOF2Action>(),
				  new GenericMotionProfile<Constants.DOF2Action>(),
				  new GenericClickProfile<Constants.DOF2ClickAction>(),
				  new GenericClickProfile<Constants.DOF2ClickAction>(), scn.terseHandler(), n);
		setDefaultGrabber(scn.pinhole().frame());
		
		setAsArcballBindings();
		
		cameraClickProfile().setClickBinding(TH_LEFT, 2, DOF2ClickAction.ALIGN_FRAME);
		cameraClickProfile().setClickBinding(TH_RIGHT, 2, DOF2ClickAction.CENTER_FRAME);
		
		cameraClickProfile().setClickBinding(TH_LEFT, 2, DOF2ClickAction.ALIGN_FRAME);
		cameraClickProfile().setClickBinding(TH_RIGHT, 2, DOF2ClickAction.CENTER_FRAME);
		
		frameClickProfile().setClickBinding(TH_LEFT, 2, DOF2ClickAction.ALIGN_FRAME);
		frameClickProfile().setClickBinding(TH_RIGHT, 2, DOF2ClickAction.CENTER_FRAME);
	
		//TODO testing:
		cameraClickProfile().setClickBinding((TH_SHIFT | TH_CTRL ), TH_RIGHT, 1, DOF2ClickAction.INTERPOLATE_TO_FIT);
		cameraClickProfile().setClickBinding(TH_CENTER, 1, DOF2ClickAction.DRAW_AXIS);
		frameClickProfile().setClickBinding(TH_CENTER, 1, DOF2ClickAction.DRAW_GRID);
		
		//clickProfile().setClickBinding(TH_LEFT, 1, DOF2ClickAction.DRAW_FRAME_SELECTION_HINT);
		//clickProfile().setClickBinding(TH_RIGHT, 1, DOF2ClickAction.DRAW_AXIS);
	
		//clickProfile().setClickBinding(TH_SHIFT, TH_LEFT, 1, DOF2ClickAction.ALIGN_FRAME);
		//clickProfile().setClickBinding(TH_SHIFT, TH_CENTER, 1, DOF2ClickAction.SHOW_ALL);
		//clickProfile().setClickBinding((TH_SHIFT | TH_CTRL ), TH_RIGHT, 1, DOF2ClickAction.INTERPOLATE_TO_FIT);
	}
	
	public void setAsFirstPersonBindings() {		
		cameraProfile().setBinding(EventConstants.TH_LEFT, DOF2Action.MOVE_FORWARD);
		cameraProfile().setBinding(EventConstants.TH_CENTER, DOF2Action.LOOK_AROUND);
		cameraProfile().setBinding(EventConstants.TH_RIGHT, DOF2Action.MOVE_BACKWARD);
		cameraProfile().setBinding(EventConstants.TH_SHIFT, EventConstants.TH_LEFT, DOF2Action.ROLL);
		cameraProfile().setBinding(EventConstants.TH_SHIFT, EventConstants.TH_CENTER, DOF2Action.DRIVE);		
	}
	
	public void setAsThirdPersonBindings() {
		frameProfile().setBinding(EventConstants.TH_LEFT, DOF2Action.MOVE_FORWARD);
    frameProfile().setBinding(EventConstants.TH_CENTER, DOF2Action.LOOK_AROUND);
    frameProfile().setBinding(EventConstants.TH_RIGHT, DOF2Action.MOVE_BACKWARD);
    frameProfile().setBinding(EventConstants.TH_SHIFT, EventConstants.TH_LEFT, DOF2Action.ROLL);
		frameProfile().setBinding(EventConstants.TH_SHIFT, EventConstants.TH_CENTER, DOF2Action.DRIVE);
	}
	
	public void setAsArcballBindings() {
		cameraProfile().setBinding(TH_LEFT, DOF2Action.ROTATE);
		cameraProfile().setBinding(TH_CENTER, DOF2Action.ZOOM);
		cameraProfile().setBinding(TH_RIGHT, DOF2Action.TRANSLATE);		
		cameraProfile().setBinding(EventConstants.TH_SHIFT, EventConstants.TH_LEFT, DOF2Action.ZOOM_ON_REGION);
		cameraProfile().setBinding(EventConstants.TH_SHIFT, EventConstants.TH_CENTER, DOF2Action.SCREEN_TRANSLATE);
		cameraProfile().setBinding(EventConstants.TH_SHIFT, EventConstants.TH_RIGHT, DOF2Action.SCREEN_ROTATE);
			
		frameProfile().setBinding(TH_LEFT, DOF2Action.ROTATE);
		frameProfile().setBinding(TH_CENTER, DOF2Action.ZOOM);
		frameProfile().setBinding(TH_RIGHT, DOF2Action.TRANSLATE);
		frameProfile().setBinding(EventConstants.TH_SHIFT, EventConstants.TH_CENTER, DOF2Action.SCREEN_TRANSLATE);
		frameProfile().setBinding(EventConstants.TH_SHIFT, EventConstants.TH_RIGHT, DOF2Action.SCREEN_ROTATE);
	}
	
	@Override
	public GenericDOF2Event<Constants.DOF2Action> feed() {
		return null;
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF2Action> cameraProfile() {
		return camProfile;
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF2Action> frameProfile() {
		return profile;
	}
	
	public void setXTranslationSensitivity(float s) {
		sens[0] = s;
	}
	
	public void setYTranslationSensitivity(float s) {
		sens[1] = s;
	}
}
