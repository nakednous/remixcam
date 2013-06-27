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

import remixlab.remixcam.agent.AbstractMotionAgent;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Grabbable;
import remixlab.remixcam.core.InteractiveCameraFrame;
import remixlab.remixcam.core.InteractiveFrame;

public class DOF3Device extends AbstractMotionAgent {
	public DOF3Device(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF3Profile();
		profile = new DOF3Profile();
		sens = new float[3];
		sens[0] = 1f;
		sens[1] = 1f;
		sens[2] = 1f;
	}
	
	@Override
	public DOF3Profile cameraProfile() {
		return (DOF3Profile)camProfile;
	}
	
	@Override
	public DOF3Profile frameProfile() {
		return (DOF3Profile)profile;
	}
	
	@Override
	public boolean addInDeviceGrabberPool(Grabbable deviceGrabber) {
		if(deviceGrabber == null)
			return false;
		if( (deviceGrabber instanceof InteractiveFrame) && !(deviceGrabber instanceof InteractiveCameraFrame) )
			if (!isInDeviceGrabberPool(deviceGrabber)) {
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
	
	public void setZTranslationSensitivity(float s) {
		sens[2] = s;
	}
	
	public void setSensitivities(float x, float y, float z) {
		sens[0] = x;
		sens[1] = y;
		sens[2] = z;
	}
}
