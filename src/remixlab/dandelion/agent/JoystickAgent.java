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

import remixlab.dandelion.core.AbstractScene;
import remixlab.dandelion.core.Constants;
//import remixlab.dandelion.core.InteractiveCameraFrame;
//import remixlab.dandelion.core.InteractiveFrame;
//import remixlab.tersehandling.core.Grabbable;
import remixlab.tersehandling.generic.profile.GenericClickProfile;
import remixlab.tersehandling.generic.profile.GenericMotionProfile;

public class JoystickAgent extends GenericBiMotionAgent<GenericMotionProfile<Constants.DOF3Action>> {
	public JoystickAgent(AbstractScene scn, String n) {
		super(new GenericMotionProfile<Constants.DOF3Action>(),
			    new GenericMotionProfile<Constants.DOF3Action>(),
			    new GenericClickProfile<Constants.DOF0Action>(), scn.terseHandler(), n);
	  setDefaultGrabber(scn.pinhole().frame());
		//camProfile = new GenericMotionProfile<Constants.DOF_3Action>();
		//profile = new GenericMotionProfile<Constants.DOF_3Action>();
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF3Action> cameraProfile() {
		return camProfile;
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF3Action> frameProfile() {
		return profile;
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
}
