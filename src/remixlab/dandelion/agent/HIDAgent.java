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
import remixlab.tersehandling.generic.event.GenericDOF6Event;
import remixlab.tersehandling.generic.profile.GenericClickProfile;
import remixlab.tersehandling.generic.profile.GenericMotionProfile;

public class HIDAgent extends GenericBiMotionAgent<GenericMotionProfile<Constants.DOF6Action>> implements Constants {
	public HIDAgent(AbstractScene scn, String n) {
		super(new GenericMotionProfile<Constants.DOF6Action>(),
			    new GenericMotionProfile<Constants.DOF6Action>(),
			    new GenericClickProfile<Constants.DOF0Action>(), scn.terseHandler(), n);
	  setDefaultGrabber(scn.pinhole().frame());
		//super(scn.terseHandler(), n);
		//camProfile = new GenericMotionProfile<Constants.DOF_6Action>();
		//profile = new GenericMotionProfile<Constants.DOF_6Action>();
		//clickProfile = new GenericClickProfile<Constants.DOF_0Action>();
		cameraProfile().setBinding(DOF6Action.NATURAL);
		frameProfile().setBinding(DOF6Action.NATURAL);
	}
	
	@Override
	public GenericDOF6Event<Constants.DOF6Action> feed() {
		return null;
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF6Action> cameraProfile() {
		return camProfile;
	}
	
	@Override
	public GenericMotionProfile<Constants.DOF6Action> frameProfile() {
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
				pool().add(deviceGrabber);
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
	
	public void setXRotationSensitivity(float s) {
		sens[3] = s;
	}
	
	public void setYRotationSensitivity(float s) {
		sens[4] = s;
	}
	
	public void setZRotationSensitivity(float s) {
		sens[5] = s;
	}
}
