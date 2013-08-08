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
import remixlab.tersehandling.generic.event.*;
import remixlab.tersehandling.generic.profile.*;

public class MouseAgent extends GenericWheeledBiMotionAgent<GenericMotionProfile<Constants.DOF2Action>> {
	public MouseAgent(AbstractScene scn, String n) {
		super(new GenericMotionProfile<WheelAction>(),
				  new GenericMotionProfile<WheelAction>(),
				  new GenericMotionProfile<DOF2Action>(),
				  new GenericMotionProfile<DOF2Action>(),
				  new GenericClickProfile<ClickAction>(),
				  new GenericClickProfile<ClickAction>(), scn.terseHandler(), n);
		setDefaultGrabber(scn.viewport().frame());
		
		setAsArcball();
		
		cameraClickProfile().setClickBinding(TH_NOMODIFIER_MASK, TH_LEFT, 2, ClickAction.ALIGN_FRAME);
		cameraClickProfile().setClickBinding(TH_NOMODIFIER_MASK, TH_RIGHT, 2, ClickAction.CENTER_FRAME);
		
		
		frameClickProfile().setClickBinding(TH_NOMODIFIER_MASK, TH_LEFT, 2, ClickAction.ALIGN_FRAME);
		frameClickProfile().setClickBinding(TH_NOMODIFIER_MASK, TH_RIGHT, 2, ClickAction.CENTER_FRAME);
		
		cameraWheelProfile().setBinding(TH_NOMODIFIER_MASK, TH_NOBUTTON, WheelAction.ZOOM);		
		frameWheelProfile().setBinding(TH_NOMODIFIER_MASK, TH_NOBUTTON, WheelAction.ZOOM);
	
		//TODO testing:
		cameraClickProfile().setClickBinding((TH_SHIFT | TH_CTRL ), TH_RIGHT, 1, ClickAction.INTERPOLATE_TO_FIT);
		cameraClickProfile().setClickBinding(TH_NOMODIFIER_MASK, TH_CENTER, 1, ClickAction.DRAW_AXIS);
		frameClickProfile().setClickBinding(TH_NOMODIFIER_MASK, TH_CENTER, 1, ClickAction.DRAW_GRID);		
		cameraClickProfile().setClickBinding(TH_SHIFT, TH_RIGHT, 1, ClickAction.PLAY_PATH_1);
	}
	
	public void setAsFirstPerson() {		
		cameraProfile().setBinding(TH_NOMODIFIER_MASK, TH_LEFT, DOF2Action.MOVE_FORWARD);
		cameraProfile().setBinding(TH_NOMODIFIER_MASK, TH_CENTER, DOF2Action.LOOK_AROUND);
		cameraProfile().setBinding(TH_NOMODIFIER_MASK, TH_RIGHT, DOF2Action.MOVE_BACKWARD);
		cameraProfile().setBinding(TH_SHIFT, TH_LEFT, DOF2Action.ROLL);
		cameraProfile().setBinding(TH_SHIFT, TH_CENTER, DOF2Action.DRIVE);
		cameraWheelProfile().setBinding(TH_CTRL, TH_NOBUTTON, WheelAction.ROLL);
		cameraWheelProfile().setBinding(TH_SHIFT, TH_NOBUTTON, WheelAction.DRIVE);
	}
	
	public void setAsThirdPerson() {
		frameProfile().setBinding(TH_NOMODIFIER_MASK, TH_LEFT, DOF2Action.MOVE_FORWARD);
    frameProfile().setBinding(TH_NOMODIFIER_MASK, TH_CENTER, DOF2Action.LOOK_AROUND);
    frameProfile().setBinding(TH_NOMODIFIER_MASK, TH_RIGHT, DOF2Action.MOVE_BACKWARD);
    frameProfile().setBinding(TH_SHIFT, TH_LEFT, DOF2Action.ROLL);
		frameProfile().setBinding(TH_SHIFT, TH_CENTER, DOF2Action.DRIVE);
	}
	
	public void setAsArcball() {
		cameraProfile().setBinding(TH_NOMODIFIER_MASK, TH_LEFT, DOF2Action.ROTATE);
		cameraProfile().setBinding(TH_NOMODIFIER_MASK, TH_CENTER, DOF2Action.ZOOM);
		cameraProfile().setBinding(TH_NOMODIFIER_MASK, TH_RIGHT, DOF2Action.TRANSLATE);		
		cameraProfile().setBinding(TH_SHIFT, TH_LEFT, DOF2Action.ZOOM_ON_REGION);
		cameraProfile().setBinding(TH_SHIFT, TH_CENTER, DOF2Action.SCREEN_TRANSLATE);
		cameraProfile().setBinding(TH_SHIFT, TH_RIGHT, DOF2Action.SCREEN_ROTATE);
			
		frameProfile().setBinding(TH_NOMODIFIER_MASK, TH_LEFT, DOF2Action.ROTATE);
		frameProfile().setBinding(TH_NOMODIFIER_MASK, TH_CENTER, DOF2Action.ZOOM);
		frameProfile().setBinding(TH_NOMODIFIER_MASK, TH_RIGHT, DOF2Action.TRANSLATE);
		frameProfile().setBinding(TH_SHIFT, TH_CENTER, DOF2Action.SCREEN_TRANSLATE);
		frameProfile().setBinding(TH_SHIFT, TH_RIGHT, DOF2Action.SCREEN_ROTATE);
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