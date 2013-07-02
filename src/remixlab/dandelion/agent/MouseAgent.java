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
import remixlab.tersehandling.core.Grabbable;
import remixlab.tersehandling.duoable.event.GenericDOF2Event;
import remixlab.tersehandling.duoable.profile.GenericClickProfile;
import remixlab.tersehandling.duoable.profile.GenericMotionProfile;

public class MouseAgent extends GenericBiMotionAgent<GenericMotionProfile<Constants.DOF2Action>> implements Constants {
	public MouseAgent(AbstractScene scn, String n) {
		super(new GenericMotionProfile<Constants.DOF2Action>(),
				  new GenericMotionProfile<Constants.DOF2Action>(),
				  new GenericClickProfile<Constants.DOF0Action>(), scn.terseHandler(), n);
		setDefaultGrabber(scn.pinhole().frame());
		//camProfile = new GenericMotionProfile<Constants.DOF_2Action>();
		//profile = new GenericMotionProfile<Constants.DOF_2Action>();
		//clickProfile = new GenericClickProfile<Constants.DOF_0Action>();
		cameraProfile().setBinding(TH_CENTER, DOF2Action.ZOOM);
		cameraProfile().setBinding(TH_LEFT, DOF2Action.ROTATE);
		cameraProfile().setBinding(TH_RIGHT, DOF2Action.TRANSLATE);
		//testing things out :P
		frameProfile().setBinding(TH_CENTER, DOF2Action.ZOOM);
		frameProfile().setBinding(TH_RIGHT, DOF2Action.ROTATE);
		frameProfile().setBinding(TH_LEFT, DOF2Action.TRANSLATE);
		
		clickProfile().setClickBinding(TH_LEFT, 1, DOF0Action.DRAW_FRAME_SELECTION_HINT);
		clickProfile().setClickBinding(TH_RIGHT, 1, DOF0Action.DRAW_AXIS);
		
		//clickProfile().setClickBinding(LEFT, 1, DOF_0Action.DRAW_AXIS);		
		//setClickBinding(RIGHT, 2, DOF_0Action.DRAW_GRID);
		
		//clickProfile().setClickBinding(RIGHT, 1, DOF_0Action.DRAW_FRAME_SELECTION_HINT);
		
		clickProfile().setClickBinding(TH_SHIFT, TH_LEFT, 2, DOF0Action.ALIGN_CAMERA);
		clickProfile().setClickBinding(TH_SHIFT, TH_CENTER, 2, DOF0Action.SHOW_ALL);
		clickProfile().setClickBinding((TH_SHIFT | TH_CTRL ), TH_RIGHT, 2, DOF0Action.ZOOM_TO_FIT);
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
	
	/**
	@Override
	public GenericClickProfile<Constants.DOF_0Action> clickProfile() {
		return clickProfile;
	}
	*/
	
	@Override
	public boolean addInPool(Grabbable deviceGrabber) {
		if(deviceGrabber == null)
			return false;
		if( (deviceGrabber instanceof InteractiveFrame) && !(deviceGrabber instanceof InteractiveCameraFrame) )
			if (!isInPool(deviceGrabber)) {
				deviceGrabberPool().add(deviceGrabber);
				return true;
			}
		return false;
	}
	
	public void setXTranslationSensitivity(float s) {
		sens[0] = s;
	}
	
	public void setYTranslationSensitivity(float s) {
		sens[1] = s;
	}
}
