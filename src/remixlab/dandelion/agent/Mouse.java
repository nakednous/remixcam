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
import remixlab.dandelion.event.DOF2Event;
import remixlab.dandelion.profile.ClickProfile;
import remixlab.dandelion.profile.DOF2Profile;
import remixlab.duoable.agent.AbstractMotionAgent;
import remixlab.tersehandling.core.Grabbable;

public class Mouse extends AbstractMotionAgent implements Constants {
	public Mouse(AbstractScene scn, String n) {
		super(scn, n);
		setDefaultGrabber(scn.pinhole().frame());
		camProfile = new DOF2Profile();
		profile = new DOF2Profile();
		clickProfile = new ClickProfile();
		sens = new float[2];
		sens[0] = 1f;
		sens[1] = 1f;
		cameraProfile().setBinding(CENTER, DOF_2Action.ZOOM);
		cameraProfile().setBinding(LEFT, DOF_2Action.ROTATE);
		cameraProfile().setBinding(RIGHT, DOF_2Action.TRANSLATE);
		//testing things out :P
		frameProfile().setBinding(CENTER, DOF_2Action.ZOOM);
		frameProfile().setBinding(RIGHT, DOF_2Action.ROTATE);
		frameProfile().setBinding(LEFT, DOF_2Action.TRANSLATE);
		
		clickProfile().setClickBinding(LEFT, 1, DOF_0Action.DRAW_FRAME_SELECTION_HINT);
		clickProfile().setClickBinding(RIGHT, 1, DOF_0Action.DRAW_AXIS);
		
		//clickProfile().setClickBinding(LEFT, 1, DOF_0Action.DRAW_AXIS);		
		//setClickBinding(RIGHT, 2, DOF_0Action.DRAW_GRID);
		
		//clickProfile().setClickBinding(RIGHT, 1, DOF_0Action.DRAW_FRAME_SELECTION_HINT);
		
		clickProfile().setClickBinding(DOF2Event.SHIFT, LEFT, 2, DOF_0Action.ALIGN_CAMERA);
		clickProfile().setClickBinding(DOF2Event.SHIFT, CENTER, 2, DOF_0Action.SHOW_ALL);
		clickProfile().setClickBinding((DOF2Event.SHIFT | DOF2Event.CTRL ), RIGHT, 2, DOF_0Action.ZOOM_TO_FIT);
	}
	
	@Override
	public DOF2Event feed() {
		return null;
	}
	
	@Override
	public DOF2Profile cameraProfile() {
		return (DOF2Profile)camProfile;
	}
	
	@Override
	public DOF2Profile frameProfile() {
		return (DOF2Profile)profile;
	}
	
	@Override
	public ClickProfile clickProfile() {
		return (ClickProfile)clickProfile;
	}
	
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
	
	public void setSensitivities(float x, float y) {
		sens[0] = x;
		sens[1] = y;
	}
}
