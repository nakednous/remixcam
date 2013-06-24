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

package remixlab.remixcam.interactivity;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class WheeledMouse extends Mouse {
	protected DOF1Profile wheelProfile;
	protected DOF1Profile frameWheelProfile;
	
	public WheeledMouse(AbstractScene scn, String n) {
		super(scn, n);
		wheelProfile = new DOF1Profile();
		frameWheelProfile = new DOF1Profile();
		
		wheelProfile.setBinding(DOF_1Action.ZOOM);
		frameWheelProfile.setBinding(DOF_1Action.ZOOM);
	}
	
	public DOF1Profile wheelProfile() {
		return wheelProfile;
	}
	
	public void setWheelProfile(DOF1Profile profile) {
		wheelProfile = profile;
	}
	
	public DOF1Profile frameWheelProfile() {
		return frameWheelProfile;
	}
	
	public void setFrameWheelProfile(DOF1Profile profile) {
		frameWheelProfile = profile;
	}
	
	@Override
	public void handle(GenericEvent<?> event) {
		//TODO warning: should be copy pasted from AbstractMotionDevice
		if(event == null)	return;		
		if( updateGrabber(event) ) return;		
		if(event instanceof GenericMotionEvent) {
			((GenericMotionEvent<?>)event).modulate(sens);
			if (deviceGrabber() != null )
				if( event instanceof DOF1Event )
					deviceGrabber().performInteraction(frameWheelProfile().handle(event));
				else
					deviceGrabber().performInteraction(frameProfile().handle(event));
			else
				if( event instanceof DOF1Event )
					scene.pinhole().frame().performInteraction(wheelProfile().handle(event));
				else
					scene.pinhole().frame().performInteraction(cameraProfile().handle(event));
		}
		
	  if(event instanceof GenericClickEvent)
	  	scene.handleEvent(clickProfile().handle(event));
	}
}
